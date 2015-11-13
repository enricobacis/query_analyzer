package enviroment;

public class Attempt {
	
	private String operations;
	private double time;
	private double cost;
	private String counters;
	private String networkCounters;
	
	
	public Attempt(String o, double t, double c, String cs, String ncs)
	{
		operations = o;
		time = t;
		cost = c;
		counters = cs;
		networkCounters = ncs;
	}
	
	public String getOperations() {
		return operations;
	}

	public void setOperations(String operations) {
		this.operations = operations;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String toString()
	{
		return "\n Attempt: -> Time: "+time+" | Cost: "+cost+" | "+
				 "Counter Encryption Status :"+counters+
				 "Counter Network Status :"+networkCounters
				 ;
	}

	

}
