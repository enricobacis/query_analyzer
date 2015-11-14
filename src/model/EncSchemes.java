package model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class EncSchemes {

	// cifrature ammesse DET - NDET - OPE - NO - PAI
	
	public static ImmutableMap<String, ImmutableList<String>> operatorsEncs = ImmutableMap
		.<String, ImmutableList<String>>builder()
		.put("Seq Scan", ImmutableList.of("NDET"))
		.put("Index Scan", ImmutableList.of("NDET"))
		.put("Bitmap Heap Scan", ImmutableList.of("DET"))
		.put("Bitmap Index Scan", ImmutableList.of("DET"))
		.put("Nested Loop", ImmutableList.of("DET"))
		.put("Merge Join", ImmutableList.of("DET"))
		.put("Hash Join", ImmutableList.of("DET"))
		.put("Index Only Scan", ImmutableList.of("NDET"))
		.put("Hash", ImmutableList.of("DET"))
		.put("Sort", ImmutableList.of("OPE"))
		.put("Limit", ImmutableList.of("NO"))
		.put("Aggregate", ImmutableList.of("DET"))
		.put("Subquery Scan", ImmutableList.of("NO"))
		.put("Materialize", ImmutableList.of("DET"))
		.build();
	
	public static ImmutableMap<String, ImmutableList<String>> functionsEncs = ImmutableMap
		.<String, ImmutableList<String>>builder()
		.put("count", ImmutableList.of("DET"))
		.put("sum", ImmutableList.of("PAI"))
		.put("avg", ImmutableList.of("PAI"))
		.put("max", ImmutableList.of("OPE"))
		.put("min", ImmutableList.of("OPE"))
		.put("substring", ImmutableList.of("DET"))
		.build();

}
