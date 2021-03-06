/* Name: Caroline Yao & Horng-Bin Justin Wei
 * EID: Chy253 & Hjw396
 * Section: Thursday 3:30-5:30pm, Friday 2-3:30pm
 * EE 422C Assignment 6
 */
package Assignment6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TicketServer {
	static int PORT = 2222;
	// EE422C: no matter how many concurrent requests you get,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;

	public static void start(int portNumber) throws IOException {
		PORT = portNumber;
		Runnable serverThread = new ThreadedTicketServer(portNumber);
		Thread t = new Thread(serverThread);
		t.start();
	}
}

class ThreadedTicketServer implements Runnable {

	String hostname = "127.0.0.1";
	String threadname = "X";
	String testcase;
	TicketClient sc;
	static RecitalHall theatre;
	int port;
	
	public ThreadedTicketServer(int port){
		theatre = new RecitalHall();
		this.port = port;
	}

	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			String requester;	//name of client
			do{
			Socket clientSocket = serverSocket.accept();	//waits for a connection request from client
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			requester = in.readLine();	//listen for request from client
			if (requester != null){
				String seat = theatre.getBestAvailableSeat();
				theatre.printTicketSeat(seat, requester);
				if(seat.equals("empty")){
					out.println("sold out");
				}
				else{
					out.println("we have seats");
				}
			}
			clientSocket.close();
			}while(requester != null);	//close after clients are done requesting (seats sold out)
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}