package imd.ufrn.br.Banco;

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
public class TCPServerBanco {
    private ServerSocket serverSocket;

    public TCPServerBanco(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    public void service() throws IOException, ClassNotFoundException{
    	Banco b = new Banco();
    	System.out.println("TCP server started - Banco");

        while (true) {
        	System.out.println( "waiting for client" );
        	Socket socket = serverSocket.accept();
            System.out.println( "connected to client" );

            try {
            	ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            	ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            	String msg = (String) input.readObject();
            	
            	System.out.println("Banco - Recebimento de: " + msg);

    			int tamanho = msg.length();
    			String protocolo = msg.substring(0,1);
    			
    			switch(protocolo) {
    			case "c":
    				String ddd = msg.substring(1,3);
    				String telefone = msg.substring(3,11);
    				String nome = msg.substring(11,tamanho);
    				System.out.println("ddd: " + ddd);
    				System.out.println("telefone: " + telefone);
    				System.out.println("nome: " + nome);
    				msg = b.criar(nome, ddd, telefone);
    				protocolo = "h";
    			break;
    			
    			case "r":
    				nome = msg.substring(1,(tamanho));
    				msg = b.buscar(nome);
    				protocolo = "h";
    			break;
    			
    			case "d":
    				nome = msg.substring(1,(tamanho));
    				msg = b.deletar(nome);
    				protocolo = "h";
    			break;
    			case "x":
    				msg = msg.substring(1,(tamanho));
    				protocolo = "h";
    			break;
    			}
    			
    			System.out.println("Banco - Enviando: " + msg);
    			System.out.println("------------------------------------");
            	
            	output.writeObject(msg);
            	output.flush();
            	
            	output.close();
            	input.close();
            	
            }catch(IOException e) {
            	//tratamento do erro
            	System.out.println( "erro" );
            }finally{
            	socket.close();
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException{
        new TCPServerBanco(args[0]).service();
    }
}
