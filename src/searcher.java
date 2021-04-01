import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher extends indexer{

	String query;
	HashMap<String, Integer> map_kw;
	
	public searcher(int docCnt) {
		this.N = docCnt;
		this.bufs_title = new String[5];
		this.map_kw = new HashMap<String, Integer>();
	}
	
	public void inputQuery(String q) {
		
		this.query = q;
		System.out.println(this.query);
		
	}

	public void extractKeyword() {
		
		// init KeywordExtractor
		this.ke = new KeywordExtractor();
		
		//extract keyword
		KeywordList kl = this.ke.extractKeyword(this.query, true);
		
		//save keywords and TF to hashmap
		for(Keyword k : kl) {
			this.map_kw.put(k.getString(), k.getCnt());
			System.out.println("추출된 key와 TF : " + k.getString() + " " + this.map_kw.get(k.getString()));
		}
	}
	
	// 유사도 계산 메소드
	public void CalcSim() {
		Map<String, Double> hm = new HashMap<String, Double>();
		
		double[] dots = new double[5];
		for(int i = 0 ; i < this.N ; i++) {
			for(String key : this.map_kw.keySet()) {
				if(this.map_keywordAndIdAndWeight.containsKey(key)) {
					System.out.println(key);
					// 내적 구하기
					String w = String.valueOf(this.map_keywordAndIdAndWeight.get(key).get(String.valueOf(i+1)));
					System.out.println("value : " + w);
					if(w == "null") {
						System.out.println(key + "의 " + (i+1) + "번째의 가중치가 없습니다.");
						System.out.println("");
					}
					else {
						double x = Math.round((double)this.map_kw.get(key)*100)/100.0;
						double y = this.map_keywordAndIdAndWeight.get(key).get(String.valueOf(i+1));
						System.out.println(x);
						System.out.println(y);
						dots[i] += x * y;
						System.out.println(dots[i]);
					}
				}
			}
			System.out.println("dot[" + i + "] : " + dots[i]);
			// 현재 i값과 내적을 해쉬맵으로 저장
			hm.put(this.bufs_title[i], dots[i]);
		}
		
		// 결과 출력
		System.out.println("<< 상위 3개 title >>");
		
		// Map.Entry 리스트 작성
		List<Entry<String, Double>> list_entries = new ArrayList<Entry<String, Double>>(hm.entrySet());

		
		// 비교함수 Comparator를 사용하여 List를 내림 차순으로 정렬
		Collections.sort(list_entries, new Comparator<Entry<String, Double>>() {
			// compare로 값을 비교
			public int compare(Entry<String, Double> obj1, Entry<String, Double> obj2) {
				// 내림 차순으로 정렬
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});

		
		int cnt = 0;
		for(Entry<String, Double> entry : list_entries) {
			if(cnt++ == 3) {
				break;
			}
			System.out.println(entry.getKey() + " : " + Math.round(entry.getValue()*100)/100.0);
			
		}
	}
	
}
