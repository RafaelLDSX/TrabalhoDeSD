package states;

public class Election implements State{
	private Integer clientId;
	
	public Election(Integer clientId) {
		this.clientId = clientId;
	}


	public String ask() {
		return "Meu id e [ID]";
	}
	
	public String answer(String msg) {
		if(msg.contains("Meu id e")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			String toProcess = msg.substring(opennedBracketIndex, closedBracketIndex);
			return comparison(Integer.parseInt(toProcess));
		}
		return null;
	}

	public String comparison(Integer recivedId) {
		if(clientId > recivedId) {
			return "Meu id e " + clientId;
		} 
		else if(recivedId > clientId) {
			return "Perdi";
		}
		return null; //TODO exception
	}

}
