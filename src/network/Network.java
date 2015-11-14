package network;

import java.util.List;

public class Network {

	private List<Node> nodes;

	public Network(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Node getNodeByName(String name) {
		if(name == null || name.equals("NoNodeNeeded"))
			return getBestNode();

		for (Node node: nodes)
			if (node.getName().equals(name))
				return node;
		return null;
	}

	// best node based on throughput
	public Node getBestNode() {
		Node best = null;
		for (Node node: nodes)
			if (best == null || node.getAesThroughput() > best.getAesThroughput())
				best = node;
		return best;
	}

	public int getNodesNumber() {
		return nodes.size();
	}

	public Node getNodeByIndex(int i) {
		return nodes.get(i);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Network: \n");
		for (Node node: nodes)
			sb.append(node + "\n");
		return sb.toString();
	}

}
