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
	 * @param ip
	 * @param n
	 */
	public Users(String ip, String n) {
		ipAdress = ip;
		name = n;
		status = 1;
	}
	
	/**
	 * Constructeur
	 * @param ip
	 */
	public Users(String ip) {
		ipAdress = ip;
		status = 1;
	}
	
	/**
	 * Constructeur
	 * @param ip
	 * @param n
	 * @param p
	 * @param e
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
	 * @param s
	 */
	public void setStatus(int s) {
		status = s;
	}
	
	/**
	 * Retourne le status de l'utilisateur
	 * @return
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Retourne le nom de l'utilisateur
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Changer le nom de l'utilisateur
	 * @param n
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Changer l'adresse IP de l'utilisateur
	 * @param ip
	 */
	public void setIP(String ip) {
		ipAdress = ip;
	}
	
	/**
	 * Retourne l'adresse IP de l'utilisateur
	 * @return
	 */
	public String getIP() {
		return ipAdress;
	}
	
	/**
	 * Modifie le port de l'utilisateur
	 * @param p
	 */
	public void setPort(int p) {
		port = p;
	}
	
	/**
	 * Retourne le port de l'utilisateur
	 * @return
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