import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
		// TODO Auto-generated method stub
		String src_path = args[1];	// 소스가 될 파일 위치 위치
		
		if(args.length > 2) {
			return;
		}
		
		if(args[0].equals("-c")) {
			System.out.println("arg[0] is -c");
			System.out.println("arg[0] is -c");
			System.out.println("arg[1] is " + src_path);
			/**** 1주차 : HTML 파일 불러서 xml로 저장 ****/
			String dest_path = "C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\save_xml";	// 저장할 위치
			
			makeCollection sir = new makeCollection();
			sir.savePath = dest_path;
			
			sir.loadFiles(src_path);
			System.out.println("html -> xml loadfile was done");
			
			sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");
			
			for (int i = 0; i < sir.files.length; i++) {
				sir.extractContents(i, "title", "p");
				sir.generateElement(i);
			}
			System.out.println("extractContents was done");
			
			sir.makeXML("\\week1\\collection.xml");
			System.out.println("makeXML was done");
		}
		else if(args[0].equals("-k")){
			System.out.println("arg[0] is -k");
			System.out.println("arg[1] is " + args[1]);
			/***** 2주차 : 키워드 추출  *****/
			String dest_path = "C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\save_xml";	// 저장할 위치
			
			makeKeyword sir = new makeKeyword();
			sir.savePath = dest_path;
			
			sir.setTitle(null);
			sir.setBody(null);
			
			sir.loadFiles(src_path);
			System.out.println("xml -> xml loadfile was done");
			
			sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");
			
			if(sir.files.length == 1) {
				for (int i = 0; i < 5 ; i++) {
					sir.extractContents_(i, "title", null);
					sir.extractKeyword();
					sir.generateElement(i);
				}
				System.out.println("extractKeyword was done");
			}
			
			sir.makeXML("\\week2\\index.xml");
		}
		
		else {
			System.out.println("명령어 인자를 정확히 입력해주세요");
		}
		
	}

}
