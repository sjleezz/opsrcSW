import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
		// TODO Auto-generated method stub
		String src_path = args[1];	// 소스가 될 파일 위치 위치
		String dest_path = "C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\save_xml";	// 저장할 위치
		
		if(args.length > 2) {
			return;
		}
		
		if(args[0].equals("-c")) {								// html -> xml 추출
			System.out.println("arg[0] is -c");
			System.out.println("arg[1] is " + src_path);
			
			/**** 2주차 : HTML 파일 불러서 xml로 저장 ****/
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
		else if(args[0].equals("-k")){							// keyword 추출
			System.out.println("arg[0] is -k");
			System.out.println("arg[1] is " + src_path);
			
			/***** 3주차 : 키워드 추출  *****/
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
			
			// title과 body에서 내용을 추출하고, body의 경우 keyword들을 추출해서 body로 삼음
			for (int i = 0; i < 5 ; i++) {
				sir.extractContent(i, "title", null);
				sir.extractKeyword(i);
				sir.generateElement(i);
			}
			System.out.println("extractKeyword was done");
			
			sir.makeXML("\\week3\\index.xml");
			
		}
		
		else if(args[0].equals("-i")) {							// makeWeight 실행
			System.out.println("arg[0] is -i");
			System.out.println("arg[1] is " + args[1]);
			
			// 4주차 : 가중치 계산 
			indexer sir = new indexer(5);
			sir.savePath = dest_path;
			
			sir.setTitle(null);
			sir.setBody(null);
			
			// open files
			sir.loadFiles(src_path);							//C:\Users\ASUS\eclipse-workspace\SimpleIR\save_xml\week2 에서 index.xml 읽을 예정
			System.out.println("xml loadfile was done");
			
			// build XML Document
			/*sir.buildXMLDocument();
			System.out.println("buildXMLDocument was done");*/
			
			for (int fileCnt = 0; fileCnt < 5 ; fileCnt++) {
				sir.extractContent(fileCnt, "title", null);				//각 doc에서의 title, body 추출
				sir.bufs_body[fileCnt] = sir.buf_body;					// bufs에 5개의 body 저장
			}
			for(int fileCnt = 0; fileCnt < 5 ; fileCnt++) {
				sir.tokenizer(fileCnt, "#", ":");					// index.xml에서 키워드, id, 빈도수 추출 후 이를 map에 저장
				System.out.println(fileCnt+1 +" tokenizer() finished!");
			}
			
			// '단어:빈도수#'으로 구성된 body의 내용들을 읽어들여서 1. 각 doc에서의 빈도수를  저장   2. 추출한 변수들을 토대로 가중치 W 계산   3. [키워드, [id, 가중치]]의 hashmap 구조를 구축 
			
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
			System.out.println("명령어 인자를 정확히 입력해주세요");
		}
		
	}

}
