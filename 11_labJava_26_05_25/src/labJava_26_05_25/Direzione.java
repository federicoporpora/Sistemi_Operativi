package labJava_26_05_25;

public enum Direzione {
	
	IN, OUT;
	
	public String toString() {
		if (this == IN) {
			return "IN";
		} else {
			return "OUT";
		}
	}
}
