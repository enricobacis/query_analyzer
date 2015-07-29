package network;

import java.util.ArrayList;

public class Node {

	private String name;
	private String type;
	private String policy;
	private ArrayList<String> data;
	
	//performance
	private double aesThroughput;
	private double bcloValueTime;
	private double paillerThroughput;
	
	//cost
	private double costPerSecond;
	
	//links
	private ArrayList<Link> links;
		
	public Node()
	{
		
	}
		
	public String toString()
	{
		String stringData = "";
		if(data != null && data.size() > 0)
			stringData = data.toString();
		
		return "Type: "+type+
				" | Name: "+name+
				" | Policy: "+policy+
				" | Data: "+stringData+
				" | Performance:  AES -> "+aesThroughput+" BCLO -> "+bcloValueTime+
				" | Cost Per Second:  "+costPerSecond+
				" | Links : "+links.toString();
	}
	
	public ArrayList<String> getData()
	{
		return data;
	}
	
	public void setData(ArrayList<String> i)
	{
		data = i;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public String getPolicy()
	{
		return policy;
	}
	
	public void setPolicy(String s)
	{
		policy = s;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String s)
	{
		type = s;
	}

	public double getAesThroughput() {
		return aesThroughput;
	}

	public void setAesThroughput(double aesThroughput) {
		this.aesThroughput = aesThroughput;
	}

	public double getBcloValueTime() {
		return bcloValueTime;
	}

	public void setBcloValueTime(double bcloValueTime) {
		this.bcloValueTime = bcloValueTime;
	}
	
	public void setLinks(ArrayList<Link> i)
	{
		links = i;
	}
	
	public ArrayList<Link> getLinks()
	{
		return links;
	}
	
	public double getCostPerSecond()
	{
		return costPerSecond;
	}
	
	public void setCostPerSecond(double i)
	{
		costPerSecond = i;
	}

	public double getPaillerThroughput() 
	{
		return paillerThroughput;
	}

	public void setPaillerThroughput(double paillerThroughput) 
	{
		this.paillerThroughput = paillerThroughput;
	}
	
}
