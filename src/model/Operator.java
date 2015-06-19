package model;

public class Operator {
	
	private String nodeType;
	private String relationName;
	private String parentRelationship;
	private int id;
	private int id_parent;
	
	public Operator(String tmp_node_type, String tmp_parent_relationship, String tmp_relation_name, int id, int id_parent)
	{
		this.nodeType = tmp_node_type;
		this.parentRelationship = tmp_parent_relationship;
		this.relationName = tmp_relation_name;
		this.setId(id);
		this.setIdParent(id_parent);
	}
	
	public Operator()
	{
		
	}
	
	public String toString()
	{
		return "Node-Type: "+nodeType
				+" | Relation-Name: "+relationName
				+" | Parent-Relationship: "+parentRelationship
				+" | ID: "+id
				+" | Parent ID: "+id_parent;
		
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdParent() {
		return id_parent;
	}

	public void setIdParent(int id_parent) {
		this.id_parent = id_parent;
	}
	
	

}
