package users;

/**
 * 
 * @author Franck Anael MBIAYA
 * @author Fabien KAMBU
 * @author Kevin KANA
 * @author Jeremie OUEDRAOGO
 * @author NGUYEN Truong Thinh
 *
 */
public class Users {
	String ipAdress;
	String name;
	int port;
	int status;
	
	/**
	 * Constructeur
	 * @param ip Adresse IP (String)
	 * @param n Nom (String)
	 */
	public Users(String ip, String n) {
		ipAdress = ip;
		name = n;
		status = 1;
	}
	
	/**
	 * Constructeur
	 * @param ip Adresse IP (String)
	 */
	public Users(String ip) {
		ipAdress = ip;
		status = 1;
	}
	
	/**
	 * Constructeur
	 * @param ip Adresse Ip (String)
	 * @param n Nom (String)
	 * @param p Port (Int)
	 * @param e Status (Int)
	 */
	public Users(String ip, String n, int p, int e) {
		ipAdress = ip;
		name = n;
		status = 1;
		port = p;
		status = e;
	}
	
	/**
	 * Changer le status de l'utilisateur
	 * @param s Status (Int)
	 */
	public void setStatus(int s) {
		status = s;
	}
	
	/**
	 * Retourne le status de l'utilisateur
	 * @return int Status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Retourne le nom de l'utilisateur
	 * @return String Nom
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Changer le nom de l'utilisateur
	 * @param n Nom (string)
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Changer l'adresse IP de l'utilisateur
	 * @param ip Adresse Ip (String)
	 */
	public void setIP(String ip) {
		ipAdress = ip;
	}
	
	/**
	 * Retourne l'adresse IP de l'utilisateur
	 * @return String Adresse Ip
	 */
	public String getIP() {
		return ipAdress;
	}
	
	/**
	 * Modifie le port de l'utilisateur
	 * @param p Port (Int)
	 */
	public void setPort(int p) {
		port = p;
	}
	
	/**
	 * Retourne le port de l'utilisateur
	 * @return int Port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Implementation de la methode tostring
	 */
	public String toString(){
		return name;
	}
}