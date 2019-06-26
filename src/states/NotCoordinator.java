package states;

public class NotCoordinator implements State{

	public String ask() {
		// Nao pergunta por enquanto
		return null;
	}

	public String answer(String msg) {
		if(msg == "Qual seu relogio?") {
			return "Meu relogio e [RELOGIO]";
		}
		return null;
	}

	

}
