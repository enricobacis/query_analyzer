package enviroment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
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
	private final int PHE_COST = 100;
	
	public Analyzer()
	{
		
	}
	
	public void Analyze(EncSchemes encSchemes, ArrayList<Operator> operators, Network network)
	{
		//1 creare la lista dei possibili metodi di encryption
		//1.1 conto il numero di tecniche
		HashMap<String, ArrayList<String>> schemes = encSchemes.getSchemes();
		
		//1.2 operatori "unici" nella query
		ArrayList<String> nodeTypes = new ArrayList<String>();
		for(int i = 0;i < operators.size();i++)
			nodeTypes.add(operators.get(i).getNodeType());
		Collections.sort(nodeTypes);
		
		//1.3 per ogni operatore unico vado a ricostruire i metodi di enc supportati
		HashMap<String, ArrayList<String>> encTable = new HashMap<String, ArrayList<String>>();
		ArrayList<String> tmpList = null;
		for(int i = 0;i< nodeTypes.size();i++)
		{
			tmpList = new ArrayList<String>();
			for (Entry<String, ArrayList<String>> entry : schemes.entrySet()) 
			{
	            ArrayList<String> values = entry.getValue();
				for(int j=0;j<values.size();j++)
				{
					if(values.get(j).equals(nodeTypes.get(i))) //l'oratore fa parte di questo sistema di encryption allora aggiorno l'array list per
																//la mappa di cifratura
					{
						tmpList.add(entry.getKey());
					}
				}
	        }
			encTable.put(nodeTypes.get(i), tmpList);
			tmpList = null;
		}
		
		//2 scansione di ogni operatore con il costo, genero tutte le possibili alternative
		
		//2.1 calcolo il totale delle alternative				
		int possibility = 1;
		for(int i = 0;i<operators.size();i++)
		{
			Operator currentOperator = operators.get(i);
			ArrayList<String> currentOperatorMethods = encTable.get(currentOperator.getNodeType());
			possibility *= currentOperatorMethods.size();			
		}
		
		//2.2 per ogni possibilità adotto un sistema simil "contatori" per provarle tutte
		int counterSize = operators.size();
		int[] counters = new int[counterSize];
		for(int i = 0;i<counters.length;i++)
			counters[i] = 1;
		
		//2.3 genero le possibilità
		while(possibility > 0)
		{
			
			/* DA FINIRE */
			possibility--;
		}
		
		
		
	}
	

}
