import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n
//./ds-server -c ./ds-sample-config01.xml -i
//./demoS1.sh Client.class -n
//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

public class Client {  
    	public static  void main(String[] args) {
	 	try{      
	 		Server cs = new Server();
	 		Server fs = new Server();
	    		int jobID = 0;
	    		int waitAmount = 0;
	    		String[] strings;	
	    		String[] jobn;
	    		String serverMsg;
	    		Integer nRecs;
	    		
	    		Socket s=new Socket("127.0.0.1",50000);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			serverMsg = (String)in.readLine();
			//System.out.println(serverMsg);
			
			dout.write(("AUTH edward\n").getBytes()); //Authentication
			serverMsg = (String)in.readLine();
			//System.out.println(serverMsg);
			
			
			
			while(!(serverMsg.contains("NONE"))){
				dout.write(("REDY\n").getBytes()); //Receive Job
				serverMsg = (String)in.readLine();
				//System.out.println(serverMsg); 
				
				while(serverMsg.contains("JCPL")) { //Check if JCPL or JOBN are received
					dout.write(("REDY\n").getBytes());
					serverMsg = (String)in.readLine();
					//System.out.println(serverMsg);
				}
				
				if(serverMsg.contains("NONE")) { break; } //Break when no jobs left
				jobn = serverMsg.split(" "); //Split into strings to to get values
				
				dout.write(("GETS Capable " + jobn[4] + " " + jobn[5] + " " + jobn[6] + "\n").getBytes()); //Checking available servers
				serverMsg = (String)in.readLine();
				//System.out.println(serverMsg);
				
				strings = serverMsg.split(" ");
				nRecs = Integer.parseInt(strings[1]);
		
				dout.write(("OK\n").getBytes());
				
				for(int i = 0; i < nRecs; i ++) { //Finding capable server
					serverMsg = (String)in.readLine();  //juju 0 active 120 0 2500 13100 1 0
					//System.out.println(serverMsg);
					strings = serverMsg.split(" ");
					if(i == 0){
						fs = new Server(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6])); 
					}
					if((cs.type == "UNTITLED" || Integer.parseInt(strings[7]) == 0 || Integer.parseInt(strings[8]) == 0) && Integer.parseInt(jobn[4]) + 1 <= Integer.parseInt(strings[4])) {
						cs = new Server(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6]));
						break;
					}
					if(i == nRecs - 1){
						cs = fs;
						break;
					} 
				}
				
				
				dout.write(("OK\n").getBytes());
				serverMsg = (String)in.readLine();
				//System.out.println(serverMsg);
				
				dout.write(("SCHD " + jobID + " " + cs.type + " " + cs.id + "\n").getBytes()); //Schedule Job
				
				serverMsg = (String)in.readLine();
				//System.out.println(serverMsg);
				
				while(!(serverMsg.contains("OK"))){ // Wait for schedule before reiterating
					serverMsg = (String)in.readLine();
				}
				
				jobID++;
			}
			

			dout.write(("QUIT\n").getBytes()); // Quit
			serverMsg = (String)in.readLine();
			//System.out.println(serverMsg);
			
			dout.flush();
			dout.close();  
			s.close();  
			
		}catch(Exception e){System.out.println(e);}  
	}
}  

class Server {
	String type;
	int id;
	int cores;
	int memory;
	int disk;
	
	public Server() {
		String type = "UNTITLED";
		int id = 0;
		int cores = 0;
		int memory = 0;
		int disk = 0;
		int amount = 0;
	}
	
	public Server(String type, int id, int cores, int memory, int disk){
		this.type = type;
		this.id = id;
		this.cores = cores;
		this.memory = memory;
		this.disk = disk;
	}
	
}

