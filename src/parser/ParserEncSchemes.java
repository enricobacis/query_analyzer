package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserEncSchemes {
	
	public static boolean schema;
	public static boolean operator;
	
	private ArrayList<String> tmp_operator;
	private String tmp_schema;
	
	public ParserEncSchemes()
	{
		//...singleton?
	}
	
	public HashMap<String ,ArrayList<String>> parseDocument(String res)
	{
		final HashMap<String ,ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
		
		
    try { 
    	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser(); 
		DefaultHandler handler = new DefaultHandler() {
	  
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
					
					
					if (qName.equalsIgnoreCase("SCHEMA")) {
						schema = true;
						tmp_schema = attributes.getValue("tag");
						tmp_operator = null;
						tmp_operator = new ArrayList<String>(); 
					}
					
					if (qName.equalsIgnoreCase("OPERATOR")) {
						operator = true;
					}
										
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("SCHEMA")) {						
						output.put(tmp_schema, tmp_operator);
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (operator) {
						String value = new String(ch, start, length);
						//System.out.println(value);
						tmp_operator.add(value);
						operator = false;
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