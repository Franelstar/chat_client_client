import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMT extends Thread {
		
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	
	public ClientMT() {
		try {
			Socket s = new Socket("localhost", 1234);
			
			bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			printWriter = new PrintWriter(s.getOutputStream(), true);
			
			this.start();
			
			Scanner sc = new Scanner(System.in);
			
			while(true) {
				String requete = sc.nextLine();
				printWriter.println(requete);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientMT();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String reponse;
		try {
		while((reponse = bufferedReader.readLine()) != null) {
				System.out.println(reponse);
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
