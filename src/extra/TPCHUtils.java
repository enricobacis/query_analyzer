package extra;

import java.util.ArrayList;
import java.util.HashMap;

import model.Operator;

public class TPCHUtils {
	
	private static HashMap<String,Integer> operators;
	
	public final static int tpch_num = 22;
	
	public TPCHUtils()
	{
		operators = new HashMap<String,Integer>();
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

}
