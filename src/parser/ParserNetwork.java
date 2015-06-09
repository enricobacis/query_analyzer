package parser;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import network.Node;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserNetwork {
	
	public static boolean node;
	public static boolean node_type;
	public static boolean node_policy;
	public static boolean node_data;
	
	private String tmp_node_type;
	private String tmp_node_policy;
	private String tmp_node_data;
	
	public ParserNetwork()
	{
		//...singleton?
	}
	
	public ArrayList<Node> parseDocument(String res)
	{
		final ArrayList<Node> output = new ArrayList<Node>();
		
		
    try { 
    	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser(); 
		DefaultHandler handler = new DefaultHandler() {
	  
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
					
					
					if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					}
					
					if (qName.equalsIgnoreCase("NODE-POLICY")) {
						node_policy = true;
					}
					
					if (qName.equalsIgnoreCase("NODE-DATA")) {
						node_data = true;
					}
					
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("NODE")) {						
						output.add(new Node(tmp_node_type, tmp_node_policy, tmp_node_data));
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (node_type) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_node_type = value;
						node_type = false;
					}
					
					if (node_policy) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_node_policy = value;
						node_policy = false;
					}
					
					if (node_data) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_node_data = value;
						node_data = false;
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