package network;

public class Link
{
	private String nodeLinked;
	private double latency;
	private double throughput;
	
	public Link(String n, double l, double t)
	{
		nodeLinked = n;
		latency = l;
		throughput = t;
	}

	public String getNodeLinked() {
		return nodeLinked;
	}

	public double getLatency() {
		return latency;
	}

	public double getThroughput(){
		return throughput;
	}
	
	public String toString()
	{
		return "Node: "+nodeLinked+
				" | Latency: "+latency+
				" | Throughput: "+throughput;
	}

	
}