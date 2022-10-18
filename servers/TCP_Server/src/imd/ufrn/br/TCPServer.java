package imd.ufrn.br;

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
public class TCPServer {
    private ServerSocket serverSocket;

    public TCPServer(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    public void service() throws IOException, ClassNotFoundException{
    	System.out.println( "server started" );

        while (true) {
        	System.out.println( "waiting for client" );
        	Socket socket = serverSocket.accept();
            System.out.println( "connected to client" );

            try {
            	PrintWriter output = new PrintWriter(socket.getOutputStream());
            	//ERRO OCORRE AQUI 
            	BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            	String msg = (String) input.readLine();
            	System.out.println( "Mensagem do cliente: " + msg);
            	
            	msg = "Resposta servidor";

            	output.write(msg);
            	output.flush();
            	
            	input.close();
            	output.close();
            	
            }catch(IOException e) {
            	//tratamento do erro
            	e.printStackTrace();
            	System.out.println( "erro" );
            }finally{
            	socket.close();
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new TCPServer(args[0]).service();
    }
}
