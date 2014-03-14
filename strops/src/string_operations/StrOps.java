package string_operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StrOps {

	
	public static String getDilineatedSubstring(String str, String pattern, int instance, boolean back){		ArrayList<String> substring_list = new ArrayList<String>();
		if(str.length() < pattern.length() || instance < 0){
			return str;			
		}
		
		String cur_instance = "";
		for(int ii = 0; ii <= str.length() - pattern.length(); ii++){
			String substr = str.substring(ii, pattern.length()+ii);
			if(substr.equals(pattern) && cur_instance.length() != 0){
				substring_list.add(cur_instance);
				cur_instance = "";
				ii += (pattern.length()-1);
			}
			else
				cur_instance += str.charAt(ii);
		}
		if(cur_instance.length() != 0)
			substring_list.add(cur_instance);
		
		
		if(instance >= substring_list.size())
			return substring_list.get(substring_list.size()-1);
		else if(back == false)
			return substring_list.get(instance);
		else
			return substring_list.get(substring_list.size() - 1 - instance);
	}
	
	public static String deleteAllInstances(String str, String pattern){
		String ss = "";
		int ii;
		for(ii = 0; ii <= str.length()-pattern.length(); ii++){
			String substr = str.substring(ii, ii + pattern.length());
			if(substr.equals(pattern))
				ii += (pattern.length()-1);
			else
				ss += str.charAt(ii);
		}
		for(; ii < str.length();ii++)
			ss += str.charAt(ii);
		return ss;
	}
	
	//cases:
	// pattern
	// *pattern
	// pattern*
	// pat*tern
	// *pat*tern
	// pat*tern*
	// *pat*tern*
	public static boolean patternMatch(String str, String pattern){
		int index = 0, ii = 0;	
		while(index < pattern.length()){	//while not done
			//get the pattern
			
			String cur_pat = "";
			boolean jump = false;
			for(; index < pattern.length(); index++){
				if(pattern.charAt(index) == '*' && cur_pat.length() != 0)
					break;
				else if(pattern.charAt(index) == '*')
					jump = true;
				else if(pattern.charAt(index) != '*')
					cur_pat += pattern.charAt(index);					
			}
			
			
			if(jump == false){
				for(int jj = 0; jj < cur_pat.length(); jj++){
					if(ii >= str.length() || str.charAt(ii) != cur_pat.charAt(jj))
						return false;
					ii++;
				}
			}
			else{
				boolean pass = false;
				for(; ii <= str.length() - cur_pat.length(); ii++){
					String substr = str.substring(ii, ii+cur_pat.length());
					if(substr.equals(cur_pat)){
						pass = true;
						break;
					}
				}
				if(pass == false)
					return false;
			}
		}
		return true;
	}
	
	public static String get_substring_between_patterns(String source, String pattern_start, String pattern_finish){
		int start = findPattern(source, pattern_start);
		if(start == -1)	return "";
		source = source.substring(start+pattern_start.length());
		start = findPattern(source, pattern_finish);
		if(start == -1)  return "";
		source = source.substring(0, start);
		return source;
	}
	
	public static int countInstances(String str, String pattern){
		int inst = 0;
		for(int ii = 0; ii <= str.length() - pattern.length(); ii++){
			String substr = str.substring(ii, ii+pattern.length());
			if(substr.equals(pattern))
				inst++;
		}
		return inst;
	}
	
	public static String trimString(String str){
		int s_index = 0, e_index = str.length()-1;
		for(;s_index < str.length(); s_index++){
			if(str.charAt(s_index) != ' ' && str.charAt(s_index) != '\n' && str.charAt(s_index) != '\r' && str.charAt(s_index) != '\t')
				break;
		}
		for(;e_index >= 0; e_index--){
			if(str.charAt(e_index) != ' ' && str.charAt(e_index) != '\n' && str.charAt(e_index) != '\r' && str.charAt(e_index) != '\t')
				break;
		}
		
		if(s_index > e_index+1 || s_index > str.length() || e_index + 1 > str.length()) return "";
		return str.substring(s_index, e_index+1);
		
	}
	
	public static int findPattern(String str, String pattern){
		for(int ii = 0; ii <= (str.length() - pattern.length()); ii++){
			String substr = str.substring(ii, ii+pattern.length());
			if(pattern.equals(substr))
				return ii;
		}
		return -1;
	}

	public static ArrayList<Integer> findAllMatches(String str, String pattern){
		ArrayList<Integer> locus = new ArrayList<Integer>();
		for(int ii = 0; ii < str.length() - pattern.length() + 1; ii++){
			String word = str.substring(ii,ii+pattern.length());
			if(pattern.equals(word))
				locus.add(ii);			
		}
		return locus;
	}
	
	public static String replaceAll(String str, String pattern, String new_pattern){
		ArrayList<String> parts = new ArrayList<String>();
		ArrayList<Integer> locus = findAllMatches(str, pattern);
		String new_str = "";
		int index = 0;
		for(int ii = 0; ii < locus.size(); ii++){
			//System.out.printf("%d = %s\n",locus.get(ii),str.substring(locus.get(ii)-10, locus.get(ii)+pattern.length() + 10));
			parts.add(str.substring(index, locus.get(ii)));
			index = locus.get(ii) + pattern.length();
		}
		parts.add(str.substring(index));
		new_str = parts.get(0);
		for(int ii = 1; ii < parts.size(); ii++){
			new_str = new_str + new_pattern;
			new_str += parts.get(ii);
		}
		return new_str;
	}
	
	public static void splitOnUppercaseLetters(String file_location, String destination_file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file_location));
			BufferedWriter bw = new BufferedWriter(new FileWriter(destination_file, false));
			String line = br.readLine();
			while(line != null){
				String new_line = "";
				for(int ii = 0; ii < line.length(); ii++){
					char cc = line.charAt(ii);
					if(cc >= 'A' && cc <= 'Z')
						new_line += '\t';
					new_line += cc;
				}
				bw.write(new_line + "\n");
				line = br.readLine();
			}
			br.close();
			bw.close();
		} catch(IOException e){
			System.out.println("[ERROR] cantrip.string_operations.StrOps:: Cannot split on uppercase letters from " + file_location + " and put into " + destination_file);
		}
	}
}
