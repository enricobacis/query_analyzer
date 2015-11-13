package enviroment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import extra.TPCHUtils;
import network.Link;
import network.Network;
import network.Node;
import model.EncSchemes;
import model.Operator;

public class Analyzer {

	/*
	 * Classe effettiva per il testing delle alternative
	 */

	//version 0.7.0

	//variabili frutto dell'analisi temporale/costo
	private double minCost;
	private double minTime;
	private String defOperations;		 //operazioni per il costo minimo in termini di tempo
	private String minCostDefOperations; //operazioni per il costo minimo in termini economici
	private TPCHUtils tpch;

	/* trick */
	/*private int trick = 2;*/

	public Analyzer(TPCHUtils tpch)
	{
		minTime = -1;
		minCost = -1;
		defOperations = "";
		minCostDefOperations = "";
		this.tpch = tpch;
	}

	public double getMinTime()
	{
		return minTime;
	}

	public String getMinTimeOperations()
	{
		return defOperations;
	}

	public String getMinCostOperations()
	{
		return minCostDefOperations;
	}

	public double getMinCost()
	{
		return minCost;
	}

	private String printCounters(int[] counters)
	{
		StringBuilder sb = new StringBuilder('[');
		for(int i: counters)
			sb.append(i + ',');
		return sb.append(']').toString();
	}

	private String printNetworkCounters(int[] counters, Network network)
	{
		StringBuilder sb = new StringBuilder('[');
		for(int i: counters)
			sb.append(network.getNodeByIndex(i - 1).getName() + ',');
		return sb.append(']').toString();
	}

	private double getTransferTime(Node prevNode, Node localNode, double dataToTransfer)
	{
		double output = 0;
		if(!prevNode.getName().equals(localNode.getName())) //se non è lo stesso nodo
		{
			for(Link link: prevNode.getLinks())
			{
				if(link.getNodeLinked().equals(localNode.getName()))
				{
					//converto il throughput da Mbit/s a MB/s
					double throughput = link.getThroughput() / 8;
					
					//i dati da trasferire sono espressi in byte
					output = dataToTransfer / (throughput * (Math.pow(10, 6)));
					
					//aggiungo la latenza della trasmissione
					return output + link.getLatency();
				}
			}
		}
		return output;
	}

	private ArrayList<Operator> findOperatorsByParentLevel(int localParentStartLevel, ArrayList<Operator> operators)
	{
		ArrayList<Operator> output = new ArrayList<Operator>();
		for(Operator op: operators)
			if(op.getIdParent() == localParentStartLevel)
				output.add(op);
		return output;
	}

	private double getEncryptionCost(String enc, int rows, int rowWidth, Node localNode) //ritorna il valore in secondi
	{
		double time = 0;
		//tecnica di cifratura usata -> DET : AES + ECB mode -> test openssl
		//							 -> NDET : AES + CBC mode (IV) (hanno sostanzialmente le stesse performance)
		//							 -> OPE : BCLO scheme

		if(enc.equals("DET") || enc.equals("NDET"))
		{
			//AES 256bit - blocco 256bit
			//perfromance su Intel Core iX teoricamente -> 700 MB/s

			double throughput = (localNode.getAesThroughput())*(Math.pow(10, 6)); //è già in byte
			int rowsWidth = rows*rowWidth;
			time = rowsWidth / throughput;

		}
		else
		{
			if(enc.equals("OPE")) //OPE
			{
				//BCLO scheme applicato su uno spazio di 256 bit
				double singleEnc = localNode.getBcloValueTime() /1000;
				time = singleEnc*rows;
			}
			else //PAI
			{
				double throughput = (localNode.getPaillerThroughput())*(Math.pow(10, 6)); //è già in byte
				int rowsWidth = rows*rowWidth;
				time = rowsWidth / throughput;
			}
		}

		return time;
	}

	private double getEncryptionNodeCost(Node localNode, double nodeTime)
	{
		return localNode.getCostPerSecond() * nodeTime;
	}

	public ArrayList<Attempt> Analyze(EncSchemes encSchemes, ArrayList<Operator> operators, Network network)
	{
		//output
		ArrayList<Attempt> output = new ArrayList<Attempt>();

		//1 creare la lista dei possibili metodi di encryption
		//1.1 tecniche e relativi operatori abbinati
		HashMap<String, ArrayList<String>> operatorsEnc = encSchemes.getOperatorsEncs();
		HashMap<String, ArrayList<String>> functionsEnc = encSchemes.getFunctionsEncs();

		//2 scorrimento della gerarchia
		//2.0 per ogni possibilità adotto un sistema simil "contatori" per provarle tutte
		int counterSize = operators.size();
		int[] counters = new int[counterSize];
		int[] countersMax = new int[counterSize];
		
		Arrays.fill(counters, 1);

		//2.1 individuo la foglia più estrema dell'albero, sarà il punto di partenza
		//int startLevel = 0; //per il momento non lo uso...
		int parentStartLevel = -1;
		for(Operator op: operators)
			if(op.getIdParent() > parentStartLevel)
			{
				//startLevel = operators.get(i).getId();
				parentStartLevel = op.getIdParent();
			}

		//2.2 calcolo il totale delle possibili alternative
		int possibility = 1;
		for(int i = 0; i < operators.size(); i++)
		{
			int opMethods = operatorsEnc.get(operators.get(i).getNodeType()).size();
			
			// !! WARNING
			// simil permutazioni semplici al momento, bisogna vedere se le cifrature
			// non escludono possibilità tra loro
			possibility *= opMethods;
			countersMax[i] = opMethods;
		}

		//2.2.2 preparo la struttura per l'esecuzione esaustiva della query
		//inizializzo contatori
		int networkNodesNumber = network.getNodesNumber();
		int networkOperatorCountersNumber = operators.size();
		int[] networkOperatorCounters = new int[networkOperatorCountersNumber];
		int[] networkCountersMax = new int[networkOperatorCountersNumber];
		
		for(int i = 0; i < networkOperatorCounters.length; i++)
		{
			networkOperatorCounters[i] = 1;
			networkCountersMax[i] = networkNodesNumber;
		}
		
		//numero di tentativi "esaustivi"
		double networkAttemps = Math.pow(networkNodesNumber, networkOperatorCountersNumber);
		//possibili nodi su cui operare ^numero di operatori da considerare

		//2.2.3 ricerca esaustiva della soluzione
		while(networkAttemps > 0)
		{
			System.out.println("Attempt: " + (int) networkAttemps);
			//2.3 parto da quella foglia per risalire la gerarchia, ogni step sarà -1 al valore attuale
			//facilitato dalle caratteristiche

			boolean admissible = true; //do per scontato che il tentativo sia ammissibile
			int currentPossibility = possibility;

			while(currentPossibility > 0)
			{
				int localParentStartLevel = parentStartLevel;
				double localCost = 0; //in time
				double localMoney = 0;
				double nodeTime = 0;
				double nodeMoney = 0;
				Node prevNode = network.getNodeByName("CL1"); //suppongo che le richieste partano dal client
				String localOperations = new String();
				int networkIndex = 0; //indice di scorrimento dell'array di contatori della rete


				while(localParentStartLevel >= -1)
				{
					ArrayList<Operator> levelOperators = findOperatorsByParentLevel(localParentStartLevel,operators);
					for(Operator localOperator: levelOperators)
					{
						Node localNode = null;

						//2.3.1
						/*network analisi*/
						//nodo sul quale eseguire l'operazione -> ora comandato dai contatori
						localNode = network.getNodeByIndex(networkOperatorCounters[networkIndex]-1);
						networkIndex++;

						localOperations += "\nNODE: "+localNode.getName()+" \n";
						/* end network analisi */

						//2.3.2
						//attributi della query
						if(localOperator.getOutput() != null)
						{
							for(int k = 0; k < localOperator.getOutput().size(); k++)
							{
								String localOutput = localOperator.getOutput().get(k);

								//verifico se l'operatore è una funzione o direttamente un attributo
								boolean function = false;
								String item = localOutput;

								/*trick*/
								/*if(item.indexOf("::") > -1)
									continue;*/

								ArrayList<String> enc = new ArrayList<String>();
								if(item.indexOf("count") > -1)
								{
									 enc = functionsEnc.get("count");
									 function = true;
								}
								if(item.indexOf("sum") > -1)
								{
									 enc = functionsEnc.get("sum");
									 function = true;
								}
								if(item.indexOf("avg") > -1)
								{
									 enc = functionsEnc.get("avg");
									 function = true;
								}
								if(item.indexOf("max") > -1)
								{
									 enc = functionsEnc.get("max");
									 function = true;
								}
								if(item.indexOf("min") > -1)
								{
									 enc = functionsEnc.get("min");
									 function = true;
								}
								if(item.indexOf("substring") > -1)
								{
									 enc = functionsEnc.get("substring");
									 function = true;
								}


								String nodePolicy = "";
								if(!function)
									nodePolicy = localNode.verifyPolicy(tpch.getItemColumn(localOutput));
								else
									nodePolicy = "Encrypted"; //per il momento le funzioni le posso considerare sempre cifrate

								if(nodePolicy.equals("No")) // non c'è visibilità sull'attributo, questa non è un tentativo ammissibile
								{
									admissible = false;
									localOperations += "Attempt arrested -> no visibility of "+localOutput+" on "+localNode.getName()+" \n";
									break;
								}
								else
								{
									//2.3.2.1
									//determino il tipo di encryption da usare
									String selectedEnc = "NO"; //suppongo non serva ma...
									if(!nodePolicy.equals("Plain")) //encryption necessaria
									{
										ArrayList<String> localOperatorEncs = operatorsEnc.get(localOperator.getNodeType());
										selectedEnc = localOperatorEncs.get(counters[localOperator.getId()]-1); //-1 perché le liste partono da 0, ma il contatore effettivo da 1
									}

									//2.3.2.2
									//costi
									if(selectedEnc.equals("NO")) //non è richieste encryption sul singolo item
									{
										localCost += 0;
										localMoney += 0;
										localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
												+"-> IDParent: "+localOperator.getIdParent()
												+" -> Item: "+k+" -> Enc: "+selectedEnc+" -> Time: 0 -> Cost : 0\n";
									}
									else //encryption richiesta dalla policy del nodo
									{

										//se non si tratta di una funzione uso gli schemi per gli operatori
										if(!function)
										{
											int itemWidth = localOperator.getPlanWidth();

												String column = tpch.getItemColumn(item);
												String table = tpch.getItemTable(item);
												if(tpch.getStructure().containsKey(table))
												{
													itemWidth = tpch.findWidthByColumn(table,column);
													nodeTime = getEncryptionCost(selectedEnc, localOperator.getPlanRows(), itemWidth, localNode);
													localCost += nodeTime;
													nodeMoney = getEncryptionNodeCost(localNode, nodeTime);
													localMoney += nodeMoney;
												}
												else
												{
													nodeTime = getEncryptionCost(selectedEnc, localOperator.getPlanRows(), itemWidth, localNode);
													localCost += nodeTime;
													nodeMoney = getEncryptionNodeCost(localNode, nodeTime);
													localMoney += nodeMoney;
												}


											localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
													+"-> IDParent: "+localOperator.getIdParent()
													+" -> Item: "+k+" -> Width: "+itemWidth+" -> Enc: "+selectedEnc+" -> Time: "+nodeTime+" -> Cost: "+nodeMoney+"\n";
										}
										else //è una funzione
										{
											//le funzioni sono trattate con uno spazio di possibilità diverse
											String functionSelectedEnc = enc.get(0);
											nodeTime = getEncryptionCost(functionSelectedEnc, localOperator.getPlanRows(), localOperator.getPlanWidth(), localNode);
											localCost += nodeTime;
											nodeMoney = getEncryptionNodeCost(localNode, nodeTime);
											localMoney += nodeMoney;

											localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
													+"-> IDParent: "+localOperator.getIdParent()
													+" -> (funct) Item: "+k+" -> Enc: "+functionSelectedEnc+" -> Time: "+nodeTime+" -> Cost: "+nodeMoney+"\n";
										}//chiusura funzione
									}//chiusura encryption
								}//chiusura ammissibilità del tentativo
							}//end scorrimento attributi
							if(admissible == false)
							{
								break;
							}

						}//end attributi


						//2.4
						//attributi impliciti
						ArrayList<String> implicit = localOperator.getImplicit();
						if(implicit != null && implicit.size() > 0) //ci sono attributi impliciti da controllare
						{
							for(String imp: implicit)
							{
								/*trick*/
								/*if(implicit.get(m).indexOf("::") > -1)
									continue;*/


								if(!TPCHUtils.isEquality(imp))
								{
									ArrayList<String> implicitAttributes = tpch.findColumnsInString(imp);
									for(String attr: implicitAttributes)
									{
										String nodePolicy = localNode.verifyPolicy(attr);
										if(nodePolicy.equals("No")) {
											//non c'è visibilità di nessun tipo per quel nodo
											admissible = false;
											break;
										}
									}
								}
								else
								{
									//nell'ugualgianza mi aspetto due colonne (join, hash, ...)
									ArrayList<String> implicitAttributes = tpch.findColumnsInString(imp);
									String prevPolicy = localNode.verifyPolicy(implicitAttributes.get(0));
									
									for(String implicitAttribute: implicitAttributes)
									{
										String currPolicy = localNode.verifyPolicy(implicitAttribute);
										if(currPolicy.equals("No") || !currPolicy.equals(prevPolicy)) //non c'è visibilità oppure c'è conflitto
										{
											//le soluzioni Plain = Encrypted sono svantaggiose, meglio Plain = Plain o Enc = Enc
											admissible = false;
											break;
										}
										else
											prevPolicy = currPolicy;
									}

								}


								if(admissible == false)
								{
									break;
								}
							}
						}//chiusura attributi impliciti

						//2.5
						//devo calcorare i tempi di trasferimento da un nodo all'altro
						if(prevNode != null && admissible == true)
						{
							double dataToTransfer = localOperator.getPlanWidth()*localOperator.getPlanRows(); //non mi interessa la singola width di ogni output, questa è la loro somma
							double transferTime = getTransferTime(prevNode,localNode, dataToTransfer);
							localCost += transferTime;
							localOperations += "TRASFERIMENTO DAL NODO "+prevNode.getName()+" AL NODO "+localNode.getName()+" Tempo -> "+transferTime+"\n";
							//si potrebbe introdurre un modello di costo per la comunicazione tra nodi, ma le policy nelle situazioni reali
							//prevedono dei costi sui dati in uscita più che sull'ingresso...
						}
						prevNode = localNode;


					}//end operators di un determinato livello
					if(admissible == false)
					{
						break;
					}

					//passo al livello gerarchicamente sopra
					localParentStartLevel--;

				}//end local parent start level
				if(admissible == false)
				{
					break;
				}

				//situazione attuale dei contatori per il tentativo in corso
				String counterStatus = printCounters(counters);
				String networkCounterStatus = printNetworkCounters(networkOperatorCounters,network);

				//aggiornamento contatori, per diversificare le possibilità
				for(int i = 0; i < counters.length; i++)
				{
					if((countersMax[i] - counters[i]) == 0) //sono già arrivato all'ultimo tentativo, passo al contatore successivo
					{
						counters[i] = 1;
						continue;
					}
					else
					{
						counters[i]++;
						break;
					}
				}


				//--> soluzione greeedy, vecchia implementazione, la tengo per avere subito sotto mano la miglior soluzione trovata
				if(localCost < minTime || minTime == -1) //seconda condizione applicata al primo giro
				{
					minTime = localCost;
					defOperations = localOperations;

					/* trick */
					/*System.out.println("MIN TIME: "+minTime);
					trick--;*/

				}
				if(localMoney < minCost || minCost == -1) //seconda condizione applicata al primo giro
				{
					minCost = localMoney;
					minCostDefOperations = localOperations;

					/* trick */
					/*System.out.println("MIN EURO: "+minCost);
					trick--;*/

				}

				/* trick */
				/*if(trick <= 0)
					return output;*/


				//voglio esplorare tutte le possibilità
				output.add(new Attempt(localOperations, localCost, localMoney, counterStatus, networkCounterStatus));

				currentPossibility--;
			}//end possibility

			//aggiorno i contatori della rete, per passare al prossimo tentivo
			for(int i = 0; i < networkOperatorCounters.length; i++)
			{
				if((networkCountersMax[i] - networkOperatorCounters[i]) == 0) //sono già arrivato all'ultimo tentativo, passo al contatore successivo
				{
					networkOperatorCounters[i] = 1;
					continue;
				}
				else
				{
					networkOperatorCounters[i]++;
					break;
				}
			}

			networkAttemps--;
		}//end network attemps

		return output;

	}



}
