package states;

public class Election implements State{
	private Integer clientId;
	private int silenceCounter;
	private boolean alreadyAsked = false;
	
	
	public Election(Integer clientId) {
		this.clientId = clientId;
		this.silenceCounter = 0;
	}


	public String ask() {
		if(!alreadyAsked) {
			alreadyAsked = true;
			return "Meu id e [ID]";
		}
		return "";
	}
	
	public String answer(String msg) {
		if(msg.contains("Meu id e")) {
			this.silenceCounter = 0;
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			String toProcess = msg.substring(opennedBracketIndex, closedBracketIndex);
			return comparison(Integer.parseInt(toProcess));
		}
		else if(msg.equals("")) {
			this.silenceCounter += 1;
		}
		else if(msg.contains("Coordenador")) {
			return "Perdi";
		}

		return this.silenceCounter > 1 ? "Venci" : "";
	}

	public String comparison(Integer receivedId) {
		if(clientId > receivedId) {
			return "Meu id e " + clientId;
		} 
		else if(receivedId > clientId) {
			return "Perdi";
		}
		return ""; //TODO exception
	}
	
	public void action() {
		this.ask();
	}

}
