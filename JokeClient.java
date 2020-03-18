/*--------------------------------------------------------

1. Brian Obmalay / 01-26-20:

2. Java build 9.0.1:


3. Precise command-line compilation examples / instructions:

> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java

> java JokeServer
> java JokeClient <servername> 
> java JokeClientAdmin  <servername>



4. Precise examples / instructions to run this program:


In separate shell windows:

> java JokeClient = After typing the name, click Enter, Then Click Enter again to start connection with Server
> java JokeClientAdmin = After typing in the mode Click Enter and then Enter again for the Admin to ask for a different server mode.

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

Only have one server running.

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.



 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:



Only Have one server. For client and admin after the initial input click enter and enter again to connect to server. 

----------------------------------------------------------*/


import java.io.*;
import java.net.*;
import java.util.*;


public class JokeClient {
	
	
	static Random r = new Random(); //Random
	
	private static String name;
	
	private static List<String> jo = new ArrayList<>(Arrays.asList("JA", "JB", "JC", "JD")); //Making List of Symbols
	private static List<String> pv = new ArrayList<>(Arrays.asList("PA", "PB", "PC", "PD")); 
	private static List<String> temp = new ArrayList<>(); ///Temp List to store used jokes and proverbs
	private static List<String> temp2 = new ArrayList<>();
	
	

	public static void main (String args[]) throws IOException {
		String serverName; 
		if (args.length<1) 
			serverName = "localhost"; 
		else serverName = args[0];

		System.out.println("Joke Client Ready to Go!!! \n");
		System.out.println("Server: " + serverName + ", Port: 4545");
		System.out.print
		("Enter your name: "); // name to be saved for this client
		System.out.flush();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //reading input from user
		
		//String name;
		name=in.readLine(); //read name from input
	
		
		
		try {
			String comment;
			comment = in.readLine(); //input from user of anything other than <enter>
			do {
				
				if (!comment.equals("quit"))//if was not inputted then connect to server
					connectServer(name, serverName);//connect to server to get joke or proverb
			} while (in.readLine().indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch (IOException x) {x.printStackTrace();}

	}


	static void connectServer(String name, String serverName) { 
		Socket sock;
		BufferedReader fromServer; //reader from server
		PrintStream toServer; // printer from server
		String textFromServer;
		String result;

		try {
			sock = new Socket(serverName, 4545); //new socket object

			fromServer=
					new BufferedReader(new InputStreamReader(sock.getInputStream())); 
			toServer = new PrintStream(sock.getOutputStream()); 

			toServer.println(name); // send name to server
			
			if ( jo.isEmpty()) { // check if joke list is empty which means all jokes have been used
				
				jo.addAll(temp); // start over cycle of jokes
				temp.clear();
			}
			
			toServer.println(jo.size()); // send size of joke list to server
			
				for ( String j : jo) {
			
					toServer.println(j); //  send jokes to server
				}
			
				if ( pv.isEmpty()) { // same as for jokes
					
					pv.addAll(temp2);
					temp2.clear();
				}	
				
			toServer.println(pv.size());
			
				for ( String p : pv) {
			
					toServer.println(p); 
				}
			
			
			
			toServer.flush();

		
				textFromServer = fromServer.readLine(); //read returning info from server
				String[] hj = textFromServer.split("\\s+"); // get the symbol of the joke or proverb to modify list
				
				String item = hj[0];
				
				if (jo.contains(item)) { // remove joke
				
				temp.add(hj[0]);
				jo.remove(hj[0]);
				

				
				}else {
					
					temp2.add(item);
					pv.remove(item); // remove proverb
					

				}
				
				
				if (textFromServer != null) {
					System.out.println(textFromServer);//print out text
					
					if (jo.isEmpty()) { // checking for empty list to determine if cycles are done
						System.out.println("JOKE CYCLE COMPLETED"); 
					}else if (pv.isEmpty()) {
						System.out.println("PROVERB CYCLE COMPLETED");
					}
				}
				
				
			sock.close(); //close socket

		} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}

