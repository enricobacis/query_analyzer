package model;

import java.util.ArrayList;
import java.util.HashMap;

public class EncSchemes {
	
	private HashMap<String, ArrayList<String>> operatorsEncs;
	private HashMap<String, ArrayList<String>> functionsEncs;
	
	public EncSchemes()
	{
		//inizializzazione delle varibili necessarie
		//cifrature ammesse DET - NDET - OPE - NO - PAI
		operatorsEncs = new HashMap<String, ArrayList<String>>();
		functionsEncs = new HashMap<String, ArrayList<String>>();
		ArrayList<String> encs = new ArrayList<String>();
		String operator = "";
		
		operator = "Seq Scan";
		encs.add("NDET");
		operatorsEncs.put(operator, encs);
		
		operator = "Index Scan";
		encs = new ArrayList<String>();
		encs.add("NDET");
		operatorsEncs.put(operator, encs);
		
		operator = "Bitmap Heap Scan";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Bitmap Index Scan";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Nested Loop";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Merge Join";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Hash Join";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Index Only Scan";
		encs = new ArrayList<String>();
		encs.add("NDET");
		operatorsEncs.put(operator, encs);
		
		operator = "Hash";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Sort";
		encs = new ArrayList<String>();
		encs.add("OPE");
		operatorsEncs.put(operator, encs);
		
		operator = "Limit";
		encs = new ArrayList<String>();
		encs.add("NO");
		operatorsEncs.put(operator, encs);
		
		operator = "Aggregate";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		operator = "Subquery Scan";
		encs = new ArrayList<String>();
		encs.add("NO");
		operatorsEncs.put(operator, encs);
		
		operator = "Materialize";
		encs = new ArrayList<String>();
		encs.add("DET");
		operatorsEncs.put(operator, encs);
		
		String function = "";
		function = "count";
		encs = new ArrayList<String>();
		encs.add("DET");
		functionsEncs.put(function,encs);
		
		function = "sum";
		encs = new ArrayList<String>();
		encs.add("PAI");
		functionsEncs.put(function,encs);
		
		function = "avg";
		encs = new ArrayList<String>();
		encs.add("PAI");
		functionsEncs.put(function,encs);	
		
		function = "max";
		encs = new ArrayList<String>();
		encs.add("OPE");
		functionsEncs.put(function,encs);
		
		function = "min";
		encs = new ArrayList<String>();
		encs.add("OPE");
		functionsEncs.put(function,encs);
		
	}

	public HashMap<String, ArrayList<String>> getOperatorsEncs() {
		return operatorsEncs;
	}

	public void setOperatorsEncs(HashMap<String, ArrayList<String>> operatorsEncs) {
		this.operatorsEncs = operatorsEncs;
	}

	public HashMap<String, ArrayList<String>> getFunctionsEncs() {
		return functionsEncs;
	}

	public void setFunctionsEncs(HashMap<String, ArrayList<String>> functionsEncs) {
		this.functionsEncs = functionsEncs;
	}

}
