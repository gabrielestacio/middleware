package imd.ufrn.br.Agenda;

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
public class TCPServerAgenda02 {
    private ServerSocket serverSocket;

    public TCPServerAgenda02(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    public void service() throws IOException, ClassNotFoundException{
    	Agenda a = new Agenda();
		System.out.println("TCP server started - Agenda01");

        while (true) {
        	System.out.println( "waiting for client" );
        	Socket socket = serverSocket.accept();
            System.out.println( "connected to client" );

            try {
            	ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            	ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            	String message = (String) input.readObject();
        		System.out.println("Agenda01 - Recebimento de: " + message);
        				
        		int tamanho = message.length();
        				
        		if(tamanho == 0) {
        			System.out.println("Confirmo Recebimento de: " + message );
        			System.out.println("Pacote vazio");
        			message = "Pacote enviado vazio";
        		}else {
        			String protocolo = message.substring(0,1);
        			System.out.println("PROTOCOLO: " +protocolo);
        			
        			
        			Socket socketBanco = new Socket("localhost", 9090);

        		    ObjectOutputStream outputB = new ObjectOutputStream(socketBanco.getOutputStream());
        		    ObjectInputStream inputB = new ObjectInputStream(socketBanco.getInputStream());
        		            
        	 	
        		    outputB.writeObject(message);
        		    outputB.flush();
        		         
        		    String message1 = (String) inputB.readObject();
        			System.out.println("Agenda01 - Recebimento do Banco: " + message1);
        			System.out.println("------------------------------------");
        		       
        		       
        	       	inputB.close();
        		    outputB.close();  	
        		    socketBanco.close();
        			
        			
        			

        			switch(protocolo) {
        			case "c":
        				message = a.Criar(message1.substring(0,1));
        				protocolo = "h";	
        			break;
        					
        			case "r":
        				message = a.Buscar(message1);
        				protocolo = "h";	
        			break;
        					
        			case "d":
        				message = a.Deletar(message1);
        				protocolo = "h";	
        			break;
        					
        				default:
        				message = "Protocolo fora do formato";
        			}

        			//ENVIO

                	output.writeObject(message);
                	output.flush();
                	
                	input.close();
                	output.close();
        		}
        				
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }finally{
	        	socket.close();
	        }
        System.out.println("TCP server terminating - Agenda01");
        }      	
}


    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new TCPServerAgenda02(args[0]).service();
    }
}
