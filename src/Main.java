import java.net.SocketException;
import java.net.UnknownHostException;


public class Main {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		Thread t1 = new Thread(new Client(2));
		
		t1.start();

	}

}
