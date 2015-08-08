package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import enviroment.Analyzer;
import enviroment.Attempt;
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
		
		/* ANALISI DI TUTTE LE QUERY TPCH */
		//voglio sapere il numero di operatori distinti e con che frquenza compaiono
		TPCHUtils tpchUtils = new TPCHUtils();
		for(int t = 1;t<=TPCHUtils.tpch_num;t++)
		{
			ArrayList<Operator> queryOperators = parserSimple.parseDocument("res/"+t+".xml");
			tpchUtils.inflateOperators(queryOperators);			
		}		
		//System.out.println(tpchUtils.getAllOperators().toString());
		
		/* PARSING DEL NETWORK */
		Network network = new Network(parsernetwork.parseDocument("config/netconfig.xml"));
		
		/* CONFIGURAZIONE DEGLI OPERATORI */		
		EncSchemes encSchemes = new EncSchemes();
		
		
		/* ANALISI DELLE QUERY */
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output/results.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		/*	TUTTO IL BENCHMARK */ 
		/*
		for(int t = 1;t<=TPCHUtils.tpch_num;t++)
		{
			writer.println("QUERY "+t);
			parser.parseDocument("res/"+t+".xml");	
			ArrayList<Attempt> results = new ArrayList<Attempt>();
			Analyzer analyzer = new Analyzer();
			results = analyzer.Analyze(encSchemes, parser.operators, network);
			writer.println("MIN TIME: "+analyzer.getMinTime()+ " sec.");
			writer.println("MIN COST: "+analyzer.getMinCost()+ " €");
			writer.println("OPERATIONS: "+analyzer.getOperations());
			writer.println("RESULTS: "+results.toString());
		}
		*/
		
		
		/* SINGOLA QUERY */
		writer.println("QUERY ");
		parser.parseDocument("res/22.xml");	
		ArrayList<Attempt> results = new ArrayList<Attempt>();
		Analyzer analyzer = new Analyzer();
		
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyy HH:mm:ss");
		Date date = new Date();
		writer.println("START ELABORATION: "+dateFormat.format(date));
		
		results = analyzer.Analyze(encSchemes, parser.operators, network);
		
		date = new Date();
		writer.println("END ELABORATION: "+dateFormat.format(date));
		
		writer.println("MIN TIME: "+analyzer.getMinTime()+ " sec.");
		writer.println("MIN COST: "+analyzer.getMinCost()+ " €");
		writer.println("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations());
		writer.println("MIN COST OPERATIONS: "+analyzer.getMinCostOperations());
		writer.println("RESULTS: "+results.toString());		
		writer.close();
		
		System.out.println("DONE");
	}
	
	

}
