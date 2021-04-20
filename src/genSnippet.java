import java.util.StringTokenizer;

public class genSnippet extends searcher{
	
	String bufs[];
			
	public genSnippet(int docCnt) {
		super(docCnt);
		// TODO Auto-generated constructor stub
	}

	public void func1() {
		// tokenize
		int cnt = 0;
		StringTokenizer st = new StringTokenizer(this.files.toString(), "\n");
		while ( st.hasMoreTokens() ) {
			String actualElement = st.nextToken();
			if(actualElement.contains(bufs[cnt++])) {
				
			}
		}
	}
	
	public void func2(String q) {
		// tokenize
		int cnt = 0;
		StringTokenizer st = new StringTokenizer(this.files.toString(), " ");
		while ( st.hasMoreTokens() ) {
			String actualElement = st.nextToken();
			bufs[cnt++] = actualElement;
		}
	}
}