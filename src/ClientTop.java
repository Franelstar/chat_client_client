import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.*;

import Protocol.Car_protocol;
import users.Group;
import users.Users;

import java.awt.*;
import java.awt.event.*;

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

public final class ClientTop extends JFrame implements ActionListener {
	String username;
	int status = 1;
	JTextArea chatmsg;
	JTextField chatip, chatip_commande;
	JButton send, exit, send_commande, exit_commande, clear;
	JComboBox listeUsers;
	
	Car_protocol car_protocol;
	
	Vector<Users> users = new Vector<Users>();
	Vector<Users> oldusers = new Vector<Users>();
	Vector<Group> groups = new Vector<Group>();
	Vector<Manageuser> clients = new Vector<Manageuser>();
	
	/**
	 * Constructeur
	 * @param uname Nom d'utilisateur (String)
	 * @throws Exception
	 */
	public ClientTop(String uname) throws Exception{
		super(uname);
		
		rechercheConnecte();
		car_protocol = new Car_protocol();
		this.username = uname;
			
		
		ServerSocket server = new ServerSocket(1234, 10);
		buildInterface(); //On construit l'interface graphique
		
		while(true) {
			Socket client = server.accept();
			System.out.println("Connexion serveur");
			
			clients.add(new Manageuser(client, false));
		}
	}
	
	/**
	 * Fonction permettant de construire l'interface graphique
	 */
	public void buildInterface() {
		send = new JButton("Envoyer");
		send_commande = new JButton("Valider");
		exit = new JButton("Sortir");
		exit_commande = new JButton("Commande");
		clear = new JButton("Clear");
		chatmsg = new JTextArea();
		chatmsg.setEditable(false);
		chatmsg.setRows(30);
		chatmsg.setColumns(50);
		chatip = new JTextField(50);
		listeUsers = new JComboBox(oldusers);
		Dimension preferredSize = listeUsers.getPreferredSize();
        preferredSize.height = 22;
        preferredSize.width = 150;
		listeUsers.setPreferredSize(preferredSize);
		chatip_commande = new JTextField(50);
		JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(sp, "Center");
		JPanel bp = new JPanel(new FlowLayout());
		JPanel bp_commande = new JPanel(new FlowLayout());
		bp.add(listeUsers);
		bp.add(chatip);
		bp.add(send);
		bp.add(exit);
		exit_commande.setEnabled(false);
		bp_commande.add(clear);
		bp_commande.add(exit_commande);
		bp_commande.add(chatip_commande);
		bp_commande.add(send_commande);
		bp.setBackground(Color.LIGHT_GRAY);
		bp.setName("CHAT CAR");
		add(bp, "North");
		add(bp_commande, "South");
		send.addActionListener(this);
		send_commande.addActionListener(this);
		exit.addActionListener(this);
		clear.addActionListener(this);
		setSize(500, 300);
		setVisible(true);
		pack();
	}
	
	/**
	 * Fonction permettant de gérer les interactions avec les différents boutons de l'interface graphique
	 * @param evt Evenement
	 */
	//@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == exit) {
			//On te retire de tous les groups
			if(groups.size() != 0) {
				for(Group g : groups) {
					sendtoall("/QUITGROUP "+g.getNomGroup()+" "+username);
				}
			}
			sendtoall(getUser(username), car_protocol.quit_());
			//pw.println(car_protocol.quit_());
			System.exit(0);
		} else if(evt.getSource() == send_commande) {
			if(chatip_commande.getText().toLowerCase().contains("/status") || 
					chatip_commande.getText().toLowerCase().contains("/users") || 
					chatip_commande.getText().toLowerCase().contains("/groups")) {
				sendtome(chatip_commande.getText());
			} else if(chatip_commande.getText().toLowerCase().contains("/sendto") || 
					chatip_commande.getText().toLowerCase().contains("/quitgroup") || 
					chatip_commande.getText().toLowerCase().contains("/addgroup") || 
					chatip_commande.getText().toLowerCase().contains("/sendtogroup") ||
					chatip_commande.getText().toLowerCase().contains("/delgroup")){
				sendtoothers(chatip_commande.getText());
			} else if(chatip_commande.getText().toLowerCase().contains("/group")){
				sendtoothers(chatip_commande.getText()+"@@"+username);
			} else {
				sendtoall(chatip_commande.getText());
			}
			
			chatip_commande.setText(null);
		} else if(evt.getSource() == clear){
			chatmsg.setText("");
		} else {
			if(chatip.getText().startsWith("/") || listeUsers.getSelectedItem().toString().equals("All Users")) {
				sendtoall(car_protocol.broadcast_(chatip.getText()));
			} else {
				sendtouser(car_protocol.sendto_(chatip.getText(), listeUsers.getSelectedItem().toString()), listeUsers.getSelectedItem().toString());
			}
			
			//pw.println("MESSAGE " + chatip.getText());
			chatip.setText(null);
		}
	}
	
	/**
	 * Fonction d'entrée de l'application
	 * @param args
	 */
	public static void main(String ... args) {
		String SetUserName = JOptionPane.showInputDialog(null, "SVP Entrez votre nom :",
				"CONNEXION TO CHAT", JOptionPane.PLAIN_MESSAGE);
		
		if(SetUserName != null && !SetUserName.trim().equals("")) {
			try {
				new ClientTop(SetUserName);
			}catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	/**
	 * Fonction permettant de rechercher les personnes connectées sur le réseau
	 * @throws IOException
	 */
	public void rechercheConnecte() throws IOException {
		final ExecutorService es = Executors.newFixedThreadPool(20);
		
		String ip = "";
		for (
			    final Enumeration< NetworkInterface > interfaces =
			        NetworkInterface.getNetworkInterfaces( );
			    interfaces.hasMoreElements( );
			)
			{
			    final NetworkInterface cur = interfaces.nextElement( );

			    if ( cur.isLoopback( ) )
			    {
			        continue;
			    }

			    for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
			    {
			        final InetAddress inet_addr = addr.getAddress( );

			        if ( !( inet_addr instanceof Inet4Address ) )
			        {
			            continue;
			        }
			        ip = inet_addr.getHostAddress( );
			    }
			}
		
		ArrayList<String> temp = new ArrayList<>();
		String tmp = "";
		for(int j = 0; j < ip.length(); j++) {
			if(ip.charAt(j) == '.') {
				temp.add(tmp);
				tmp = "";
			}else{
				tmp += ip.charAt(j);
			}
		}
		ip = temp.get(0)+"."+temp.get(1)+"."+temp.get(2);
		
		final int timeout = 200;
		final ArrayList<Future<Boolean>> futures = new ArrayList<>();
		int port = 1234;
		
		for (int i = 0; i <= 255; i++) {
			String ip0 = ip + "." + i;
		    futures.add(portIsOpen(es, ip0, port, timeout));
		  }
		
		es.shutdown();
	}
	
	/**
	 * Fonction permettant de savoir si un utilisateur est dans le réseau
	 * @param nom Nom de l'utilisateur (String)
	 * @return boolean
	 */
	public Boolean est_utilisateur(String nom) {
		for(Users user : users) {
			if(user.getName().equals(nom)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Fonction permettant de savoir si une adresse IP est dans le réseau
	 * @param ip Adresse IP (String)
	 * @return boolean
	 */
	public Boolean est_utilisateurIP(String ip) {
		for(Users user : users) {
			if(user.getIP().equals(ip))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Fonction permettant de retourner un utilisateur à partir de son nom
	 * @param nom Nom de l'utilisateur(String)
	 * @return user Objet de type USERS - Null si aucun utilisateur trouvé
	 */
	public Users getUser(String nom) {
		for(Users user : users) {
			if(user.getName().equals(nom))
				return user;
		}
		return null;
	}
	
	/**
	 * Fonction permettant de retourner un utilisateur à partir de son adresse IP
	 * @param ip Adresse IP de l'utilisateur
	 * @return user Objet de type USERS - Null si aucun utilisateur trouvé
	 */
	public Users getUserIP(String ip) {
		for(Users user : users) {
			if(user.getIP().equals(ip))
				return user;
		}
		return null;
	}
	
	/**
	 * Fonction permettant de snifer le réseau pour rechercher les serveurs connecté sur le port 1234
	 * @param es
	 * @param ip
	 * @param port
	 * @param timeout
	 */
	public Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
		return es.submit(new Callable<Boolean>() {
	      @Override public Boolean call() {
	        try {
	          Socket socket = new Socket();
	          socket.connect(new InetSocketAddress(ip, port), timeout);
	          
	          clients.add(new Manageuser(new Socket(ip, 1234), true));

	          socket.close();
	          
	          return true;
	        } catch (Exception ex) {
	          return false;
	        }
	      }
		});
	}
	
	/**
	 * Fonction permettant d'envoyer un message à des utilisateurs du réseau
	 * @param user Utilisateur (USERS)
	 * @param message Message à envoyer (String)
	 */
	public void sendtoall(Users user, String message) {
		ArrayList<String> envoye = new ArrayList<String>();
		for(Manageuser c : clients) {
			if(!envoye.contains(c.getchatuser().getName())  && c.getchatuser() != null && !c.getchatuser().getName().equals("")) {
				c.sendMessage(user.getName(), message);
				envoye.add(c.getchatuser().getName());
			}
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message à tous les utilisateurs du réseau
	 * @param message Message à envoyer (String)
	 */
	public void sendtoall(String message) {
		ArrayList<String> envoye = new ArrayList<String>();
		for(Manageuser c : clients) {
			if(c.getchatuser() != null && !envoye.contains(c.getchatuser().getName()) && !c.getchatuser().getName().equals("")) {
				c.sendMessage(message);
				envoye.add(c.getchatuser().getName());
			}
		}
	}
	
	/*
	 * Fonction permettant d'envoyer un message à une utilisateur du réseau
	 */
	public void sendtouser(String message, String user) {
		ArrayList<String> envoye = new ArrayList<String>();
		for(Manageuser c : clients) {
			if((c.getchatuser() != null && c.getchatuser().getName().equals(user)) || (c.getchatuser() != null && c.getchatuser().getName().equals(username))) {
				if(!envoye.contains(c.getchatuser().getName())){
					c.sendMessage(message);
					envoye.add(c.getchatuser().getName());
				}
			}
		}
	}
	
	/**
	 * Fonction permettant d'exécuter les commandes qui n'ont pas besoin de faire intervenir les autres utilisateurs
	 * @param message Message à envoyer (String)
	 */
	public void sendtome(String message) {
		for(Manageuser c : clients) {
			if(c.getchatuser() != null && c.getchatuser().getName().equals(username)) {
				c.sendMessage(message);
				break;
			}
		}
	}
	
	/**
	 * Fonction permettant de gérer les commandes qui n'ont pas été implémenté graphiquement
	 * @param message Message à envoyer (String)
	 */
	public void sendtoothers(String message) {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		ArrayList<String> envoye = new ArrayList<String>();
		ArrayList<String> reponseProtocol = car_protocol.requete(message);
		if(reponseProtocol.get(0) == "410" || reponseProtocol.get(0) == "300") {
			for(int i = 2; i < reponseProtocol.size(); i++) {
				for(Manageuser c : clients) {
					if((c.getchatuser() != null && c.getchatuser().getName().equals(reponseProtocol.get(i))) || (c.getchatuser() != null && c.getchatuser().getName().equals(username))) {
						if(!envoye.contains(c.getchatuser().getName())){
							c.sendMessage(message);
							envoye.add(c.getchatuser().getName());
						}
					}
				}
			}
		} else if(reponseProtocol.get(0) == "310" || reponseProtocol.get(0) == "320" || reponseProtocol.get(0) == "420" || reponseProtocol.get(0) == "360") {
			//On recupère le groupe
			Group g = null;
			for(Group grou : groups) {
				if(grou.getNomGroup().equals(reponseProtocol.get(1)))
					g = grou;
			}
			
			if(g != null) {
				if(reponseProtocol.get(0) == "420" || reponseProtocol.get(0) == "360") {
					for(Users user : g.listeUsers()) {
						for(Manageuser c : clients) {
							if((c.getchatuser() != null && c.getchatuser().getName().equals(user.getName())) || (c.getchatuser() != null && c.getchatuser().getName().equals(username))) {
								if(!envoye.contains(c.getchatuser().getName())){
									c.sendMessage(message);
									envoye.add(c.getchatuser().getName());
								}
							}
						}
					}
				} else if(reponseProtocol.get(0) == "310") {
					//On verifie si l'utilisaeur qu'on veut enlever est dans le groupe
					Users u = null;
					for(Users user : g.listeUsers()) {
						if(user.getName().equals(reponseProtocol.get(2)))
							u = user;
					}
					
					if(u != null) {
						for(Users user : g.listeUsers()) {
							for(Manageuser c : clients) {
								if((c.getchatuser() != null && c.getchatuser().getName().equals(user.getName())) || (c.getchatuser() != null && c.getchatuser().getName().equals(username))) {
									if(!envoye.contains(c.getchatuser().getName())){
										c.sendMessage(message);
										envoye.add(c.getchatuser().getName());
									}
								}
							}
						}
					} else {
						chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
						chatmsg.append("L'utilisateur que vous avez utilisé n'existe pas\n\n");
					}
				} else if(reponseProtocol.get(0) == "320") {
					//On verifie si l'utilisaeur qu'on veut ajouter existe
					Users u = null;
					for(Users user : users) {
						if(user.getName().equals(reponseProtocol.get(2)))
							u = user;
					}
					
					if(u != null) {
						//On vérifie si l'utilisateur est déja dans le groupe
						Boolean est_deja_dant_groupe = false;
						for(Users user : g.listeUsers()) {
							if(user.getName().equals(u.getName()))
								est_deja_dant_groupe = true;
						}
						
						if(!est_deja_dant_groupe) {
							for(Users user : g.listeUsers()) {
								for(Manageuser c : clients) {
									if((c.getchatuser() != null && c.getchatuser().getName().equals(user.getName())) || (c.getchatuser() != null && c.getchatuser().getName().equals(username)) || (c.getchatuser() != null && c.getchatuser().getName().equals(u.getName()))) {
										if(!envoye.contains(c.getchatuser().getName())){
											c.sendMessage(message);
											envoye.add(c.getchatuser().getName());
										}
									}
								}
							}
						} else {
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
							chatmsg.append(u.getName() + " fait déjà partir du groupe " + g.getNomGroup() + "\n\n");
						}
					} else {
						chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
						chatmsg.append("L'utilisateur que vous avez utilisé n'existe pas\n\n");
					}
				}
			} else {
				chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
				chatmsg.append("Le groupe que vous avez utilisé n'existe pas\n\n");
			}
		}
	}
	
	/**
	 * Classe représentant un socket dans le système
	 *
	 */
	class Manageuser extends Thread{
		
		Users gotuser = null;
		BufferedReader input;
		PrintWriter output;
		Socket socketClient;
		
		/**
		 * Constructeur
		 * @param client Socket client (Socket)
		 * @param u Utilisateur (USERS)
		 * @throws Exception
		 */
		public Manageuser(Socket client, Users u) throws Exception{
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(client.getOutputStream(), true);
			socketClient = client;
			gotuser = u;
			
			output.println("/CONNECT " + username + " " + status);
			System.out.println("Conclient");
			
			start();
		}
		
		/**
		 * Constructeur
		 * @param client Socket client (Socket)
		 * @param contact (Boolean)
		 * @throws Exception
		 */
		public Manageuser(Socket client, Boolean contact) throws Exception{
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(client.getOutputStream(), true);
			socketClient = client;
			
			if(contact) {
				gotuser = new Users(socketClient.getRemoteSocketAddress().toString().substring(1, 15), "", 
	        			  socketClient.getLocalPort(), 1);
				users.add(gotuser);
				output.println("/CONNECT " + username + " " + status);
				System.out.println("Premiere partie");
			}
			
			start();
		}
		
		/**
		 * Fonction permettant d'envoyer un message à un utilisateur
		 * @param chatuser Nom du destinataire (String)
		 * @param chatmsg Message à envoyer (String)
		 */
		public void sendMessage(String chatuser, String chatmsg) {
			output.println(chatmsg);
		}
		
		/**
		 * Fonction permettant d'envoyer un message à tous les utilisateur
		 * @param chatmsg Message à envoyer (String)
		 */
		public void sendMessage(String chatmsg) {
			if(!gotuser.getName().equals("")) {
				output.println(chatmsg);
			}
		}
		
		/**
		 * Fonction premettant de mettre à jour la liste des utilisateurs sur l'interface graphique
		 */
		protected void miseAJour() {
			
			ArrayList<String> envoye = new ArrayList<String>();
			int selectionne = listeUsers.getSelectedIndex();
			oldusers.clear();
			oldusers.add(new Users("0.0.0.0", "All Users"));
			for(Users user : users) {
				if(!user.getName().equals(username) && !user.getName().equals("")) {
					if(!envoye.contains(user.getName())){
						oldusers.add(user);
						envoye.add(user.getName());
					}
				}
			}
			if(selectionne >= 0)
				listeUsers.setSelectedIndex(selectionne);
			else
				listeUsers.setSelectedIndex(0);
		}
		
		/**
		 * Fonction permettant de retourner l'utilisateur lié à cette socket
		 * @return gotuser Utilisateur de la socket (Users)
		 */
		public Users getchatuser() {
			return gotuser;
		}
		
		@Override
		public void run() {
			String Line;
			try {
				while(true) {
					Line = input.readLine();
					System.out.println(Line);
					ArrayList<String> reponseProtocol = car_protocol.requete(Line);
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date();
					
					if(!est_utilisateurIP(socketClient.getRemoteSocketAddress().toString().substring(1, 15)) && gotuser == null) {
						 if(reponseProtocol.get(0) == "200" && !est_utilisateur(reponseProtocol.get(1))) {
				        	  gotuser = new Users(socketClient.getRemoteSocketAddress().toString().substring(1, 15), reponseProtocol.get(1), 
				        			  socketClient.getLocalPort(), 1);
				        	  if(reponseProtocol.size() == 3) {
				        		  gotuser.setStatus(Integer.parseUnsignedInt(reponseProtocol.get(2)));
				        	  }
				        	  
				        	  if(!est_utilisateur(gotuser.getName()))
				        		  users.add(gotuser);
					          
					          try {
					        	  System.out.println("Jinitie la troisieme partie avec :"+reponseProtocol.get(1));
					        	  clients.add(new Manageuser(new Socket(socketClient.getRemoteSocketAddress().toString().substring(1, 15), 1234), true));
					          } catch(Exception ex) {
					        	  
					          }
					          
					          System.out.println("Deuxieme partie");
					          System.out.println("On est connecté");
					          output.println(car_protocol.broadcast_("Connexion réussie"));
				          }
					} else if(est_utilisateurIP(socketClient.getRemoteSocketAddress().toString().substring(1, 15))) {
						
						if(reponseProtocol.get(0) == "200" && !est_utilisateur(reponseProtocol.get(1))) {
							gotuser = getUserIP(socketClient.getRemoteSocketAddress().toString().substring(1, 15));
				        	 
							gotuser.setName(reponseProtocol.get(1));
				        	  if(reponseProtocol.size() == 3) {
				        		  gotuser.setStatus(Integer.parseUnsignedInt(reponseProtocol.get(2)));
				        	  }
					          //users.add(gotuser);
					          System.out.println("Fin la troisieme partie avec :"+reponseProtocol.get(1));
					          System.out.println("Troisieme partie");
					          System.out.println("On est connecté");
					          output.println(car_protocol.broadcast_("Connexion réussie"));
						 }        
					}
						
					if (gotuser != null && est_utilisateur(gotuser.getName())){
												
						if(reponseProtocol.get(0).equals("210")) {
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n\n");
							if(users.remove(gotuser) && !gotuser.getName().equals("")) {
								clients.remove(this);
								
								chatmsg.append(gotuser.getName() + " ==> Déconnecté\n\n");
								socketClient.close();
							}
							
						} else if(reponseProtocol.get(0).equals("400")) {
							if(gotuser.getName().equals(username)){
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append("Moi ==> ");
								chatmsg.append(reponseProtocol.get(1)+" (Broadcast)\n\n");
							} else if(!gotuser.getName().equals("")) {
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append(gotuser.getName()+" ==> ");
								chatmsg.append(reponseProtocol.get(1)+" (Broadcast)\n\n");
							}
						} else if(reponseProtocol.get(0).equals("260")) { //USERS
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
							if(reponseProtocol.size() == 1){
								chatmsg.append("Liste des utilisateurs connectés :\n");
								for(Users user : users) {
									if(!user.getName().equals("")) {
										chatmsg.append(user.getName()+" ==> Connecté\n");
									}
								}
								chatmsg.append("\n");
							} else {
								for(int i = 1; i < reponseProtocol.size(); i++) {
									for(Users user : users) {
										if(!user.getName().equals("") && user.getName().equals(reponseProtocol.get(i))) {
											chatmsg.append(user.getName()+" ==> Connecté\n");
										}
									}
								}
								chatmsg.append("\n");
							}
						} else if(reponseProtocol.get(0).equals("240")) {
							if(gotuser != null && !gotuser.getName().equals("")) {
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								gotuser.setStatus(Integer.parseInt(reponseProtocol.get(1)));
								chatmsg.append(gotuser.getName() + " ==> Nouveau status à " + reponseProtocol.get(1) + "\n\n");
								
								if(gotuser.getName().equals(username)) {
									status = Integer.parseInt(reponseProtocol.get(1));
								}
							}
						} else if(reponseProtocol.get(0).equals("250")) { //USERS
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
							if(reponseProtocol.size() == 1){
								chatmsg.append("Status des utilisateurs connectés :\n");
								for(Users user : users) {
									if(!user.getName().equals("")) {
										chatmsg.append(user.getName()+" ==> Status " + user.getStatus() + "\n");
									}
								}
								chatmsg.append("\n");
							} else {
								for(int i = 1; i < reponseProtocol.size(); i++) {
									for(Users user : users) {
										if(!user.getName().equals("") && user.getName().equals(reponseProtocol.get(i))) {
											chatmsg.append(user.getName()+" ==> Status " + user.getStatus() + "\n");
										}
									}
								}
								chatmsg.append("\n");
							}
						} else if(reponseProtocol.get(0).equals("410")) {
							
							if(gotuser.getName().equals(username)){
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append("Moi ==> ");
								chatmsg.append(reponseProtocol.get(1)+" ");
								chatmsg.append("(Reçu par : ");
								for(int i = 2; i < reponseProtocol.size(); i++) {
									if(i == reponseProtocol.size()-1)
										chatmsg.append(reponseProtocol.get(i));
									else
										chatmsg.append(reponseProtocol.get(i)+", ");
								}
								chatmsg.append(")\n\n");
							} else if(!gotuser.getName().equals("")) {
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append(gotuser.getName()+" ==> ");
								chatmsg.append(reponseProtocol.get(1)+" ");
								chatmsg.append("(Reçu par : ");
								for(int i = 2; i < reponseProtocol.size(); i++) {
									if(i == reponseProtocol.size()-1)
										chatmsg.append(reponseProtocol.get(i));
									else
										chatmsg.append(reponseProtocol.get(i)+", ");
								}
								chatmsg.append(")\n\n");
							}
							
						} else if(reponseProtocol.get(0).equals("220")) {
							System.out.println(Line);
							output.println("/ID "+username+" "+status);
						} else if(reponseProtocol.get(0).equals("230")) {
							if(gotuser.getName().equals("")) {
								System.out.println(Line);
								gotuser.setName(reponseProtocol.get(1));
								gotuser.setStatus(Integer.parseInt(reponseProtocol.get(2)));
								output.println(car_protocol.broadcast_("Connexion réussie"));
							}
						} else if(reponseProtocol.get(0).equals("300") || reponseProtocol.get(0).equals("340")) { //création groupe
							//On verifie que aucun groupe ne porte ce nom
							Boolean trouve = false;
							for(Group gou : groups) {
								if(gou.getNomGroup().equals(reponseProtocol.get(1)))
									trouve = true;
							}
							if(!trouve) {
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append("Vous avez été ajouté dans le groupe <<" + reponseProtocol.get(1) + ">>\n");
								chatmsg.append("Liste des membres du groupe : | ");
								Group g = new Group(reponseProtocol.get(1));
								ArrayList<String> ajoute = new ArrayList<String>();
								for(int i = 2; i < reponseProtocol.size(); i++) {
									Users u = null;
									for(Users user : users) {
										if(user.getName().equals(reponseProtocol.get(i))) {
											if(!ajoute.contains(user.getName())){
												u = user;
												ajoute.add(user.getName());
											}
										}
									}
									if(u != null) {
										g.ajouterUser(u);
										chatmsg.append(u.getName()+" | ");
									}
								}
								groups.add(g);
	
								chatmsg.append("\n\n");
							}
							
						} else if(reponseProtocol.get(0) == "310") {
							//On recupère le groupe
							Group g = null;
							for(Group grou : groups) {
								if(grou.getNomGroup().equals(reponseProtocol.get(1)))
									g = grou;
							}
							
							if(g != null) {
								//On verifie si l'utilisaeur qu'on doit enlever est dans le groupe
								Users u = null;
								for(Users user : g.listeUsers()) {
									if(user.getName().equals(reponseProtocol.get(2)))
										u = user;
								}
								
								if(u != null) {
									//on enleve
									g.listeUsers().remove(u);
									chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
									if(u.getName().equals(username)){
										groups.remove(g);
										chatmsg.append("Vous avez été supprimé du groupe " + g.getNomGroup() + "\n\n");
									} else {
										chatmsg.append(u.getName() + " a été supprimé du groupe " + g.getNomGroup() + "\n\n");
									}
								}
							}
						} else if(reponseProtocol.get(0) == "320") {
							//On recupère le groupe
							Group g = null;
							for(Group grou : groups) {
								if(grou.getNomGroup().equals(reponseProtocol.get(1)))
									g = grou;
							}
							
							if(g != null) {//l'utilisateur est déjà dans le groupe, on le notifie
								Users u = null;
								for(Users user : users) {
									if(user.getName().equals(reponseProtocol.get(2)))
										u = user;
								}
								
								if(u != null) {
									if(!g.listeUsers().contains(u)) {
										//on ajoute
										g.listeUsers().add(u);
										chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
										chatmsg.append(u.getName() + " a été ajouté dans le groupe " + g.getNomGroup() + "\n\n");
									}
								}
							} else if(username.equals(reponseProtocol.get(2))) { //l'utilisateur n'est pas encore dans le groupe, on créé le groupe parce que c'est lui quon veut ajouter
								output.println("/LISTUSERSGROUP "+reponseProtocol.get(1));
							}
						} else if(reponseProtocol.get(0) == "330"){
							//On recupère le groupe
							Group g = null;
							for(Group grou : groups) {
								if(grou.getNomGroup().equals(reponseProtocol.get(1)))
									g = grou;
							}
							
							if(g != null) {
								String membregroup = "/USERSGROUP "+reponseProtocol.get(1)+" ";
								for(Users user : g.listeUsers()) {
									membregroup = membregroup + user.getName()+"@@";
								}
								output.println(membregroup);
							}
							
						} else if(reponseProtocol.get(0) == "350") {
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
							if(groups.size() != 0){
								chatmsg.append("Liste des groupes auxquels vous appartenez :\n");
								for(Group g : groups) {
									chatmsg.append("\t"+g.getNomGroup()+" : \n");
									for(Users user : g.listeUsers()) {
										if(!user.getName().equals("")) {
											chatmsg.append("\t\t"+user.getName()+" ==> Status " + user.getStatus() + "\n");
										}
									}
								}
								chatmsg.append("\n");
							} else {
								chatmsg.append("Vous n'appartenez à aucun groupe \n\n");
							}
						} else if(reponseProtocol.get(0) == "360") {
							//On verifie que aucun groupe ne porte ce nom
							Group g = null;
							for(Group gou : groups) {
								if(gou.getNomGroup().equals(reponseProtocol.get(1)))
									g = gou;
							}
							if(g != null){
								groups.remove(g);
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								chatmsg.append("Le groupe " + reponseProtocol.get(1) + " a été supprimé \n\n");
							}
						} else if(reponseProtocol.get(0).equals("420")) {
							
							//On verifie que aucun groupe ne porte ce nom
							Group g = null;
							for(Group gou : groups) {
								if(gou.getNomGroup().equals(reponseProtocol.get(1)))
									g = gou;
							}
							if(g != null){
								chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
								if(gotuser.getName().equals(username)){
									chatmsg.append(g.getNomGroup()+" (Moi) ==> ");
								} else {
									chatmsg.append(g.getNomGroup()+" ("+gotuser.getName()+") ==> ");
								}
								
								chatmsg.append(reponseProtocol.get(2)+" ");
								chatmsg.append("\n\n");
							}
							
						} else if(reponseProtocol.get(0).equals("900")) {
							chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
							chatmsg.append("---------- APPLICATION DE TCHAT AVEC LE PROTOCOLE CAR");
						} else if(reponseProtocol.get(0).startsWith("E")) {
							output.println(car_protocol.broadcast_(reponseProtocol.get(0)));
						}
					}
					
					
					if(gotuser.getName().equals("")) {
						output.println("/WHO");
					}
					
					//On met à jour la liste
					miseAJour();
					//Fin mise à jour de la liste
				}
			}catch(Exception ex) {
				if(users.remove(gotuser) && !gotuser.getName().equals("")) {
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date();
					clients.remove(this);
					chatmsg.append("----------------------- " + format.format(date) + " -------------------------\n");
					chatmsg.append(gotuser.getName() + " ==> Déconnecté\n\n");
					
					//On sort de tous les groups
					
					try {
						socketClient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println(ex.getMessage());
			}
		}
	}
}