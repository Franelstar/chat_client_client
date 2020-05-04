package users;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Franck Anael MBIAYA
 * @author Fabien KAMBU
 * @author Kevin KANA
 * @author Jeremie OUEDRAOGO
 * @author NGUYEN Truong Thinh
 *
 */
public class Group {
	private String nomGroupe;
	private ConcurrentLinkedQueue<Users> users = new ConcurrentLinkedQueue<Users>();
	
	/**
	 * Constructeur
	 * @param nom Nom du groupe (String)
	 */
	public Group(String nom) {
		nomGroupe = nom;
	}
	
	/**
	 * Permet d'ajouter un utilisateur
	 * @param u Utilisateur (Users)
	 */
	public void ajouterUser(Users u) {
		users.add(u);
	}
	
	/**
	 * Permet d'enlever un utilisateur dans le groupe
	 * @param u Utilisateur du groupe (Users)
	 */
	public void enleverUser(Users u) {
		users.remove(u);
	}
	
	/**
	 * Retourne la liste des utilisateurs
	 * 
	 */
	public ConcurrentLinkedQueue<Users> listeUsers(){
		return users;
	}
	
	/**
	 * Retourne le nom du groupe
	 * @return String Nom du groupe
	 */
	public String getNomGroup() {
		return nomGroupe;
	}
}
