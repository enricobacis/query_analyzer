package network;

import java.util.List;

public class Node {

	private String name;
	private String type;

	private List<String> plainVisibility;
	private List<String> encryptedVisibility;

	//performance
	private double aesThroughput;
	private double bcloValueTime;
	private double paillerThroughput;

	//cost
	private double costPerSecond;

	//links
	private List<Link> links;

	public Node() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<String> getPlainVisibility() {
		return plainVisibility;
	}

	public void setPlainVisibility(List<String> plainVisibility) {
		this.plainVisibility = plainVisibility;
	}

	public List<String> getEncryptedVisibility() {
		return encryptedVisibility;
	}

	public void setEncryptedVisibility(List<String> encryptedVisibility) {
		this.encryptedVisibility = encryptedVisibility;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<Link> getLinks() {
		return links;
	}

	public double getCostPerSecond() {
		return costPerSecond;
	}

	public void setCostPerSecond(double costPerSecond) {
		this.costPerSecond = costPerSecond;
	}

	public double getPaillerThroughput() {
		return paillerThroughput;
	}

	public void setPaillerThroughput(double paillerThroughput) {
		this.paillerThroughput = paillerThroughput;
	}

	public String verifyPolicy(String attribute) {
		if (encryptedVisibility.contains(attribute))
			return "Encrypted";
		if (plainVisibility.contains(attribute))
			return "Plain";
		return "No";
	}
	
	public String toString() {
		String stringDataPlain = "";
		if (plainVisibility != null && plainVisibility.size() > 0)
			stringDataPlain = plainVisibility.toString();

		String stringDataEnc = "";
		if(encryptedVisibility != null && encryptedVisibility.size() > 0)
			stringDataEnc = encryptedVisibility.toString();

		return "Type: " + type +
				" | Name: " + name +
				" | PlainVisibility: " + stringDataPlain +
				" | EncryptedVisibility: " + stringDataEnc +
				" | Performance:  AES -> " + aesThroughput + " BCLO -> " + bcloValueTime +
				" | Cost Per Second:  " + costPerSecond +
				" | Links : " + links.toString();
	}

}
