import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n
//./ds-server -c ./ds-sample-config01.xml -i
//./demoS1.sh Client.class -n
//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

public class Client {  
    	public static  void main(String[] args) {
	 	try{      
	 		Server cs = new Server(); //Current server
	 		Server fs = new Server(); //First server
	 		Server ls = new Server(); //Last server
	    		int jobID = 0;
	    		String[] strings;	
	    		String[] jobn;
	    		String serverMsg;
	    		Integer nRecs;
	    		
	    		Socket s=new Socket("127.0.0.1",50000);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			serverMsg = (String)in.readLine();
			
			dout.write(("AUTH edward\n").getBytes()); //Authentication
			serverMsg = (String)in.readLine();
			
			
			
			while(!(serverMsg.contains("NONE"))){
				dout.write(("REDY\n").getBytes()); //Receive Job
				serverMsg = (String)in.readLine();
				
				while(serverMsg.contains("JCPL")) { //Check if JCPL or JOBN are received
					dout.write(("REDY\n").getBytes());
					serverMsg = (String)in.readLine();
				}
				
				if(serverMsg.contains("NONE")) { break; } //Break when no jobs left
				jobn = serverMsg.split(" "); //Split into strings to to get values
				
				dout.write(("GETS Capable " + jobn[4] + " " + jobn[5] + " " + jobn[6] + "\n").getBytes()); //Checking available servers
				serverMsg = (String)in.readLine();
				
				strings = serverMsg.split(" ");
				nRecs = Integer.parseInt(strings[1]);
		
				dout.write(("OK\n").getBytes());
				
				for(int i = 0; i < nRecs; i ++) { //Finding capable server
					serverMsg = (String)in.readLine();
					strings = serverMsg.split(" ");
					if(i == 0){ //initialize first server
						fs = new Server(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6])); 
					}
					if((cs.type == "UNTITLED" || Integer.parseInt(strings[7]) == 0 || Integer.parseInt(strings[8]) == 0) && Integer.parseInt(jobn[4]) <= Integer.parseInt(strings[4])) { //initialize current server
						cs = new Server(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6]));
						break;
					}
					if(i == nRecs - 1){ //initialize last server
						ls = new Server(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]), Integer.parseInt(strings[6]));
						if(jobID % 2 == 0){ //use modulo for consistent turnaround time and rental cost
							cs = fs;
						}else{
							cs = ls;
						}
						break;
					} 
				}
				
				
				dout.write(("OK\n").getBytes());
				serverMsg = (String)in.readLine();
				
				dout.write(("SCHD " + jobID + " " + cs.type + " " + cs.id + "\n").getBytes()); //Schedule Job
				
				serverMsg = (String)in.readLine();
				
				while(!(serverMsg.contains("OK"))){ // Wait for schedule before reiterating
					serverMsg = (String)in.readLine();
				}
				
				jobID++;
			}
			

			dout.write(("QUIT\n").getBytes()); // Quit
			serverMsg = (String)in.readLine();
			
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

