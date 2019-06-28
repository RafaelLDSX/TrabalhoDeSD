package states;

public class NoElection implements State{
	
	public String ask() {
		return "Quem e o coordenador?";
	}
	
	public String answer(String msg) {
		switch(msg) {
		case "Quem e o coordenador?":
			return null;
		}
		return null;
	}
	
	
}
