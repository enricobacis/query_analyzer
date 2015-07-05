package enviroment;

public class Attempt {
	
	private String operations;
	private double time;
	private double cost;
	
	public Attempt(String o, double t, double c)
	{
		operations = o;
		time = t;
		cost = c;
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
		return "Attempt: -> Time: "+time+" Cost: "+cost+"\n";
	}

}
