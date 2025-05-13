package com.java.main.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomKeywords {
	
	
	public static void logsForCustomKeywordsCaseSensitive(List<String> customKeywords, String fileName) throws IOException{
		
		Path logFilePath = Paths.get(fileName);
		
		
			
			List<String> file = Files.lines(logFilePath).collect(Collectors.toList());
			
			for(String msg: customKeywords) {
				
				List<String> customMessages = file.stream().filter(s-> s.contains(msg)).collect(Collectors.toList());
				long customMessagesCount = (long)customMessages.stream().count();
				List<Integer> customMessagesLineNumbers = IntStream.range(0, file.size()).filter(i -> file.get(i).contains(msg)).boxed().map(n -> n+1).collect(Collectors.toList());
							
				
				System.out.println();
				
				System.out.println("---------------------- " + msg + " MESSAGES ----------------------");
				System.out.println();
				System.out.println("Total " + msg + " Messages: "+ customMessagesCount);
				System.out.println();
				
				for(int i=0;i<customMessagesCount;i++) {
					System.out.println("Message Number: " + (i+1));
					System.out.println("Line Number: " + customMessagesLineNumbers.get(i));
					System.out.println("Message Description: " + customMessages.get(i));
					System.out.println();
				}			
								
			}
		
	}

	public static void logsForCustomKeywordsCaseInSensitive(List<String> customKeywords, String fileName) throws IOException{
		
		Path logFilePath = Paths.get(fileName);
		
		
		
		List<String> file = Files.lines(logFilePath).map(s -> s.toUpperCase()).collect(Collectors.toList());
		
		for(String msg: customKeywords) {
			
			List<String> customMessages = file.stream().filter(s-> s.contains(msg.toUpperCase())).collect(Collectors.toList());
			long customMessagesCount = (long)customMessages.stream().count();
			List<Integer> customMessagesLineNumbers = IntStream.range(0, file.size()).filter(i -> file.get(i).contains(msg.toUpperCase())).boxed().map(n -> n+1).collect(Collectors.toList());
						
			
			System.out.println();
			
			System.out.println("---------------------- " + msg + " MESSAGES ----------------------");
			System.out.println();
			System.out.println("Total " + msg + " Messages: "+ customMessagesCount);
			System.out.println();
			
			for(int i=0;i<customMessagesCount;i++) {
				System.out.println("Message Number: " + (i+1));
				System.out.println("Line Number: " + customMessagesLineNumbers.get(i));
				System.out.println("Message Description: " + customMessages.get(i));
				System.out.println();
			}			
							
		}
		
		
	}

}
