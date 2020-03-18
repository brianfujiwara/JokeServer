/*--------------------------------------------------------

1. Brian Obmalay / 01-26-20:

2. Java build 9.0.1:

e.g. build 1.5.0_06-b05

3. Precise command-line compilation examples / instructions:

e.g.:

> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java

> java JokeServer
> java JokeClient <servername> 
> java JokeClientAdmin  <servername>


4. Precise examples / instructions to run this program:



In separate shell windows:

> java JokeServer 
> java JokeClient = After typing the name, click Enter, Then Click Enter again to start connection with Server
> java JokeClientAdmin = After typing in the mode, either type in "J" or "P" Click Enter and then Enter again for the Admin to ask for a different server mode.

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

Only have one server running.

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

e.g.:

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:



Only Have one server. For client and admin after the initial input click enter and enter again to connect to server. 

----------------------------------------------------------*/



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class JokeClientAdmin { 
	
	public static void main (String args[]) throws IOException{
		
		String serverName; 
		String mode;
		if (args.length<1) 
			serverName = "localhost"; 
		else serverName = args[0];
		
		
		System.out.println("ADMIN \n");
		System.out.println("Using server: " + serverName + ", Port: 5050");
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		

		
		
		try {
			
			do {
				
				System.out.print
				("Enter Server Mode of either 'J' or 'P' : "); // ask for mode to be sent to server
				System.out.flush();
				mode = in.readLine();
				
				if (!in.readLine().equals("quit")) // if input is not quit connect to server
					setMode(mode, serverName);
			} while (in.readLine().indexOf("quit")<0);
			System.out.println("This connection was cancelled by the user.");
		}catch (IOException x) {x.printStackTrace();}
		
	}
	
	
	static void setMode(String mode , String servername) {
		
		Socket sock;
		PrintStream toServer; // printer from server
		BufferedReader fromServer;
		String messageBack;
		
		try {
			
			sock = new Socket(servername, 5050);
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			
			toServer.println(mode); // send mode to server
			
			messageBack = fromServer.readLine(); // read mode from server that was sent back
			if (messageBack != null) {System.out.println(messageBack);}
			
			sock.close();
			
		}catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
		
		
	}
}

