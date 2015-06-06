package parser;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Operator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserXML {
	
	public static boolean plan;
	public static boolean plans;
	public static boolean node_type;
	public static boolean parent_relationship;
	public static boolean relation_name;
	
	private String tmp_node_type;
	private String tmp_parent_relationship;
	private String tmp_relation_name;
	
	public ParserXML()
	{
		//...singleton?
	}
	
	
	/*
	 * Logica del parse: ad ogno i nodo "plan" crea una struttura, nella quale vado ad inserire diversi campi
	 * ->tipo di operatore (node type)
	 * ->parent relationship
	 * ->tabella coinvolta (relation name)
	 *  
	 */
	public ArrayList<Operator> parseDocument(String res)
	{
		final ArrayList<Operator> output = new ArrayList<Operator>();
		
		
    try { 
    	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser(); 
		DefaultHandler handler = new DefaultHandler() {
	  
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
					
					
					if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					}
					
					if (qName.equalsIgnoreCase("RELATION-NAME")) {
						relation_name = true;
					}
					
					if (qName.equalsIgnoreCase("PARENT-RELATIONSHIP")) {
						parent_relationship = true;
					}
					
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("OUTPUT")) {						
						output.add(new Operator(tmp_node_type,
												tmp_parent_relationship,
												tmp_relation_name));
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (node_type) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_node_type = value;
						node_type = false;
					}
					
					if (parent_relationship) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_parent_relationship = value;
						parent_relationship = false;
					}
					
					if (relation_name) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_relation_name = value;
						relation_name = false;
					}
					
					
			  
				} 
	     };
 
	     saxParser.parse(res, handler);
	     
 
     } catch (Exception e) {
       e.printStackTrace();
     }
    	
    	return output;
   }
 
}