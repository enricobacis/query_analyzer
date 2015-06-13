package extra;

import java.util.ArrayList;

import model.Operator;

public class TPCHUtils {
	
	private static ArrayList<String> operators;
	
	public final static int tpch_num = 22;
	
	public TPCHUtils()
	{
		operators = new ArrayList<String>();
	}
	
	public void inflateOperators(ArrayList<Operator> op)
	{
		for(int i = 0;i<op.size();i++)
		{
			String operator = op.get(i).getNodeType();
			int check = operators.lastIndexOf(operator);
			if(check < 0) //non c'è quindi posso aggiungerlo
				operators.add(operator);			
		}
		
	}
	
	public ArrayList<String> getAllOperators()
	{
		return operators;
	}

}
