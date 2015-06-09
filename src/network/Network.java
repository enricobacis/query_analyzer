package network;

import java.util.ArrayList;

public class Network {
	
	private ArrayList<Node> nodes;
	
	public Network(ArrayList<Node> list)
	{
		nodes = list;
	}
	
	public String showNetwork()
	{
		String output = "Network: \n";
		for(int i = 0;i<nodes.size();i++)
			output += nodes.get(i).toString()+"\n";
		return output;
			
	}

}
