package main;

import java.util.ArrayList;

import model.Operator;
import parser.ParserXML;

public class Main {

	public final static int tpch_num = 22;
	
	public static void main(String[] args) {
		
		ParserXML parser = new ParserXML();
		
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
		
		ArrayList<Operator> queryOperators = parser.parseDocument("res/22.xml");
		
		System.out.println(queryOperators.size());
		for(int i = 0;i<queryOperators.size();i++)
			System.out.println(queryOperators.get(i).toString());
		
		//fare il parser del network
		

	}

}
