package enviroment;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import com.google.common.io.ByteStreams;

import extra.TPCHUtils;
import network.Network;
import parser.ParserNetwork;
import parser.ParserXML;

public class Environment {
	
	public static void performanceAnalysis(Network network) {
		int networkSize = network.getNodesNumber();
		double total = 0;
		System.out.println("Network size:" + networkSize);
		
		for(int t = 1; t <= TPCHUtils.tpch_num; t++) {
			ParserXML parser = new ParserXML();
			parser.parseDocument("res/" + t + ".xml");
			double networkAttemps = Math.pow(networkSize, parser.operators.size());
			total += networkAttemps;
			System.out.println("Query # " + t + " -> Attempts: " + networkAttemps);
			parser.clearParser();
		}
		
		// computed mean time for a single attempt
		total = total * 0.061;
		double seconds = (total / 1000) % 60;
		double minutes = (total / (1000 * 60)) % 60;
		double hours = (total / (1000 * 60 * 60)) % 24;
		System.out.printf("%.0f hours %.0f minutes %.0f seconds\n", hours, minutes, seconds);
	}
	
	public static void analyzeQuery(Network network, ParserXML query, PrintWriter writer) {
		Analyzer analyzer = new Analyzer();
		writer.println("START ELABORATION: " + new Date());
		List<Attempt> results = analyzer.analyze(query.operators, network);
		writer.println("END ELABORATION: " + new Date());
		System.out.println("--------");
		
		writer.println("MIN TIME: " + analyzer.getMinTime() + " sec.");
		System.out.println("MIN TIME: " + analyzer.getMinTime() + " sec.");
		writer.println("MIN COST: " + analyzer.getMinCost() + " €");
		System.out.println("MIN COST: " + analyzer.getMinCost() + " €");
		
		writer.println("MIN TIME OPERATIONS: " + analyzer.getMinTimeOperations());
		writer.println("MIN COST OPERATIONS: " + analyzer.getMinCostOperations());
		writer.println("RESULTS: " + results.toString());
		writer.close();
		System.out.println("--------");
	}
	
	public static void analyzeQuery(Network network, ParserXML query) {
		analyzeQuery(network, query, new PrintWriter(System.out));
	}
	
	public static void analyzeQuery(String configfile, String queryfile, PrintWriter writer) {
		System.out.println("Query: " + queryfile + ", Configuration: " + configfile);
		Network network = ParserNetwork.parse(configfile);
		ParserXML query = new ParserXML();
		query.parseDocument(queryfile);
		analyzeQuery(network, query, writer);
	}
	
	public static void quietAnalyzeQuery(String configfile, String queryfile) {
		PrintWriter nullwriter = new PrintWriter(ByteStreams.nullOutputStream());
		analyzeQuery(configfile, queryfile, nullwriter);
	}
	
	public static void analyzeQuery(String configfile, String queryfile) {
		analyzeQuery(configfile, queryfile, new PrintWriter(System.out));
	}

}
