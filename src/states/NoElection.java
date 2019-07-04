package states;

public class NoElection implements State{
	
	public String ask() {
		return "Quem e o coordenador?";
	}
	
	public String answer(String msg) {
		if(msg.contains("ELEICOES JA")) {
			return msg;
		}
		else if(msg.contains("Quem e o coordenador?")) {
			return "";
		}
		else if(msg.equals("")) {
			return "ELEICOES JA";
		}
		return "";
	}
	
	public void action() {
		//TODO
	}
	
}
