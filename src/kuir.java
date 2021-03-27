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
		
		if(args[0].equals("-c")) {								// html -> xml ����
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
			
			sir.makeXML("\\week2\\collection.xml");
			System.out.println("makeXML was done");
		}
		else if(args[0].equals("-k")){							// keyword ����
			System.out.println("arg[0] is -k");
			System.out.println("arg[1] is " + src_path);
			
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
			
			// title�� body���� ������ �����ϰ�, body�� ��� keyword���� �����ؼ� body�� ����
			for (int i = 0; i < 5 ; i++) {
				sir.extractContent(i, "title", null);
				sir.extractKeyword(i);
				sir.generateElement(i);
			}
			System.out.println("extractKeyword was done");
			
			sir.makeXML("\\week3\\index.xml");
			
		}
		
		else if(args[0].equals("-i")) {							// makeWeight ����
			System.out.println("arg[0] is -i");
			System.out.println("arg[1] is " + args[1]);
			
			// 4���� : ����ġ ��� 
			indexer sir = new indexer(5);
			sir.savePath = dest_path;
			
			sir.setTitle(null);
			sir.setBody(null);
			
			// open files
			sir.loadFiles(src_path);							//C:\Users\ASUS\eclipse-workspace\SimpleIR\save_xml\week2 ���� index.xml ���� ����
			System.out.println("xml loadfile was done");
			
			// build XML Document
			/*sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");*/
			
			for (int fileCnt = 0; fileCnt < 5 ; fileCnt++) {
				sir.extractContent(fileCnt, "title", null);				//�� doc������ title, body ����
				sir.bufs_body[fileCnt] = sir.buf_body;					// bufs�� 5���� body ����
			}
			for(int fileCnt = 0; fileCnt < 5 ; fileCnt++) {
				sir.tokenizer(fileCnt, "#", ":");					// index.xml���� Ű����, id, �󵵼� ���� �� �̸� map�� ����
				System.out.println(fileCnt+1 +" tokenizer() finished!");
			}
			
			// '�ܾ�:�󵵼�#'���� ������ body�� ������� �о�鿩�� 1. �� doc������ �󵵼���  ����   2. ������ �������� ���� ����ġ W ���   3. [Ű����, [id, ����ġ]]�� hashmap ������ ���� 
			
			sir.extractW();
			System.out.println("extractDF() finished!");
			
		

			for(String key : sir.map_keywordAndIdAndWeight.keySet()) {
				System.out.print(key + " -> ");
				for(String k : sir.map_keywordAndIdAndWeight.get(key).keySet()) {
					System.out.print(k + " " + sir.map_keywordAndIdAndWeight.get(key).get(k) + " ");
				}
				System.out.println("");
			}
			
			sir.createHashMapFile("\\week4\\index.post");
			System.out.println("createHashMapFile() finished!");
			
			System.out.println("calculating weight was done");
			
			//sir.makeXML("\\week3\\index.post");
		}
		
		else {
			System.out.println("��ɾ� ���ڸ� ��Ȯ�� �Է����ּ���");
		}
		
	}

}
