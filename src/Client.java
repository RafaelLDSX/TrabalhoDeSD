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
import java.util.Random;


public class Client implements Runnable{
	
	private final int id;
	private boolean isCoordinator;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private List<InetAddress> broadcastAddresses;
	private byte[] buffer;
	private Status status;
	
	
	public int getId() {
		return this.id;
	}
	
	public boolean IsCoordinator() {
		return isCoordinator;
	}
	
	public Client(int id) throws SocketException, UnknownHostException{
		this.id = id;
		buffer = new byte[500];
		socket = new DatagramSocket(25565);
		socket.setBroadcast(true);
		broadcastAddresses = this.listAllBroadcastAddresses();
		socket.setSoTimeout(3000);
		packet = new DatagramPacket(buffer, buffer.length);
		status = Status.NORMAL;
		isCoordinator = false;
	}
	
	public void run(){
		
		boolean running = true;
		Random random = new Random();
		
		while(running){
			switch(this.status) {
			case NORMAL:
				this.listen();
				if(random.ints(0, 4).findFirst().getAsInt() == 1) {
					this.askForCoordinator();
				}
				break;
			case ELECTION:
				this.listen();
			}
			
			
		}
		socket.close();
		
	}
	
	//passando a mensagem, envia-a em broadcast
	public void sendMessage(String msg) {
		buffer = msg.getBytes();

		try{
			packet.setPort(25565);
			
			for(InetAddress address : broadcastAddresses) {
				packet.setAddress(address);
				socket.send(packet);
			}
			
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	//espera uma mensagem por um tempo e a processa se recebida
	public DatagramPacket listen() {
		System.out.println("Client " + this.id + ": listening...");
		try {
			socket.receive(packet);
			answerMessage(packet.getData());
		}
		catch(SocketTimeoutException e) {
			System.out.println("Client " + this.id + ": No message received");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}
	
	//pergunta pelo coordenador e, caso não obtenha resposta, começa uma eleição
	public void askForCoordinator() {
		System.out.println("Client " + this.id + ": who is the coordinator?");
		String msg = "who is the coordinator?";
		this.sendMessage(msg);
		try {
			socket.setSoTimeout(3500);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		try {
		 	socket.receive(packet);
		}
		catch(SocketTimeoutException e) {
			this.startElection();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//processa mensagens
	public void answerMessage(byte[] msg) {
		String parsedMessage = new String(msg);
		switch(parsedMessage) {
		case "who is the coordinator?":
			if(this.IsCoordinator()) {
				System.out.println("Client " + this.id + ": I am, my id is " + this.id);
				this.sendMessage("I am, my id is " + this.id);
			}
			else{
				System.out.println("Client " + this.id + ": Not me!");
			}
			break;
		}
	}
	
	//começa a eleição
	public void startElection() {
		this.status = Status.ELECTION;
		System.out.println("Client " + this.id + ": Election START!...");
	}
	
	public void close(){
		socket.close();
	}
	
	//procura pelos endereços de broadcast da rede
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
	
}
