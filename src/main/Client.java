package main;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import parser.Parser;
import states.Coordinator;
import states.Election;
import states.NoElection;
import states.NotCoordinator;
import states.State;

import static java.lang.Thread.sleep;


public class Client implements Runnable{
	
	private final Integer id;
	private boolean isCoordinator;
	private DatagramSocket socket;
	private List<InetAddress> broadcastAddresses;
	private State state;
	private Clock clock;
	private Thread clockThread;
	
	
	public int getId() {
		return this.id;
	}
	
	public boolean IsCoordinator() {
		return isCoordinator;
	}
	
	public Client(int id) throws SocketException, UnknownHostException{
		this.id = id;
		socket = new DatagramSocket(25565);
		socket.setBroadcast(true);
//		socket.setSoTimeout(3000);
//		broadcastAddresses = this.listAllBroadcastAddresses();
		state = new NoElection(); 
		clock = new Clock(id);
		clockThread = new Thread(clock);
		clockThread.start();
	}
	
	public void run(){
		
		boolean running = true;
		Random random = new Random();

		/*
		while(true){
			System.out.println("Relogio" + clock.getCounter());
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/

//		while(running){
//			this.ask();
//			this.listen();
//			
//			if(this.state == Coordinator) {
//				if(this.state.berkFlag == false) {
//					state.ask();
//			  	}
//			  	else if(this.state.berkFlag == true) {
//			 		state.averageClock(clock);
//			  	}
//			}
//			 
//		}
		
//		for(InetAddress i : broadcastAddresses) {
//			this.sendMessage("alou", i);
//		}
		this.listen();

		socket.close();

	}
	
	public void ask() {
		String toSend = state.ask();
		if(toSend != null) {
			if(toSend.contains("ID")) {
				toSend.replace("ID", this.id.toString() );
			}
		//TODO send message()
		}
	}
	
	public void answer(String msg) {
		String toSend = state.answer(msg);
		if(toSend.contains("RELOGIO")) {
			toSend.replace("RELOGIO", clock.getCounter().toString() );
		}
		else if (toSend.equals("Perdi")) { 
			changeState("NotCoordinator");
			toSend = null;
		}
		if(toSend != null) {
			//TODO send message()
		}
	}

	public void listen() {
		byte[] buf = new byte[255];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			System.out.println("Ouvindo");
			this.socket.receive(packet);
			System.out.println("Ouvi");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Parser.toString(packet.getData()));
		
	}
	
	public void changeState(String nextState) {
		if(nextState.equals("Election")) {
			state = new Election(this.id);
		}
		else if(nextState.equals("NotCoordinator")) {
			state = new NotCoordinator();
		}
		else if(nextState.equals("Coordinator")) {
			state = new Coordinator(this.id);
		}
		else {
			//TODO exception
		}		
		
	}
	
	public void sendMessage(String msg, InetAddress address) {
		byte[] msgInBytes = Parser.toBytes(msg);
		DatagramPacket packet = new DatagramPacket(msgInBytes, msgInBytes.length, address, 25565);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	List<InetAddress> listAllBroadcastAddresses() throws SocketException {
//	    List<InetAddress> broadcastList = new ArrayList<>();
//	    Enumeration<NetworkInterface> interfaces 
//	      = NetworkInterface.getNetworkInterfaces();
//	    while (interfaces.hasMoreElements()) {
//	        NetworkInterface networkInterface = interfaces.nextElement();
//	 
//	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//	            continue;
//	        }
//	 
//	        networkInterface.getInterfaceAddresses().stream() 
//	          .map(a -> a.getBroadcast())
//	          .filter(Objects::nonNull)
//	          .forEach(broadcastList::add);
//	    }
//	    return broadcastList;
//	}
}
