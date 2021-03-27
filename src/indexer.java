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
	
	
	public indexer(int fileCnt) {		// 필요한 자료구조들 선언 및 초기화
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
		
		for(String key : this.map_keywordAndIdAndTF.keySet()) {				// 현재 키워드와 빈도수가 저장된 map에서 모든 키워드들을 가지고 df값을 구한다
			for(int j = 0 ; j < this.N ; j++) {
				if(this.keywords[j].contains(key)) {
					count++;
				}
			}
			if(count == 0) {
				System.out.println(key + "의 df가 0입니다(오류).");
				System.exit(1);
			}
			df = count;	// 현재 key의 df 계산
			for(String ID : this.map_keywordAndIdAndTF.get(key).keySet()) {
				tf = this.map_keywordAndIdAndWeight.get(key).get(ID);						// 현재 key의 tf 계산 실행
				W = calculateWeight(key, tf, df);											// df와 tf를 이용해서 최종적인 가중치 W를 계산
				this.map_keywordAndIdAndWeight.get(key).put(ID, Math.round(W*100)/100.0);	// 나중에 가중치로 바꿔치기할 용도로 복사해두기
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
		    
		    if(this.map_keywordAndIdAndTF.containsKey(keyword)) {									// 만일 이미 keyword가 있다면, 다른 doc의 같은 keyword이므로
		    	this.map_keywordAndIdAndTF.get(keyword).put(String.valueOf(fileCnt+1), double_tf);		// 기존에 있는 keyword의 value hashmap에 현재 파일 id와 빈도수(tf) 저장
		    	this.map_keywordAndIdAndWeight.get(keyword).put(String.valueOf(fileCnt+1), double_tf);		
		    }
		    else {
		    	map_IdAndTF.put(String.valueOf(fileCnt+1), double_tf);							// map 형태로 저장
			    this.map_keywordAndIdAndTF.put(keyword, map_IdAndTF);						// [keyword , [id, tf]]		
			    this.map_keywordAndIdAndWeight.put(keyword, map_IdAndTF);	
		    }
		}
	}
	
	public void createHashMapFile(String filename) throws IOException {	// hashmap file 생성, key와 value 입력
		
		FileOutputStream fileStream = new FileOutputStream(this.savePath + filename);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		objectOutputStream.writeObject(this.map_keywordAndIdAndWeight);
		objectOutputStream.close();
	}
	
}
