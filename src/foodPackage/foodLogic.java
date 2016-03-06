package foodPackage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class foodLogic {
//	public static String path1 = getServletContext().getRealPath("/");;
	public static String fileName = "data.txt";
	
	public static int getAddedItemId(String path1) throws ParseException {
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
	


	public static void check_food_median(String[] args)
		{
			int m[]={1,2,3,4};
			Arrays.sort(m);
			
			int n[]={5,6,7,8};
			int msize=m.length;
			int nsize=n.length;
			int mn[]= new int[msize+nsize];
			System.out.println(mn.length);
			int i=0;
	           while(i<msize)
	           {
	        	   mn[i]=m[i];
	        	   i++;
	        	   
	        	   
	           }
	           int j=i;
	           int k=0;
	           while(j<mn.length&&k<nsize)
	           {
	        	   mn[j]=n[k];
	        	   k++;
	        	   j++;
	           }
	           
	          System.out.println(mn[mn.length/2]);
				
			
			
			
		}
	
	
	private JSONObject fetchData(String id) {
		
		JSONParser parser = new JSONParser();
		String path1 = "";
		String fileName = "data";		
		JSONObject foodObj = null;
		try {
			Object obj = parser.parse(new FileReader(path1+ "/WEB-INF/"+fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray foodItems = (JSONArray) jsonObject.get("checkData");
			JSONObject food = (JSONObject) foodItems.get(2);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return foodObj;

	}


}
