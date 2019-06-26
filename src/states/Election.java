package states;

public class Election implements State{

	public String ask() {
		return "Meu id e [ID]";
	}
	
	public String answer(String msg) {
		if(msg.contains("Meu id e")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			String toProcess = msg.substring(opennedBracketIndex, closedBracketIndex);
			return toProcess;
			// int hisId = Integer.parseInt(toProcess);

		}
		return null;
	}

	

}
