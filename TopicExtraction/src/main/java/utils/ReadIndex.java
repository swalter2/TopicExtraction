package utils;



import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;



public class ReadIndex {

	private final Analyzer analyzer;
        private final  IndexReader reader ;
        private final  IndexSearcher searcher;

	
	public ReadIndex(String pathToIndex) throws IOException{
                this.analyzer = new EnglishAnalyzer();
                this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(pathToIndex)));
                this.searcher = new IndexSearcher(reader);
                
	}
	
	
        
        public Set<String> runSearch(List<String> terms)
			throws IOException {
            Set<String> sentences = new HashSet<>();
            try {

                BooleanQuery booleanQuery = new BooleanQuery();
                for(String term : terms){
                    booleanQuery.add(new QueryParser("plain", analyzer).parse("\""+term.toLowerCase()+"\""), BooleanClause.Occur.MUST);
                }
                
                System.out.println(booleanQuery.toString());
                    
                
                int hitsPerPage = 99999;
		    
		    
	        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
	        searcher.search(booleanQuery, collector);
	        
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          ArrayList<String> result = new ArrayList<>();
	          String sentence = d.get("parsed");
                  sentences.add(sentence);
                }
		}
            catch(Exception e){
		System.out.println("Error in term: "+terms.toString());
            }
		
		return sentences;
	}
        

}
