package states;

import main.Client;
import main.Clock;

public class Coordinator implements State{
	private int clientId;
	private boolean berkFlag = false;
	private int notCoordCounter = 0;
	private Double notCoordClockSum = 0.0;
	private Clock waitClock;
	private Thread waitClockThread;
	private Client client;

	public Coordinator(int clientId, Client client) {
		this.clientId = clientId;
		this.client = client;
	}

	public Clock getWaitClock(){
		return waitClock;
	}

	public String ask() {
		return "Qual seu relogio?";
	}
	
	public String answer(String msg) {
		if (msg.equals("Quem e o coordenador?")) {
			return "Eu sou o coordenador";
		}
		else if (msg.equals("Comecar Berkley")) {
			waitClock = new Clock(1, client);
			waitClockThread = new Thread(waitClock);
			waitClockThread.start();
			return "Qual seu relogio?";
		}
		else if(msg.contains("Meu relogio e")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			countClock(Double.parseDouble(msg.substring(opennedBracketIndex+1, closedBracketIndex)));
		}
		else if(msg.contains("Enviar media")){
			int average = averageClock(client.getClock().getCounter()).intValue();
			String s = "Valor da media e [" + average + "]";
			adjustClock(average);
			return s;
		}
		return "";
	}
	
	public void action() {
		//TODO
	}
	
	// The methods below will be used for calculating the new clock 
	public void countClock(Double recivedClock) {
		if(this.berkFlag == false) {
			this.berkFlag = true;
		}
		this.notCoordCounter += 1;
		int value = recivedClock.intValue() - client.getClock().getCounter().intValue();
		this.notCoordClockSum += value;
	}
	
	public Double averageClock(Double coordClock) {
			return client.getClock().getCounter() + ((this.notCoordClockSum)/(this.notCoordCounter+1));
	}
	// Sometime this you be used, must be implemented a chance to end the berkley mode
	public void resetBerkFlag() {
		this.berkFlag = false;
	}

	public void adjustClock(int average){
		Double time = Double.valueOf(average);
		client.getClock().setCounter(time);
		System.out.println("Processo " + client.getId() + " - Relógio ajustado para " + average);
	}
}
