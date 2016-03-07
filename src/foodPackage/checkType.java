package foodPackage;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class checkType {
	
	public static int typeOnBasisOfXml(String xml_file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		String type = "";
		
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml_file));
    	try{
    		Document doc = dBuilder.parse(is);
    		doc.getDocumentElement().normalize();
    		type = doc.getDocumentElement().getNodeName();
    	}
    	catch(Exception e){
    		return 3;
    	}
		if(type.equals("SelectedFoodItems")){
			return 2;
		}
		else if(type.equals("NewFoodItems")){
			return 1;
		}
		else{
			return 3;
		}
		
		
	}

}
