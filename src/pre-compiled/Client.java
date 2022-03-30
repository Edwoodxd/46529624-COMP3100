import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n

	public class Client {  
	    	public static void main(String[] args) {  
	    		try{      
	    		String[] strings;
	    		int iterate = 0;	
	    		
			Socket s=new Socket("127.0.0.1",50000);  
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			String str = (String)in.readLine();
			System.out.println(str);
			dout.flush();  
			
			dout.write(("AUTH Edward\n").getBytes()); //Authentication
			str = (String)in.readLine();
			System.out.println(str);
			dout.flush();
			
			dout.write(("REDY\n").getBytes());
			str = (String)in.readLine();
			strings = str.split(" ");
			dout.write(("GETS Capable " + strings[4] + " " + strings[5] + " " + strings[6] + "\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			dout.write(("OK\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			dout.write(("OK\n").getBytes());
			str = (String)in.readLine();
			System.out.println(str);
			dout.write(("SCHD " + 0 + " super-silk 0\n").getBytes());
			
			dout.write(("QUIT\n").getBytes());
			dout.close();  
			s.close();  
			
		}catch(Exception e){System.out.println(e);}  
	}  
}  
