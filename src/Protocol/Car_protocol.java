/**
 * 
 */
package Protocol;

import java.util.ArrayList;

import users.Users;

/**
 * @author Franck Anael MBIAYA
 * @author Fabien KAMBU
 * @author Kevin KANA
 * @author Jeremie OUEDRAOGO
 * @author NGUYEN Truong Thinh
 * 
 * @version 1
 *
 */
public class Car_protocol {
	
	private String[] parts;
	private String[] partsMessage;
	private String[] partsSendto;
	private ArrayList<String> resultat_commande;
	
	public ArrayList<String> requete(String commande) {
		parts = commande.trim().replaceAll(" {2,}", " ").split(" ");
		
		partsMessage = commande.trim().split("@@@");
		partsSendto = commande.trim().split("@@@");
		
		resultat_commande = new ArrayList<String>();
		
		switch(parts[0].toLowerCase()) {
			case "/connect":
				connect();
				break;
			case "/users":
				users();
				break;
			case "/sstatus":
				sstatus();
				break;
			case "/status":
				status();
				break;
			case "/quit":
				quit();
				break;
			case "/who":
				who();
				break;
			case "/id":
				id();
				break;
			case "/group":
				group();
				break;
			case "/quitgroup":
				quitgroup();
				break;
			case "/addgroup":
				addgroup();
				break;
			case "/listusersgroup":
				listusersgroup();
				break;
			case "/usersgroup":
				usersgroup();
				break;
			case "/groups":
				groups();
				break;
			case "/delgroup":
				delgroup();
				break;
			case "/":
				aide();
				break;
			default:
				resultat_commande.add("ERREUR 1000");
				break;
		}
		
		if(partsMessage[0].toLowerCase().equals("/broadcast")) {
			resultat_commande = new ArrayList<String>();
			broadcast();
		}
		if(partsSendto[0].toLowerCase().equals("/sendto")) {
			resultat_commande = new ArrayList<String>();
			sendto();
		}
		if(partsSendto[0].toLowerCase().equals("/sendtogroup")) {
			resultat_commande = new ArrayList<String>();
			sendtogroup();
		}
		
		return resultat_commande;
	}
	
	/**
	 * COMMANDE CONNECT
	 * Code général 2000
	 */
	public void connect() {
		if(parts.length == 2) {
			resultat_commande.add("2000");
			resultat_commande.add(parts[1]);
		} else if(parts.length == 3) {
			resultat_commande.add("2000");
			resultat_commande.add(parts[1]);
			resultat_commande.add(parts[2]);
		} else {
			resultat_commande.add("2001");
		}
	}
	
	public String connect_(String message) {
		return "/CONNECT " + message;
	}
	
	/**
	 * COMMANDE USERS
	 * Affiche la Liste des utilisateurs connectés
	 * Code général 3000
	 * 
	 * @param list_users
	 * @return
	 */
	public void users() {
		if(parts.length == 1) {
			resultat_commande.add("3000");
		} else {
			resultat_commande.add("3000");
			for(int i = 1; i < parts.length; i++) {
				resultat_commande.add(parts[i]);
			}
		}
	}
	
	/**
	 * COMMANDE MESSAGE
	 * Code général 4000
	 * 
	 * @param list_users
	 * @return
	 */
	public void broadcast() {
		if(partsMessage.length < 2) {
			resultat_commande.add("ERREUR 4001");
		}else{
			resultat_commande.add("4000");
			for(int i = 0; i < partsMessage.length; i++)
				resultat_commande.add(partsMessage[1]);
		}
	}
	public String broadcast_(String message) {
		return "/BROADCAST@@@" + message;
	}
	
	/**
	 * COMMANDE MESSAGE
	 * Code général 12000
	 * 
	 * @param list_users
	 * @return
	 */
	public void sendto() {
		if(partsSendto.length != 3) {
			resultat_commande.add("ERREUR 12001");
		}else{
			String message = partsSendto[2];
			String[] dest = partsSendto[1].split("@@");
			resultat_commande.add("12000");
			resultat_commande.add(message);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	public String sendto_(String message, String user) {
		return "/SENDTO@@@"+user+"@@@"+message;
	}
	
	/*public String sendto_(String message) {
		return "SENDTO@@@" + message;
	}*/
	
	/**
	 * COMMANDE QUIT
	 * Code général 7000
	 */
	public void quit() {
		if(parts.length != 1) {
			resultat_commande.add("ERREUR 7001");
		}else{
			resultat_commande.add("7000");
		}
	}
	
	public String quit_() {
		return "/QUIT";
	}
	
	/**
	 * COMMANDE SSTATUS
	 * Liste des utilisateurs de la messagerie
	 * Code général 9000
	 * 
	 * @param list_users
	 * @return
	 */
	public void sstatus() {
		if(parts.length != 2) {
			resultat_commande.add("ERREUR 9001");
		}else{
			if(parts[1].equals("1") || parts[1].equals("2") || parts[1].equals("3")) {
				resultat_commande.add("9000");
				resultat_commande.add(parts[1]);
			} else {
				resultat_commande.add("ERREUR 9002");
			}
		}
	}
	public String sstatus_(int status) {
		return "/SSTATUS " + status;
	}
	
	/**
	 * COMMANDE STATUS
	 * Liste des utilisateurs de la messagerie
	 * Code général 10000
	 * 
	 * @param list_users
	 * @return
	 */
	public void status() {
		if(parts.length == 1) {
			resultat_commande.add("10000");
		} else {
			resultat_commande.add("10000");
			for(int i = 1; i < parts.length; i++) {
				resultat_commande.add(parts[i]);
			}
		}
	}
	
	public String status_(Users user, int status) {
		return "/STATUS " + user.getName() + " " + status;
	}
	
	/**
	 * COMMANDE WHO
	 * Liste des utilisateurs de la messagerie
	 * Code général 13000
	 * 
	 * @param list_users
	 * @return
	 */
	public void who() {
		if(parts.length == 1) {
			resultat_commande.add("13000");
		} else {
			resultat_commande.add("13001");
		}
	}
	
	/**
	 * COMMANDE ID
	 * Liste des utilisateurs de la messagerie
	 * Code général 14000
	 * 
	 * @param list_users
	 * @return
	 */
	public void id() {
		if(parts.length == 2) {
			resultat_commande.add("14000");
			resultat_commande.add(parts[1]);
		} else if(parts.length == 3) {
			resultat_commande.add("14000");
			resultat_commande.add(parts[1]);
			resultat_commande.add(parts[2]);
		} else {
			resultat_commande.add("140001");
		}
	}
	
	/**
	 * COMMANDE GROUP
	 * Code général 15000
	 * 
	 * @param list_users
	 * @return
	 */
	public void group() {
		if(parts.length != 3) {
			resultat_commande.add("ERREUR 15001");
		}else{
			String nom_group = parts[1];
			String[] dest = parts[2].split("@@");
			resultat_commande.add("15000");
			resultat_commande.add(nom_group);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	/**
	 * COMMANDE QUITGROUP
	 * Code général 15000
	 * 
	 * @param list_users
	 * @return
	 */
	public void quitgroup() {
		if(parts.length != 3) {
			resultat_commande.add("ERREUR 16001");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("16000");
			resultat_commande.add(nom_group);
			resultat_commande.add(parts[2]);
		}
	}
	
	/**
	 * COMMANDE ADDGROUP
	 * Code général 17000
	 * 
	 * @param list_users
	 * @return
	 */
	public void addgroup() {
		if(parts.length != 3) {
			resultat_commande.add("ERREUR 17001");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("17000");
			resultat_commande.add(nom_group);
			resultat_commande.add(parts[2]);
		}
	}
	
	/**
	 * COMMANDE LISTUSERSGROUP
	 * Code général 18000
	 * 
	 * @param list_users
	 * @return
	 */
	public void listusersgroup() {
		if(parts.length != 2) {
			resultat_commande.add("ERREUR 18001");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("18000");
			resultat_commande.add(nom_group);
		}
	}
	
	/**
	 * COMMANDE USERSGROUP
	 * Code général 19000
	 * 
	 * @param list_users
	 * @return
	 */
	public void usersgroup() {
		if(parts.length != 3) {
			resultat_commande.add("ERREUR 19001");
		}else{
			String nom_group = parts[1];
			String[] dest = parts[2].split("@@");
			resultat_commande.add("19000");
			resultat_commande.add(nom_group);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	/**
	 * COMMANDE GROUPS
	 * Code général 20000
	 * 
	 * @param list_users
	 * @return
	 */
	public void groups() {
		if(parts.length != 1) {
			resultat_commande.add("ERREUR 20001");
		}else{
			resultat_commande.add("20000");
		}
	}
	
	/**
	 * COMMANDE DELGROUP
	 * Code général 21000
	 * 
	 * @param list_users
	 * @return
	 */
	public void delgroup() {
		if(parts.length != 2) {
			resultat_commande.add("ERREUR 21001");
		}else{
			resultat_commande.add("21000");
			resultat_commande.add(parts[1]);
		}
	}
	
	/**
	 * COMMANDE SENDTOGROUP
	 * Code général 22000
	 * 
	 * @param list_users
	 * @return
	 */
	public void sendtogroup() {
		if(partsSendto.length != 3) {
			resultat_commande.add("ERREUR 22001");
		}else{
			String message = partsSendto[2];
			resultat_commande.add("22000");
			resultat_commande.add(partsSendto[1]);
			resultat_commande.add(message);
		}
	}
	
	/**
	 * COMMANDE aide
	 * Code général 23000
	 * 
	 * @param list_users
	 * @return
	 */
	public void aide() {
		if(partsSendto.length != 1) {
			resultat_commande.add("ERREUR 23001");
		}else{
			resultat_commande.add("23000");
		}
	}
}
