/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.testhelpers.FrontendMock;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ListCoursesTest {

    private FrontendMock front;
    private Command list;
    

    @Before
    public void setUp() {
        front = new FrontendMock();
        list = new ListCourses(front, null);
    }
    
     @Test
    public void createNewEcho() {
       ListCourses lc = new ListCourses(front, new Logic());
       assertNotNull(lc);
    }
    
   @Test 
    public void testCheckDataSuccess() throws ProtocolException{
        ListCourses ls = new ListCourses(front, new Logic());
        ls.setParameter("", "juuh");
        try {
            ls.checkData();
        } catch(ProtocolException p) {
            fail("testCheckDataSuccess failed");
        }
    }
    
    @Test
    public void testNoAuthPrintsError() {
        ClientData.setUserData("", "");

        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("authorize first"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }
        
    }
    
    @Test
    public void testWithAuthPrintsCourses() {
        ClientData.setUserData("test", "1234");
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("tira"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }   
    }
    
    @Test
    public void testWithAuthPrintsSeveralCourses(){
        ClientData.setUserData("test", "1234");
        try {
            list.execute();
            assertTrue(front.getMostRecentLine().contains("tmc-eclipse"));
        }
        catch (ProtocolException ex) {
            Logger.getLogger(ListCoursesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("unexpected exception");
        }  
    }
    
    @Test
    public void setParameterTest(){
        list.setParameter("asdf", "bsdf");
    }
    
    @Test
    public void checkDataTest(){
        try {
            list.checkData();
        }
        catch (ProtocolException ex) {
            fail("listcourses should not throw exception from checkData");
        }
    }

}
