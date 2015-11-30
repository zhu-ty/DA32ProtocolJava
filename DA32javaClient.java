import java.io.*;
import java.net.*;    //�����õ�Socket��д��Java.net�����
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
				JSONObject else_data = new JSONObject();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
				
				//���ݰ���
			    data.put("name","cwx");
				data.put("text",outgoing.getText());
				//����json����
				obj.put("id",825307441);
			    obj.put("type","text");
//			    obj.put("time",df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
			    obj.put("time","2015.11.13 11:22:13");
				obj.put("data",data);
				obj.put("else",else_data);
				
				//����MD5��
//			    String jsonText = obj.toString();
			    
				String jsonMd5 = obj.getString("type")+obj.getString("time")+data.getString("name")+data.getString("text");
			    int jsonid= obj.getInt("id");
			   	byte []jsonMd5_byte = arraycat(intToByteArray1(jsonid),jsonMd5.getBytes());//����������ת��Ϊbyte���鲢��

			    System.out.print(jsonMd5_byte);
			    
			    MD5Util Info_MD5 = new MD5Util();
				obj.put("md5",Info_MD5.ByteToMD5(jsonMd5_byte));
				String jsonText = "{"+"\"id\":"+obj.getInt("id")+","+"\"type\":"+"\""+obj.getString("type")+"\""+","+
						"\"time\":"+"\""+obj.getString("time")+"\""+","+"\"else\":"+else_data.toString()+","
						+"\"data\":"+data.toString()+","+"\"md5\":"+"\""+obj.getString("md5")+"\""+"}";
				//�����
				StringWriter out = new StringWriter();
			    obj.write(out);
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
	 //����������ת��Ϊbyte����
	 public byte[] intToByteArray1(int i) {   
		  byte[] result = new byte[4];   
		  result[3] = (byte)((i >> 24) & 0xFF);
		  result[2] = (byte)((i >> 16) & 0xFF);
		  result[1] = (byte)((i >> 8) & 0xFF); 
		  result[0] = (byte)(i & 0xFF);
		  return result;
		 }
	 //�ַ����������
	 byte[] arraycat(byte[] buf1,byte[] buf2)
	 {
	 byte[] bufret=null;
	 int len1=0;
	 int len2=0;
	 if(buf1!=null)
	 len1=buf1.length;
	 if(buf2!=null)
	 len2=buf2.length;
	 if(len1+len2>0)
	 bufret=new byte[len1+len2];
	 if(len1>0)
	 System.arraycopy(buf1,0,bufret,0,len1);
	 if(len2>0)
	 System.arraycopy(buf2,0,bufret,len1,len2);
	 return bufret;
	 }
	 //����MD5���ܽ��� 
	 public class MD5Util {    
	    /*** 
	     * MD5���� ����32λmd5�� 
	     */  
	    public String ByteToMD5(byte[] byteArray){  
	        MessageDigest md5 = null;  
	        try{  
	            md5 = MessageDigest.getInstance("MD5");  
	        }catch (Exception e){  
	            System.out.println(e.toString());  
	            e.printStackTrace();  
	            return "";  
	        } 
	        byte[] md5Bytes = md5.digest(byteArray);  
	        StringBuffer hexValue = new StringBuffer();  
	        for (int i = 0; i < md5Bytes.length; i++){  
	            int val = ((int) md5Bytes[i]) & 0xff;  
	            if (val < 16)  
	                hexValue.append("0");  
	            hexValue.append(Integer.toHexString(val));  
	        }  
	        return hexValue.toString();  
	  
	    }  
	  
	    /** 
	     * ���ܽ����㷨 ִ��һ�μ��ܣ����ν��� 
	     */   
	    public String convertMD5(String inStr){  
	  
	        char[] a = inStr.toCharArray();  
	        for (int i = 0; i < a.length; i++){  
	            a[i] = (char) (a[i] ^ 't');  
	        }  
	        String s = new String(a);  
	        return s;  
	  
	    } 
	}	
	public static void main(String[] args) {
		new DA32javaClient().go();
	}
}
