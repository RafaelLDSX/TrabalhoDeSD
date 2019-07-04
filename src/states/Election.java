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
			//int opennedBracketIndex = msg.indexOf("[");
			//int closedBracketIndex = msg.indexOf("]");
			//String toProcess = msg.substring(opennedBracketIndex+1, closedBracketIndex);
			msg = msg.replace("Meu id e ", "");
			msg = msg.replace("[", "");
			msg = msg.replace("]", "");
			return comparison(Integer.parseInt(msg.trim()));
		}
		else if(msg.equals("")) {
			this.silenceCounter += 1;
		}
		else if(msg.contains("Coordenador")) {
			return "Perdi";
		}
		else if(msg.contains("ELEICOES JA")){
			return "Meu id e [ID]";
		}

		return this.silenceCounter > 5 ? "Venci" : "";
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
