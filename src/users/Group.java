package users;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Group {
	private String nomGroupe;
	private ConcurrentLinkedQueue<Users> users = new ConcurrentLinkedQueue<Users>();
	
	public Group(String nom) {
		nomGroupe = nom;
	}
	
	public void ajouterUser(Users u) {
		users.add(u);
	}
	
	public void enleverUser(Users u) {
		users.remove(u);
	}
	
	public ConcurrentLinkedQueue<Users> listeUsers(){
		return users;
	}
	
	public String getNomGroup() {
		return nomGroupe;
	}
}
