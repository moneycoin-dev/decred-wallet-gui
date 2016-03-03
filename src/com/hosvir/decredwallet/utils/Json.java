package com.hosvir.decredwallet.utils;

import java.util.ArrayList;

/**
 * 
 * @author Troy
 *
 */
public class Json {
	
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
				splitLine = ss.split(":\\s");
				
				if(splitLine.length > 1){
					String name = cleanString(splitLine[0]);
					String value = cleanString(splitLine[1]);
					
					jo.addJsonObject(new JsonObjects(name,value));
				}
			}
			
			jsonObjects.add(jo);
		}
		
		return jsonObjects;
	}
	
	private static String cleanString(String string){
		return string.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").trim();
	}

}
