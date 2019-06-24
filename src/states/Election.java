package states;

public class Election implements State{

	public String ask() {
		return "Meu id é [#]";
	}
	
	public String answer(String msg) {
		if(msg.contains("Meu id é")) {
			int opennedBracketIndex = msg.indexOf("[");
			int closedBracketIndex = msg.indexOf("]");
			String toProcess = msg.substring(opennedBracketIndex, closedBracketIndex);
			
			int hisId = Integer.parseInt(toProcess);
		}
		return null;
	}

	

}
