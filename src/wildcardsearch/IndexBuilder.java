/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wildcardsearch;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 *
 * @author dkarter
 */
public class IndexBuilder {
    private static final String TEST_DOCS_XML = "files/testdocs.xml";
    private static Directory dir;
    private static HashMap<String,Boolean> docSearchResults = new HashMap<String, Boolean>();
    private static final Version V = Version.LUCENE_42;
    private static final String INDEX_DIRECTORY = "index";

    
    
    public static IndexReader BuildOrLoadIndex(boolean forceBuild) {
        IndexReader ir = null;
        
        openIndexDirectory();
        
        if (forceBuild || !checkIndexDirectory()) {
            //delete existing directory to avoid appending the same text
            //File f = new File(INDEX_DIRECTORY);
            //FileUtils.deleteDirectory(INDEX_DIRECTORY);
            
            //index not found, build it
            writeIndex(new StandardAnalyzer(V));
        }
        
        
        //try to read index
        try {
            ir = DirectoryReader.open(dir);
        } catch (IOException ex) {
            System.err.println("** failed to build or load index. \n" + ex.toString());           
        }
        
        return ir;
    } 
    
    /// Check if index directory exists
    private static boolean checkIndexDirectory() {
        File f = new File(INDEX_DIRECTORY);
        return f.exists();
    }
            
    
    //open index directory so it can be loaded into memory and used as needed
    private static void openIndexDirectory() {        
        try {
            dir = FSDirectory.open(new File(INDEX_DIRECTORY));
        } catch (Exception e) {
            System.out.println("** Index dir error: " + e.toString());
        }
    }
    
    //writes the index files
    private static void writeIndex(Analyzer analyzer) {
        try {
            String filename = TEST_DOCS_XML;
            
            //prepare xml reader
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.parse(new File(filename));
            
            //Normalize text representation
            doc.getDocumentElement().normalize();
            
            NodeList docs = doc.getElementsByTagName("DOC");
            
            
            //index writer
            IndexWriter iw = new IndexWriter(dir, new IndexWriterConfig(V, analyzer));
            
            //loop docs xml and store in writer
            for (int i = 0; i<docs.getLength(); i++) {
                Node d = docs.item(i);
                if (d.getNodeType() == Node.ELEMENT_NODE) {
                    String docno = ((Element)d).getElementsByTagName("DOCNO").item(0).getChildNodes().item(0).getNodeValue().trim();
                    String text = ((Element)d).getElementsByTagName("TEXT").item(0).getChildNodes().item(0).getNodeValue().trim();
                    
                    //prepare search result list of documents
                    docSearchResults.put(docno, false);
                    
                    addDoc(iw, docno, text);
                }
            }
                      
            iw.close();
            
       } catch (SAXParseException e) {
            System.err.println("** Parsing error, line "
                    + e.getLineNumber() + ", uri " + e.getSystemId());
       } catch (Exception ex) {
            System.err.println(ex.toString());
       }
       
    }
    
    private static void addDoc(IndexWriter w, String docno, String text) 
        throws IOException {
       Document doc = new Document();
       
       
       doc.add(new StringField("docno", docno, Field.Store.YES));
       doc.add(new TextField("text", text, Field.Store.YES));
       w.addDocument(doc);
    }
}
