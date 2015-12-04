import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.*;
import org.json.*;
import org.json.JSONObject;

public class DA32javaServer {

	ArrayList clientOutputStreams;
	public int ListenPort = 3232;//���Ӽ����˿�
	public int max_connection = 100;//�������������
	public int max_byte_once = 100000;//��󵥴���ȡ�ֽ���
	public int head_byte_size = 10;//��ǰ׺����
    public int end_byte_size = 2;//����׺����
    
    public static byte[] head_2_bytes = {(byte) 0x32,(byte) 0xA0 };// ���̶�ǰ׺������
    public static byte[] end_2_bytes = {(byte) 0x42,(byte) 0xF0};// ���̶���׺����

    // ��ʼ��3232�ϼ�������ȷ���ص��¼��Ѿ�ע��
	public void start_listening() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(ListenPort);
			
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
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		DataInputStream isReader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
//				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				isReader = new DataInputStream(sock.getInputStream());
//				reader = new BufferedReader(isReader);
			} catch (Exception ex) {ex.printStackTrace();}
		}
		
		//Json������
		public void run() {
//			String message;
			byte[] message = null;
			try {
				while (true) {
					isReader.readFully(message);
					if(message == null)break;
					//�Խ��յ�json�����д���
//					JSONObject  dataJson=new JSONObject(message.toString());
//					JSONObject  data=dataJson.getJSONObject("data");
//					JSONObject  data_else=dataJson.getJSONObject("else");
//					
//					int id=dataJson.getInt("id");
//					String type=dataJson.getString("type");
//					String time=dataJson.getString("time");
//					String name=data.getString("name");
//					String text=data.getString("text");
//					String dataelse=data_else.toString();
//					String md5=dataJson.getString("md5");
//
//					//����MD5�벢����У��
//					String jsonMd5 = dataJson.getString("type")+dataJson.getString("time")+data.getString("name")+data.getString("text");
//				    int jsonid= dataJson.getInt("id");
//				   	byte []jsonMd5_byte = arraycat(intToByteArray1(jsonid),jsonMd5.getBytes());//����������ת��Ϊbyte���鲢��
//				   	if(md5.equals(ByteToMD5(jsonMd5_byte)))
//				   	{
//				   		System.out.println("MD5ƥ��ɹ���");
//				   	}
//				   	
//					//�����Ϣ
//					System.out.println("id:"+id);
//					System.out.println("type:"+type);
//					System.out.println("time:"+time);
//					System.out.println("name:"+name);
//					System.out.println("text:"+text);
//					System.out.println("else:"+dataelse);
//					System.out.println("md5:"+md5);
					//System.out.println("reader" + message);
					System.out.println("md5:");
					tellEveryone(message.toString());
				}
			} catch(Exception ex) {ex.printStackTrace();}
		}
	}
	//������
	public static void main(String[] args) {
		new DA32javaServer().start_listening();
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
		 int len1=0;int len2=0;
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
	 //MD5�����ɺ���
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
}
