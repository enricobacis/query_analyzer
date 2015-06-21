package enviroment;

import java.util.ArrayList;
import java.util.HashMap;
import network.Network;
import model.EncSchemes;
import model.Operator;

public class Analyzer {
	
	/*
	 * Classe effettiva per il testing delle alternative
	 */
	
	//costi fittizi andranno analizzati secondo la mole di dati
	private final int NDET_COST = 10;
	private final int DET_COST = 20;
	private final int OPE_COST = 50;
	//private final int PHE_COST = 100;
	
	//variabili frutto dell'analisi
	private int minCost;
	private String defOperations;
	
	public Analyzer()
	{
		minCost = -1;
		defOperations = "";
	}
	
	public int getMinCost()
	{
		return minCost;
	}
	
	public String getOperations()
	{
		return defOperations;
	}
	
	private ArrayList<Operator> findOperatorsByParentLevel(int localParentStartLevel, ArrayList<Operator> operators)
	{
		ArrayList<Operator> output = new ArrayList<Operator>();
		for(int i=0;i<operators.size();i++)
			if(operators.get(i).getIdParent() == localParentStartLevel)
				output.add(operators.get(i));
		return output;		
	}
	
	public void Analyze(EncSchemes encSchemes, ArrayList<Operator> operators, Network network)
	{
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
			int localCost = 0;
			String localOperations = "";
			
			while(localParentStartLevel >= -1)
			{
				ArrayList<Operator> levelOperators = findOperatorsByParentLevel(localParentStartLevel,operators);
				for(int j=0;j<levelOperators.size();j++)
				{
					Operator localOperator = levelOperators.get(j);
					
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
					}
					else
						dataNeeded = "Computational or Client";
					localOperations += "NODE: "+dataNeeded+" (data) "+localOperator.getRelationName()+"\n";
					//////
					
					
					
					ArrayList<String> localOperatorEncs = operatorsEnc.get(localOperator.getNodeType());
					String selectedEnc = localOperatorEncs.get(counters[localOperator.getId()]-1); //-1 perchè le liste partono da 0, ma il contatore effettivo da 1
					
					//per ogni <output> -> <item> devo applicare la tecnica di cifratura....ma attenzione
					//se l'item è una funzione devo applicare un metodo di cifratura specifico per quella funzione
					for(int k=0;k<localOperator.getOutput().size();k++)
					{
						//questo procedimento si può scrivere molto meglio
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
						
						//suppongo che per funzioni ci sia un solo metodo di cifratura ammesso ora come ora...
						if(!function)
						{
							//conta il selectedEnc
							if(selectedEnc.equals("NDET"))
								localCost += NDET_COST;
							else if(selectedEnc.equals("DET"))
								localCost += DET_COST;
							else if(selectedEnc.equals("OPE"))
								localCost += OPE_COST;
							else 
								localCost += 0;
							
							localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
									+"-> IDParent: "+localOperator.getIdParent()			
									+" -> Item: "+k+" -> Enc: "+selectedEnc+"\n";
						}
						else
						{
							String functionSelectedEnc = enc.get(0);
							if(functionSelectedEnc.equals("NDET"))
								localCost += NDET_COST;
							else if(functionSelectedEnc.equals("DET"))
								localCost += DET_COST;
							else if(functionSelectedEnc.equals("OPE"))
								localCost += OPE_COST;
							else 
								localCost += 0;
							
							localOperations += localOperator.getNodeType()+"-> ID: "+localOperator.getId()
									+"-> IDParent: "+localOperator.getIdParent()
									+" -> (funct) Item: "+k+" -> Enc: "+functionSelectedEnc+"\n";
						}
						
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
			
			if(localCost < minCost || minCost == -1) //seconda condizione applicata al primo giro
			{
				minCost = localCost;
				defOperations = localOperations;
			}
			
			possibility--;
		}
		
		
		
	}
	
}
