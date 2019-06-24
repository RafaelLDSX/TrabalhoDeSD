package states;

public class NoElection implements State{
	
	public String ask() {
		return "quem é o coordenador?";
	}
	
	public String answer(String msg) {
		switch(msg) {
		case "quem é o coordenador?":
			return null;
		}
		return null;
	}
	
	
}
