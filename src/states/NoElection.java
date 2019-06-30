package states;

public class NoElection implements State{
	
	public String ask() {
		return "Quem e o coordenador?";
	}
	
	public String answer(String msg) {
		switch(msg) {
		case "ELEICOES JA":
			return msg;
		case "Quem e o coordenador?":
			return "";
		case "":
			return "ELEICOES JA";
			
		}
		return "";
	}
	
	public void action() {
		//TODO
	}
	
}
