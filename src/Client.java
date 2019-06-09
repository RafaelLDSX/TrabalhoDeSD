import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Client implements Runnable{
	
	private final int id;
	private boolean isCoordinator;
	private DatagramSocket socket;
	private InetAddress address;
	private byte[] buffer;
	
	public int getId() {
		return this.id;
	}
	
	public boolean IsCoordinator() {
		return isCoordinator;
	}
	
	public Client(int id) throws SocketException, UnknownHostException{
		this.id = id;
		socket = new DatagramSocket();
		socket.setBroadcast(true);
		address = this.listAllBroadcastAddresses().get(0);
		socket.setSoTimeout(3000);
	}
	
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
			
			this.listen();
			
		}
		scanner.close();
		socket.close();
		
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
	
	public void askForCoordinator() throws IOException {
		String msg = "who is the coordinator?";
		this.sendMessage(msg);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
		 	socket.receive(packet);
		}
		catch(SocketTimeoutException e) {
			this.startElection();
		}
	}
	
	public DatagramPacket listen() {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
			answerMessage(packet.getData());
		}
		catch(SocketTimeoutException e) {
			System.out.println("Client " + this.id + ": nothing happened...");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}
	
	public void answerMessage(byte[] msg) {
		String parsedMessage = new String(msg);
		switch(parsedMessage) {
		case "who is the coordinator":
			if(this.IsCoordinator()) {
				this.sendMessage("I am, my id is " + this.id);
			}
			break;
		}
	}
	
	public void startElection() {

	}
	
	List<InetAddress> listAllBroadcastAddresses() throws SocketException {
	    List<InetAddress> broadcastList = new ArrayList<>();
	    Enumeration<NetworkInterface> interfaces 
	      = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	        NetworkInterface networkInterface = interfaces.nextElement();
	 
	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	            continue;
	        }
	 
	        networkInterface.getInterfaceAddresses().stream() 
	          .map(a -> a.getBroadcast())
	          .filter(Objects::nonNull)
	          .forEach(broadcastList::add);
	    }
	    return broadcastList;
	}
	
	public void close(){
		socket.close();
	}
	
}
