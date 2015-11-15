package main;

import java.io.PrintWriter;

import com.google.common.io.ByteStreams;

import enviroment.Environment;

public class Main {

	public static void main(String[] args) {
		
		String bench1 = "config/netconfig_bench1.xml";
		String bench2 = "config/netconfig_bench2.xml";
		String bench3 = "config/netconfig_bench3.xml";
		String bench4 = "config/netconfig_only_client.xml";
		
		String query4 = "res/4.xml";
		
		PrintWriter nullwriter = new PrintWriter(ByteStreams.nullOutputStream());
		
		System.out.println("\nQuery #4 - Bench 1");
		Environment.analyzeQuery(bench1, query4, nullwriter);
		
		System.out.println("\nQuery #4 - Bench 2");
		Environment.analyzeQuery(bench2, query4, nullwriter);
		
		System.out.println("\nQuery #4 - Bench 3");
		Environment.analyzeQuery(bench3, query4, nullwriter);
		
		System.out.println("\nQuery #4 - Bench 4");
		Environment.analyzeQuery(bench4, query4, nullwriter);
	}

}
