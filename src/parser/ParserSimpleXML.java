package parser;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Operator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserSimpleXML {
	
	public static boolean node_type;
	
	private String tmp_node_type;
	
	public ParserSimpleXML()
	{
	}
	
	
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
									
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("NODE-TYPE")) {						
						output.add(new Operator(tmp_node_type,
												0,
												0));
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (node_type) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_node_type = value;
						node_type = false;
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