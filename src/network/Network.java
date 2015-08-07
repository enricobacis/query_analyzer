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

	public String searchNodeByRelation(String dataNeeded) { //se ci sono più nodi candidati
		
		/*//devo scegliere il migliore per throughput
		ArrayList<Node> candidates = new ArrayList<Node>();
		Node output = null;
		
		for(int i=0;i<nodes.size();i++)
		{
			ArrayList<String> datas = nodes.get(i).getData();
			if(datas != null)
			{
				for(int j=0;j<datas.size();j++)
					if(datas.get(j).equals(dataNeeded))
					{
						candidates.add(nodes.get(i));
						break;
					}
			}
		}
		
		if(candidates.size() > 0)
		{
			output = candidates.get(0);
			for(int i = 1; i<candidates.size(); i++)
				if(candidates.get(i).getAesThroughput() > output.getAesThroughput())
					output = candidates.get(i);	
			return output.getName();
		}
		*/
		return null;
		
	}
	
	public String getNodePolicy(String nodeName)
	{
		/*
		String output="Plain"; //di default non impongo nessuna encryption
		for(int i = 0;i<nodes.size();i++)
			if(nodes.get(i).getName().equals(nodeName))
			{
				output = nodes.get(i).getPolicy();
				break;
			}
		return output;
		*/
		return null;
	}
	
	public Node getNodeByName(String name)
	{
		if(name.equals("NoNodeNeeded") || name == null)
			return getBestNode();
		
		for(int i = 0;i<nodes.size();i++)
			if(nodes.get(i).getName().equals(name))
				return nodes.get(i);
		return null;
	}

	public Node getBestNode() {
		
		//miglior nodo per throughput  -->più alto il throughput più le prestazioni della macchina sono alte..e quindi il bclo andrà meglio di
		//conseguenza
		Node output = nodes.get(0);
		for(int i = 1; i<nodes.size(); i++)
			if(nodes.get(i).getAesThroughput() > output.getAesThroughput())
				output = nodes.get(i);			
		
		return output;
	}

}
