import java.io.*;
import java.net.*;    //�����õ�Socket��д��Java.net�����
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.json.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DA32javaClient {
	JTextField outgoing;
	PrintWriter writer;
	Socket sock;
	
	public void go() {
		JFrame frame = new JFrame("DA32javaClient");                //�½�һ������DA32javaClient�ĶԻ���
		JPanel mainPanel = new JPanel();
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");                       //�½�Send��ť
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
				//�����Json��
				JSONObject obj = new JSONObject();
				JSONObject _else = new JSONObject();
				JSONObject data = new JSONObject();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
				
				obj.put("id","01");
			    obj.put("type","text");
			    obj.put("time",df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
			    //���ݰ���
			    data.put("name","NJH_THU");
				data.put("text",outgoing.getText());
				
				obj.put("data",data);
				obj.put("md5","a1d16ec13b789e457bf440be6eda56cc");
				
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
