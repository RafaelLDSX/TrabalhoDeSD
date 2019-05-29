import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client implements Runnable{
	
	private DatagramSocket socket;
	private InetAddress address;
	private byte[] buffer;
	
	public void run(){
		
		boolean running = true;
		Scanner scanner = new Scanner(System.in);
		while(running){
			String msg = scanner.nextLine();
			sendMessage(msg);
			
			if(msg.equals("end")){
				running = false;
				continue;
			}
			
		}
		socket.close();
		
	}
	
	public Client() throws SocketException, UnknownHostException{
		socket = new DatagramSocket();
		socket.setBroadcast(true);
		address = InetAddress.getLocalHost();
	}
	
	public void sendMessage(String msg) {
		buffer = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 25565);
		
		try{
			socket.send(packet);
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void close(){
		socket.close();
	}
	
}
