import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("J'attends une connexion");
			
			Socket s = ss.accept();
			
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			
			System.out.println("J'attends un nombre");
			int nb = is.read();
			
			int res = nb*12;
			System.out.println("J'ai recu le nombre " + nb + " et la reponse est " + res);
			System.out.println("J'envois la reponse");
			os.write(res);
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
