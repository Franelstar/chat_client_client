import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Etablissement de la connexion au serveur");
			Socket socket = new Socket("localhost", 1234);
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			Scanner s = new Scanner(System.in);
			System.out.print("Donner un nombre: ");
			int nb = s.nextInt();
			System.out.println("J'envois la requete au serveur");
			os.write(nb);
			
			System.out.println("J'attends la reponse du serveur");
			int reponse = is.read();
			System.out.println("Reponse = " + reponse);
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
