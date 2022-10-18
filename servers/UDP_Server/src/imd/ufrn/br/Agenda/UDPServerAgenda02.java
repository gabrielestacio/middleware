package imd.ufrn.br.Agenda;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class UDPServerAgenda02 {
	public UDPServerAgenda02(String port) {
		Agenda a = new Agenda();
		System.out.println("Udp server started - Agenda02");
		try {
			
			
			//RECEBE
			DatagramSocket serversocket = new DatagramSocket(Integer.parseInt(port));
			while (true) {
				byte[] receivemessage = new byte[1024];
				DatagramPacket receivepacket = new DatagramPacket(receivemessage, receivemessage.length);
				serversocket.receive(receivepacket);
				String message = new String(receivepacket.getData());
				
				int tamanho = message.length();
				
				System.out.println("Agenda02 - Recebimento de: " + message);
				
				if(tamanho <= 11) {
					if(tamanho == 0) {
						System.out.println("Confirmo Recebimento de: " + message + "\nfrom: " + receivepacket.getAddress());
						System.out.println("Pacote vazio");
						message = "Pacote enviado vazio";
					}else {
						System.out.println("Confirmo Recebimento de: " + message + "\nfrom: " + receivepacket.getAddress());
						System.out.println("Pacote fora do formato");
						message = "Pacote enviado fora do formato do protocolo";
					}

				}else {
					String protocolo = message.substring(0,1);

					//ENVIO
					byte[] replymsg1 = new byte[1024];
					replymsg1 = message.getBytes();
					@SuppressWarnings("resource")
					DatagramSocket serversocket1 = new DatagramSocket();
					DatagramPacket sendPacketAgenda = new DatagramPacket(replymsg1,replymsg1.length,InetAddress.getByName("localhost"),8080);
					serversocket1.send(sendPacketAgenda);
					//ENVIO
					
					//RECEBE
					byte[] receivemessage1 = new byte[1024];
					DatagramPacket receivepacket1 = new DatagramPacket(receivemessage1, receivemessage1.length);
					serversocket1.receive(receivepacket1);
					String message1 = new String(receivepacket1.getData());
					//RECEBE
					
					System.out.println("Agenda02 - Recebimento do Banco: " + message1);
					System.out.println("------------------------------------");

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
					byte[] replymsg = message.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(replymsg,replymsg.length,receivepacket.getAddress(),receivepacket.getPort());
					serversocket.send(sendPacket);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("udp server terminating - Agenda02");
	}

	public static void main(String[] args) {
		new UDPServerAgenda02(args[0]);
	}
}




