package imd.ufrn.br.Banco;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerBanco {
	public UDPServerBanco(String port) {
		Banco b = new Banco();
		System.out.println("Udp server started - Banco");
		try {
			DatagramSocket serversocket = new DatagramSocket(Integer.parseInt(port));
			while (true) {
				byte[] receivemessage = new byte[1024];
				DatagramPacket receivepacket = new DatagramPacket(receivemessage, receivemessage.length);
				serversocket.receive(receivepacket);
				String message = new String(receivepacket.getData());
				
				System.out.println("Banco - Recebimento de: " + message);
	
				int tamanho = message.length();
				String protocolo = message.substring(0,1);
				
				switch(protocolo) {
				case "c":
					String ddd = message.substring(1,3);
					String telefone = message.substring(3,11);
					String nome = message.substring(11,(tamanho-11));
					System.out.println("ddd: " + ddd);
					System.out.println("telefone: " + telefone);
					System.out.println("nome: " + nome);
					message = b.criar(nome, ddd, telefone);
					protocolo = "h";
				break;
				
				case "r":
					nome = message.substring(1,(tamanho-1));
					message = b.buscar(nome);
					protocolo = "h";
				break;
				
				case "d":
					nome = message.substring(1,(tamanho-1));
					message = b.deletar(nome);
					protocolo = "h";
				break;
				case "x":
					message = message.substring(1,(tamanho-1));
					protocolo = "h";
				break;
				}
				
				System.out.println("Banco - Enviando: " + message);
				System.out.println("------------------------------------");
				byte[] replymsg = message.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(replymsg,replymsg.length,receivepacket.getAddress(),receivepacket.getPort());
				serversocket.send(sendPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("udp server terminating - Banco");
	}

	public static void main(String[] args) {
		new UDPServerBanco(args[0]);
	}
}





