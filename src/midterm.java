import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class midterm {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		String src_path = args[1];	// 소스가 될 파일 위치 위치
		String dest_path = "C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\save_xml";	
		
		if(args[0].equals("-f")) {								
			
			genSnippet sir = new genSnippet(0);
			sir.savePath = dest_path;
			
			// open files
			sir.loadFiles("C:\\Users\\ASUS\\eclipse-workspace\\SimpleIR\\src\\input.txt");
			sir.func2(args[3]); 
			sir.func1(); 
			
			
			
		}
	}
}
