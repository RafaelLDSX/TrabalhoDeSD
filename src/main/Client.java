package main;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import states.Coordinator;
import states.Election;
import states.NoElection;
import states.NotCoordinator;
import states.State;
import util.Logger;
import util.Parser;

import static java.lang.Thread.sleep;


public class Client implements Runnable{
	
	private final Integer id;
	private boolean isCoordinator;
	private DatagramSocket socket;
	private List<InetAddress> broadcastAddresses;
	private State state;
	private Clock clock;
	private Thread clockThread;
	private Logger logger;
	private InetAddress myAddress;
	
	
	public int getId() {
		return this.id;
	}
	public State getState() { return this.state; }
	public Clock getClock() { return this.clock; }
	
	public boolean IsCoordinator() {
		return isCoordinator;
	}
	
	public Client(int id) throws SocketException, UnknownHostException{
		this.id = id;
		socket = new DatagramSocket(null);
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(25565));
		socket.setBroadcast(true);
		socket.setSoTimeout(3000);
		broadcastAddresses = this.listAllBroadcastAddresses();
		state = new NoElection(); 
		clock = new Clock(id);
		clockThread = new Thread(clock);
		clockThread.start();
		logger = new Logger();
		byte[] buffer = new byte[255];
		DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
		try {
			for(InetAddress i : this.broadcastAddresses) {
				this.sendMessage("Sou eu", i);
			}
			socket.receive(pkt);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		myAddress = pkt.getAddress();
		
	}
	
	public void run(){
		
		boolean running = true;
		Random random = new Random();

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter one of the following:");
		System.out.println("1 - Listen");
		System.out.println("2 - Ask for coordinator");

		switch(scanner.nextInt()) {
		case 1:
			this.listen();
			break;
		case 2:
			this.ask();
			break;
		}
		
		while(running) {
			this.action();
		}

		scanner.close();
		socket.close();

	}
	
	public void ask() {
		String toSend = state.ask();
		if(toSend != null && !toSend.equals("")) {
			if(toSend.contains("ID")) {
				toSend = toSend.replace("ID", this.id.toString() );
			}
			for(InetAddress i : this.broadcastAddresses) {
				this.sendMessage(toSend, i);
			}
		}
	}
	
	public void answer(String msg) {
		String toSend = state.answer(msg);
		if(toSend.contains("RELOGIO")) {
			toSend = toSend.replace("RELOGIO", clock.getCounter().toString() );
		}
		else if (toSend.equals("Perdi")) { 
			changeState(new NotCoordinator(this));
			toSend = "";
		}
		else if(toSend.equals("ELEICOES JA")){
			changeState(new Election(this.getId()));
		}
		else if(toSend.equals("Venci")) {
			System.out.println("Processo " + id + " - Foi selecionado como Coordenador");
			changeState(new Coordinator(this.getId(), this));
			toSend = state.answer("Comecar Berkley");
			System.out.println("Processo " + id + " - Decidiu comecar Berkley");
		}
		else if(toSend.contains("Valor da media e [")){

		}
		
		if(toSend != null && !toSend.equals("")) {
			for(InetAddress i : this.broadcastAddresses)
				this.sendMessage(toSend, i);
		}
		
	}

	public void listen() {
		byte[] buf = new byte[255];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			
			System.out.println("Ouvindo");
			this.socket.receive(packet);
			
			//se nao receber uma mensagem de si mesmo, prosseguir normalmente
			String checkId = "ID: " + this.id.toString();
			String parsed = Parser.toString(packet.getData());
			if(!parsed.contains(checkId)) {
				//String parsed = Parser.toString(packet.getData());
				this.logger.log("IN - " + parsed);
				parsed = parsed.replaceAll("[0-9]+", "");
				parsed = parsed.replace("ID:  - ", "");
				System.out.println(parsed);
				this.answer(parsed);
			} 
			
			//se a mensagem for de si mesmo, ignorar e tentar ouvir mensagem de outro processo
			else {
				this.listen();
			}
			
		} catch (SocketTimeoutException e) {
			this.answer("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void action() {
		this.ask();
		try {
			this.socket.setSoTimeout(3000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.listen();
	
	}
	
	public void changeState(State state) {
		this.state = state;	
		
	}
	
	public void sendMessage(String msg, InetAddress address) {
		byte[] msgInBytes = Parser.toBytes(msg);
		msg = "ID: " + this.id.toString() + " - " + msg; 
		DatagramPacket packet = new DatagramPacket(msgInBytes, msgInBytes.length, address, 25565);
		try {
			this.socket.send(packet);
			this.logger.log("OUT - " + msg);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
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
}
