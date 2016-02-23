package main;

import enviroment.Environment;

public class Main {

	public static void main(String[] args) {

		String query = "res/4.xml";

		// client
		Environment.quietAnalyzeQuery("config/new/bench5.xml", query);
		
		// client + 1 storage
		Environment.quietAnalyzeQuery("config/new/bench6.xml", query);
		
		// client + 1 storage + 1 computational
		Environment.quietAnalyzeQuery("config/new/bench7.xml", query);
		
		// client + 2 storage + 1 computational
		Environment.quietAnalyzeQuery("config/new/bench8.xml", query);
		
		// client + 8 storage + 1 computational
		Environment.quietAnalyzeQuery("config/new/bench9.xml", query);

	}

}
