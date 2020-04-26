import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.*;

import Protocol.Car_protocol;
import users.Users;

import static java.lang.System.out;

import java.awt.*;
import java.awt.event.*;

public final class ClientTop extends JFrame implements ActionListener {
	String username;
	int status = 1;
	JTextArea chatmsg;
	JTextField chatip, chatip_commande;
	JButton send, exit, send_commande, exit_commande;
	
	Car_protocol car_protocol;
	
	ConcurrentLinkedQueue<Users> users = new ConcurrentLinkedQueue<Users>();
	ConcurrentLinkedQueue<Manageuser> clients = new ConcurrentLinkedQueue<Manageuser>();
	
	public ClientTop(String uname, String servername) throws Exception{
		super(uname);
		
		// On cherche la liste des personnes disponibles
		
		rechercheConnecte();
		car_protocol = new Car_protocol();
		this.username = uname;
			
		
		ServerSocket server = new ServerSocket(1234, 10);
		buildInterface();
		
		/*
		out.println("Le serveur est en cour");
		
		chatusers = new Socket(servername, 1234);
		br = new BufferedReader(new InputStreamReader(chatusers.getInputStream()));
		pw = new PrintWriter(chatusers.getOutputStream(), true);
		pw.println(car_protocol.connect_(uname));
		buildInterface();
		new MessageThread().start();
		*/
		
		while(true) {
			Socket client = server.accept();
			System.out.println("Connexion serveur");
			
			clients.add(new Manageuser(client, false));
		}
	}
	
	public void buildInterface() {
		send = new JButton("Envoyer");
		send_commande = new JButton("Valider");
		exit = new JButton("Sortir");
		exit_commande = new JButton("Commande");
		chatmsg = new JTextArea();
		chatmsg.setEditable(false);
		chatmsg.setRows(30);
		chatmsg.setColumns(50);
		chatip = new JTextField(50);
		chatip_commande = new JTextField(50);
		JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(sp, "Center");
		JPanel bp = new JPanel(new FlowLayout());
		JPanel bp_commande = new JPanel(new FlowLayout());
		bp.add(chatip);
		bp.add(send);
		bp.add(exit);
		exit_commande.setEnabled(false);
		bp_commande.add(exit_commande);
		bp_commande.add(chatip_commande);
		bp_commande.add(send_commande);
		bp.setBackground(Color.LIGHT_GRAY);
		bp.setName("KSTARK CHAT APP.");
		add(bp, "North");
		add(bp_commande, "South");
		send.addActionListener(this);
		send_commande.addActionListener(this);
		exit.addActionListener(this);
		setSize(500, 300);
		setVisible(true);
		pack();
	}
	
	//@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == exit) {
			sendtoall(getUser(username), car_protocol.quit_());
			//pw.println(car_protocol.quit_());
			System.exit(0);
		} else if(evt.getSource() == send_commande) {
			if(chatip_commande.getText().toLowerCase().equals("status") || chatip_commande.getText().toLowerCase().contains("users")) {
				sendtome(chatip_commande.getText());
			} else if(chatip_commande.getText().toLowerCase().contains("sendto")){
				sendtoothers(chatip_commande.getText());
			}
			else {
				sendtoall(chatip_commande.getText());
			}
			
			chatip_commande.setText(null);
		} else {
			System.out.println(car_protocol.broadcast_(chatip.getText())+" (((( ICI");
			sendtoall(car_protocol.broadcast_(chatip.getText()));
			//pw.println("MESSAGE " + chatip.getText());
			chatip.setText(null);
		}
	}
	
	public static void main(String ... args) {
		String SetUserName = JOptionPane.showInputDialog(null, "Please Entrez votre nom :",
				"KSTARK CHAT APP.", JOptionPane.PLAIN_MESSAGE);
		String servername = "localhost";
		
		try {
			new ClientTop(SetUserName, servername);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
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
		System.out.println(ip);
		
		final int timeout = 200;
		final ArrayList<Future<Boolean>> futures = new ArrayList<>();
		int port = 1234;
		
		for (int i = 0; i <= 255; i++) {
			String ip0 = ip + i;
		    futures.add(portIsOpen(es, ip0, port, timeout));
		  }
		
		es.shutdown();
	}
	
	public Boolean est_utilisateur(String nom) {
		for(Users user : users) {
			if(user.getName().equals(nom)){
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean est_utilisateurIP(String ip) {
		for(Users user : users) {
			if(user.getIP().equals(ip))
				return true;
		}
		
		return false;
	}
	
	public Users getUser(String nom) {
		for(Users user : users) {
			if(user.getName().equals(nom))
				return user;
		}
		return null;
	}
	
	public Users getUserIP(String ip) {
		for(Users user : users) {
			if(user.getIP().equals(ip))
				return user;
		}
		return null;
	}
	
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
	
	public void sendtoall(Users user, String message) {
		ArrayList<Users> envoye = new ArrayList<Users>();
		for(Manageuser c : clients) {
			if(!envoye.contains(c.getchatuser())  && c.getchatuser() != null && !c.getchatuser().equals("")) {
				c.sendMessage(user.getName(), message);
				envoye.add(c.getchatuser());
			}
		}
	}
	
	public void sendtoall(String message) {
		ArrayList<Users> envoye = new ArrayList<Users>();
		for(Manageuser c : clients) {
			if(!envoye.contains(c.getchatuser()) && c.getchatuser() != null && !c.getchatuser().equals("")) {
				c.sendMessage(message);
				envoye.add(c.getchatuser());
			}
		}
	}
	
	public void sendtome(String message) {
		for(Manageuser c : clients) {
			if(c.getchatuser() != null && c.getchatuser().getName().equals(username)) {
				c.sendMessage(message);
				break;
			}
		}
	}
	
	public void sendtoothers(String message) {
		ArrayList<String> reponseProtocol = car_protocol.requete(message);
		if(reponseProtocol.get(0) == "12000") {
			for(int i = 2; i < reponseProtocol.size(); i++) {
				for(Manageuser c : clients) {
					if((c.getchatuser() != null && c.getchatuser().getName().equals(reponseProtocol.get(i))) || (c.getchatuser() != null && c.getchatuser().getName().equals(username))) {
						c.sendMessage(message);
						break;
					}
				}
			}
		}
	}
	
	class Manageuser extends Thread{
		
		Users gotuser = null;
		BufferedReader input;
		PrintWriter output;
		Socket socketClient;
		
		public Manageuser(Socket client, Users u) throws Exception{
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(client.getOutputStream(), true);
			socketClient = client;
			gotuser = u;
			
			output.println("CONNECT " + username + " " + status);
			System.out.println("Conclient");
			
			start();
		}
		
		public Manageuser(Socket client, Boolean contact) throws Exception{
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(client.getOutputStream(), true);
			socketClient = client;
			
			if(contact) {
				gotuser = new Users(socketClient.getRemoteSocketAddress().toString().substring(1, 15), "", 
	        			  socketClient.getLocalPort(), 1);
				users.add(gotuser);
				output.println("CONNECT " + username + " " + status);
				System.out.println("Premiere partie");
			}
			
			start();
		}
		
		public void sendMessage(String chatuser, String chatmsg) {
			output.println(chatmsg);
		}
		
		public void sendMessage(String chatmsg) {
			if(!gotuser.getName().equals("")) {
				output.println(chatmsg);
			}
		}
		
		public Users getchatuser() {
			return gotuser;
		}
		
		@Override
		public void run() {
			String Line;
			try {
				while(true) {
					Line = input.readLine();
					//System.out.println(Line);
					ArrayList<String> reponseProtocol = car_protocol.requete(Line);
					//output.println(Line+"\n");
					
					if(!est_utilisateurIP(socketClient.getRemoteSocketAddress().toString().substring(1, 15)) && gotuser == null) {
						 if(reponseProtocol.get(0) == "2000" && !est_utilisateur(reponseProtocol.get(1))) {
				        	  gotuser = new Users(socketClient.getRemoteSocketAddress().toString().substring(1, 15), reponseProtocol.get(1), 
				        			  socketClient.getLocalPort(), 1);
				        	  if(reponseProtocol.size() == 3) {
				        		  gotuser.setStatus(Integer.parseUnsignedInt(reponseProtocol.get(2)));
				        	  }
					          users.add(gotuser);
					          
					          try {
					        	  System.out.println("Jinitie la troisieme partie avec :"+reponseProtocol.get(1));
					        	  clients.add(new Manageuser(new Socket(socketClient.getRemoteSocketAddress().toString().substring(1, 15), 1234), true));
					          } catch(Exception ex) {
					        	  
					          }
					          
					          System.out.println("Deuxieme partie");
					          System.out.println("On est connecté");
					          output.println(car_protocol.broadcast_("Connexion réussie"));
				          } else if(gotuser == null) {
				        	 // output.println("Vous nete pas connecté");
				          }
					} else if(est_utilisateurIP(socketClient.getRemoteSocketAddress().toString().substring(1, 15))) {
						
						if(reponseProtocol.get(0) == "2000" && !est_utilisateur(reponseProtocol.get(1))) {
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
												
						if(reponseProtocol.get(0).equals("7000")) {
							
							if(users.remove(gotuser)) {
								clients.remove(this);
								chatmsg.append(gotuser.getName() + " ==> Déconnecté\n");
								socketClient.close();
							}
							
						} else if(reponseProtocol.get(0).equals("4000")) {
							if(gotuser.getName().equals(username)){
								chatmsg.append("Moi ==> ");
								chatmsg.append(reponseProtocol.get(1)+"\n");
							} else if(!gotuser.getName().equals("")) {
								chatmsg.append(gotuser.getName()+" ==> ");
								chatmsg.append(reponseProtocol.get(1)+"\n");
							}
						} else if(reponseProtocol.get(0).equals("3000")) { //USERS
							if(reponseProtocol.size() == 1){
								chatmsg.append("Lisre des utilisateurs connectés :\n");
								for(Users user : users) {
									if(!user.getName().equals("")) {
										chatmsg.append(user.getName()+" ==> Connecté\n");
									}
								}
							} else {
								for(int i = 1; i < reponseProtocol.size(); i++) {
									for(Users user : users) {
										if(!user.getName().equals("") && user.getName().equals(reponseProtocol.get(i))) {
											chatmsg.append(user.getName()+" ==> Connecté\n");
										}
									}
								}

							}
						} else if(reponseProtocol.get(0).equals("9000")) {
							if(gotuser != null && !gotuser.getName().equals("")) {
								gotuser.setStatus(Integer.parseInt(reponseProtocol.get(1)));
								chatmsg.append(gotuser.getName() + " ==> Nouveau status à " + reponseProtocol.get(1) + "\n");
							}
						} else if(reponseProtocol.get(0).equals("10000")) { //USERS
							if(reponseProtocol.size() == 1){
								chatmsg.append("Status des utilisateurs connectés :\n");
								for(Users user : users) {
									if(!user.getName().equals("")) {
										chatmsg.append(user.getName()+" ==> Status " + user.getStatus() + "\n");
									}
								}
							} else {
								for(int i = 1; i < reponseProtocol.size(); i++) {
									for(Users user : users) {
										if(!user.getName().equals("") && user.getName().equals(reponseProtocol.get(i))) {
											chatmsg.append(user.getName()+" ==> Status " + user.getStatus() + "\n");
										}
									}
								}
							}
						} else if(reponseProtocol.get(0).equals("12000")) {
							
							if(gotuser.getName().equals(username)){
								chatmsg.append("Moi ==> ");
								chatmsg.append(reponseProtocol.get(1)+" ");
								chatmsg.append("(Reçu par : ");
								for(int i = 2; i < reponseProtocol.size(); i++) {
									if(i == reponseProtocol.size())
										chatmsg.append(reponseProtocol.get(i));
									else
										chatmsg.append(reponseProtocol.get(i)+", ");
								}
								chatmsg.append(")\n");
							} else if(!gotuser.getName().equals("")) {
								chatmsg.append(gotuser.getName()+" ==> ");
								chatmsg.append(reponseProtocol.get(1)+" ");
								chatmsg.append("(Reçu par : ");
								for(int i = 2; i < reponseProtocol.size(); i++) {
									if(i == reponseProtocol.size())
										chatmsg.append(reponseProtocol.get(i));
									else
										chatmsg.append(reponseProtocol.get(i)+", ");
								}
								chatmsg.append(")\n");
							}
							
						} else {
							//chatmsg.append(reponseProtocol.get(0)+"\n");
						}
					}
					

					
					/*
					
					if(reponseProtocol.get(0) == "1000") {
						output.println(reponseProtocol.get(0));
					} else {
						if(reponseProtocol.get(0) == "3000") {
							if(reponseProtocol.size() > 1) {
								chatmsg.append("Liste des utilisateur : ");
								for(int i = 1; i < reponseProtocol.size(); i++) {
									users.add(new Users(reponseProtocol.get(i)));
									chatmsg.append(reponseProtocol.get(i)+" ");
								}
								chatmsg.append("\n");
							}
						} else if(reponseProtocol.get(0) == "4000") {
							chatmsg.append(reponseProtocol.get(1)+"\n");
							
						} else if(reponseProtocol.get(0) == "6000") { //Un utilisateur déconnecté
							chatmsg.append("Déconnection de "+ reponseProtocol.get(1) +"\n");
							Users user = null;
							for(Users u : users) {
								if(u.getName().equals(reponseProtocol.get(1)))
									user = u;
							}
							users.remove(user);
						} else if(reponseProtocol.get(0) == "8000") { //Un utilisateur déconnecté
							chatmsg.append("Connexion de "+ reponseProtocol.get(1) +"\n");
							Users user = null;
							for(Users u : users) {
								if(u.getName().equals(reponseProtocol.get(1)))
									user = u;
							}
							users.remove(user);
						} else if(reponseProtocol.get(0) == "10000"){ //Status
							chatmsg.append("Le statut de "+ reponseProtocol.get(1) +" est maintenant "+reponseProtocol.get(2)+"\n");
							for(Users user : users) {
								if(user.getName().equals(reponseProtocol.get(1))) {
									user.setStatus(Integer.parseInt(reponseProtocol.get(2)));
								}
							}
						}
					}*/
					//chatmsg.append(Line+"\n");
					//pw.println(Line+"\n");
					
					/*System.out.println(users.size());
					for(Users user : users) {
						System.out.println("utilisateru -> " +user.getName());
					}*/
				}
			}catch(Exception ex) {
				if(users.remove(gotuser)) {
					clients.remove(this);
					chatmsg.append(gotuser.getName() + " ==> Déconnecté\n");
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
