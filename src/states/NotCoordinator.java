package states;

import main.Client;

public class NotCoordinator implements State{

	Client client;

	public NotCoordinator(Client client){
		this.client = client;
	}

	public String ask() {
		// Nao pergunta por enquanto
		return "";
	}

	public String answer(String msg) {
		if(msg.equals("Qual seu relogio?")) {
			return "Meu relogio e [RELOGIO]";
		}
		else if(msg.contains("Valor da media e [")){
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			String toProcess = msg.substring(opennedBracketIndex, closedBracketIndex);
			int average = Integer.parseInt(toProcess);
			adjustClock(average);
		}
		return "";
	}

	public void action() {
		//TODO
	}

	public void adjustClock(int average){
		Double time = Double.valueOf(average);
		client.getClock().setCounter(time);
		System.out.println("Processo " + client.getId() + " - Relógio ajustado para " + average);
	}

}
