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

e.g.:

In separate shell windows:

> java JokeServer 
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class JokeServer {
	
	static String Mode = "J"; //Mode for Server to be in, P for proverb, J for joke mode, J is default
	
	//private static HashMap<String, String> gorr = new HashMap<>();

	public static void main(String a[]) throws IOException{
		
		String serverName;
		String server2;
		if (a.length<1) {
			serverName = "localhost"; //servername used but only have one server anyway
		}else {
			serverName = a[0];
		}
				
				
		int q_len = 6; 
		int port = 4545; // port for client
		
		AdminThread AL = new AdminThread(); // Making a new thread for admin to connect
	    Thread t = new Thread(AL);
	    t.start(); 
		
		
		Socket sock; //socket object
		ServerSocket servsock = new ServerSocket(port, q_len); 
		
		
		System.out.println
		("Hey there, this is Brian Obmalay's server, listening at port 4545. \n");
		while(true) {
			sock = servsock.accept(); //looking for client to connect to
			new Worker(sock, Mode).start(); //pass mode to thread worker to determine which hashmap to pull from to send to client
		}
	}
	
		
	
	
}


class AdminThread implements Runnable {
	  public static boolean Control = true;

	  
	  public void run(){ // Wait for admin to connect
	    System.out.println("This is the Joke Server");
	    
	    int q_len = 6; 
	    int port = 5050;  //Port for admin to connect to
	    Socket sock;

	    try{
	      ServerSocket servsock = new ServerSocket(port, q_len);
	      while (Control) {
		// wait for the next ADMIN client connection:
		sock = servsock.accept();
		new AdminWorker (sock).start(); 
	      }
	    }catch (IOException ioe) {System.out.println(ioe);}
	  }
	  
}

class AdminWorker extends Thread { //worker for admin
	
	Socket sock;
	
	
	AdminWorker(Socket sock){
		
		this.sock =sock;
	}
	
	
	public void run() {
		PrintStream out = null; 
		BufferedReader in = null;
		
		try {
			
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream())); 
			out = new PrintStream(sock.getOutputStream());
			
			JokeServer.Mode = in.readLine(); //reads mode sent from admin 
			
			out.println("server is now in " + JokeServer.Mode + " mode"); //sends admin message to tell which mode it is in
			
		}catch(IOException ioe){
			
		}
		
		
	}
}



class Worker extends Thread{ //thread for handling client connections
	Socket sock;
	static Random shuffle = new Random(); 
	
	static HashMap<String, String> gorr = new HashMap<>(); // Maps to contain all the jokes (gorr) and provers
	static HashMap<String, String> pv = new HashMap<>();
	
	
  //static List<String> RandomJokes = new ArrayList<String>(gorr.keySet());
  //static List<String> RandomPV = new ArrayList<String>(pv.keySet());
  static List<String> RandomPV = new ArrayList<String>(); // Empty list that are filled when client sends over modified list.
  static List<String> RandomJokes = new ArrayList<String>();
	
	
	String name;
	static String Mode;
	
	
	
	Worker (Socket s, String Mode) { 
		
		sock = s; 
		
		Worker.Mode = Mode;
		
		pv.put("PA", "Flattery will get you nowhere");  ///proverbs to fill map
		pv.put("PB", "Never say never");
		pv.put("PC", "Opportunity never knocks twice at any man's door");
		pv.put("PD", "All you need is love");
		
		gorr.put("JA", "Why did the gym close down? It just didn't work out!");
		gorr.put("JB", "What did the buffalo say when his son left for college? Bison!");
		gorr.put("JC", "Here, I bought you a calendar. Your days are numbered now.");
		gorr.put("JD", "I have many jokes about unemployed people, sadly none of them work.");
		
		 //RandomJokes = new ArrayList<String>(gorr.keySet());
		// RandomPV = new ArrayList<String>(pv.keySet());
	
	}
	
	public void run() {
		PrintStream out = null; 
		BufferedReader in = null; 
		try {
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream())); 
			out = new PrintStream(sock.getOutputStream()); 
			try {
				//String name; 
				name = in.readLine();  // getting name of client 
				
				
				
				int tom = Integer.parseInt(in.readLine()); // getting number of elements in Joke List sent from client
				
				for(int b =0; b < tom; b++ ) {
					
					RandomJokes.add(in.readLine()); // adding jokes from client
					
					
				}
				
				
				int jerry = Integer.parseInt(in.readLine()); // getting number of elements in Proverb list sent from client
	
				for(int b =0; b < jerry; b++ ) {
					
					RandomPV.add(in.readLine()); // adding jokes from client
					
					
				}
				
				//System.out.println(RandomPV); // Print list of proverbs sent over .... for debugging purposes 
				//System.out.println(RandomJokes); // Print list of Jokes sent over .... for debugging purposes
				printToClient(name, out); // joke or proverb to client
			}catch (IOException x) { 
				System.out.println("Server Pipline Issue has occurred");
				x.printStackTrace();
			}
			sock.close(); 
		
		
	}catch (IOException ioe) {System.out.println(ioe);}
}
	
	static void printToClient (String name, PrintStream out) { 
		try {
			
			if ( Mode.equals("J")) { // If mode is Joke 
			
			
			String bj = RandomJokes.get(shuffle.nextInt(RandomJokes.size())); //shuffle jokes
			String jk = gorr.get(bj); // get joke from map
			
			
			out.println(bj + " " + name+":" + " "+ jk);
			
			RandomJokes.clear(); // clear both list
			RandomPV.clear();
			
			}else if (Mode.equals("P")) { // If mode is Proverb
				
				String bj = RandomPV.get(shuffle.nextInt(RandomPV.size()));
				String jk = pv.get(bj);
				
				
				out.println(bj + " " + name+":" + " "+ jk);
				
				RandomPV.clear();
				RandomJokes.clear();
			}
			
			
			
			
		} catch(Exception ex) {
			out.println ("A Problem has occurred " );
		}
	}
	

}
