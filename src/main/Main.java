package main;

import java.util.ArrayList;

import enviroment.Analyzer;
import extra.TPCHUtils;
import model.EncSchemes;
import model.Operator;
import network.Network;
import parser.ParserNetwork;
import parser.ParserSimpleXML;
import parser.ParserXML;

public class Main {
	
	
	public static void main(String[] args) {
		
		ParserXML parser = new ParserXML(); //parser che crea la struttura ad albero
		ParserSimpleXML parserSimple = new ParserSimpleXML(); //parser che non si preoccupa della struttura ma estrae gli operatori di una query
		ParserNetwork parsernetwork = new ParserNetwork();
		//ParserEncSchemes parserencschemes = new ParserEncSchemes();
		
		
		/* ANALISI DI TUTTE LE QUERY TPCH */
		//voglio sapere il numero di operatori distinti e con che frquenza compaiono
		TPCHUtils tpchUtils = new TPCHUtils();
		for(int t = 1;t<=TPCHUtils.tpch_num;t++)
		{
			ArrayList<Operator> queryOperators = parserSimple.parseDocument("res/"+t+".xml");
			tpchUtils.inflateOperators(queryOperators);			
		}
		System.out.println(tpchUtils.getAllOperators().toString());
		
		//nuovo metodo di parsing, stabilisce una gerarchia degli operatori
		/* MI LIMITO ALLA 22 */
		parser.parseDocument("res/22.xml");		
		for(int i = 0; i< parser.operators.size(); i++)
					System.out.println(parser.operators.get(i).toString());
		
		
		/* PARSING DEL NETWORK */
		Network network = new Network(parsernetwork.parseDocument("config/netconfig.xml"));
		System.out.println(network.showNetwork());
		
		/* CONFIGURAZIONE DEGLI OPERATORI */		
		EncSchemes encSchemes = new EncSchemes();
		System.out.println(encSchemes.getOperatorsEncs().toString());
		System.out.println(encSchemes.getFunctionsEncs().toString());
		
		
		/* ANALISI DELLA QUERY */
		Analyzer analyzer = new Analyzer();
		analyzer.Analyze(encSchemes, parser.operators, network);
		System.out.println("MIN COST: "+analyzer.getMinCost());
		System.out.println("OPERATIONS: "+analyzer.getOperations());
		
	}

}
