package me.pmilon.RubidiaCore.utils;

public class JSONUtils {

	public static String toJSON(String s){
		return "\"" + s + "\"";
	}
	
	public static String toJSON(String[] s){
		String j = s.length > 1 ? "[" : "";
		for(int i = 0;i < s.length;i++){
			j += toJSON(s[i]);
			if(i != s.length-1)j += ",";
		}
		j += s.length > 1 ? "]" : "";
		return j;
	}
}
