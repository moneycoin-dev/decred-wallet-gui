package com.hosvir.decredwallet.utils;

import java.util.ArrayList;

/**
 * 
 * @author Troy
 *
 */
public class Json {
	
	/*public static void main(String[] args){
		ArrayList<JsonObject> jsonObjects = parseJson("" +
"["+
"  { " +
    "\"account\": \"\"," +
    "\"address\": \"Dse8hrkqep25xoqqit5QPcvQs8Q3XiMbsb7\"," +
    "\"amount\": -40, " +
    "\"blockhash\": \"000000000000272c620ffb8fb551c5888f1c3025e1e73231ce2954f7c5f89bc2\"," +
    "\"blocktime\": 1455283908," +
    "\"category\": \"send\"," +
    "\"confirmations\": 4071," +
    "\"fee\": -0.05," +
    "\"time\": 1455282284," +
    "\"timereceived\": 1455282284," +
    "\"txid\": \"12306fd37e9e438eedf95487d4208d82a50ddc75f71b338a8c408fa5d2565ca6\"," +
    "\"vout\": 0," +
    "\"walletconflicts\": []" +
  "}," +
  "{" +
    "\"account\": \"\"," +
    "\"address\": \"Dse8hrkqep25xoqqit5QPcvQs8Q3XiMbsb7\"," +
    "\"amount\": -90," +
    "\"blockhash\": \"00000000000004283c5ce7c153b8c537f9ac4b0be59984ff148ea76f4784f292\"," +
    "\"blocktime\": 1455114922," +
    "\"category\": \"send\"," +
    "\"confirmations\": 4637," +
    "\"fee\": -0.05," +
    "\"time\": 1455114550," +
    "\"timereceived\": 1455114550," +
    "\"txid\": \"9750426880bf68aa317010b304d90c7542c652c9152fb54306663d2228832c1d\"," +
    "\"vout\": 0," +
    "\"walletconflicts\": []" +
  "}" +
"]");
		
		for(JsonObject jo : jsonObjects){
			System.out.println(jo.getObjectCount() + " " + jo.getValueByName("address") + " " + jo.getValueByName("amount"));
		}
	}*/
	
	public static ArrayList<JsonObject> parseJson(String string) {
		ArrayList<JsonObject> jsonObjects = new ArrayList<JsonObject>();
		String[] splitString;
		String[] splitLines;
		String[] splitLine;
		
		//Split objects
		splitString = string.split("},");
		
		for(String s : splitString){
			JsonObject jo = new JsonObject();
			splitLines = s.split(",");
			
			for(String ss : splitLines){
				splitLine = ss.split(":");
				
				String name = cleanString(splitLine[0]);
				String value = cleanString(splitLine[1]);
				
				jo.addJsonObject(new JsonObjects(name,value));
			}
			
			jsonObjects.add(jo);
		}
		
		return jsonObjects;
	}
	
	private static String cleanString(String string){
		return string.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").trim();
	}

}
