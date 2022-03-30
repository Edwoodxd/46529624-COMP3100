import java.io.*;
import java.net.*;  

//./ds-server -c ./ds-sample-config01.xml -v brief -n

	public class Client {  
	    	public static void main(String[] args) {  
	    		try{      
	    		
			Socket s=new Socket("127.0.0.1",50000);  
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());
			
			dout.write(("HELO\n").getBytes()); //Sending HELO to server
			String str = (String)in.readLine();
			System.out.println(str);
			dout.flush();  
			
			dout.close();  
			s.close();  
			
		}catch(Exception e){System.out.println(e);}  
	}  
}  
