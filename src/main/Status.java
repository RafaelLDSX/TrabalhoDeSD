package main;

public enum Status {
	NORMAL(0),
	ELECTION(1);
	
	private int statusIndex;
	
	Status(int i) {
		this.statusIndex = i;
	}
	
	public int getStatusIndex() {
		return this.statusIndex;
	}
}
