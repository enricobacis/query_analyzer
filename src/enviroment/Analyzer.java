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
	
	//variabili frutto dell'analisi
	private double minCost;
	private double minTime;
	private String defOperations;
	
	public Analyzer()
	{
		minTime = -1;
		minCost = -1;
		defOperations = "";
	}
	
	public double getMinTime()
	{
		return minTime;
	}
	
	public String getOperations()
	{
		return defOperations;
	}
	
	public double getMinCost()
	{
		return minCost;
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
			
			double throughput = (localNode.getAesThroughput())*(Math.pow(10, 6)); //è già in byte
			int rowsWidth = rows*rowWidth;
			time = rowsWidth / throughput;			
			
		}else //OPE
		{
			//BCLO scheme applicato su uno spazio di 256 bit
			double singleEnc = localNode.getBcloValueTime() /1000;
			time = singleEnc*rows;			
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
		for(int i = 0;i<counters.length;i++)
			counters[i] = 1;
		int[] countersMax = new int[counterSize];
		
		
		//2.1 individuo la foglia più estrema dell'albero, sarà il punto di partenza
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
			possibility *= currentOperatorMethods.size();	//simil permutazioni semplici al momento, bisogna vedere se le cifrature non escludono possibilità tra loro
															//WARNING!!
			countersMax[i] = currentOperatorMethods.size();
		}
		
		//2.3 parto da quella foglia per risalire la gerarchia, ogni step sarà -1 al valore attuale
		//facilitato dalle caratteristiche
		while(possibility > 0)
		{
			int localParentStartLevel = parentStartLevel;
			double localCost = 0; //in time
			double localMoney = 0;
			double nodeTime = 0;
			double nodeMoney = 0;
			String localOperations = "";
			
			while(localParentStartLevel >= -1)
			{
				ArrayList<Operator> levelOperators = findOperatorsByParentLevel(localParentStartLevel,operators);
				for(int j=0;j<levelOperators.size();j++)
				{
					Operator localOperator = levelOperators.get(j);
					Node localNode = null;
					/*network analisi*/					
					//nodo sul quale eseguire l'operazione ANDRà SICURAMENTE ESTESO CON ALTRE VARIABILI
					String dataNeeded = localOperator.getRelationName();
					if(dataNeeded != null)
					{
						if(dataNeeded.equals("none"))
							dataNeeded = "NoNodeNeeded";
						else
							dataNeeded = network.searchNodeByRelation(dataNeeded); //dovrà essere fatto un discorso
																					//di analisi con la compatibilità dell'enc...
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
						selectedEnc = localOperatorEncs.get(counters[localOperator.getId()]-1); //-1 perchè le liste partono da 0, ma il contatore effettivo da 1
					}					
					
					//per ogni <output> -> <item> devo applicare la tecnica di cifratura....ma attenzione
					//se l'item è una funzione devo applicare un metodo di cifratura specifico per quella funzione
					for(int k=0;k<localOperator.getOutput().size();k++)
					{
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
								//le funzioni sono trattate più semplicemente con OPE quindi hanno uno spazio di possibilità diverse
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
						
					}//close output					
				}//close operators
				
				//passo al livello gerarchicamente sopra
				localParentStartLevel--;				
				
			}//close while possibility
			
			
			//aggiornamento contatori, per diversificare le possibilità
			for(int i = 0;i<counters.length;i++)
			{
				if((countersMax[i] - counters[i]) == 0) //sono già arrivato all'ultimo tentativo, passo al contatore successivo
					continue;
				else
				{
					counters[i]++;
					break;
				}
			}
			
			//--> soluzione greeedy, vecchia implementazione, la tengo per avere subito sotto mano la miglior soluzione trovata
			if(localCost < minTime || minTime == -1) //seconda condizione applicata al primo giro
			{
				minCost = localMoney;
				minTime = localCost;
				defOperations = localOperations;
			}
			
			
			//voglio esplorare tutte le possibilità
			output.add(new Attempt(localOperations, localCost, localMoney));
			
			
			possibility--;
		}
		
		return output;
		
	}

	
	
}
