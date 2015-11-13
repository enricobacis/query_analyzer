package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Operator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserXML {

	public ArrayList<Operator> operators;

	private static boolean node_type;
	private static boolean parent_relationship;
	private static boolean relation_name;
	private static boolean plan_rows;
	private static boolean plan_width;
	private static boolean actual_loops;
	private static boolean output;
	private static boolean item;

	private static boolean filter;
	private static boolean filter_single;

	private Operator tmp;
	private ArrayList<String> tmpOutput;
	private ArrayList<String> tmpImplicit;

	private int id;
	private int id_parent;

	private HashMap<Integer, Boolean> subPlans;

	public ParserXML()
	{
		id=-1;
		id_parent=-1;
		operators = new ArrayList<Operator>();
		subPlans = new HashMap<Integer, Boolean>();
	}

	public void clearParser()
	{
		id=-1;
		id_parent=-1;
		operators = new ArrayList<Operator>();
		subPlans = new HashMap<Integer, Boolean>();
	}


	/*
	 * Logica del parse: ad ogno i nodo "plan" crea una struttura, nella quale vado ad inserire diversi campi
	 * ->tipo di operatore (node type)
	 * ->parent relationship
	 * ->tabella coinvolta (relation name)
	 *
	 */
	public void parseDocument(String res)
	{

    try {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		DefaultHandler handler = new DefaultHandler() {

				public void startElement(String uri, String localName,String qName,
			                Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("PLAN")) {
						tmp = new Operator();
						tmpImplicit = new ArrayList<String>();
						id++;
						subPlans.put(id, false);
					}

					if (qName.equalsIgnoreCase("PLANS")) {

						//aggiorno lo stato delle subquery
						tmp.setImplicit(tmpImplicit);

						subPlans.remove(id);
						subPlans.put(id, true);

						tmp.setId(id);
						tmp.setIdParent(id_parent);
						operators.add(tmp);
						id_parent = id;
					}

					//per leggere i dati
					if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					}

					if (qName.equalsIgnoreCase("PLAN-ROWS")) {
						plan_rows = true;
					}

					if (qName.equalsIgnoreCase("PLAN-WIDTH")) {
						plan_width = true;
					}

					if (qName.equalsIgnoreCase("ACTUAL-LOOPS")) {
						actual_loops = true;
					}

					if (qName.equalsIgnoreCase("RELATION-NAME")) {
						relation_name = true;
					}

					if (qName.equalsIgnoreCase("PARENT-RELATIONSHIP")) {
						parent_relationship = true;
					}

					if (qName.equalsIgnoreCase("OUTPUT")) {
						output = true;
						tmpOutput = new ArrayList<String>();
					}

					if (qName.equalsIgnoreCase("ITEM") && output == true) {
						item = true;
					}

					if (qName.equalsIgnoreCase("ITEM") && filter == true) {
						filter_single = true;
					}

					//operatori impliciti
					if (qName.equalsIgnoreCase("FILTER")) {
						filter_single = true;
					}

					if (qName.equalsIgnoreCase("GRUOP-KEY")) {
						filter = true;
					}

					if (qName.equalsIgnoreCase("SORT-KEY")) {
						filter = true;
					}

					if (qName.equalsIgnoreCase("HASH-COND")) {
						filter_single = true;
					}

					if (qName.equalsIgnoreCase("JOIN-FILTER")) {
						filter_single = true;
					}

					if (qName.equalsIgnoreCase("INDEX-COND")) {
						filter_single = true;
					}

					if (qName.equalsIgnoreCase("MERGE-COND")) {
						filter_single = true;
					}

				}

				public void endElement(String uri, String localName,
					String qName) throws SAXException {

					if (qName.equalsIgnoreCase("PLAN")) {
						if(subPlans.get(id) == false) //l'elemento è una foglia non ha sottopiani, lo devo aggiungere
						{
							tmp.setImplicit(tmpImplicit);

							subPlans.remove(id);
							subPlans.put(id, true);

							tmp.setId(id);
							tmp.setIdParent(id_parent);
							operators.add(tmp);
						}
					}

					if (qName.equalsIgnoreCase("OUTPUT")) {
						output = false;
						tmp.setOutput(tmpOutput);
					}

					if (qName.equalsIgnoreCase("GROUP-KEY")) {
						filter = false;
					}

					if (qName.equalsIgnoreCase("SORT-KEY")) {
						filter = false;
					}

				}

				public void characters(char ch[], int start, int length) throws SAXException {


					if (node_type) {
						String value = new String(ch, start, length);
						tmp.setNodeType(value);
						node_type = false;
					}

					if (parent_relationship) {
						String value = new String(ch, start, length);
						tmp.setParentRelationship(value);
						parent_relationship = false;
					}

					if (relation_name) {
						String value = new String(ch, start, length);
						tmp.setRelationName(value);
						relation_name = false;
					}

					if (plan_rows) {
						String value = new String(ch, start, length);
						tmp.setPlanRows(Integer.valueOf(value));
						plan_rows = false;
					}

					if (plan_width) {
						String value = new String(ch, start, length);
						tmp.setPlanWidth(Integer.valueOf(value));
						plan_width = false;
					}

					if (actual_loops) {
						String value = new String(ch, start, length);
						tmp.setActualLoops(Integer.valueOf(value));
						actual_loops = false;
					}

					if (item) {
						String value = new String(ch, start, length);
						tmpOutput.add(value);
						item = false;
					}

					if (filter_single) {
						String value = new String(ch, start, length);
						tmpImplicit.add(value);
						filter_single = false;
					}

				}
	     };

	     saxParser.parse(res, handler);


     } catch (Exception e) {
       e.printStackTrace();
     }


   }

}