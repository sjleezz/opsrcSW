import java.io.File;
import java.io.FileInputStream;
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
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

public class makeCollection {
	
	File dir;
	File[] files;
	
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	org.w3c.dom.Document docum;
	Element docs;
	
	String buf_title;
	String buf_body;
	
	String savePath;
	Document jdoc;
	
	
	public void setTitle(String t) {
		this.buf_title = t;
	}
	
	public void setBody(String b) {
		this.buf_body = b;
	}
	
	public void loadFiles(String path) {
		this.dir = new File(path);
		this.files = this.dir.listFiles();
	}
	
	public void buildXMLDocument() throws ParserConfigurationException  {
		/**** XML ���� ����� �ڵ� ****/
		this.docFactory = DocumentBuilderFactory.newInstance();
		this.docBuilder = this.docFactory.newDocumentBuilder();
		this.docum = this.docBuilder.newDocument();	//jsoup document�� w3c document�� �ٲ� �ʿ� ���� �׳� ���� �����̽� Ǯ�� org.w3c.dom.Document������ ���� ��
		// docs element ����
		this.docs = this.docum.createElement("docs");
		docum.appendChild(this.docs);
	}
	
	//���� ���Ͽ��� �۾��ϴ� ���
	public void extractContents(int i, String wantTag1, String wantTag2) throws TransformerException, IOException {
			this.jdoc = Jsoup.parse(files[i], "UTF-8");		// jsoup document ��ü ����
			extractTitle(i, wantTag1);
			extractBody(i, wantTag2);
		
	}
	
	// xml file �ϳ����� �۾��ϴ� ���
	public void extractContent(int i, String wantTag1, String wantTag2) throws TransformerException, IOException {
		FileInputStream fis = new FileInputStream(files[0]);
		this.jdoc = Jsoup.parse(fis, "UTF-8", "", Parser.xmlParser());		// jsoup document ��ü ����
		extractTitle(i, wantTag1);
		extractBody(i, wantTag2);
	
	}
	public void extractTitle(int i, String wantTag)  {
		
		if(files.length > 1) {										// input ���� ������ ������
			Elements titles = this.jdoc.select(wantTag);	// title�� ������ ������ ����
			this.buf_title = titles.get(0).text();				// 0��° title �±׸� ������
		}
		else if (files.length == 1) {								// input ���� ������ �� �� 
			Elements titles = this.jdoc.getElementsByTag(wantTag);			// title�� ������ ������ ����
			this.buf_title = titles.get(i).text();				// i��° title �±׸� ������
		}
		
	}
	
	public void extractBody(int i, String wantTag)  {
		StringBuilder buf_bod = new StringBuilder();
		if(wantTag != null) {											// ���� ���ϴ� �±װ� ������ �� �±� �����ϰ� ��� ������
			Elements bods = this.jdoc.select(wantTag);					// <p>�±׸� ����� ����
			for(org.jsoup.nodes.Element bod : bods) {
				buf_bod.append(bod.text());
			}
			this.buf_body = buf_bod.toString();
			
			return;
		}
		else {															// ���� ���ϴ� �±� ������ body���� �׳� ��� ������
			Elements bods = this.jdoc.select("body");					// id�� i�� �±� �� body�� ����
			System.out.println(bods.get(i).text());
			this.buf_body = bods.get(i).text();
			
			return;
		}
	}
	
	public void generateElement(int i) {
		/*** element ���� ***/
		
		// doc element ����
		Element doc = this.docum.createElement("doc");
		this.docs.appendChild(doc);
		
		//�Ӽ��� id
		doc.setAttribute("id", Integer.toString(i));
		
		//title, body ���� �߰�
		//title element
		Element title = this.docum.createElement("title");
		title.appendChild(this.docum.createTextNode(this.buf_title));
		doc.appendChild(title);
		
		//body element
		Element body = this.docum.createElement("body");
		body.appendChild(this.docum.createTextNode(this.buf_body));
		doc.appendChild(body);
	}
	
	public void makeXML(String filename) throws FileNotFoundException, TransformerException {
		// xml ���Ϸ� ���� 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(docum);
		StreamResult result = new StreamResult(new FileOutputStream(new File(this.savePath + filename)));
		transformer.transform(source, result);
	}
	

}
