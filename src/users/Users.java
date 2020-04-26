package users;

public class Users {
	String ipAdress;
	String name;
	int port;
	int status;
	
	public Users(String ip, String n) {
		ipAdress = ip;
		name = n;
		status = 1;
	}
	
	public Users(String ip) {
		ipAdress = ip;
		status = 1;
	}
	
	public Users(String ip, String n, int p, int e) {
		ipAdress = ip;
		name = n;
		status = 1;
		port = p;
		status = e;
	}
	
	public void setStatus(int s) {
		status = s;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setIP(String ip) {
		ipAdress = ip;
	}
	
	public String getIP() {
		return ipAdress;
	}
	
	public void setPort(int p) {
		port = p;
	}
	
	public int getPort() {
		return port;
	}
}