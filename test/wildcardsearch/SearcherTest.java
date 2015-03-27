/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wildcardsearch;

import java.lang.reflect.Method;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.misc.Regexp;

/**
 *
 * @author dkarter
 */
public class SearcherTest {
    
    public SearcherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class Searcher.
     */
    @Test
    public void testSearch() {
//        System.out.println("search");
//        String wildcardQuery = "";
//        int results = 0;
//        Searcher instance = null;
//        instance.search(wildcardQuery, results);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    @Test
    public void testGetRegExQuery() {
        Searcher instance = new Searcher(null);
        String input = "*work*le";
        String expectedOutput = ".*?work.*?le";
        assertEquals(expectedOutput, instance.getRegExQuery(input));
        
        input = "N_w York";
        expectedOutput = "N[A-Za-z0-9]w York";
        assertEquals(expectedOutput, instance.getRegExQuery(input));
        
        input = "F-1#";
        expectedOutput = "F-1[0-9]";
        assertEquals(expectedOutput, instance.getRegExQuery(input));
        
        input = "SP@@ACH";
        expectedOutput = "SP[A-Za-z][A-Za-z]ACH";
        assertEquals(expectedOutput, instance.getRegExQuery(input));
        
        //combined
        input = "*de_st@@ding * L@w o_ ##";
        expectedOutput = ".*?de[A-Za-z0-9]st[A-Za-z][A-Za-z]ding .*? L[A-Za-z]w o[A-Za-z0-9] [0-9][0-9]";
        assertEquals(expectedOutput, instance.getRegExQuery(input));
        
    }
    
    @Test
    public void testRegExpZeroOrMore() {
        
        String input[] = {"Workable", "9 Attainable", "=Administrable", "unable", "able"};
        
        String regex = ".*?able";
        
        for (String s : input)
            assertTrue("0 or more characters [valid] [" + s + "]", s.matches(regex));
        
        String invalidInput[] = {"abl$e$", "Attainable1", "Administrabl4e", "unablle", "abl"};
        
                
        for (String s : invalidInput)
            assertFalse("0 or more characters [invalid] [" + s + "]", s.matches(regex));
        
                
    }
    
    @Test
    public void testRegExpPreciselyOneCharacter() {

        String input[] = {"New York", "N3w York", "NEw York"};
        
        String regex = "N[A-Za-z0-9]w York";
        
        for (String s : input)
            assertTrue("Precisely one character (number or letter) [valid] [" + s + "]", s.matches(regex));
        
        String invalidInput[] = {"Nw York", "N$w York", "Néw York", "Neew York", "N33w York"};
        
                
        for (String s : invalidInput)
            assertFalse("Precisely one character (number or letter) [invalid] [" + s + "]", s.matches(regex));
        
    }
    
    @Test
    public void testRegExpPreciselyOneNumber() {

        String input[] = {"F-182", "F-100", "F-199"};
        
        String regex = "F-1[0-9][0-9]";
        
        for (String s : input)
            assertTrue("Precisely one number (number or letter) [valid] [" + s + "]", s.matches(regex));
        
        String invalidInput[] = {"F-1", "F-1ab", "F-1--", "F-12e", "F-123456" };
        
                
        for (String s : invalidInput)
            assertFalse("Precisely one number (number or letter) [invalid] [" + s + "]", s.matches(regex));
        
    }
    
    @Test
    public void testRegExpPreciselyOneLetter() {

        String input[] = {"SPAAACH", "SPeaACH", "SPaEACH"};
        
        String regex = "SP[A-Za-z][A-Za-z]ACH";
        
        for (String s : input)
            assertTrue("Precisely one letter (number or letter) [valid] [" + s + "]", s.matches(regex));
        
        String invalidInput[] = {"SP44ACH", "SPACH", "SP--ACH", "SPaACH", "SPtoolongACH" };
        
                
        for (String s : invalidInput)
            assertFalse("Precisely one letter (number or letter) [invalid] [" + s + "]", s.matches(regex));
        
    }
}
