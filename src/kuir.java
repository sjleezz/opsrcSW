import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
		// TODO Auto-generated method stub
		String src_path = args[1];	// �ҽ��� �� ���� ��ġ ��ġ
		String dest_path = "C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\save_xml";	// ������ ��ġ
		
		if(args.length > 2) {
			return;
		}
		
		if(args[0].equals("-c")) {
			System.out.println("arg[0] is -c");
			System.out.println("arg[0] is -c");
			System.out.println("arg[1] is " + src_path);
			
			/**** 2���� : HTML ���� �ҷ��� xml�� ���� ****/
			makeCollection sir = new makeCollection();
			sir.savePath = dest_path;
			
			// open files
			sir.loadFiles(src_path);
			System.out.println("html -> xml loadfile was done");
			
			// build XML Document
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
			
			/***** 3���� : Ű���� ����  *****/
			makeKeyword sir = new makeKeyword();
			sir.savePath = dest_path;
			
			sir.setTitle(null);
			sir.setBody(null);
			
			// open files
			sir.loadFiles(src_path);
			System.out.println("xml loadfile was done");
			
			// build XML Document
			sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");
			
			for (int i = 0; i < 5 ; i++) {
				sir.extractContent(i, "title", null);
				sir.extractKeyword();
				sir.generateElement(i);
			}
			System.out.println("extractKeyword was done");
			
			sir.makeXML("\\week2\\index.xml");
		}
		
		else if(args[0].equals("-w")) {	// makeWeight ����
			System.out.println("arg[0] is -w");
			System.out.println("arg[1] is " + args[1]);
			
			/***** 4���� : ����ġ ���  *****/
			makeWeight sir = new makeWeight();
			sir.savePath = dest_path;
			
			sir.setTitle(null);
			sir.setBody(null);
			
			// open files
			sir.loadFiles(src_path);
			System.out.println("xml loadfile was done");
			
			// build XML Document
			sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");
			
			
		}
		
		else {
			System.out.println("��ɾ� ���ڸ� ��Ȯ�� �Է����ּ���");
		}
		
	}

}
