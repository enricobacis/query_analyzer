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

	public String searchNodeByRelation(String dataNeeded) {
		for(int i=0;i<nodes.size();i++)
			if(nodes.get(i).getData().equals(dataNeeded))
				return nodes.get(i).getName();
		return "noFound";
	}
	
	public String getNodePolicy(String nodeName)
	{
		String output="Plain"; //di default non impongo nessuna encryption
		for(int i = 0;i<nodes.size();i++)
			if(nodes.get(i).getName().equals(nodeName))
			{
				output = nodes.get(i).getPolicy();
				break;
			}
		return output;
	}

}
