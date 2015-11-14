package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncSchemes {

	// cifrature ammesse DET - NDET - OPE - NO - PAI
	private Map<String, List<String>> operatorsEncs;
	private Map<String, List<String>> functionsEncs;

	@SuppressWarnings("serial")
	public EncSchemes()
	{
		operatorsEncs = new HashMap<String, List<String>>() {{
			put("Seq Scan", Arrays.asList("NDET"));
			put("Index Scan", Arrays.asList("NDET"));
			put("Bitmap Heap Scan", Arrays.asList("DET"));
			put("Bitmap Index Scan", Arrays.asList("DET"));
			put("Nested Loop", Arrays.asList("DET"));
			put("Merge Join", Arrays.asList("DET"));
			put("Hash Join", Arrays.asList("DET"));
			put("Index Only Scan", Arrays.asList("NDET"));
			put("Hash", Arrays.asList("DET"));
			put("Sort", Arrays.asList("OPE"));
			put("Limit", Arrays.asList("NO"));
			put("Aggregate", Arrays.asList("DET"));
			put("Subquery Scan", Arrays.asList("NO"));
			put("Materialize", Arrays.asList("DET"));
		}};
		
		functionsEncs = new HashMap<String, List<String>>() {{
			put("count", Arrays.asList("DET"));
			put("sum", Arrays.asList("PAI"));
			put("avg", Arrays.asList("PAI"));
			put("max", Arrays.asList("OPE"));
			put("min", Arrays.asList("OPE"));
			put("substring", Arrays.asList("DET"));
		}};
	}

	public Map<String, List<String>> getOperatorsEncs() {
		return operatorsEncs;
	}

	public void setOperatorsEncs(Map<String, List<String>> operatorsEncs) {
		this.operatorsEncs = operatorsEncs;
	}

	public Map<String, List<String>> getFunctionsEncs() {
		return functionsEncs;
	}

	public void setFunctionsEncs(Map<String, List<String>> functionsEncs) {
		this.functionsEncs = functionsEncs;
	}

}
