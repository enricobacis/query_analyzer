package network;

public class Node {

	private String type;
	private String policy;
	private String data;
	private String name;
	
	public Node(String t,String p,String d, String n)
	{
		type = t;
		policy = p;
		data = d;
		name = n;
	}
	
	public String toString()
	{
		return "Type: "+type+
				" | Name: "+name+
				" | Policy: "+policy+
				" | Data: "+data;
	}
	
	public String getData()
	{
		return data;
	}
	
	public String getName()
	{
		return name;
	}
	
}
