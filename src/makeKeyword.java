import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class makeKeyword extends makeCollection{
	
	KeywordExtractor ke;
	
	public void extractKeyword() {
		
		StringBuilder buf = new StringBuilder();
		// init KeywordExtractor
		this.ke = new KeywordExtractor();
		//extract keyword
		KeywordList kl = this.ke.extractKeyword(this.buf_body, true);
		//use extracted list
		for(Keyword k : kl) {
			buf.append(k.getString() + ":" + k.getCnt() + "#");
		}
		this.buf_body = buf.toString();
	}
	
}
