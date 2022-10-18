package LoadBalance;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;




public class UDPServerLoadBalance {
	public UDPServerLoadBalance(String port) throws IOException {

		@SuppressWarnings("resource")
		DatagramSocket serversocket1 = new DatagramSocket();
		@SuppressWarnings("resource")
		DatagramSocket serversocket2 = new DatagramSocket();
		@SuppressWarnings("resource")
		DatagramSocket serversocket3 = new DatagramSocket();
		
		int bancoON   = 1;
		String banco = "1";
		int agenda01ON = 1;
		int agenda02ON = 1;
		
		System.out.println("Udp server started - LoadBalance");

		//try {
			
			
			//RECEBE
			DatagramSocket serversocket = new DatagramSocket(Integer.parseInt(port));
			while (true) {
				byte[] receivemessage = new byte[1024];
				DatagramPacket receivepacket = new DatagramPacket(receivemessage, receivemessage.length);
				serversocket.receive(receivepacket);
				String message = new String(receivepacket.getData());
				
				
				//ENVIO BANCO ----------------------------------------------------------------------------
				message = "x" + message;
				byte[] replymsg1 = new byte[1024];
				replymsg1 = message.getBytes();
				DatagramPacket sendPacketBanco = new DatagramPacket(replymsg1,replymsg1.length,InetAddress.getByName("localhost"),8080);
				serversocket1.send(sendPacketBanco);
				///ENVIO
				
				//RECEBE
				try {
					while(true) {
						System.out.println("Banco - Envio : "+ message);
						byte[] receivemessage1 = new byte[1024];
						DatagramPacket receivepacket1 = new DatagramPacket(receivemessage1, receivemessage1.length);
						serversocket1.setSoTimeout(100);
						serversocket1.receive(receivepacket1);
						banco = new String(receivepacket1.getData());
						System.out.println("Banco - Recebimento : "+ banco);
						bancoON = 1;
						
						int tamanho = message.length();
						message = message.substring(1,(tamanho-1));
						
						break;
					}
				}catch (IOException e) {
					//e.printStackTrace();
					//System.out.println("Banco - Recebimento : Fora do ar");
					bancoON = 0;
				}
				//RECEBE BANCO ----------------------------------------------------------------------------
				
				
				
				if(bancoON == 1) {
					//ENVIO AGENDA01 ----------------------------------------------------------------------------
					byte[] replymsg2 = new byte[1024];
					replymsg2 = message.getBytes();
					DatagramPacket sendPacketAgenda01 = new DatagramPacket(replymsg2,replymsg2.length,InetAddress.getByName("localhost"),8081);
					serversocket2.send(sendPacketAgenda01);
					///ENVIO
					
					//RECEBE
					try {
						while(true) {
							System.out.println("Agenda_01 - Envio : "+ message);
							byte[] receivemessage2 = new byte[1024];
							DatagramPacket receivepacket2 = new DatagramPacket(receivemessage2, receivemessage2.length);
							serversocket2.setSoTimeout(100);
							serversocket2.receive(receivepacket2);
							String message2 = new String(receivepacket2.getData());
							System.out.println("Agenda_01 - Recebido : "+ message2);
							agenda01ON = 1;
							message = message2;
							break;
						}
					}catch (IOException e) {
						//e.printStackTrace();
						System.out.println("Agenda_01 - Status: Fora do ar");
						agenda01ON = 0;
					}
					//RECEBE AGENDA01 ----------------------------------------------------------------------------
					
					if(agenda01ON == 0) {
						//ENVIO AGENDA02 ----------------------------------------------------------------------------
						byte[] replymsg3 = new byte[1024];
						replymsg3 = message.getBytes();
						DatagramPacket sendPacketAgenda02 = new DatagramPacket(replymsg3,replymsg3.length,InetAddress.getByName("localhost"),8082);
						serversocket3.send(sendPacketAgenda02);
						///ENVIO
						
						//RECEBE
						try {
							while(true) {
								System.out.println("Agenda_02 - Envio : "+ message);
								byte[] receivemessage3 = new byte[1024];
								DatagramPacket receivepacket3 = new DatagramPacket(receivemessage3, receivemessage3.length);
								serversocket3.setSoTimeout(100);
								serversocket3.receive(receivepacket3);
								String message3 = new String(receivepacket3.getData());
								System.out.println("Agenda_02 - Recebido : "+ message3);
								agenda02ON = 1;
								message = message3;
								break;
							}
						}catch (IOException e) {
							//e.printStackTrace();
							System.out.println("Agenda_02 - Status: Fora do ar");
							agenda02ON = 0;
						}
						//RECEBE AGENDA02 ----------------------------------------------------------------------------
					}
				}
				

				if(bancoON == 0) {
					message = "Banco fora do ar";
					System.out.println("Banco - Status : Fora do ar");
				}
				if(agenda01ON == 0 && agenda02ON == 0) {
					message = "Banco - Stratus: ON; \nAgenda_01 - Stratus: fora do ar; \nAgenda_02 - Stratus: fora do ar";
					System.out.println("Banco - Stratus: ON; \nAgenda_01 - Stratus: fora do ar; \nAgenda_02 - Stratus: fora do ar");
				}
				if(agenda01ON == 0) {
					System.out.println("Banco - Stratus: ON; \nAgenda_01 - Stratus: fora do ar; \nAgenda_02 - Stratus: ON");
				}
				
				System.out.println("LoadBalance - Enviando : "+ message);
				System.out.println("------------------------------------");
				//ENVIO
				byte[] replymsg = message.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(replymsg,replymsg.length,receivepacket.getAddress(),receivepacket.getPort());
				serversocket.send(sendPacket);
			}
	}

	public static void main(String[] args) throws IOException{
		new UDPServerLoadBalance(args[0]);
	}
}
