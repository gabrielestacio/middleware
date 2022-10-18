package imd.ufrn.br;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.net.Socket;

@SuppressWarnings("unused")
public class TCPClient {
		public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException{

	    try {
	       Socket socket = new Socket("localhost", 9099);

	       ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	       ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	            
	       //create
	       String msg = "c8488665599Maria";
	       
	       //read
	       //String msg = "rMaria";
	       
	       //delet
	       //String msg = "dMaria";
	       
	       output.writeObject(msg);
	       output.flush();
	         
	       msg = (String) input.readObject();
       	   System.out.println( "Mensagem do servidor: " + msg);
	       
	       
	       input.close();
	       output.close();  	
	       socket.close();
	       
	    }catch(IOException e) {
        	//tratamento do erro
        	System.out.println( "erro" );
        }
		
	}
}
