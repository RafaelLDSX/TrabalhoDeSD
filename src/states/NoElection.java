package states;

public class NoElection implements State{
	
	public String ask() {
		return "quem � o coordenador?";
	}
	
	public String answer(String msg) {
		switch(msg) {
		case "quem � o coordenador?":
			return null;
		}
		return null;
	}
	
	
}
