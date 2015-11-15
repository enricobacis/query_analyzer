package extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import model.Operator;

public class TPCHUtils {

	//classe di supporto che rappresenta una colonna all'interno di una tabella del tpch
	private class Column {
		private String columnName;
		private int columnWidth;

		public Column(String name, int width) {
			this.columnName = name;
			this.columnWidth = width;
		}

		public String getColumnName() {
			return columnName;
		}

		public int getColumnWidth() {
			return columnWidth;
		}
	}

	private Map<String,Integer> operators;
	private static ImmutableMap<String, ImmutableList<Column>> structure;
	private ImmutableSet<String> tableNames;
	public final static int tpch_num = 22;

	public TPCHUtils() {
		operators = new HashMap<String,Integer>();
		
		structure = ImmutableMap.<String, ImmutableList<Column>>builder()
		.put("customer", ImmutableList.of(
				 new Column("c_custkey",        4),
				 new Column("c_name",          25),
				 new Column("c_address",       40),
				 new Column("c_nationkey",      4),
				 new Column("c_phone",         15),
				 new Column("c_acctbal",       15),
				 new Column("c_mktsegment",    10),
				 new Column("c_comment",      117)))
		
		.put("nation", ImmutableList.of(
				 new Column("n_nationkey",      4),
				 new Column("n_name",          25),
				 new Column("n_regionkey",      4),
				 new Column("n_comment",      152)))
		
		.put("lineitem", ImmutableList.<Column>builder()
			.add(new Column("l_orderkey",       4))
			.add(new Column("l_partkey",        4))
			.add(new Column("l_suppkey",        4))
			.add(new Column("l_linenumber",     4))
			.add(new Column("l_quantity",      15))
			.add(new Column("l_extendedprice", 15))
			.add(new Column("l_discount",      15))
			.add(new Column("l_tax",           15))
			.add(new Column("l_returnflag",     1))
			.add(new Column("l_linestatus",     1))
			.add(new Column("l_shipdate",       4))
			.add(new Column("l_commitdate",     4))
			.add(new Column("l_receiptdate",    4))
			.add(new Column("l_shipinstruct",  25))
			.add(new Column("l_shipmode",      10))
			.add(new Column("l_comment",       44))
			.build())
		
		.put("orders", ImmutableList.of(
				 new Column("o_orderkey",       4),
				 new Column("o_custkey",        4),
				 new Column("o_orderstatus",    1),
				 new Column("o_totalprice",    15),
				 new Column("o_orderdate",      4),
				 new Column("o_orderpriority", 15),
				 new Column("o_clerk",         15),
				 new Column("o_shippriority",   4),
				 new Column("o_comment",       79)))
		
		.put("part", ImmutableList.of(
				 new Column("p_partkey",        4),
				 new Column("p_name",          55),
				 new Column("p_mfgr",          15),
				 new Column("p_brand",         10),
				 new Column("p_type",          25),
				 new Column("p_size",           4),
				 new Column("p_container",     10),
				 new Column("p_retailprice",   15),
				 new Column("p_comment",       23)))
		
		.put("partsupp", ImmutableList.of(
				 new Column("ps_partkey",       4),
				 new Column("ps_suppkey",       4),
				 new Column("ps_availqty",      4),
				 new Column("ps_supplycost",   15),
				 new Column("ps_comment",     199)))
		
		.put("region", ImmutableList.of(
				 new Column("r_regionkey",      4),
				 new Column("r_name",          25),
				 new Column("r_comment",      152)))
		
		.put("supplier", ImmutableList.of(
				 new Column("s_suppkey",        4),
				 new Column("s_name",          25),
				 new Column("s_address",       40),
				 new Column("s_nationkey",      4),
				 new Column("s_phone",         15),
				 new Column("s_acctbal",       15),
				 new Column("s_comment",      101)))

		.build();
		
		tableNames = structure.keySet();
	}
	
	public List<String> findColumnsInString(String s) {
		List<String> output = new ArrayList<String>();

		for (String table: tableNames)
			for (Column column: structure.get(table))
				if (s.indexOf(column.columnName) > -1)
					output.add(column.columnName);

		return output;
	}


	public void inflateOperators(List<Operator> ops) {
		for (Operator op: ops) {
			String operator = op.getNodeType();
			if (operators.containsKey(operator)) {
				int check = operators.get(operator);
				operators.remove(operator);
				operators.put(operator, check + 1);
			} else
				operators.put(operator, 1);
		}
	}

	public Map<String,Integer> getAllOperators() {
		return operators;
	}

	public ImmutableMap<String, ImmutableList<Column>> getStructure() {
		return structure;
	}

	public String getItemColumn(String relationName) {
		String[] output = relationName.split("\\."); //nome_tabella.nome_colonna
		return output.length > 1 ? output[1] : output[0];
	}

	//replace dell'alias
	public String getItemTable(String relationName) {
		relationName.replaceAll("_[1-5]", "");
		String[] output = relationName.split("\\."); //nome_tabella.nome_colonna
		return output[0]; //query su singola tabella
	}

	public int findWidthByColumn(String table, String column) {
		for (Column col: structure.get(table))
			if (col.getColumnName().equals(column))
				return col.getColumnWidth();
		return -1;
	}

	public static boolean isEquality(String string) {
		return string.contains("=");
	}

}
