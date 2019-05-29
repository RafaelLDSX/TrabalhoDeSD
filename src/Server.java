import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Server implements Runnable{
	
	private DatagramSocket socket;
	private byte[] buffer = new byte[256];
	
	public Server() throws SocketException, UnknownHostException{
		socket = new DatagramSocket(25565);
	}
	
	public void run(){
		
		boolean running = true;
		
		while(running){
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try{
				socket.receive(packet);
			}
			catch(IOException e){
				System.out.println(e.getMessage());
			}
			String received = new String(packet.getData(), 0, packet.getLength());
			
			if(received.equals("end")){
				running = false;
				System.out.println("Faleceu...");
				continue;
			}
			System.out.println(received);
		}
		socket.close();
	}
	
}
