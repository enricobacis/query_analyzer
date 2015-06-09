package network;

public class Node {

	private String name;
	private String policy;
	private String data;
	
	public Node(String n,String p,String d)
	{
		name = n;
		policy = p;
		data = d;
	}
	
	public String toString()
	{
		return "Name: "+name+
				" | Policy: "+policy+
				" | Data: "+data;
	}
	
}
