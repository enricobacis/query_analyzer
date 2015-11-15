package parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import network.Node;
import network.Link;
import network.Network;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserNetwork {


	public static Network parse(String res) {
		
		final List<Node> nodes = new ArrayList<Node>();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				
				Node tmp_node;
				List<String> tmp_datas_enc;
				List<String> tmp_datas_plain;
				String tmp_nodeLinked;
				double tmp_latency;
				double tmp_throughput;
				List<Link> tmp_links;
				String supportAttribute;
				boolean node_type, node_data, node_linked, aes, bclo,
				        // node_datas, performance, links, link, 
				        throughput, cost_per_second, pailler, latency;


				public void startElement(String uri, String localName,String qName,
			                Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("NODE")) {
						// node = true;
						tmp_node = new Node();
						tmp_node.setName(attributes.getValue("name"));
					} else if (qName.equalsIgnoreCase("NODE-TYPE")) {
						node_type = true;
					} else if (qName.equalsIgnoreCase("NODE-DATAS")) {
						// node_datas = true;
						tmp_datas_enc = new ArrayList<String>();
						tmp_datas_plain = new ArrayList<String>();
					} else if (qName.equalsIgnoreCase("NODE-DATA")) {
						supportAttribute = attributes.getValue("attribute");
						node_data = true;
					} else if (qName.equalsIgnoreCase("PERFORMANCE")) {
						// performance = true;
					} else if (qName.equalsIgnoreCase("AES-THROUGHPUT")) {
						aes = true;
					} else if (qName.equalsIgnoreCase("PAILLER-THROUGHPUT")) {
						pailler = true;
					} else if (qName.equalsIgnoreCase("BCLO-VALUE-TIME")) {
						bclo = true;
					} else if (qName.equalsIgnoreCase("LINKS")) {
						tmp_links = new ArrayList<Link>();
					//    links = true;
					// } else if (qName.equalsIgnoreCase("LINK")) {
					//	 link = true;
					} else if (qName.equalsIgnoreCase("NODE-LINKED")) {
						node_linked = true;
					} else if (qName.equalsIgnoreCase("LATENCY")) {
						latency = true;
					} else if (qName.equalsIgnoreCase("THROUGHPUT")) {
						throughput = true;
					} else if (qName.equalsIgnoreCase("COST-PER-SECOND")) {
						cost_per_second = true;
					}

				}

				public void endElement(String uri, String localName,
					String qName) throws SAXException {

					if (qName.equalsIgnoreCase("NODE")) {
						nodes.add(tmp_node);
					//	node = false;
					} else if (qName.equalsIgnoreCase("NODE-DATAS")) {
						tmp_node.setEncryptedVisibility(tmp_datas_enc);
						tmp_node.setPlainVisibility(tmp_datas_plain);
					//	node_datas = false;
					// } else if (qName.equalsIgnoreCase("PERFORMANCE")) {
					//	performance = false;
					} else if (qName.equalsIgnoreCase("LINKS")) {
						tmp_node.setLinks(tmp_links);
					//	links = false;
					} else if (qName.equalsIgnoreCase("LINK")) {
						tmp_links.add(new Link(tmp_nodeLinked, tmp_latency, tmp_throughput));
					//	link = false;
					}

				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (node_type) {
						String value = new String(ch, start, length);
						tmp_node.setType(value);
						node_type = false;
					}
					
					if (node_data) {
						switch (new String(ch, start, length)) {
						case "Encrypted":
							tmp_datas_enc.add(supportAttribute);
							break;
						
						case "Plain":
							tmp_datas_plain.add(supportAttribute);
							break;
							
						// The "No" are not taken into account.
						}
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

    	return new Network(nodes);
   }

}