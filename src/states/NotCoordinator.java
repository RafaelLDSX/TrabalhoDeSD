package states;

public class NotCoordinator implements State{

	public String ask() {
		// Nao pergunta por enquanto
		return "";
	}

	public String answer(String msg) {
		if(msg.equals("Qual seu relogio?")) {
			return "Meu relogio e [RELOGIO]";
		}
		return "";
	}

	public void action() {
		//TODO
	}

}
