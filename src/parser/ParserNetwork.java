package parser;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import network.Node;
import network.Link;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//basato su SAX
public class ParserNetwork {
	
	public static boolean node;
	public static boolean node_type;
	public static boolean node_datas;
	public static boolean node_data;
	public static boolean performance;
	public static boolean aes;
	public static boolean bclo;
	public static boolean links;
	public static boolean link;
	public static boolean node_linked;
	public static boolean latency;
	public static boolean throughput;
	public static boolean cost_per_second;
	public static boolean pailler;
	
	private Node tmp_node;
	private ArrayList<String> tmp_datas_enc;
	private ArrayList<String> tmp_datas_plain;
	private String tmp_nodeLinked;
	private double tmp_latency;
	private double tmp_throughput;
	private ArrayList<Link> tmp_links;
	private String supportAttribute;
	
	public ParserNetwork()
	{
		
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
					
					if (qName.equalsIgnoreCase("NODE")) {
						node = true;
						tmp_node = new Node();
						String tmp_name = attributes.getValue("name");
						tmp_node.setName(tmp_name);
										
					}
					
					if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					}
					
										
					if (qName.equalsIgnoreCase("NODE-DATAS")) {
						node_datas = true;
						tmp_datas_enc = new ArrayList<String>();
						tmp_datas_plain = new ArrayList<String>();
					}
					
					if (qName.equalsIgnoreCase("NODE-DATA")) {
						supportAttribute = attributes.getValue("attribute");
						node_data = true;
					}
					
					if (qName.equalsIgnoreCase("PERFORMANCE")) {
						performance = true;
					}
					
					if (qName.equalsIgnoreCase("AES-THROUGHPUT")) {
						aes = true;
					}
					
					if (qName.equalsIgnoreCase("PAILLER-THROUGHPUT")) {
						pailler = true;
					}
					
					if (qName.equalsIgnoreCase("BCLO-VALUE-TIME")) {
						bclo = true;
					}
					
					if (qName.equalsIgnoreCase("LINKS")) {
						tmp_links = new ArrayList<Link>();
						links = true;
					}
					
					if (qName.equalsIgnoreCase("LINK")) {
						link = true;
					}
					
					if (qName.equalsIgnoreCase("NODE-LINKED")) {
						node_linked = true;
					}
					
					if (qName.equalsIgnoreCase("LATENCY")) {
						latency = true;
					}
					
					if (qName.equalsIgnoreCase("THROUGHPUT")) {
						throughput = true;
					}
					
					if (qName.equalsIgnoreCase("COST-PER-SECOND")) {
						cost_per_second = true;
					}
					
			 
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					
					if (qName.equalsIgnoreCase("NODE")) {						
						output.add(tmp_node);
						node = false;
					}
					
					if (qName.equalsIgnoreCase("NODE-DATAS")) {	
						tmp_node.setEncryptedVisibility(tmp_datas_enc);
						tmp_node.setPlainVisibility(tmp_datas_plain);
						node_datas= false;
					}
					
					if (qName.equalsIgnoreCase("PERFORMANCE")) {
						performance = false;
					}
					
					if (qName.equalsIgnoreCase("LINKS")) {
						tmp_node.setLinks(tmp_links);
						links = false;
					}
					
					if (qName.equalsIgnoreCase("LINK")) {
						tmp_links.add(new Link(tmp_nodeLinked, tmp_latency, tmp_throughput));
						link = false;
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
										
					if (node_type) {
						String value = new String(ch, start, length);
						tmp_node.setType(value);
						node_type = false;
					}
					
										
					if (node_data) {
						String value = new String(ch, start, length);
						if(value.equals("Encrypted"))
							tmp_datas_enc.add(supportAttribute);
						if(value.equals("Plain"))
							tmp_datas_plain.add(supportAttribute);
						
						// i "No" li conto come se non esistessero...
						
						node_data = false;
					}
					
					if (aes) {
						String value = new String(ch, start, length);
						tmp_node.setAesThroughput(Double.parseDouble(value));
						aes = false;
					}
					
					if (bclo) {
						String value = new String(ch, start, length);
						tmp_node.setBcloValueTime(Double.parseDouble(value));
						bclo = false;
					}
					
					if (node_linked) {
						String value = new String(ch, start, length);
						tmp_nodeLinked = value;
						node_linked = false;
					}
					
					if (latency) {
						String value = new String(ch, start, length);
						tmp_latency = Double.parseDouble(value);
						latency = false;
					}
					
					if (throughput) {
						String value = new String(ch, start, length);
						tmp_throughput = Double.parseDouble(value);
						throughput = false;
					}
					
					if (pailler) {
						String value = new String(ch, start, length);
						tmp_node.setPaillerThroughput(Double.parseDouble(value));
						pailler = false;
					}	
					
					if (cost_per_second) {
						String value = new String(ch, start, length);
						tmp_node.setCostPerSecond(Double.parseDouble(value));
						cost_per_second = false;
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