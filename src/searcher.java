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
			System.out.println("����� key�� TF : " + k.getString() + " " + this.map_kw.get(k.getString()));
		}
	}
	
	// ���絵 ��� �޼ҵ�

	public void CalcSim() { 
		Map<String, Double> hm = new HashMap<String, Double>();
		double[] cosine_result = new double[this.N];
		double[] dots = new double[this.N];	// �ش� ������ ����
		
		// ������ ���
		for(int i = 0 ; i < this.N ; i++) {
			double w_a = 0.0;	// �и��� �� ��Ʈ ������ ���� ����ϱ� ���� ����
			double w_b = 0.0;	// �и��� �� ��Ʈ ������ ���� ����ϱ� ���� ����
			for(String key : this.map_kw.keySet()) {
				if(this.map_keywordAndIdAndWeight.containsKey(key)) {
					
					// ������ ���� ����ġ�� null�� ��� ó����
					String w = String.valueOf(this.map_keywordAndIdAndWeight.get(key).get(String.valueOf(i+1)));
					if(w == "null") {
						w_b += 0.0;	// ������ ���� �ش� Ű���尡 ��� ����ġ�� null�� ��� 0.0���� ���
						System.out.println(key + "�� " + (i+1) + "��°�� ����ġ�� ������ ���� �����ϴ�.");
						System.out.println("");
					}
					else {
						// ���� (����)
						double x = Math.round((double)this.map_kw.get(key)*100)/100.0;
						double y = this.map_keywordAndIdAndWeight.get(key).get(String.valueOf(i+1));
						dots[i] += x * y;			// ���� ������ ����
						
						// �и�� ������. �����ؼ� ���ذ��� ����
						w_a += Math.pow(x, 2);
						w_b += Math.pow(y, 2);
					}
				}
			}
			
			// �������� �и�
			double denom = Math.sqrt(w_a) * Math.sqrt(w_b);
			
			// �ڻ��� ���絵 ���
			if(denom == 0.0) {
				cosine_result[i] = 0.0;
			}
			else {
				cosine_result[i] = dots[i] / denom;
			}
			
			
			// ���� i���� �ڻ��� ���絵���� �ؽ������� ����
			hm.put(this.bufs_title[i], cosine_result[i]);
			
			// ��� ���
			System.out.println("<< ���� 3�� title >>");
			
			// Map.Entry ����Ʈ �ۼ�
			List<Entry<String, Double>> list_entries = new ArrayList<Entry<String, Double>>(hm.entrySet());

			
			// ���Լ� Comparator�� ����Ͽ� List�� ���� �������� ����
			Collections.sort(list_entries, new Comparator<Entry<String, Double>>() {
				// compare�� ���� ��
				public int compare(Entry<String, Double> obj1, Entry<String, Double> obj2) {
					// ���� �������� ����
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});
			
			int cnt = 0;
			for(Entry<String, Double> entry : list_entries) {
				double val = Math.round(entry.getValue()*100)/100.0;
				
				if(val == 0.0) {
					break;
				}
				System.out.println(entry.getKey() + " : " + val);
				
			}
		}
	}		
	public void innerProduct() {
		Map<String, Double> hm = new HashMap<String, Double>();
		
		double[] dots = new double[5];
		for(int i = 0 ; i < this.N ; i++) {
			for(String key : this.map_kw.keySet()) {
				if(this.map_keywordAndIdAndWeight.containsKey(key)) {
					System.out.println(key);
					// ���� ���ϱ�
					String w = String.valueOf(this.map_keywordAndIdAndWeight.get(key).get(String.valueOf(i+1)));
					System.out.println("value : " + w);
					if(w == "null") {
						System.out.println(key + "�� " + (i+1) + "��°�� ����ġ�� �����ϴ�.");
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
			// ���� i���� ������ �ؽ������� ����
			hm.put(this.bufs_title[i], dots[i]);
		}
		
		// ��� ���
		System.out.println("<< ���� 3�� title >>");
		
		// Map.Entry ����Ʈ �ۼ�
		List<Entry<String, Double>> list_entries = new ArrayList<Entry<String, Double>>(hm.entrySet());

		
		// ���Լ� Comparator�� ����Ͽ� List�� ���� �������� ����
		Collections.sort(list_entries, new Comparator<Entry<String, Double>>() {
			// compare�� ���� ��
			public int compare(Entry<String, Double> obj1, Entry<String, Double> obj2) {
				// ���� �������� ����
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
	

