package com.java.main.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.java.main.dsa.Trie;
import com.java.main.dsa.Graph;
import com.java.main.dsa.SlidingWindow;
import com.java.main.dsa.PriorityQueueHelper;


public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("************** Welcome to Log Analyzer **************");
		System.out.println();
		
		System.out.println("Enter your file location in the format C:\\Users\\John Doe\\file.log");
		String fileName =  sc.nextLine();
		Path logFilePath = Paths.get(fileName);
		
		
		try {
			
			boolean isRunning = true;
			
			while(isRunning) {
				
			
			List<String> file = Files.lines(logFilePath).map(line -> line.toUpperCase()).collect(Collectors.toList());
						
			System.out.println("Press '1' to get default log highlights");
			System.out.println("Press '2' to get customized log highlights");
			System.out.println("Press '3' to quit");
			
			System.out.println("Enter your choice: ");
			
			String choice = sc.nextLine();
			System.out.println();
			
			if(choice.equals("1")) {				
				
				System.out.println("Analyzing your file....");
				System.out.println();
				System.out.println("******************** Default LOG Highlights ********************");
				System.out.println();
				System.out.println("Fetching ERROR, WARNING and SUCCESS messages from your file....");
				
				List<String> errorMessages = file.stream().filter(s -> s.contains("ERROR")).collect(Collectors.toList());
				long errorMessagesCount = (long)errorMessages.stream().count();
				List<Integer> errorMessagesLineNumbers = IntStream.range(0, file.size()).filter(i -> file.get(i).contains("ERROR")).boxed().map(n -> n+1).collect(Collectors.toList());
				
				List<String> warningMessages = file.stream().map(s -> s.toUpperCase()).filter(s-> s.contains("WARNING")).collect(Collectors.toList());
				long warningMessagesCount = (long)warningMessages.stream().count();
				List<Integer> warningMessagesLineNumbers = IntStream.range(0, file.size()).filter(i -> file.get(i).contains("WARNING")).boxed().map(n -> n+1).collect(Collectors.toList());
				
				List<String> successMessages = file.stream().map(s -> s.toUpperCase()).filter(s-> s.contains("SUCCESS")).collect(Collectors.toList());
				long successMessagesCount = (long)successMessages.stream().count();
				List<Integer> successMessagesLineNumbers = IntStream.range(0, file.size()).filter(i -> file.get(i).contains("SUCCESS")).boxed().map(n -> n+1).collect(Collectors.toList());
				
				System.out.println();
				
				System.out.println("---------------------- ERROR MESSAGES ----------------------");
				System.out.println();
				System.out.println("Total ERROR Messages: "+ errorMessagesCount);
				System.out.println();
				
				for(int i=0;i<errorMessagesCount;i++) {
					System.out.println("Message Number: " + (i+1));
					System.out.println("Line Number: " + errorMessagesLineNumbers.get(i));
					System.out.println("Message Descpription: " + errorMessages.get(i));
					System.out.println();
				}
				
				System.out.println();
				
				System.out.println("---------------------- WARNING MESSAGES ----------------------");
				System.out.println();
				System.out.println("Total WARNING Messages: "+ warningMessagesCount);
				System.out.println();
				
				for(int i=0;i<warningMessagesCount;i++) {
					System.out.println("Message Number: " + (i+1));
					System.out.println("Line Number: " + warningMessagesLineNumbers.get(i));
					System.out.println("Message Description: " + warningMessages.get(i));
					System.out.println();
				}
				
				System.out.println();
				
				System.out.println("---------------------- SUCCESS MESSAGES ----------------------");
				System.out.println();
				System.out.println("Total SUCCESS Messages: "+ successMessagesCount);
				System.out.println();
				
				for(int i=0;i<successMessagesCount;i++) {
					System.out.println("Message Number: " + (i+1));
					System.out.println("Line Number: " + successMessagesLineNumbers.get(i));
					System.out.println("Message Description: " + successMessages.get(i));
					System.out.println();
				}
				
			} else if(choice.equals("2")) {
				
				System.out.println("Analyzing your file....");
				System.out.println();
				System.out.println("******************** Customized LOG Highlights ********************");
				System.out.println();
				
				System.out.println("Enter number of keywords you want to search for:");
				int keywords = sc.nextInt();
				sc.nextLine();
				
				System.out.println();
				
				System.out.println("Press '1' if you want keywords to be case-sensitive");
				System.out.println("Press '2' if you do not want keywords to be case-sensitive");
				System.out.println("Enter your choice:");
				
				String caseChoice = sc.nextLine();
				
				System.out.println();
				
				List<String> customKeywords = new ArrayList<>();
				
				for(int i=0;i<keywords;i++) {
					System.out.println("Enter Keyword " + (i+1)+ ":");
					String keyword = sc.nextLine();
					customKeywords.add(keyword);
				}
				
				if(caseChoice.equals("1")) {
					
					System.out.println("Fetching customized messages from your file....");
										
					CustomKeywords.logsForCustomKeywordsCaseSensitive(customKeywords, fileName);
					
					
				}else if(caseChoice.equals("2")) {
					
					System.out.println("Fetching customized messages from your file....");
							
					CustomKeywords.logsForCustomKeywordsCaseInSensitive(customKeywords, fileName);
					
				} else {
					System.out.println("Invalid choice");
					continue;
				}
				
				
			}  else if (choice.equals("4")) {
				System.out.println("Enter the number of top logs you want to retrieve:");
				int topN = sc.nextInt();
				sc.nextLine(); // Consume the newline character

				System.out.println("Fetching top " + topN + " logs based on severity...");

				PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> {
					// Custom comparator to prioritize ERROR > WARNING > SUCCESS
					if (a.contains("ERROR") && !b.contains("ERROR")) return -1;
					if (b.contains("ERROR") && !a.contains("ERROR")) return 1;
					if (a.contains("WARNING") && !b.contains("WARNING")) return -1;
					if (b.contains("WARNING") && !a.contains("WARNING")) return 1;
					return 0;
				});

				for (String log : file) {
					pq.offer(log);
					if (pq.size() > topN) {
						pq.poll();
					}
				}

				System.out.println("Top " + topN + " Logs:");
				while (!pq.isEmpty()) {
					System.out.println(pq.poll());
				}
			}  else if(choice.equals("3")) {
				
				isRunning = false;
	
			} else {
				System.out.println("Invalid Choice");
				continue;
			}
			
			}
						
		} catch (IOException e) {
			
			System.out.println("Unable to locate the file: " + e.getMessage());
			System.out.println("Exiting Application.....");
			System.out.println("Application Closed");
			System.exit(0);
			
		}
		
		finally {
			sc.close();
			System.out.println("Exiting Application.....");
			System.out.println("Application Closed");
			System.exit(0);
		}
		
	}

}
