package foodPackage;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import jdk.internal.jfr.events.FileWriteEvent;
//import sun.org.mozilla.javascript.internal.json.JsonParser;

public class Server extends HttpServlet {

	
	
	public static String path1 = "";
	public static String fileName = "data.txt";
	
	 @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		path1 = getServletContext().getRealPath("/");
		
		String xmlStr = makeString(req);
		
		int type;
		try {
			type = checkType.typeOnBasisOfXml(xmlStr);
			if(type == 1){
				renderXml.updateDBWithData(xmlStr,resp);
			}
			else if(type == 2){
				String response = retrieveFromDB(xmlStr);
				resp.getWriter().println(response);
			}
			else if(type ==3){
				String res = renderXml.errorText();
				resp.getWriter().println(res);
			}
			
			
		}
		catch (NullPointerException p1) {
			p1.printStackTrace();
			String xmlResp = renderXml.errorText();
			resp.getWriter().println(xmlResp);
		} 
		catch (ParserConfigurationException p2) {
			p2.printStackTrace();
		} catch (SAXException p3) {
			p3.printStackTrace();
		} catch (ParseException p4) {
			p4.printStackTrace();
		}
		
			 
	}
	 
	 
	public String retrieveFromDB(String xml) throws ParserConfigurationException, SAXException, IOException {
		
		ArrayList<String> idArray = fetchIds(xml);
		String resBackString = "";
		resBackString = renderXml.intializeText(0);
		for(String id : idArray){
			boolean bib = isPresentInDB(id);
			if(bib){
				JSONObject food = dataBackEnd(id);
				String country = (String)food.get("-country");
				String name = (String)food.get("name");
				String description = (String)food.get("description");
				String category = (String)food.get("category");
				String price = (String)food.get("price");
				resBackString += renderXml.createNewResponse(name,description,category,price,id,country,bib);
				
				
			}
			else{
				resBackString += renderXml.createNewResponse(null,null,null,null,id,null,bib);
				
			}
			
			//responseString += getResponseString(exists,country,id,name,description,category,price);
		}
		
		resBackString += renderXml.intializeText(1);
		
		return resBackString;
	}
	


	private boolean isPresentInDB(String id) {
		// TODO Auto-generated method stub		
		JSONParser parser = new JSONParser();
		boolean exists = false;
		
		try {
			Object obj = parser.parse(new FileReader(path1+ "/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("FoodItemData");
			for(int i =0 ; i< foodItems.size() ; i++){
				JSONObject food = (JSONObject) foodItems.get(i);
				if(food.get("id").equals(id)){
					exists = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return exists;

	}


	private ArrayList<String> fetchIds(String xml) throws ParserConfigurationException, SAXException, IOException {
		
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				InputSource is = new InputSource();
				ArrayList<String> al= new ArrayList<String>();
		        is.setCharacterStream(new StringReader(xml));
				Document doc = dBuilder.parse(is);
				NodeList n1= doc.getElementsByTagName("FoodItemId");
				
				
				for(int i =0 ; i< n1.getLength() ; i++){
					Element element = (Element) n1.item(i);
					String id = element.getTextContent();
					al.add(id);
							
				}
				
				
				return al;
		
		
		
	}


	
	
	public static void addFoodToDb(String country, int itemId, String name, 
			String description, String category,String price) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
			System.out.println(country+ " " + itemId + " " + name + " " + description + " " +
		" " + category + " " + price );
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path1+"/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("FoodItemData");
			
			JSONObject food = new JSONObject();
			food.put("-country",country);
			food.put("id", String.valueOf(itemId));
			food.put("name", name);
			food.put("description", description);
			food.put("category", category);
			food.put("price", price);
			
			foodItems.add(food);
			
			FileWriter file = new FileWriter(path1 + "/WEB-INF/"+fileName);
			file.write(jsonObject.toJSONString());
			
			file.flush();
			file.close();
		
	}


	public static int addedItemResponse() throws ParseException {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		int create_id =0;
		
		try {
			Object obj = parser.parse(new FileReader(path1+"/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("FoodItemData");
			for(int i =0 ; i< foodItems.size() ; i++){
				JSONObject food = (JSONObject) foodItems.get(i);
				int value = Integer.valueOf((String)food.get("id"));
				if(value > create_id){
					create_id = value;
				}
			}
			create_id = create_id+1;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return create_id;
	}



	public static int getItem(String name,String category) throws ParseException {
		// TODO Auto-generated method stub
		int itemId = 0;
		JSONParser parser = new JSONParser();
		
		try {
			
			
			Object obj = parser.parse(new FileReader(path1+"/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("FoodItemData");
			for(int i =0 ; i< foodItems.size() ; i++){
				JSONObject food = (JSONObject) foodItems.get(i);
				String fname = (String) food.get("name");
				String cname = (String) food.get("category");
				if(fname.equals(name) && cname.equals(category)){
					itemId = Integer.valueOf((String)food.get("id"));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return itemId;

	}
	
	private JSONObject dataBackEnd(String id) {
		
		JSONParser parser = new JSONParser();
		JSONObject foodObj = null;
		try {
			Object obj = parser.parse(new FileReader(path1+ "/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("FoodItemData");
			for(int i =0 ; i< foodItems.size() ; i++){
				JSONObject food = (JSONObject) foodItems.get(i);
				if(food.get("id").equals(id)){
					foodObj = food;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return foodObj;

	}


	public static boolean checkDB(String name, String category) {
		// TODO Auto-generated method stub
		boolean found = false;
		
		JSONParser par = new JSONParser();
		
		try {
			
			Object obj = par.parse(new FileReader(path1+ "/WEB-INF/"+fileName));
			JSONObject jObj = (JSONObject) obj;
			JSONArray items = (JSONArray) jObj.get("FoodItemData");
			for(int i =0 ; i< items.size() ; i++){
				JSONObject food = (JSONObject) items.get(i);
				
				String categoryName = (String) food.get("category");
				
				String name1 = (String) food.get("name");
				
				if(categoryName.equals(category) && name1.equals(name) ){
					found = true;
					break;
				}
			}
			
		} catch (IOException p1) {
			p1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		return found;

	}
	


	public static String makeString(HttpServletRequest req) {
		// TODO Auto-generated method stub
		String xml_file = null;
		try{
			byte[] data = new byte[req.getContentLength()];
			
			InputStream is = req.getInputStream();
			
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.read(data,0,data.length);
			
			if(req.getCharacterEncoding() !=null){
				xml_file = new String(data, req.getCharacterEncoding());
			}else{
				xml_file = new String(data);
			}
			return xml_file;
		}
		catch(IOException p){
			System.out.println(p.toString());
		}

		return xml_file;
	}


	 
//	public static String existingText(int id){
//		String xml = "<FoodItemExists xmlns=\"http://cse564.asu.edu/PoxAssignment\">";
//		xml += "<FoodItemId>";
//		xml += Integer.toString(id);
//		xml += "</FoodItemId>";
//		xml += "</FoodItemExists>";
//		return xml;
//	}
	
	 
}
