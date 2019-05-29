import java.net.SocketException;
import java.net.UnknownHostException;


public class Main {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		Thread t2 = new Thread(new Server());
		Thread t1 = new Thread(new Client());
		
		t2.start();
		t1.start();

	}

}
