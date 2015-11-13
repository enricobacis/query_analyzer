package network;

import java.util.ArrayList;

public class Node {

	private String name;
	private String type;

	private ArrayList<String> plainVisibility;
	private ArrayList<String> encryptedVisibility;

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
		String stringDataPlain = "";
		if(plainVisibility != null && plainVisibility.size() > 0)
			stringDataPlain = plainVisibility.toString();

		String stringDataEnc = "";
		if(encryptedVisibility != null && encryptedVisibility.size() > 0)
			stringDataEnc = encryptedVisibility.toString();

		return "Type: "+type+
				" | Name: "+name+
				" | PlainVisibility: "+stringDataPlain+
				" | EncryptedVisibility: "+stringDataEnc+
				" | Performance:  AES -> "+aesThroughput+" BCLO -> "+bcloValueTime+
				" | Cost Per Second:  "+costPerSecond+
				" | Links : "+links.toString();
	}



	public String getName()
	{
		return name;
	}

	public void setName(String s)
	{
		name = s;
	}


	public ArrayList<String> getPlainVisibility() {
		return plainVisibility;
	}

	public void setPlainVisibility(ArrayList<String> plainVisibility) {
		this.plainVisibility = plainVisibility;
	}

	public ArrayList<String> getEncryptedVisibility() {
		return encryptedVisibility;
	}

	public void setEncryptedVisibility(ArrayList<String> encryptedVisibility) {
		this.encryptedVisibility = encryptedVisibility;
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

	public String verifyPolicy(String attribute)
	{
		if(encryptedVisibility.contains(attribute))
			return "Encrypted";
		if(plainVisibility.contains(attribute))
			return "Plain";
		return "No";
	}

}
