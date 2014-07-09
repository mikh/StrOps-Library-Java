package test;

import java.util.ArrayList;

import string_operations.StrOps;

public class debug {
	
	public static void main(String[] args){
	//	System.out.println("get_substring_between_patterns(Hello World, He, ld) = " +  StrOps.get_substring_between_patterns("Hello World", "He", "ld"));
		
	//	System.out.println("abababababavvabababababavvababababbababvvababbbaabavvabababvvaa");
	//	System.out.println(StrOps.replaceAll("abababababavvabababababavvababababbababvvababbbaabavvabababvvaa","vv","xx"));
	//	StrOps.splitOnUppercaseLetters("C:\\Users\\Mikhail\\Desktop\\Dropbox\\EC504 Grading\\Submissions\\HomeworkOne\\names.txt", "C:\\Users\\Mikhail\\Desktop\\Dropbox\\EC504 Grading\\Submissions\\HomeworkOne\\names.split.txt");
		StrOps.splitOnUppercaseLetters("C:\\Users\\Mikhail\\Desktop\\Dropbox\\EC504 Grading\\Submissions\\HomeworkTwo\\name.txt", "C:\\Users\\Mikhail\\Desktop\\Dropbox\\EC504 Grading\\Submissions\\HomeworkTwo\\name.split.txt");
	
		ArrayList<String> opening_sequences = new ArrayList<String>();
		ArrayList<String> closing_sequences = new ArrayList<String>();
		opening_sequences.add("<");
		closing_sequences.add(">");
		opening_sequences.add("\"");
		closing_sequences.add("\"");
		opening_sequences.add("\'");
		closing_sequences.add("\'");
		opening_sequences.add(">");
		closing_sequences.add("<");
		String line = "xml version=\"1.0\" encoding=\"utf-8\"";
		StrOps.getAllTextBetweenPatternsIgnoringSections(line, " ", opening_sequences, closing_sequences);
	}
}
