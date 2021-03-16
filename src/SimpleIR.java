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
		
		/**** HTML ���� �ҷ����� �ڵ� ****/
		String path = "C:\\Users\\ASUS\\Desktop\\htmls";
		
		File dir = new File(path);
		File files[] = dir.listFiles();
		
		/**** XML ���� ����� �ڵ� ****/
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		org.w3c.dom.Document docum = docBuilder.newDocument();	//jsoup document�� w3c document�� �ٲ� �ʿ� ���� �׳� ���� �����̽� Ǯ�� org.w3c.dom.Document������ ���� ��
		
		
		
		// docs element ����
		Element docs = docum.createElement("docs");
		docum.appendChild(docs);
		
		
		/**** title, div ���� ���ͼ� xml�� ����  ****/
		for (int i = 0; i < files.length; i++) {
			try {
				StringBuilder buf_title = new StringBuilder();
				StringBuilder buf_body = new StringBuilder();
				
				Document jdoc = Jsoup.parse(files[i], "UTF-8");		// jsoup document ��ü ����
				
				Elements titles = jdoc.getElementsByTag("title");	// title�� ������ ������ ����
				
				Elements divs = jdoc.select("p");					// <p>�±׸� ����� ����
				for(org.jsoup.nodes.Element div : divs) {
					buf_body.append(div.text());
				}
				if(titles.size() > 0) {
					buf_title.append(titles.get(0).text()); 		// title �±��� �� ù��°�� string���� ������
				}
				
				// doc �±� ���� �� html���� ���� �ϳ��� xml�� �߰�
				//doc element ����
				Element doc = docum.createElement("doc");
				docs.appendChild(doc);
				
				//�Ӽ��� id
				doc.setAttribute("id", Integer.toString(i));
				
				//title, body ���� �߰�
				//title element
				Element title = docum.createElement("title");
				title.appendChild(docum.createTextNode(buf_title.toString()));
				doc.appendChild(title);
				
				//body element
				Element body = docum.createElement("body");
				body.appendChild(docum.createTextNode(buf_body.toString()));
				doc.appendChild(body);
				
				// xml ���Ϸ� ���� 
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
