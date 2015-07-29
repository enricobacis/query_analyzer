package enviroment;

import java.util.ArrayList;
import java.util.HashMap;

import extra.TPCHUtils;
import network.Network;
import network.Node;
import model.EncSchemes;
import model.Operator;

public class Analyzer {
	
	/*
	 * Classe effettiva per il testing delle alternative
	 */
	
	//version 0.3.0
	
	//variabili frutto dell'analisi temporale/costo
	private double minCost;
	private double minTime;
	private String defOperations;		 //operazioni per il costo minimo in termini di tempo
	private String minCostDefOperations; //operazioni per il costo minimo in termini economici
	
	public Analyzer()
	{
		minTime = -1;
		minCost = -1;
		defOperations = "";
		minCostDefOperations = "";
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
		String out = "[";
		for(int i = 0;i<counters.length-1;i++)
			out += counters[i]+",";
		out += counters[counters.length-1]+"]";
		return out;
		
	}
	
	private double getTransferTime(Node prevNode, Node localNode, double dataToTransfer) 
	{
		double output = 0;
		if(!prevNode.getName().equals(localNode.getName())) //se non � lo stesso nodo
		{
			for(int i=0;i<prevNode.getLinks().size();i++)
			{
				if(prevNode.getLinks().get(i).getNodeLinked().equals(localNode.getName()))
				{
					double throughput = prevNode.getLinks().get(i).getThroughput() / 8; //converto il throughput da Mbit/s a MB/s
					//i dati da trasferire sono espressi in byte
					output = dataToTransfer / (throughput * (Math.pow(10, 6))); 
					return output + prevNode.getLinks().get(i).getLatency() ; //aggiungo la latenza della trasmissione			
				}
			}
		}
		return output;
		
	}
	
	private ArrayList<Operator> findOperatorsByParentLevel(int localParentStartLevel, ArrayList<Operator> operators)
	{
		ArrayList<Operator> output = new ArrayList<Operator>();
		for(int i=0;i<operators.size();i++)
			if(operators.get(i).getIdParent() == localParentStartLevel)
				output.add(operators.get(i));
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
			
			double throughput = (localNode.getAesThroughput())*(Math.pow(10, 6)); //� gi� in byte
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
				double throughput = (localNode.getPaillerThroughput())*(Math.pow(10, 6)); //� gi� in byte
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
		//2.0 per ogni possibilit� adotto un sistema simil "contatori" per provarle tutte
		int counterSize = operators.size();
		int[] counters = new int[counterSize];
		for(int i = 0;i<counters.length;i++)
			counters[i] = 1;
		int[] countersMax = new int[counterSize];
		
		
		//2.1 individuo la foglia pi� estrema dell'albero, sar� il punto di partenza
		//int startLevel = 0; //per il momento non lo uso...
		int parentStartLevel = -1;
		for(int i = 0; i<operators.size();i++)
			if(operators.get(i).getIdParent() > parentStartLevel)
			{
				//startLevel = operators.get(i).getId();
				parentStartLevel = operators.get(i).getIdParent();
			}
		
		//2.2 calcolo il totale delle possibili alternative				
		int possibility = 1;
		for(int i = 0;i<operators.size();i++)
		{
			Operator currentOperator = operators.get(i);
			ArrayList<String> currentOperatorMethods = operatorsEnc.get(currentOperator.getNodeType());
			possibility *= currentOperatorMethods.size();	//simil permutazioni semplici al momento, bisogna vedere se le cifrature non escludono possibilit� tra loro
															//WARNING!!
			countersMax[i] = currentOperatorMethods.size();
		}
		
		//2.3 parto da quella foglia per risalire la gerarchia, ogni step sar� -1 al valore attuale
		//facilitato dalle caratteristiche
		while(possibility > 0)
		{
			int localParentStartLevel = parentStartLevel;
			double localCost = 0; //in time
			double localMoney = 0;
			double nodeTime = 0;
			double nodeMoney = 0;
			Node prevNode = network.getNodeByName("CL1"); //suppongo che le richieste partano dal client
			String localOperations = new String();
			
			while(localParentStartLevel >= -1)
			{
				ArrayList<Operator> levelOperators = findOperatorsByParentLevel(localParentStartLevel,operators);
				for(int j=0;j<levelOperators.size();j++)
				{
					Operator localOperator = levelOperators.get(j);
					Node localNode = null;
					/*network analisi*/					
					//nodo sul quale eseguire l'operazione
					String dataNeeded = localOperator.getRelationName();
					if(dataNeeded != null)
					{
						if(dataNeeded.equals("none"))
							dataNeeded = "NoNodeNeeded";
						else
							dataNeeded = network.searchNodeByRelation(dataNeeded); 
						
						localNode = network.getNodeByName(dataNeeded);	//se fosse "NoNodeNeeded" torna il best node	
					}
					else
					{
						localNode = network.getBestNode();					
					}
					localOperations += "\nNODE: "+localNode.getName()+" (data) "+localOperator.getRelationName()+" (policy) "+localNode.getPolicy()+"\n";					
					//////
					/* end network analisi */
					
					//policy del nodo
					String nodePolicy = localNode.getPolicy();
					String selectedEnc = "NO"; //suppongo non serva ma...					
					if(!nodePolicy.equals("Plain")) //encryption necessaria
					{
						ArrayList<String> localOperatorEncs = operatorsEnc.get(localOperator.getNodeType());
						selectedEnc = localOperatorEncs.get(counters[localOperator.getId()]-1); //-1 perch� le liste partono da 0, ma il contatore effettivo da 1
					}					
					
					//per ogni <output> -> <item> devo applicare la tecnica di cifratura....ma attenzione
					//se l'item � una funzione devo applicare un metodo di cifratura specifico per quella funzione
					if(localOperator.getOutput() != null)
					{
						for(int k=0;k<localOperator.getOutput().size();k++)
						{
							if(selectedEnc.equals("NO")) //non � richieste encryption sul singolo item
							{
								localCost += 0;
								localMoney += 0;
								localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
										+"-> IDParent: "+localOperator.getIdParent()			
										+" -> Item: "+k+" -> Enc: "+selectedEnc+" -> Time: 0 -> Cost : 0\n";
							}
							else //encryption richiesta dalla policy del nodo
							{
								boolean function = false;
								String item = localOperator.getOutput().get(k);						
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
								
								//suppongo che per funzioni ci sia un solo metodo di cifratura ammesso ora come ora...
								if(!function)
								{
									int itemWidth = localOperator.getPlanWidth();
									if(localOperator.getRelationName() != null) //fa riferimento ad una tabella
									{
										String table = localOperator.getRelationName();
										String column = TPCHUtils.getItemColumn(item);
										if(TPCHUtils.getStructure().containsKey(table))
										{
											itemWidth = TPCHUtils.findWidthByColumn(table,column);
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
								else
								{
									//le funzioni sono trattate pi� semplicemente con OPE quindi hanno uno spazio di possibilit� diverse
									String functionSelectedEnc = enc.get(0);
									nodeTime = getEncryptionCost(functionSelectedEnc, localOperator.getPlanRows(), localOperator.getPlanWidth(), localNode);
									localCost += nodeTime;
									nodeMoney = getEncryptionNodeCost(localNode, nodeTime);
									localMoney += nodeMoney;									
									
									localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
											+"-> IDParent: "+localOperator.getIdParent()
											+" -> (funct) Item: "+k+" -> Enc: "+functionSelectedEnc+" -> Time: "+nodeTime+" -> Cost: "+nodeMoney+"\n";
								}
							} //close encryption needed
													
						}//close single output
						
					}//close output
					//devo calcorare i tempi di trasferimento da un nodo all'altro
					if(prevNode != null)
					{
						double dataToTransfer = localOperator.getPlanWidth()*localOperator.getPlanRows(); //non mi interessa la singola width di ogni output, questa � la loro somma
						double transferTime = getTransferTime(prevNode,localNode, dataToTransfer);
						localCost += transferTime;
						localOperations += "TRASFERIMENTO DAL NODO "+prevNode.getName()+" AL NODO "+localNode.getName()+" Tempo -> "+transferTime+"\n";
						//si potrebbe introdurre un modello di costo per la comunicazione tra nodi, ma le policy nelle situazioni reali
						//prevedono dei costi sui dati in uscita pi� che sull'ingresso...
					}
					prevNode = localNode;
					
				}//close operators
				
				//passo al livello gerarchicamente sopra
				localParentStartLevel--;				
				
			}//close while possibility
			
			//situazione attuale dei contatori per il tentativo in corso
			String counterStatus = printCounters(counters);
			
			//aggiornamento contatori, per diversificare le possibilit�
			for(int i = 0;i<counters.length;i++)
			{
				if((countersMax[i] - counters[i]) == 0) //sono gi� arrivato all'ultimo tentativo, passo al contatore successivo
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
			}
			if(localMoney < minCost || minCost == -1) //seconda condizione applicata al primo giro
			{
				minCost = localMoney;
				minCostDefOperations = localOperations;
			}
			
			
			//voglio esplorare tutte le possibilit�			
			output.add(new Attempt(localOperations, localCost, localMoney, counterStatus));			
			
			possibility--;
		}
		
		return output;
		
	}

	
	
}
