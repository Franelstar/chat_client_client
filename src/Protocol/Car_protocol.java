/**
 * 
 */
package Protocol;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import users.Users;

/**
 * @author Franck Anael MBIAYA
 * @author Fabien KAMBU
 * @author Kevin KANA
 * @author Jeremie OUEDRAOGO
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
			case "nuser":
				nuser();
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
	/*public String users_(Vector<Users> list_users) {
		if(!list_users.isEmpty()) {
			String users = "USERS ";
			
			for(Users user : list_users) {
				users += user.getName() + " ";
			}
			
			return users;
		}
		return "USERS";
	}*/
	
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
				String[] dest = mesetuti[0].trim().split("$$$");
				
				resultat_commande.add("12000");
				resultat_commande.add(message);
				for(int i = 0; i < dest.length; i++)
					resultat_commande.add(dest[i]);
			}
		}
	}
	
	/*public String sendto_(String message) {
		return "SENDTO@@@" + message;
	}*/
	
	/**
	 * COMMANDE DECONNECT
	 * Code général 5000
	 */
	public void deconnect() {
		if(parts.length != 1) {
			resultat_commande.add("ERREUR 5001");
		}else{
			resultat_commande.add("5000");
		}
	}
	
	public String deconnect_() {
		return "DECONNECT";
	}
	
	/**
	 * COMMANDE DISCONNECTED
	 * Code général 6000
	 */
	public void disconnected() {
		if(parts.length != 2) {
			resultat_commande.add("ERREUR 6001");
		}else{
			resultat_commande.add("6000");
			resultat_commande.add(parts[1]);
		}
	}
	
	public String disconnected_(Users user) {
		return "DISCONNECTED " + user.getName();
	}
	
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
	 * COMMANDE USERS
	 * Liste des utilisateurs de la messagerie
	 * Code général 8000
	 * 
	 * @param list_users
	 * @return
	 */
	public void nuser() {
		if(parts.length < 2) {
			resultat_commande.add("ERREUR 8001");
		}else{
			resultat_commande.add("8000");
			resultat_commande.add(parts[1]);
		}
	}
	public String nuser_(Users user) {
		return "NUSER " + user.getName();
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
}
