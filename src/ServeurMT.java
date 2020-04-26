import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

import Protocol.Car_protocol;

public class ServeurMT extends Thread {
	private int nbClients;
	private ArrayList<Socket> sokets = new ArrayList<Socket>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServeurMT().start();
		//On peut ici créer des interface graphique
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("Démarrage du serveur");
			
			while(true) {
				Socket s = ss.accept();
				sokets.add(s);
				++nbClients;
				
				new Conversation(s, nbClients).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void broadcastMessage(String message, Socket source) {
		for(Socket s : sokets) {
			try {
				//if(s != source) {
					PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
					pw.println(message);
				//}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Conversation extends Thread {
		private Socket socket;
		private int numeroClient;
		
		public Conversation(Socket socket, int numero) {
			this.socket = socket;
			this.numeroClient = numero;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				InputStream is = socket.getInputStream(); //Pour lire un octet
				InputStreamReader isr = new InputStreamReader(is); //Pour lire un mot
				BufferedReader br = new BufferedReader(isr); // Pour lire une ligne
				
				OutputStream os = socket.getOutputStream(); // Pour écrire un octet
				PrintWriter pw = new PrintWriter(os, true); // Pour écrire un mot (true pour envoyer apres une ligne)

				String ip = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion du client no " + nbClients + " ip = " + ip);
				
				pw.println("Bienvenu, vous êtes le client numéro : " + numeroClient);
				
				while(true) {
					String req = br.readLine();
					//broadcastMessage(ip + " a envoyé le message => " + req, socket);
					Car_protocol car_protocol = new Car_protocol();
					//String retour = car_protocol.requete(req);
					//broadcastMessage(retour, socket);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
