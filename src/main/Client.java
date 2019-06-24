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
import states.NoElection;
import states.State;


public class Client implements Runnable{
	
	private final Integer id;
	private boolean isCoordinator;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private List<InetAddress> broadcastAddresses;
	private byte[] buffer;
	private State state;
	
	
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
		socket.setSoTimeout(3000);
		packet = new DatagramPacket(buffer, buffer.length);
		isCoordinator = false;
		state = new NoElection();
	}
	
	public void run(){
		
		boolean running = true;
		Random random = new Random();
		
		while(running){
			
			this.ask();
			this.listen();
		}
		socket.close();
		
	}
	
	public void ask() {
		String toSend = state.ask();
		if(toSend != null) {
			if(toSend.contains("#")) {
				toSend.replace("#", this.id.toString() );
			}
			//send message
		}
	}
	
	public void answer(String msg) {
		String toSend = state.answer(msg);
		if(toSend != null) {
			//send message
		}
	}
	
	public void listen() {
		
	}
	
}
