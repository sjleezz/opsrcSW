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
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Element;

public class makeKeyword {
	
	File dir;
	File[] files;
	
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	org.w3c.dom.Document docum;
	Element docs;
	
	String buf_title;
	String buf_body;
	
	KeywordExtractor ke;
	
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
		/**** XML 파일 만들기 코드 ****/
		this.docFactory = DocumentBuilderFactory.newInstance();
		this.docBuilder = this.docFactory.newDocumentBuilder();
		this.docum = this.docBuilder.newDocument();	//jsoup document를 w3c document로 바꿀 필요 없이 그냥 네임 스페이스 풀로 org.w3c.dom.Document형으로 쓰면 됨
		// docs element 생성
		this.docs = this.docum.createElement("docs");
		docum.appendChild(this.docs);
	}
	
	
	public void extractContents(int i, String wantTag1, String wantTag2) throws TransformerException, IOException {
			this.jdoc = Jsoup.parse(files[i], "UTF-8");		// jsoup document 객체 생성
			extractTitle(i, wantTag1);
			extractBody(i, wantTag2);
		
	}
	
	public void extractContents_(int i, String wantTag1, String wantTag2) throws TransformerException, IOException {
		this.jdoc = Jsoup.parse(files[0], "UTF-8");		// jsoup document 객체 생성
		extractTitle(i, wantTag1);
		extractBody(i, wantTag2);
	
	}
	public void extractTitle(int i, String wantTag)  {
		
		if(files.length > 1) {										// input 파일 개수가 여러개
			Elements titles = this.jdoc.select(wantTag);	// title의 내용을 빼오기 위함
			this.buf_title = titles.get(0).text();				// 0번째 title 태그를 가져옴
		}
		else if (files.length == 1) {								// input 파일 개수가 한 개 
			Elements titles = this.jdoc.getElementsByTag(wantTag);			// title의 내용을 빼오기 위함
			this.buf_title = titles.get(i).text();				// i번째 title 태그를 가져옴
		}
		
	}
	
	public void extractBody(int i, String wantTag)  {
		StringBuilder buf_body = new StringBuilder();
		if(wantTag != null) {											// 제거 원하는 태그가 있으면 그 태그 제거하고 모두 가져옴
			Elements bods = this.jdoc.select(wantTag);					// <p>태그를 지우기 위함
			for(org.jsoup.nodes.Element bod : bods) {
				buf_body.append(bod.text());
			}
			this.buf_body = buf_body.toString();
		}
		else {															// 제거 원하는 태그 없으면 body에서 그냥 모두 가져옴
			Elements bods = this.jdoc.select("#" + i);			
			this.buf_body = bods.get(0).text();
		}
		
	}
	
	public void generateElement(int i) {
		/*** element 생성 ***/
		
		// doc element 생성
		Element doc = docum.createElement("doc");
		this.docs.appendChild(doc);
		
		//속성값 id
		doc.setAttribute("id", Integer.toString(i));
		
		//title, body 내용 추가
		//title element
		Element title = docum.createElement("title");
		title.appendChild(docum.createTextNode(this.buf_title));
		doc.appendChild(title);
		
		//body element
		Element body = docum.createElement("body");
		body.appendChild(docum.createTextNode(this.buf_body));
		doc.appendChild(body);
	}
	
	public void makeXML(String filename) throws FileNotFoundException, TransformerException {
		// xml 파일로 쓰기 
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(docum);
		StreamResult result = new StreamResult(new FileOutputStream(new File(this.savePath + filename)));
		transformer.transform(source, result);
	}
	
	public void extractKeyword() {
		
		StringBuilder buf = new StringBuilder();
		// init KeywordExtractor
		this.ke = new KeywordExtractor();
		//extract keyword
		KeywordList kl = this.ke.extractKeyword(this.buf_body, true);
		//use extracted list
		for(Keyword k : kl) {
			buf.append(k.getString() + ":" + k.getCnt() + "#");
		}
		this.buf_body = buf.toString();
	}
	
}
