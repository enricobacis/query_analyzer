package model;

public class Operator {
	
	private String nodeType;
	private String relationName;
	private String parentRelationship;
	
	public Operator(String tmp_node_type, String tmp_parent_relationship, String tmp_relation_name)
	{
		this.nodeType = tmp_node_type;
		this.parentRelationship = tmp_parent_relationship;
		this.relationName = tmp_relation_name;
	}
	
	public String toString()
	{
		return "Node-Type: "+nodeType
				+" | Relation-Name: "+relationName
				+" | Parent-Relationship: "+parentRelationship;
	}
	
	public String getNodeType()
	{
		return this.nodeType;
	}
	
	public String getRelationName()
	{
		return this.relationName;
	}
	
	public String getParentRelationship()
	{
		return this.parentRelationship;
	}
	
	public void setNodeType(String s)
	{
		this.nodeType = s;
	}
	
	public void setRelationName(String s)
	{
		this.relationName = s;
	}
	
	public void setParentRelationship(String s)
	{
		this.parentRelationship = s;
	}

}
