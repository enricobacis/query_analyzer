package model;

import java.util.List;

public class Operator {

	private String nodeType;
	private String relationName;
	private String parentRelationship;
	private int actualLoops;
	private int planRows; //righe ritornate dall'operatore
	private int planWidth; //dimensione in byte delle righe (o della singola riga?)
	private int id;
	private int id_parent;
	private List<String> output;
	private List<String> implicit;
	private String filter;

	public Operator() {}
	
	//usato solo dal parser simple
	public Operator(String tmp_node_type, int id, int id_parent) {
		this.setNodeType(tmp_node_type);
		this.setIdParent(id_parent);
		this.setId(id);
	}

	public String toString() {
		return "Node-Type: " + nodeType
				+ " | ID: " + id
				+ " | Parent ID: " + id_parent
				+ " | Relation Name: " + relationName
				+ " | Parent Relationship: " + parentRelationship
				+ " | Plan Rows: " + planRows
				+ " | Plan Width: " + planWidth
				+ " | Actual Loops: " + actualLoops
				+ " | Output Items: " + (output == null ? "" : output.toString())
				+ " | Implicit Items: " + (implicit == null ? "" : implicit.toString())
				+ " | Filter: " + filter;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public String getRelationName() {
		return this.relationName;
	}

	public String getParentRelationship() {
		return this.parentRelationship;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public void setParentRelationship(String parentRelationship) {
		this.parentRelationship = parentRelationship;
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

	public int getPlanRows() {
		return planRows;
	}

	public void setPlanRows(int planRows) {
		this.planRows = planRows;
	}

	public int getPlanWidth() {
		return planWidth;
	}

	public void setPlanWidth(int planWidth) {
		this.planWidth = planWidth;
	}

	public int getActualLoops() {
		return actualLoops;
	}

	public void setActualLoops(int actualLoops) {
		this.actualLoops = actualLoops;
	}

	public List<String> getOutput() {
		return output;
	}

	public void setOutput(List<String> output) {
		this.output = output;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public List<String> getImplicit() {
		return implicit;
	}

	public void setImplicit(List<String> implicit) {
		this.implicit = implicit;
	}

}
