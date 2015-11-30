import java.io.*;
import java.net.*;
import java.security.MessageDigest;
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
		//Json������
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					//�Խ��յ�json�����д���
					JSONObject  dataJson=new JSONObject(message);
					JSONObject  data=dataJson.getJSONObject("data");
					JSONObject  data_else=dataJson.getJSONObject("else");
					
					int id=dataJson.getInt("id");
					String type=dataJson.getString("type");
					String time=dataJson.getString("time");
					String name=data.getString("name");
					String text=data.getString("text");
					String dataelse=data_else.toString();
					String md5=dataJson.getString("md5");

					//����MD5�벢����У��
					String jsonMd5 = dataJson.getString("type")+dataJson.getString("time")+data.getString("name")+data.getString("text");
				    int jsonid= dataJson.getInt("id");
				   	byte []jsonMd5_byte = arraycat(intToByteArray1(jsonid),jsonMd5.getBytes());//����������ת��Ϊbyte���鲢��
				   	MD5Util Info_MD5 = new MD5Util();
				   	if(md5.equals(Info_MD5.ByteToMD5(jsonMd5_byte)))
				   	{
				   		System.out.println("MD5ƥ��ɹ���");
				   	}
				   	
					//�����Ϣ
					System.out.println("id:"+id);
					System.out.println("type:"+type);
					System.out.println("time:"+time);
					System.out.println("name:"+name);
					System.out.println("text:"+text);
					System.out.println("else:"+dataelse);
					System.out.println("md5:"+md5);
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
	 /** 
	 * ����MD5���ܽ��� 
	 * @author tfq 
	 * @datetime 2011-10-13 
	 */  
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
}
