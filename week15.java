package week15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class week15 {

 public static void main(String[] args) throws IOException, ParseException {
	 
	 String[] client = new String[2];
	 String clientId = null;
	 String clientSecret = null;
	 
	 String INPUT_NAME = null;
	 
	 System.out.print("�˻�� �Է��ϼ���.: ");
	 Scanner sc = new Scanner(System.in);
	 String s = sc.nextLine();
	 INPUT_NAME = s;
	 
	 File file = new File("./api/key.txt");
	 FileReader fr = new FileReader(file);
	 BufferedReader br = new BufferedReader(fr);
	 String line = "";
	 for(int i = 0 ; (line = br.readLine()) != null ; i++) {
		 client[i] = line;
	 }
	 
     clientId = client[0]; 		//���ø����̼� Ŭ���̾�Ʈ ���̵�"
     clientSecret = client[1]; 	//���ø����̼� Ŭ���̾�Ʈ ��ũ����"

     String text = null;
     try {
         text = URLEncoder.encode(INPUT_NAME, "UTF-8");
     } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("�˻��� ���ڵ� ����",e);
     }
     	
     String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + text;   			 // json ���
     //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml ���

     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("X-Naver-Client-Id", clientId);
     requestHeaders.put("X-Naver-Client-Secret", clientSecret);
     String responseBody = get(apiURL,requestHeaders);

     parseData(responseBody);
     
     
 }

 private static String get(String apiUrl, Map<String, String> requestHeaders){
     HttpURLConnection con = connect(apiUrl);
     try {
         con.setRequestMethod("GET");
         for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
             con.setRequestProperty(header.getKey(), header.getValue());
         }

         int responseCode = con.getResponseCode();
         if (responseCode == HttpURLConnection.HTTP_OK) { // ���� ȣ��
             return readBody(con.getInputStream());
         } else { // ���� �߻�
             return readBody(con.getErrorStream());
         }
     } catch (IOException e) {
         throw new RuntimeException("API ��û�� ���� ����", e);
     } finally {
         con.disconnect();
     }
 }

 private static HttpURLConnection connect(String apiUrl){
     try {
         URL url = new URL(apiUrl);
         return (HttpURLConnection)url.openConnection();
     } catch (MalformedURLException e) {
         throw new RuntimeException("API URL�� �߸��Ǿ����ϴ�. : " + apiUrl, e);
     } catch (IOException e) {
         throw new RuntimeException("������ �����߽��ϴ�. : " + apiUrl, e);
     }
 }

 private static String readBody(InputStream body){
     InputStreamReader streamReader = new InputStreamReader(body);

     try (BufferedReader lineReader = new BufferedReader(streamReader)) {
         StringBuilder responseBody = new StringBuilder();

         String line;
         while ((line = lineReader.readLine()) != null) {
             responseBody.append(line);
         }

         return responseBody.toString();
     } catch (IOException e) {
         throw new RuntimeException("API ������ �дµ� �����߽��ϴ�.", e);
     }
 }
 
 private static void parseData(String result) throws ParseException {
	 
	 JSONParser jsonParser = new JSONParser();
     JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
     JSONArray infoArray = (JSONArray) jsonObject.get("items");
     for(int i = 0 ; i < infoArray.size(); i++) {
    	 JSONObject item = (JSONObject) infoArray.get(i);
    	 System.out.println("=item_" + i + " ======================================");
    	 System.out.println("title: " + item.get("title"));
    	 System.out.println("subtitle: " + item.get("subtitle"));
    	 System.out.println("director: " + item.get("director"));
    	 System.out.println("actor :" + item.get("actor"));
    	 System.out.println("userRating :" + item.get("userRating"));
    	 System.out.println("");
     }
 }
 
}
