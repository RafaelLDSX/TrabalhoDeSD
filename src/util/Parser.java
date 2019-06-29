package util;

public class Parser {
	
	public static String toString(byte[] msg) {
		return new String(msg);
	}
	
	public static byte[] toBytes(String msg) {
		return msg.getBytes();
	}
	
}
