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
	 * Interprétation de la commande CONNECT
	 * Code général 200-209
	 */
	public void connect() {
		if(parts.length == 2) {
			resultat_commande.add("200");
			resultat_commande.add(parts[1]);
		} else if(parts.length == 3) {
			resultat_commande.add("200");
			resultat_commande.add(parts[1]);
			resultat_commande.add(parts[2]);
		} else {
			resultat_commande.add("E201");
		}
	}
	
	/**
	 * Interprétation de la commande USERS
	 * Code général 260-269
	 * 
	 */
	public void users() {
		if(parts.length == 1) {
			resultat_commande.add("260");
		} else {
			resultat_commande.add("260");
			for(int i = 1; i < parts.length; i++) {
				resultat_commande.add(parts[i]);
			}
		}
	}
	
	/**
	 * Interprétation de la commande BROADCAST
	 * Code général 400-409
	 */
	public void broadcast() {
		if(partsMessage.length < 2) {
			resultat_commande.add("E401");
		}else{
			resultat_commande.add("400");
			for(int i = 0; i < partsMessage.length; i++)
				resultat_commande.add(partsMessage[1]);
		}
	}
	
	public String broadcast_(String message) {
		return "/BROADCAST@@@" + message;
	}
	
	/**
	 * Interprétation de la commande MESSAGE
	 * Code général 410-419
	 */
	public void sendto() {
		if(partsSendto.length != 3) {
			resultat_commande.add("E411");
		}else{
			String message = partsSendto[2];
			String[] dest = partsSendto[1].split("@@");
			resultat_commande.add("410");
			resultat_commande.add(message);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	public String sendto_(String message, String user) {
		return "/SENDTO@@@"+user+"@@@"+message;
	}
	
	/**
	 * Interprétation de la commande QUIT
	 * Code général 210-219
	 */
	public void quit() {
		if(parts.length != 1) {
			resultat_commande.add("E211");
		}else{
			resultat_commande.add("210");
		}
	}
	
	public String quit_() {
		return "/QUIT";
	}
	
	/**
	 * Interprétation de la commande SSTATUS
	 * Code général 240-249
	 */
	public void sstatus() {
		if(parts.length != 2) {
			resultat_commande.add("E241");
		}else{
			if(parts[1].equals("1") || parts[1].equals("2") || parts[1].equals("3")) {
				resultat_commande.add("240");
				resultat_commande.add(parts[1]);
			} else {
				resultat_commande.add("E242");
			}
		}
	}
	
	public String sstatus_(int status) {
		return "/SSTATUS " + status;
	}
	
	/**
	 * Interprétation de la commande  STATUS
	 * Code général 250-259
	 */
	public void status() {
		if(parts.length == 1) {
			resultat_commande.add("250");
		} else {
			resultat_commande.add("250");
			for(int i = 1; i < parts.length; i++) {
				resultat_commande.add(parts[i]);
			}
		}
	}
	
	public String status_(Users user, int status) {
		return "/STATUS " + user.getName() + " " + status;
	}
	
	/**
	 * 
	 * Interprétation de la commande WHO
	 * Code général 220-229
	 * 
	 */
	public void who() {
		if(parts.length == 1) {
			resultat_commande.add("220");
		} else {
			resultat_commande.add("E221");
		}
	}
	
	/**
	 * 
	 * Interprétation de la commande ID
	 * Code général 230-239
	 * 
	 */
	public void id() {
		if(parts.length == 2) {
			resultat_commande.add("230");
			resultat_commande.add(parts[1]);
		} else if(parts.length == 3) {
			resultat_commande.add("230");
			resultat_commande.add(parts[1]);
			resultat_commande.add(parts[2]);
		} else {
			resultat_commande.add("E231");
		}
	}
	
	/**
	 * Interprétation de la commande GROUP
	 * Code général 300-309
	 */
	public void group() {
		if(parts.length != 3) {
			resultat_commande.add("E301");
		}else{
			String nom_group = parts[1];
			String[] dest = parts[2].split("@@");
			resultat_commande.add("300");
			resultat_commande.add(nom_group);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	/**
	 * Interprétation de la commande QUITGROUP
	 * Code général 310-319
	 */
	public void quitgroup() {
		if(parts.length != 3) {
			resultat_commande.add("E311");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("310");
			resultat_commande.add(nom_group);
			resultat_commande.add(parts[2]);
		}
	}
	
	/**
	 * Interprétation de la commande ADDGROUP
	 * Code général 320-329
	 * 
	 */
	public void addgroup() {
		if(parts.length != 3) {
			resultat_commande.add("E321");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("320");
			resultat_commande.add(nom_group);
			resultat_commande.add(parts[2]);
		}
	}
	
	/**
	 * Interprétation de la commande LISTUSERSGROUP
	 * Code général 330-339
	 */
	public void listusersgroup() {
		if(parts.length != 2) {
			resultat_commande.add("E331");
		}else{
			String nom_group = parts[1];
			resultat_commande.add("330");
			resultat_commande.add(nom_group);
		}
	}
	
	/**
	 * Interprétation de la commande USERSGROUP
	 * Code général 340-349
	 */
	public void usersgroup() {
		if(parts.length != 3) {
			resultat_commande.add("E341");
		}else{
			String nom_group = parts[1];
			String[] dest = parts[2].split("@@");
			resultat_commande.add("340");
			resultat_commande.add(nom_group);
			for(int i = 0; i < dest.length; i++) {
				resultat_commande.add(dest[i]);
			}
		}
	}
	
	/**
	 * Interprétation de la commande GROUPS
	 * Code général 350-359
	 */
	public void groups() {
		if(parts.length != 1) {
			resultat_commande.add("E351");
		}else{
			resultat_commande.add("350");
		}
	}
	
	/**
	 * Interprétation de la commande DELGROUP
	 * Code général 360-369
	 */
	public void delgroup() {
		if(parts.length != 2) {
			resultat_commande.add("E361");
		}else{
			resultat_commande.add("360");
			resultat_commande.add(parts[1]);
		}
	}
	
	/**
	 * Interprétation de la commande SENDTOGROUP
	 * Code général 420-429
	 */
	public void sendtogroup() {
		if(partsSendto.length != 3) {
			resultat_commande.add("E421");
		}else{
			String message = partsSendto[2];
			resultat_commande.add("420");
			resultat_commande.add(partsSendto[1]);
			resultat_commande.add(message);
		}
	}
	
	/**
	 * COMMANDE aide
	 * Code général 900
	 */
	public void aide() {
		if(partsSendto.length != 1) {
			resultat_commande.add("E901");
		}else{
			resultat_commande.add("900");
		}
	}
}
