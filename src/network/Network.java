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
		StringBuilder sb = new StringBuilder("Network: \n");
		for (Node node: nodes)
			sb.append(node + "\n");
		return sb.toString();
	}


	public Node getNodeByName(String name)
	{
		if(name.equals("NoNodeNeeded") || name == null)
			return getBestNode();

		for (Node node: nodes)
			if (node.getName().equals(name))
				return node;
		return null;
	}

	public Node getBestNode() {
		//miglior nodo per throughput  -->più alto il throughput più le prestazioni della macchina sono alte..e quindi il bclo andrà meglio di
		//conseguenza
		
		Node best = null;
		for (Node node: nodes)
			if (best == null || node.getAesThroughput() > best.getAesThroughput())
				best = node;
		return best;
	}

	public int getNodesNumber()
	{
		return nodes.size();
		/*
		int output = 0;
		for(int i = 0; i<nodes.size(); i++)
			if(!nodes.get(i).getType().equals("Client"))
				output++;
		return output;
		*/
	}

	public Node getNodeByIndex(int i)
	{
		return nodes.get(i);
	}

}
