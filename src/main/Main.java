package main;

import java.util.ArrayList;

import network.Network;
import model.EncSchemes;
import model.Operator;
import parser.ParserEncSchemes;
import parser.ParserNetwork;
import parser.ParserXML;

public class Main {

	public final static int tpch_num = 22;
	
	public static void main(String[] args) {
		
		ParserXML parser = new ParserXML();
		ParserNetwork parsernetwork = new ParserNetwork();
		ParserEncSchemes parserencschemes = new ParserEncSchemes();
		
		/* ANALISI DI TUTTE LE QUERY TPCH */
		/*
		for(int t = 1;t<=tpch_num;t++)
		{
			ArrayList<Operator> queryOperators = parser.parseDocument("res/"+t+".xml");
			
			System.out.println(queryOperators.size());
			for(int i = 0;i<queryOperators.size();i++)
				System.out.println(queryOperators.get(i).toString());
		}
		*/
		
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
		

	}

}
