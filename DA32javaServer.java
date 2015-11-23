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

					//对接收的json包进行处理
					JSONObject  dataJson=new JSONObject(message);
					JSONObject  data=dataJson.getJSONObject("data");
					
					String id=dataJson.getString("id");
					String type=dataJson.getString("type");
					String md5=dataJson.getString("md5");
					String time=dataJson.getString("time");
					String name=data.getString("name");
					String text=data.getString("text");
					
					System.out.println("name:"+name);
					System.out.println("text:"+text);
					System.out.println("type:"+type);
					System.out.println("id:"+id);
					System.out.println("md5:"+md5);
					System.out.println("time:"+time);
					//System.out.println("reader" + message);
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
