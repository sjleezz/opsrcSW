import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.transform.TransformerException;

public class indexer extends makeKeyword {
	int N;
	String[] bufs_body;
	ArrayList[] keywords;
	
	HashMap<String, HashMap<String, Double>> map_keywordAndIdAndTF;			// [Keyword, [Id, TF]]
	HashMap<String, HashMap<String, Double>> map_keywordAndIdAndWeight;		// result : [Keyword and [Id , Weight]]
	
	
	public indexer(int fileCnt) {		// �ʿ��� �ڷᱸ���� ���� �� �ʱ�ȭ
		this.N = fileCnt;
		
		this.bufs_body = new String[5];
		this.keywords = new ArrayList[5];
		for(int i = 0 ; i < this.N ; i++) {
			this.keywords[i] = new ArrayList<String>();
		}
		this.map_keywordAndIdAndTF = new HashMap<String, HashMap<String, Double>>();
		this.map_keywordAndIdAndWeight = new HashMap<String, HashMap<String, Double>>();
	}
	
	
	public double calculateWeight(String key, double tf, int df) {
		
		double IDF = Math.log((double)this.N/(double)df);
		double W = tf * IDF;
		return W;
	}
	
	public void extractW() throws TransformerException, IOException {
		int count = 0;
		double tf = 0;
		int df = 0;
		double W= 0.0;
		
		for(String key : this.map_keywordAndIdAndTF.keySet()) {				// ���� Ű����� �󵵼��� ����� map���� ��� Ű������� ������ df���� ���Ѵ�
			for(int j = 0 ; j < this.N ; j++) {
				if(this.keywords[j].contains(key)) {
					count++;
				}
			}
			if(count == 0) {
				System.out.println(key + "�� df�� 0�Դϴ�(����).");
				System.exit(1);
			}
			df = count;	// ���� key�� df ���
			for(String ID : this.map_keywordAndIdAndTF.get(key).keySet()) {
				tf = this.map_keywordAndIdAndWeight.get(key).get(ID);						// ���� key�� tf ��� ����
				W = calculateWeight(key, tf, df);											// df�� tf�� �̿��ؼ� �������� ����ġ W�� ���
				this.map_keywordAndIdAndWeight.get(key).put(ID, Math.round(W*100)/100.0);	// ���߿� ����ġ�� �ٲ�ġ���� �뵵�� �����صα�
			}
			count = 0;
		}
	}
	
	public void tokenizer(int fileCnt, String delim1, String delim2) {				// delim1 : "#"  ,   delim2 : ":"
		
		// tokenize
		StringTokenizer st = new StringTokenizer(this.bufs_body[fileCnt], delim1);
		while ( st.hasMoreTokens() ) {
			HashMap<String, Double> map_IdAndTF = new HashMap<String, Double>();
			
		    String actualElement = st.nextToken();
		    StringTokenizer et = new StringTokenizer(actualElement, delim2);

		    if ( et.countTokens() != 2 ) {
		        throw new RuntimeException("Unexpeced format");
		    }
		    String keyword = et.nextToken();
		    String tf = et.nextToken();
		    
		    int int_tf = Integer.parseInt(tf);
		    double double_tf = (double)int_tf;
		    
		    // tokenizing ok
		    this.keywords[fileCnt].add(keyword);
		    
		    if(this.map_keywordAndIdAndTF.containsKey(keyword)) {									// ���� �̹� keyword�� �ִٸ�, �ٸ� doc�� ���� keyword�̹Ƿ�
		    	this.map_keywordAndIdAndTF.get(keyword).put(String.valueOf(fileCnt+1), double_tf);		// ������ �ִ� keyword�� value hashmap�� ���� ���� id�� �󵵼�(tf) ����
		    	this.map_keywordAndIdAndWeight.get(keyword).put(String.valueOf(fileCnt+1), double_tf);		
		    }
		    else {
		    	map_IdAndTF.put(String.valueOf(fileCnt+1), double_tf);							// map ���·� ����
			    this.map_keywordAndIdAndTF.put(keyword, map_IdAndTF);						// [keyword , [id, tf]]		
			    this.map_keywordAndIdAndWeight.put(keyword, map_IdAndTF);	
		    }
		}
	}
	
	public void createHashMapFile(String filename) throws IOException {	// hashmap file ����, key�� value �Է�
		
		FileOutputStream fileStream = new FileOutputStream(this.savePath + filename);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		objectOutputStream.writeObject(this.map_keywordAndIdAndWeight);
		objectOutputStream.close();
	}
	
}
