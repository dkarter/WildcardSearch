/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wildcardsearch;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


/**
 *
 * @author dkarter
 */
public class Searcher {
    private IndexReader ir;
    private IndexSearcher is;
    
    public Searcher(IndexReader ir) {
        if (ir != null) {
            this.ir = ir;
            this.is = new IndexSearcher(ir);
        }
    }
    
    public void searchAndPrint(String wildcardQuery, int results) {
        TopDocs hits = search(wildcardQuery, results);
        
        System.out.println("Total Hits (Documents): " + hits.totalHits);
        System.out.println("Top 10 Documents:");
        
        // List results
        for (ScoreDoc sd : hits.scoreDocs) {
            try {
                Document doc = is.doc(sd.doc);
                System.out.println(doc.get("docno"));
            } catch (IOException ex) {
                System.err.println("** Searching failed: " + ex.toString());
            }
        }
        
    }
    
    // ** Note: RegEx in lucene has incomplete support (e.g. \d not working): 
    // http://stackoverflow.com/questions/9332343/what-regular-expression-features-are-supported-by-solr-edismax
    // http://lucene.apache.org/core/4_2_1/core/org/apache/lucene/util/automaton/RegExp.html
    public String getRegExQuery(String wildcardQuery) {
        return wildcardQuery
                .replace("@", "[A-Za-z]")
                .replace("#", "[0-9]")
                .replace("_", "[A-Za-z0-9]")
                .replace("*", ".*?");
    }

    public TopDocs search(String wildcardQuery, int results) {
        String regexQuery = getRegExQuery(wildcardQuery);
        RegexpQuery rq = new RegexpQuery(new Term("text", regexQuery));
        TopDocs hits = null;
        
        try {   
            hits = is.search(rq, results);
        } catch (IOException ex) {
            System.err.println("** Searching failed: " + ex.toString());            
        }
        
        return hits;
    }
}
