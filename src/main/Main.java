package main;

import java.util.ArrayList;

import enviroment.Analyzer;
import extra.TPCHUtils;
import network.Network;
import model.EncSchemes;
import model.Operator;
import parser.ParserEncSchemes;
import parser.ParserNetwork;
import parser.ParserXML;

public class Main {
	
	
	public static void main(String[] args) {
		
		ParserXML parser = new ParserXML();
		ParserNetwork parsernetwork = new ParserNetwork();
		ParserEncSchemes parserencschemes = new ParserEncSchemes();
		
		/* ANALISI DI TUTTE LE QUERY TPCH */
		TPCHUtils tpchUtils = new TPCHUtils();
		for(int t = 1;t<=TPCHUtils.tpch_num;t++)
		{
			ArrayList<Operator> queryOperators = parser.parseDocument("res/"+t+".xml");
			tpchUtils.inflateOperators(queryOperators);			
		}
		System.out.println(tpchUtils.getAllOperators().toString());
		
		/* MI LIMITO ALLA 22 */
		ArrayList<Operator> queryOperators = parser.parseDocument("res/22.xml");
		
		System.out.println(queryOperators.size());
		for(int i = 0;i<queryOperators.size();i++)
			System.out.println(queryOperators.get(i).toString());
		
		/* PARSING DEL NETWORK */
		Network network = new Network(parsernetwork.parseDocument("config/netconfig.xml"));
		System.out.println(network.showNetwork());
		
		/* PARSING DELLA CONFIGURAZIONE DEGLI OPERATORI */
		EncSchemes encSchemes = new EncSchemes(parserencschemes.parseDocument("config/encschemes.xml"));
		System.out.println(encSchemes.showEncSchemes());
		
		/* ANALISI DELLA QUERY */
		Analyzer analyzer = new Analyzer();
		analyzer.Analyze(encSchemes, queryOperators, network);
		System.out.println("MIN COST: "+analyzer.getMinCost());
		System.out.println("OPERATIONS: "+analyzer.getOperations());
		
	}

}
