package extra;

import java.util.ArrayList;
import java.util.HashMap;



import model.Operator;

public class TPCHUtils {
	
	private class Column //classe di supporto che rappresenta una colonna all'interno di una tabella del tpch
	{
		private String columnName;
		private int columnWidth;
		
		public Column(String name, int width)
		{
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
	
	private static HashMap<String,Integer> operators;
	private static HashMap<String, ArrayList<Column>> structure;
	private static ArrayList<String> tableNames;
	
	public final static int tpch_num = 22;
	
	public TPCHUtils()
	{
		operators = new HashMap<String,Integer>();
		structure = new HashMap<String, ArrayList<Column>>();
		tableNames = new ArrayList<String>();
		createTPCHStructure();
	}
	
	public void createTPCHStructure()
	{
		String tableName = "customer";
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column("c_custkey", 4));
		cols.add(new Column("c_name", 25));
		cols.add(new Column("c_address", 40));
		cols.add(new Column("c_nationkey", 4));
		cols.add(new Column("c_phone", 15));
		cols.add(new Column("c_acctbal", 15));
		cols.add(new Column("c_mktsegment", 10));
		cols.add(new Column("c_comment", 117));	
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "lineitem";
		cols = new ArrayList<Column>();
		cols.add(new Column("l_orderkey", 4));
		cols.add(new Column("l_partkey", 4));
		cols.add(new Column("l_suppkey", 4));
		cols.add(new Column("l_linenumber", 4));
		cols.add(new Column("l_quantity", 15));
		cols.add(new Column("l_extendedprice", 15));
		cols.add(new Column("l_discount", 15));
		cols.add(new Column("l_tax", 15));
		cols.add(new Column("l_returnflag", 1));
		cols.add(new Column("l_linestatus", 1));
		cols.add(new Column("l_shipdate", 4));
		cols.add(new Column("l_commitdate", 4));
		cols.add(new Column("l_receiptdate", 4));
		cols.add(new Column("l_shipinstruct", 25));
		cols.add(new Column("l_shipmode", 10));
		cols.add(new Column("l_comment", 44));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "nation";
		cols = new ArrayList<Column>();
		cols.add(new Column("n_nationkey", 4));
		cols.add(new Column("n_name", 25));
		cols.add(new Column("n_regionkey", 4));
		cols.add(new Column("n_comment", 152));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "orders";
		cols = new ArrayList<Column>();
		cols.add(new Column("o_orderkey", 4));
		cols.add(new Column("o_custkey", 4));
		cols.add(new Column("o_orderstatus", 1));
		cols.add(new Column("o_totalprice", 15));
		cols.add(new Column("o_orderdate", 4));
		cols.add(new Column("o_orderpriority", 15));
		cols.add(new Column("o_clerk", 15));
		cols.add(new Column("o_shippriority", 4));
		cols.add(new Column("o_comment", 79));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "part";
		cols = new ArrayList<Column>();
		cols.add(new Column("p_partkey", 4));
		cols.add(new Column("p_name", 55));
		cols.add(new Column("p_mfgr", 15));
		cols.add(new Column("p_brand", 10));
		cols.add(new Column("p_type", 25));
		cols.add(new Column("p_size", 4));
		cols.add(new Column("p_container", 10));
		cols.add(new Column("p_retailprice", 15));
		cols.add(new Column("p_comment", 23));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "partsupp";
		cols = new ArrayList<Column>();
		cols.add(new Column("ps_partkey", 4));
		cols.add(new Column("ps_suppkey", 4));
		cols.add(new Column("ps_availqty", 4));
		cols.add(new Column("ps_supplycost", 15));
		cols.add(new Column("ps_comment", 199));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "region";
		cols = new ArrayList<Column>();
		cols.add(new Column("r_regionkey", 4));
		cols.add(new Column("r_name", 25));
		cols.add(new Column("r_comment", 152));
		structure.put(tableName, cols);
		tableNames.add(tableName);
		
		tableName = "supplier";
		cols = new ArrayList<Column>();
		cols.add(new Column("s_suppkey", 4));
		cols.add(new Column("s_name", 25));
		cols.add(new Column("s_address", 40));
		cols.add(new Column("s_nationkey", 4));
		cols.add(new Column("s_phone", 15));
		cols.add(new Column("s_acctbal", 15));
		cols.add(new Column("s_comment", 101));
		structure.put(tableName, cols);		
		tableNames.add(tableName);
	}
	
	public static ArrayList<String> findColumnsInString(String s)
	{
		ArrayList<String> output = new ArrayList<String>();
		for(int i = 0; i<tableNames.size();i++)
		{
			ArrayList<Column> cols = structure.get(tableNames.get(i));
			for(int j=0;j<cols.size();j++)
			{
				if(s.indexOf(cols.get(j).columnName) > -1)
					output.add(cols.get(j).columnName);
			}
		}
		return output;
	}
	
	
	public void inflateOperators(ArrayList<Operator> op)
	{
		for(int i = 0;i<op.size();i++)
		{
			String operator = op.get(i).getNodeType();
			if(operators.containsKey(operator))
			{
				int check = operators.get(operator);
				check++;
				operators.remove(operator);
				operators.put(operator, check);
			}
			else
				operators.put(operator,1);	
			
		}
		
	}
	
	public HashMap<String,Integer> getAllOperators()
	{
		return operators;
	}
	
	public static HashMap<String, ArrayList<Column>> getStructure()
	{
		return structure;
	}

	public static String getItemColumn(String relationName) {
		String[] output = relationName.split("\\."); //nome_tabella.nome_colonna
		if(output.length > 1)
			return output[1];
		else
			return output[0]; //query su singola tabella
	}
	
	public static String getItemTable(String relationName) {
		//replace dell'alias
		if(relationName.indexOf("1") > -1 || relationName.indexOf("2") > -1 || relationName.indexOf("3") > -1 || relationName.indexOf("4") > -1 || relationName.indexOf("5") > -1)
		{
			relationName = relationName.replace("_1", "");
			relationName = relationName.replace("_2", "");
			relationName = relationName.replace("_3", "");
			relationName = relationName.replace("_4", "");
			relationName = relationName.replace("_5", "");
		}
		String[] output = relationName.split("\\."); //nome_tabella.nome_colonna
		return output[0]; //query su singola tabella
	}

	public static int findWidthByColumn(String table, String column) {
		int output = 4; //dimensione di default di moltissimi campi
		ArrayList<Column> tableFields = structure.get(table);
		for(int i = 0;i<tableFields.size();i++)
		{
			if(tableFields.get(i).getColumnName().equals(column))
			{
				output = tableFields.get(i).getColumnWidth();
				break;
			}
		}
		return output;
	}

	public static boolean isEquality(String string) {
		if(string.indexOf("=") >= 0)
			return true;
		return false;
	}
	
}
