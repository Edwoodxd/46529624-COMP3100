import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n
//./ds-server -c ./ds-sample-config01.xml -i

public class Client {  
    	public static  void main(String[] args) {
	 	try{      
	    		int iterate = 0;
	    		String[] strings;	
	    		String serverMsg;
	    		Integer nRecs;
	    		
	    		Socket s=new Socket("127.0.0.1",50000);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			serverMsg = (String)in.readLine();
			System.out.println(serverMsg);
			
			dout.write(("AUTH Edward\n").getBytes()); //Authentication
			serverMsg = (String)in.readLine();
			System.out.println(serverMsg);
			
			
			
			while(!(serverMsg.contains("NONE"))){
				dout.write(("REDY\n").getBytes()); //Receive Job
				serverMsg = (String)in.readLine();
				System.out.println(serverMsg);
				strings = serverMsg.split(" "); //Split into strings to to get values
				
				while(serverMsg.contains("JCPL")) { //Check if JCPL or JOBN are received
					dout.write(("REDY\n").getBytes());
					serverMsg = (String)in.readLine();
				}
				if(serverMsg.contains("NONE")) { break; } //Break when no jobs left
				strings = serverMsg.split(" ");
				dout.write(("GETS Capable " + strings[4] + " " + strings[5] + " " + strings[6] + "\n").getBytes()); //Checking available servers
				serverMsg = (String)in.readLine();
				System.out.println(serverMsg);
				
				strings = serverMsg.split(" ");
				nRecs = Integer.parseInt(strings[1]);
				System.out.println(nRecs);
				
				dout.write(("OK\n").getBytes());
				serverMsg = (String)in.readLine();
				System.out.println(serverMsg);
				
				
				for(int i = 0; i < nRecs; i++){
						
				}
				
				dout.write(("OK\n").getBytes());
				serverMsg = (String)in.readLine();
				System.out.println(serverMsg);
				
				dout.write(("SCHD " + iterate + " super-silk 0\n").getBytes()); //Schedule Job
				serverMsg = (String)in.readLine();
				System.out.println(serverMsg);
				
				while(!(serverMsg.contains("OK"))){ // Wait for schedule before reiterating
					serverMsg = (String)in.readLine();
				}
				
				iterate++;
			}
			
			dout.write(("QUIT\n").getBytes()); // Quit
			dout.close();  
			s.close();  
			
		}catch(Exception e){System.out.println(e);}  
	}
}  

class serverInfo() {
	String type;
	int id;
	int cores;
	int memory;
	int disk;
	
	public void serverInfo(String type; int id; int cores; int memory; int disk){
		this.type = type;
		this.id = id;
		this.cores = cores;
		this.memory = memory;
		this.disk = disk;
	}
	
	
}

