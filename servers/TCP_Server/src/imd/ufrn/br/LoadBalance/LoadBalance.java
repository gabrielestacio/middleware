package imd.ufrn.br.LoadBalance;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class LoadBalance {
    private ServerSocket serverSocket;

    public LoadBalance(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    public void service() throws IOException, ClassNotFoundException{

    	System.out.println("TCP server started - LoadBalance");
    	
		int bancoON   = 0;
		int agenda01ON = 0;
		int agenda02ON = 0;
		String message = "";

        while (true) {
        	System.out.println( "waiting for client" );
        	Socket socket = serverSocket.accept();
            System.out.println( "connected to client" );

            try {
            	PrintWriter output = new PrintWriter(socket.getOutputStream());
            	BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            	message = (String) input.readLine();
            	
            	System.out.println("LoadBalance - Recebimento de Cliente: " + message);

            	//BANCO ------------------------------------------------------------------
                try {
                	Socket socketB = new Socket("localhost", 9090);
                	socketB.setSoTimeout(100);
                	ObjectOutputStream outputB = new ObjectOutputStream(socketB.getOutputStream());
                	ObjectInputStream inputB = new ObjectInputStream(socketB.getInputStream());
         	            
                	message = "x"+message;
                	outputB.writeObject(message);
                	outputB.flush();
                	System.out.println("Enviando para Banco: " + message);
         	         
                	message = (String) inputB.readObject();
                	System.out.println("Recebimento de Banco: " + message);
         	       
         	       
                	inputB.close();
                	outputB.close();  	
                	socketB.close();
                	bancoON = 1;
         	       
         	     }catch(IOException e) {
                 	//tratamento do erro
         	    	//System.out.println( "erro" );
                 	bancoON = 0;
                 }
            	//BANCO ------------------------------------------------------------------
                
                
              //AGENDA01 ------------------------------------------------------------------
                if(bancoON == 1) {
	                try {
	                	Socket socketA1 = new Socket("localhost", 9091);
	                	socketA1.setSoTimeout(100);
	                	ObjectOutputStream outputA1 = new ObjectOutputStream(socketA1.getOutputStream());
	                	ObjectInputStream inputA1 = new ObjectInputStream(socketA1.getInputStream());
	         	            
	                	outputA1.writeObject(message);
	                	outputA1.flush();
	                	System.out.println("Enviando para Agenda01: " + message);
	         	         
	                	message = (String) inputA1.readObject();
	                	System.out.println("Recebimento de Agenda01: " + message);
	         	       
	         	       
	                	inputA1.close();
	                	outputA1.close();  	
	                	socketA1.close();
	                	agenda01ON = 1;
	         	       
	         	     }catch(IOException e) {
	                 	//tratamento do erro
	         	    	//System.out.println( "erro" );
	         	    	agenda01ON = 0;
	                 }
                }
            	//AGENDA01 ------------------------------------------------------------------
                
              //AGENDA02 ------------------------------------------------------------------
                if(agenda01ON == 0 && bancoON == 1) {
	                try {
	                	Socket socketA2 = new Socket("localhost", 9092);
	                	socketA2.setSoTimeout(100);
	                	ObjectOutputStream outputA2 = new ObjectOutputStream(socketA2.getOutputStream());
	                	ObjectInputStream inputA2 = new ObjectInputStream(socketA2.getInputStream());
	         	            
	                	outputA2.writeObject(message);
	                	outputA2.flush();
	                	System.out.println("Enviando para Agenda02: " + message);
	         	         
	                	message = (String) inputA2.readObject();
	                	System.out.println("Recebimento de Agenda02: " + message);
	         	       
	         	       
	                	inputA2.close();
	                	outputA2.close();  	
	                	socketA2.close();
	                	agenda02ON = 1;
	         	       
	         	     }catch(IOException e) {
	                 	//tratamento do erro
	         	    	//System.out.println( "erro" );
	         	    	agenda02ON = 0;
	                 }
                }
            	//AGENDA02 ------------------------------------------------------------------
                
                
                
                
                if(bancoON == 0) {
					message = "Banco - Status : Fora do ar";
					System.out.println(message);
				}
                else{
	                if(agenda01ON == 0 && agenda02ON == 0 && bancoON == 0) {
						message = "Banco - Stratus: ON; \nAgenda_01 - Stratus: fora do ar; \nAgenda_02 - Stratus: fora do ar";
						System.out.println(message);
                	}
				}
				if(agenda01ON == 0 && bancoON == 1) {
					System.out.println("Banco - Stratus: ON; \nAgenda_01 - Stratus: fora do ar; \nAgenda_02 - Stratus: ON");
				}
				
				System.out.println("LoadBalance - Enviando: "+ message);
				System.out.println("------------------------------------");
            	
            	
            	output.write(message);
            	output.flush();
            	
            	input.close();
            	output.close();
            	socket.close();
            	
            }catch(IOException e) {
            	//tratamento do erro
            	//System.out.println( "erro" );
            }finally{
            	//socket.close();
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new LoadBalance(args[0]).service();
    }
}
