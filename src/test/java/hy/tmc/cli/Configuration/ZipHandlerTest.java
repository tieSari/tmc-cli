package hy.tmc.cli.Configuration;

import hy.tmc.cli.testhelpers.FileWriterHelper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class ZipHandlerTest {

    ZipHandler handler;
    FileWriterHelper helper = new FileWriterHelper();
    String testZipPath = "testResources/test.zip";
    String unzipPath = "testResources/unzips";
    String javaFile = unzipPath + "/viikko1/Viikko1_004.Muuttujat/src/Muuttujat.java";

    @Before
    public void setup() {
        handler = new ZipHandler(testZipPath, unzipPath);
    }

    @After
    public void teardown() {
        try {
            FileUtils.deleteDirectory(new File(unzipPath));
        }
        catch (IOException ex) {
            fail("Failed to clear test directory");
        }
        new File(unzipPath).mkdir();
    }

    @Test
    public void findsFile() {
        assertTrue(new File(testZipPath).exists());
    }

    @Test
    public void oneFileUnzips() {
        try {
            handler.unzip();
            assertTrue(new File(unzipPath + "/viikko1").exists());
            assertTrue(new File(unzipPath + "/viikko1/Viikko1_004.Muuttujat").exists());
            assertTrue(new File(unzipPath + "/viikko1/Viikko1_004.Muuttujat/lib").exists());
        }
        catch (IOException ex) {
            fail("failed to unzip");
        }
        catch (net.lingala.zip4j.exception.ZipException ex) {
            fail("failed to unzip");
        }
    }

    @Test
    public void sourceFolderUnzips() {
        try {
            assertFalse(new File(javaFile).exists());
            handler.unzip();
            assertTrue(new File(javaFile).exists());
        }
        catch (IOException ex) {
            fail("failed to unzip");
        }
        catch (net.lingala.zip4j.exception.ZipException ex) {
            fail("failed to unzip");
        }
    }

    @Test
    public void sourceFolderDoesntOverride() {
        try {
            handler.unzip();
            File file = new File(javaFile);
            assertTrue(file.exists());
            helper.writeStuffToFile(javaFile);
            long modified = file.lastModified();
            handler.unzip();
            assertEquals(modified, file.lastModified());
        }
        catch (IOException ex) {
            fail("failed to unzip");
        }
        catch (net.lingala.zip4j.exception.ZipException ex) {
            fail("failed to unzip");
        }
    }

    @Test
    public void otherStuffIsOverwritten() {
        try {
            handler.unzip();
            File file = new File(unzipPath + "/viikko1/Viikko1_004.Muuttujat/build.xml");
            assertTrue(file.exists());
            helper.writeStuffToFile(file.getAbsolutePath());
            long modified = file.lastModified();
            handler.unzip();
            assertNotEquals(modified, file.lastModified());
        }
        catch (IOException ex) {
            fail("failed to unzip");
        }
        catch (net.lingala.zip4j.exception.ZipException ex) {
            fail("failed to unzip");
        }
    }

    @Test
    public void setFilePathSetsPath() {
        handler.setZipPath("best");
        assertEquals("best", handler.getZipPath());
    }

    @Test
    public void setUnzipPathSetsPath() {
        handler.setUnzipLocation("best");
        assertEquals("best", handler.getUnzipLocation());
    }
    
    @Test
    public void doesntUnzipBadPath() {
        try {
            handler.setZipPath("nonexistingplace");
            handler.unzip();
            fail("did not raise exception");
        }
        catch (IOException ex) {
            fail("Didn't work");
        }
        catch (net.lingala.zip4j.exception.ZipException ex) {
            //ok
        }
        
    }

}
