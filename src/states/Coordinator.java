package states;

public class Coordinator implements State{
	private int clientId;
	private boolean berkFlag = false;
	private int notCoordCouter = 0;
	private int notCoordClockSum = 0;

	public Coordinator(int clientId) {
		this.clientId = clientId;
	}

	public String ask() {
		return "Qual seu relogio?";
	}
	
	public String answer(String msg) {
		if (msg == "quem e o coordenador?") {
			return "Eu sou o coordenador";
		}
		else if(msg.contains("Meu relogio e")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			averageClock(Integer.parseInt(msg.substring(opennedBracketIndex, closedBracketIndex)));
		}
		return null;
	}

	public void averageClock(int recivedClock) {
		if(this.berkFlag == true) {
			this.notCoordCouter += 1;
			this.notCoordClockSum += recivedClock;
		}
		else if(this.berkFlag == false) {
			this.berkFlag = true;
			this.notCoordCouter = 1;
			this.notCoordClockSum = recivedClock;
		}
	}
	// Sometime this you be used, must be implemented a chance to end the berkley mode
	public void resetBerkFlag() {
		this.berkFlag = false;
	}
}
