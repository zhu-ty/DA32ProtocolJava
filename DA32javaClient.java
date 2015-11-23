import java.io.*;
import java.net.*;    //我们用的Socket是写在Java.net下面的
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.json.*;

public class DA32javaClient {
	JTextField outgoing;
	PrintWriter writer;
	Socket sock;
	
	public void go() {
		JFrame frame = new JFrame("DA32javaClient");                //新建一个名叫DA32javaClient的对话框
		JPanel mainPanel = new JPanel();
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");                       //新建Send按钮
		sendButton.addActionListener(new SendButtonListener () );
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		setUpNetworking();
	frame.setSize(400, 500);
	frame.setVisible(true);
	}
	
	private void setUpNetworking() {
		try{
			sock = new Socket("127.0.0.1",3232);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		}  catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev){
			try{
				JSONObject obj = new JSONObject();
				obj.put("name",outgoing.getText());
				obj.put("num","01");
			    obj.put("balance","00");
			    obj.put("type","text");

				StringWriter out = new StringWriter();
			    obj.write(out);
			    String jsonText = out.toString();
			    System.out.print(jsonText);
			    
				writer.println(jsonText);
				writer.flush();
			} catch(Exception ex){
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
	public static void main(String[] args) {
		new DA32javaClient().go();
	}

}
