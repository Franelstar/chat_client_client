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
		partsSendto = commande.trim().split(" ");
		
		resultat_commande = new ArrayList<String>();
		
		switch(parts[0].toLowerCase()) {
			case "connect":
				connect();
				break;
			case "users":
				users();
				break;
			case "sstatus":
				sstatus();
				break;
			case "status":
				status();
				break;
			case "quit":
				quit();
				break;
			case "who":
				who();
				break;
			case "id":
				id();
				break;
			default:
				resultat_commande.add("ERREUR 1000");
				break;
		}
		
		if(partsMessage[0].toLowerCase().equals("broadcast")) {
			resultat_commande = new ArrayList<String>();
			broadcast();
		}
		if(partsSendto[0].toLowerCase().equals("sendto")) {
			resultat_commande = new ArrayList<String>();
			sendto();
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
		return "CONNECT " + message;
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
		return "BROADCAST@@@" + message;
	}
	
	/**
	 * COMMANDE MESSAGE
	 * Code général 12000
	 * 
	 * @param list_users
	 * @return
	 */
	public void sendto() {
		if(partsSendto.length != 2) {
			resultat_commande.add("ERREUR 12001");
		}else{
			String[] mesetuti;
			mesetuti = partsSendto[1].trim().split("@@@");
			
			if(mesetuti.length != 2) {
				resultat_commande.add("ERREUR 12002");
			} else {
				String message = mesetuti[1];
				String[] dest = mesetuti[0].split("@@");
				System.out.println(dest.length);
				resultat_commande.add("12000");
				resultat_commande.add(message);
				for(int i = 0; i < dest.length; i++) {
					resultat_commande.add(dest[i]);
					System.out.println(dest[i]);
				}
			}
		}
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
		return "QUIT";
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
		return "SSTATUS " + status;
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
		return "STATUS " + user.getName() + " " + status;
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
}
