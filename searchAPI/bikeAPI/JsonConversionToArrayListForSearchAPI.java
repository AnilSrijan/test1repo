package searchAPI.bikeAPI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import classicDriver.XMLExtractor;

/**
 * @author Deepshikha
 *
 */
//TODO need to work on this, presently this is on hold as I am working on Create API
public class JsonConversionToArrayListForSearchAPI extends XMLExtractor{
	ArrayList<String> jsonValues = new ArrayList<String>();
	List<List<String>> listOfListsOfJson = new ArrayList<List<String>>();  

	/**
	 * this method is for replacing value sop as to match the values form excel sheet and json
	 * @param cellVAlue
	 * @return
	 */
	private String replaceKeywords(String cellVAlue){

		switch (cellVAlue) {
		case "km" :  cellVAlue = "kilometers";
		break;

		case "mi" :  cellVAlue = "miles";
		break;

		case "private_seller" : cellVAlue = "Private seller";
		break;

		case "dealer" :  cellVAlue = "Dealer";
		break;

		case "auction_house" :  cellVAlue = "Auction house";
		break;

		case "en-US" :  cellVAlue = "English";
		break;
		}

		return cellVAlue;

	}

	private ArrayList<String> arrayListFromJson(JSONObject jsonObj) throws Exception{

		ArrayList<String> listFromJSON=new ArrayList<String>();
		ArrayList<String> list = getListFromXMLTest("jsonListVariable");
		for(String l1:list){
			//System.out.println("list from l1==> "+ l1);
			if(l1.contains(",")){
				String key = l1.substring(0, l1.lastIndexOf(","));
				String objectType = l1.substring(l1.lastIndexOf(",")+1);

				if(objectType.equals("jsonArray")){
					String parent=key.substring(0,key.indexOf(","));

					String child=key.substring(key.indexOf(",")+1);

					listFromJSON.add(getValueFromJsonArray(jsonObj, parent, child));
				}

				else if (objectType.equals("jsonObject"))
				{
					String parent=key.substring(0,key.indexOf(","));

					String child=key.substring(key.indexOf(",")+1);

					listFromJSON.add(getValueFromJsonObject(jsonObj, parent, child));

				} 
			}
			else {

				listFromJSON.add(getValueFromObject(jsonObj, l1));
			}	
		}

		return listFromJSON;
	}


	/*These
	@throws Exception */



	
	/**
	 * this method will be used for JSON extraction for root having multiple objects(Array)
	 * @param baseElement
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getArrayListMulti(String baseElement,String url) throws Exception{
		String jsonFileName1=convertJsonAPIOutputtoString(url);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonFileName1);
		JSONObject jsonObj=(JSONObject) obj;
		obj=jsonObj.get(baseElement);
		JSONArray jsonArr= (JSONArray) obj;
		int count = jsonArr.size();
		System.out.println("The size is " + count);

		for(int temp=0;temp<count;temp++){
			obj=jsonArr.get(temp);
			jsonObj=(JSONObject) obj;
			ArrayList<String> listArr = arrayListFromJson(jsonObj);
			listOfListsOfJson.add(listArr);
		}
		int i=0;
		for(List<String> arrList : listOfListsOfJson){

			System.out.println("List"+i);
			i++;
			for(String str : arrList){
				System.out.println(str);
			}

		}
		return listOfListsOfJson;

	}


	public void concatJSONObject(String baseElement,String url) throws Exception{

		String jsonFileName1=convertJsonAPIOutputtoString(url);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonFileName1);
		JSONObject jsonObj=(JSONObject) obj;
		obj=jsonObj.get(baseElement);
		JSONArray jsonArr= (JSONArray) obj;
		int count = jsonArr.size();
		System.out.println("The size is " + count);

		ArrayList<String> listFromXML = getListFromXMLTest("jsonListVariable");
		String outVal="";
		System.out.println("-------"+ listFromXML);
		for(String tempList:listFromXML){	
			outVal="";
			String[] listarr = tempList.split(","); //Split the xml values into array on basis of ','
			System.out.println("Listarray.." + listarr.length);

			for(int i=0;i<listarr.length;i++){
				String[] subPart = listarr[i].split("@");
				System.out.println("Subpart [0]-->>" + subPart[0]);
				for(int itr=0; itr<jsonFileName1.length(); itr++){
					String seperatorToBeAdded="";
					if(subPart.length==2)
						seperatorToBeAdded=subPart[1];

					outVal=outVal+seperatorToBeAdded;
					System.out.println("OUTVAL is ==>>" + outVal);


					for(int temp=0;temp<count;temp++){
						obj=jsonArr.get(temp);
						jsonObj=(JSONObject) obj;
						ArrayList<String> listArr = arrayListFromJson(jsonObj);
						listOfListsOfJson.add(listArr);

					}
					System.out.println("LIST is ==>>" + listOfListsOfJson);

					for(List<String> arrList : listOfListsOfJson){
						System.out.println("List"+i);
						i++;
						for(String str : arrList){
							System.out.println(str);
						}
					}
				}
			}}
	}
	//	return listOfListsOfJson;

	//convert json api output to a string
	private String convertJsonAPIOutputtoString(String url) throws IOException{
		URL urlAPI = new URL(url);
		//for opening the connection
		HttpURLConnection conn =
				(HttpURLConnection) urlAPI.openConnection();

		//to check if we are connected to internet
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		//to disconnect the connection
		conn.disconnect();
		return sb.toString();
	}

	//for the values that are stored in JSON Object and have parent and child
	public String getValueFromJsonObject(JSONObject jsonObj,String parent,String child) throws Exception{
		Object obj= null;
		String outVal=null;		

		if (jsonObj.containsKey(parent))
		{
			obj=jsonObj.get(parent);
			jsonObj=(JSONObject) obj;

			if(jsonObj.containsKey(child)){

				if(jsonObj.get(child)==null){
					outVal="null";
				}
				else{
					String childValue=jsonObj.get(child).toString();
					if(child.equals("Mileage"))
						childValue=removeSpace(childValue);
					if(childValue.endsWith(".0")){
						String temp=childValue;
						outVal=temp.substring(0, temp.indexOf(".0"));
					}
					else
						outVal=childValue;

				}
			}
			
			else{
				outVal=outVal+","+"no element";
			}
		}
		return outVal;

	}

	//for the values that are stored in JSON Arrays and have parent and child
	private String getValueFromJsonArray(JSONObject jsonObj,String parent,String child) throws FileNotFoundException, IOException, ParseException{
		Object obj= null;
		if (jsonObj.containsKey(parent))
		{
			obj=jsonObj.get(parent);
			JSONArray jsonArray=(JSONArray) obj;
			String outVal = "";
			for (int i =0; i<jsonArray.size();i++){
				obj=jsonArray.get(i);
				jsonObj = (JSONObject) obj;

				if(jsonObj.containsKey(child)){
					if(jsonObj.get(child)==null){

					}
					else{
						if(jsonObj.get(child).toString().endsWith(".0")){
							String temp=jsonObj.get(child).toString();
							outVal=outVal+","+temp.substring(0, temp.indexOf(".0"));
						}
						else
							outVal=outVal+","+jsonObj.get(child).toString();
					}
				}
				else{
					outVal=outVal+","+"noelement";
				}
			}
			outVal = outVal.substring(1);

			return outVal;
		}
		else
		{
			return null;
		}
	}

	private String removeSpace(String childValue){
		childValue=childValue.replace(" ", "");
		return childValue;

	}
	//for the values that are stored in JSON Object and have neither parent nor child
	public String getValueFromObject(JSONObject jsonObj,String parent) throws FileNotFoundException, IOException, ParseException{

		Object obj = null;
		String outVal=null;
		if(jsonObj.get(parent)==null){
			outVal="null";
		}
		else{
			if(jsonObj.get(parent).toString().endsWith(".0")){
				String temp=jsonObj.get(parent).toString();
				outVal=temp.substring(0, temp.indexOf(".0"));
			}
			else
				outVal=jsonObj.get(parent).toString();
		}
		return outVal;
	}



}
