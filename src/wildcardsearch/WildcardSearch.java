/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wildcardsearch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexReader;



//TODO: remove comment below
/*
 * resources:
 *  permuterm:
 *      http://sujitpal.blogspot.com/2011/10/lucene-wildcard-query-and-permuterm.html
 *  other resources:
 *      http://lucene.apache.org/core/old_versioned_docs/versions/3_5_0/api/all/org/apache/lucene/search/WildcardQuery.html
 *      http://www.jguru.com/faq/view.jsp?EID=480194
 *      http://stackoverflow.com/questions/2432486/lucene-wildcard-queries
 *
 *      http://www.ibm.com/developerworks/library/wa-lucene/         
 */

/**
 *
 * @author dkarter
 */


public class WildcardSearch {
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        IndexReader ir = IndexBuilder.BuildOrLoadIndex(false);
        Searcher s = new Searcher(ir);
        String wildcardQueries[] = {"####",
                                    "environ*",
                                    "agri@ulture",
                                    "*ment", 
                                    "*ember", 
                                    "####_", 
                                    "# percent", 
                                    "@@@@"};
        
        for (String q : wildcardQueries) {
            System.out.println();
            System.out.println("=================================================================");
            System.out.println("Wildcard query: " + q + " generated regex: " + s.getRegExQuery(q));
            s.searchAndPrint(q, 10);
            System.out.println("=================================================================");
        }
        

       
        
    }
    
   

}
