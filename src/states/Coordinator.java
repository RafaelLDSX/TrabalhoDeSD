package states;

public class Coordinator implements State{
	private int clientId;
	private boolean berkFlag = false;
	private int notCoordCounter = 0;
	private Double notCoordClockSum = 0.0;

	public Coordinator(int clientId) {
		this.clientId = clientId;
	}

	public String ask() {
		return "Qual seu relogio?";
	}
	
	public String answer(String msg) {
		if (msg.equals("Quem e o coordenador?")) {
			return "Eu sou o coordenador";
		}
		else if(msg.contains("Meu relogio e")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			countClock(Double.parseDouble(msg.substring(opennedBracketIndex, closedBracketIndex)));
		}
		return null;
	}
	
	// The methods below will be used for calculating the new clock 
	public void countClock(Double recivedClock) {
		if(this.berkFlag == true) {
			this.notCoordCounter += 1;
			this.notCoordClockSum += recivedClock;
		}
		else if(this.berkFlag == false) {
			this.berkFlag = true;
			this.notCoordCounter = 1;
			this.notCoordClockSum = recivedClock;
		}
	}
	
	public Double averageClock(Double coordClock) {
		if(this.berkFlag == true) {
			return (coordClock+this.notCoordClockSum)/(this.notCoordCounter+1);
		}
		return null; //TODO treatment if flag is false
	}
	// Sometime this you be used, must be implemented a chance to end the berkley mode
	public void resetBerkFlag() {
		this.berkFlag = false;
	}
}
