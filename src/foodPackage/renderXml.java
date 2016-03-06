package foodPackage;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class renderXml {
	
	public static String createNewResponse(String n1,String d1, String c1, String p1,String id,String c11,boolean bob){
		String xml_file = "";
		if (bob){
		xml_file += "<FoodItem country="+ "\"" + c1  + "\">";
		xml_file += "<id>"+ id +"</id>";
		xml_file += "<name>"+ n1 +"</name>";
		xml_file += "<description>"+ d1 +"</description>";
		xml_file += "<category>"+ c11 +"</category>";
		xml_file += "<price>"+ p1 +"</price>";
		xml_file += "</FoodItem>";}
		else{
			xml_file += "<InvalidFoodItem>";
			xml_file += "<FoodItemId>" + id + "</FoodItemId>";
			xml_file += "</InvalidFoodItem>";
		}
		
		return xml_file;
		
	}
	
	public static String intializeText(int position){
		String xml_file = "";
		if (position == 0){
		xml_file += "<RetrievedFoodItems xmlns=\"http://cse564.asu.edu/PoxAssignment\">";
		}
		else if(position == 1){
			xml_file += "</RetrievedFoodItems>";
		}
		return xml_file;
	}
	
	public static String errorText(){
		String xml_file = "<InvalidMessage xmlns=\"http://cse564.asu.edu/PoxAssignment\"/>";
		return xml_file;
	}
	
	public static String addedText(int id){
		String xml_file = "<FoodItemAdded xmlns=\"http://cse564.asu.edu/PoxAssignment\">";
		xml_file += "<FoodItemId>";
		xml_file += Integer.toString(id);
		xml_file += "</FoodItemId>";
		xml_file += "</FoodItemAdded>";
		return xml_file;
	}
	
	public static String existingText(int id){
		String xml_file = "<FoodItemExists xmlns=\"http://cse564.asu.edu/PoxAssignment\">";
		xml_file += "<FoodItemId>";
		xml_file += Integer.toString(id);
		xml_file += "</FoodItemId>";
		xml_file += "</FoodItemExists>";
		return xml_file;
	}
	
	public static void updateDBWithData(String xml, HttpServletResponse resp) throws NullPointerException, ParseException, ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		String name = fetchName(xml);
		String category = fetchCategory(xml);
		String description = fetchDescription(xml);
		String country = fetchCountry(xml);
		String price = fetchPrice(xml);
		
		System.out.println(country);
		
		String responseXML ="";
		if((name.length() ==0) || (category.length() == 0)){
			responseXML = renderXml.errorText();
			resp.getWriter().println(responseXML);
			
		}
		else if(FoodItem.checkDB(name,category)){
			int itemId =  FoodItem.getItem(name,category);
			responseXML = renderXml.existingText(itemId);
			//responseXML = String.valueOf(itemId);
			resp.getWriter().println(responseXML);
		}
	
		else if(!(FoodItem.checkDB(name, category))){
			int itemId = FoodItem.addedItemResponse();
			FoodItem.addFoodToDb(country,itemId,name,description,category,price);
			responseXML = renderXml.addedText(itemId);
			resp.getWriter().println(responseXML);
		}
		
		
		
	}
	
	public static String fetchName(String xml) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
		Document doc = dBuilder.parse(is);
		NodeList n1= doc.getChildNodes();
		
		String name=null;
		for(int i=0;i<n1.getLength();i++)
		{
		    Element e1=(Element)n1.item(i);
		    name=e1.getElementsByTagName("name").item(0).getTextContent();
		}
		
		return name;
	}
	
	public static String fetchCategory(String xml) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
		Document doc = dBuilder.parse(is);
		NodeList n1= doc.getChildNodes();
		
		String category=null;
		for(int i=0;i<n1.getLength();i++)
		{
		    Element e1=(Element)n1.item(i);
		    category=e1.getElementsByTagName("category").item(0).getTextContent();
		}
		
		return category;
	}
	
	public static String fetchDescription(String xml) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
		Document doc = dBuilder.parse(is);
		NodeList n1= doc.getChildNodes();
		
		String description=null;
		for(int i=0;i<n1.getLength();i++)
		{
		    Element e1=(Element)n1.item(i);
		    description=e1.getElementsByTagName("description").item(0).getTextContent();
		}
		
		return description;
	}
	
	public static String fetchCountry(String xml_file) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml_file));
		Document doc = dBuilder.parse(is);
		NodeList node1= doc.getElementsByTagName("FoodItem");
		  Element element1=(Element)node1.item(0);
		  NamedNodeMap base=element1.getAttributes();
		String country=null;
		for(int i=0;i<base.getLength();i++)
		{
		  
		    Node attr= base.item(i);
		    country=attr.getNodeValue().toString();
		}
		
		return country;
	}
	
	public static String fetchPrice(String xml) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		InputSource input = new InputSource();
        input.setCharacterStream(new StringReader(xml));
		Document doc = dBuilder.parse(input);
		NodeList node1= doc.getChildNodes();
		
		String price=null;
		for(int i=0;i<node1.getLength();i++)
		{
		    Element element1=(Element)node1.item(i);
		    price=element1.getElementsByTagName("price").item(0).getTextContent();
		}
		
		return price;
	}
	
	private int get_by_data(String xml) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
				int temp = 0;
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				InputSource is = new InputSource();
//				NodeList n1= dBuilder.getElementsByTagName("FoodItemId");
				temp += 1;
				return temp;
		
		
		
	}
	
	
	
	

}
