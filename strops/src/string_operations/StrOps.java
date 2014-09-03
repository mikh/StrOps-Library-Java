package string_operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StrOps {

	//has an error issue if the sections contain each other's opening/closing sequences
	public static String getNextSection(String str, ArrayList<String> opening_sequences, ArrayList<String> closing_sequences, int start_index){
		//first find opening sequence
		String section = null;
		int open = -1, ii, s_index, e_index = -1;
		for(ii = start_index; ii < str.length(); ii++){
			for(int jj = 0; jj < opening_sequences.size(); jj++){
				if(opening_sequences.get(jj).length() <= str.length() - ii){
					String substr = str.substring(ii, ii + opening_sequences.get(jj).length());
					if(substr.equals(opening_sequences.get(jj))){
						open = jj;
						break;
					}
				}
			}
			if(open != -1)
				break;
		}
		
		//look for end sequence
		if(open != -1){
			s_index = ii;
			ii += opening_sequences.get(open).length();
			for(; ii < str.length(); ii++){
				String opening = opening_sequences.get(open);
				for(int jj = 0; jj < opening_sequences.size(); jj++){
					if(opening_sequences.get(jj).equals(opening)){
						if(closing_sequences.get(jj).length() <= str.length() - ii){
							String substr = str.substring(ii, ii + closing_sequences.get(jj).length());
							if(substr.equals(closing_sequences.get(jj))){
								e_index = ii + substr.length();
								break;
							}
						}
					}
				}
				if(e_index != -1)
					break;
			}
			if(e_index != -1)
				section = str.substring(s_index, e_index);
		}
		
		return section;
	}

	//breaks a string on sequences matching 'pattern', but ignores 'pattern' if it is within a section.
	//assumes each opening tag has only one closing tag
	public static ArrayList<String> getAllTextBetweenPatternsIgnoringSections(String str, String pattern, ArrayList<String> opening_sequences, ArrayList<String>closing_sequences){
		ArrayList<String> mapping = new ArrayList<String>();
		String rewriteString = "";
		for(int ii = 0; ii < str.length(); ii++){
			boolean found = false;
			for(int jj = 0; jj < opening_sequences.size(); jj++){
				if(opening_sequences.get(jj).length() <= str.length() - ii){
					String substr = str.substring(ii, ii + opening_sequences.get(jj).length());
					if(substr.equals(opening_sequences.get(jj))){
						int second_index = findPatternAfterIndex(str, closing_sequences.get(jj), ii + opening_sequences.get(jj).length());
						if(second_index != -1){
							found = true;
							String trans = str.substring(ii, second_index + substr.length());
							mapping.add(trans);
							rewriteString += String.format("\"$%d$\"",mapping.size()-1);
							ii = second_index;
						}
					}
				}
			}
			if(!found)
				rewriteString += str.charAt(ii);
		}

		ArrayList<String> output = new ArrayList<String>();
		int index = 0;
		for(int ii = 0; ii <= rewriteString.length() - pattern.length(); ii++){
			String substr = rewriteString.substring(ii, ii + pattern.length());
			if(substr.equals(pattern)){
				output.add(rewriteString.substring(index, ii));
				index = ii + pattern.length();
			}
		}
		if(index < rewriteString.length())
			output.add(rewriteString.substring(index));

		for(int ii = 0; ii < output.size(); ii++){
			for(int jj = 0; jj < mapping.size(); jj++){
				output.set(ii, output.get(ii).replace(String.format("\"$%d$\"", jj), mapping.get(jj)));
			}
		}
		return output;

	}
	
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
	
	public static int findPatternAfterIndex(String str, String pattern, int index){
		for(int ii = index; ii <= (str.length() - pattern.length()); ii++){
			String substr = str.substring(ii, ii+pattern.length());
			if(pattern.equals(substr))
				return ii;
		}
		return -1;
	}
	
	/**
	 * Similar to findPatternAfterIndex but it doesn't trigger if the pattern has been escaped with \ or is within quotes
	 * @param str - original string
	 * @param pattern - pattern to find
	 * @param index - starting index
	 * @return index of first pattern found or -1
	 */
	public static int findNonDelimitedPatternAfterIndex(String str, String pattern, int index){
		boolean single_quotes = false, double_quotes = false, delimited = false;
		for(int ii = index; ii <= (str.length() - pattern.length()); ii++){
			if((str.charAt(ii) == '\'') && !double_quotes && !delimited){
				if(!single_quotes) 
					single_quotes = true;
				else
					single_quotes = false;
			} else if((str.charAt(ii) == '\"') && !single_quotes && !delimited){
				if(!double_quotes)
					double_quotes = true;
				else
					double_quotes = false;
			}
			else if(str.charAt(ii) == '\\'){
				if(delimited)
					delimited = false;
				else
					delimited = true;
			}
			
			if(!single_quotes && !double_quotes && !delimited){
				String substr = str.substring(ii, ii + pattern.length());
				if(pattern.equals(substr))
					return ii;
			}
			
			if(delimited && str.charAt(ii) != '\\')
				delimited = false;
		}
		return -1;
	}
	
	/**
	 * Trims the input string and removes " or ' quotes if they exist
	 * @param s - input string
	 * @return - string with outer quotes removed
	 */
	public static String removeQuotes(String s){
		s = s.trim();
		if(s.length() > 0){
			char c = s.charAt(s.length()-1);
			if(c == '\'' || c == '\"')
				s = s.substring(0, s.length()-1);
		}
		if(s.length() > 0){
			char c = s.charAt(0);
			if(c == '\'' || c == '\"')
				s = s.substring(1);
		}
		return s;
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
	
	public static void printArrayList(ArrayList<String> list){
		for(int ii = 0; ii < list.size(); ii++)
			System.out.println(list.get(ii));
	}
}
