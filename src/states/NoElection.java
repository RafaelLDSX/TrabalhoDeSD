package states;

public class NoElection implements State{
	
	public String ask() {
		return "quem e o coordenador?";
	}
	
	public String answer(String msg) {
		switch(msg) {
		case "quem e o coordenador?":
			return null;
		}
		return null;
	}
	
	
}
