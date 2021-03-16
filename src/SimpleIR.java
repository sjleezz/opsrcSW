import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

public class SimpleIR {
	
	public static void main(String[] args) throws ParserConfigurationException, FileNotFoundException, TransformerException {
		
		/**** HTML 파일 불러오기 코드 ****/
		String path = "C:\\Users\\ASUS\\Desktop\\htmls";
		
		File dir = new File(path);
		File files[] = dir.listFiles();
		
		/**** XML 파일 만들기 코드 ****/
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document docum = docBuilder.newDocument();	//jsoup document를 w3c document로 바꿀 필요 없이 그냥 네임 스페이스 풀로 org.w3c.dom.Document형으로 쓰면 됨
		
		
		
		// docs element 생성
		Element docs = docum.createElement("docs");
		docum.appendChild(docs);
		
		
		/**** title, div 내용 빼와서 xml에 저장  ****/
		for (int i = 0; i < files.length; i++) {
			try {
				StringBuilder buf_title = new StringBuilder();
				StringBuilder buf_body = new StringBuilder();
				
				Document jdoc = Jsoup.parse(files[i], "UTF-8");		// jsoup document 객체 생성
				
				Elements titles = jdoc.getElementsByTag("title");	// title의 내용을 빼오기 위함
				
				Elements divs = jdoc.select("p");					// <p>태그를 지우기 위함
				for(org.jsoup.nodes.Element div : divs) {
					buf_body.append(div.text());
				}
				if(titles.size() > 0) {
					buf_title.append(titles.get(0).text()); 		// title 태그의 맨 첫번째를 string으로 가져옴
				}
				
				// doc 태그 생성 및 html파일 내용 하나씩 xml에 추가
				//doc element 생성
				Element doc = docum.createElement("doc");
				docs.appendChild(doc);
				
				//속성값 id
				doc.setAttribute("id", Integer.toString(i));
				
				//title, body 내용 추가
				//title element
				Element title = docum.createElement("title");
				title.appendChild(docum.createTextNode(buf_title.toString()));
				doc.appendChild(title);
				
				//body element
				Element body = docum.createElement("body");
				body.appendChild(docum.createTextNode(buf_body.toString()));
				doc.appendChild(body);
				
				// xml 파일로 쓰기 
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
						
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
						
				DOMSource source = new DOMSource(docum);
				StreamResult result = new StreamResult(new FileOutputStream(new File("C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\src\\collection.xml")));
						
				transformer.transform(source, result);
				
				
				
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println("done");
	}
}
