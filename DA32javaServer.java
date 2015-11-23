import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

import org.json.JSONObject;
public class DA32javaServer {

	ArrayList clientOutputStreams;
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {ex.printStackTrace();}
		}
		
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					//�Խ��յ�json�����д���
					JSONObject  info=new JSONObject(message);
//					JSONObject info=dataJson.getJSONObject(null);
					String province=info.getString("name");
					String city=info.getString("num");
					String district=info.getString("balance");
					String address=info.getString("type"); 
					System.out.println(province+city+district+address);
//					System.out.println(dataJson);					
					System.out.println("reader" + message);
					tellEveryone(message);
				}
			} catch(Exception ex) {ex.printStackTrace();}
		}
	}
	 
	public static void main(String[] args) {
		new DA32javaServer().go();
	}
	
	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(3232);
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void tellEveryone (String message) {
		
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
