package main;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws SocketException, UnknownHostException {

		Scanner scan = new Scanner(System.in);
		int id = scan.nextInt();

		Thread t1 = new Thread(new Client(id));
		
		t1.start();

	}

}
