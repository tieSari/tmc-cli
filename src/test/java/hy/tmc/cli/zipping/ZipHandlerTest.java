package hy.tmc.cli.zipping;

import hy.tmc.cli.testhelpers.FileWriterHelper;
import java.io.File;
import java.io.IOException;
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
    String projectPath = unzipPath + "/viikko1/Viikko1_001.Nimi";
    String javaFile = projectPath + "/src/Nimi.java";
    MoveDecider decider;

    public ZipHandlerTest() {
        decider = new DefaultMoveDecider();
    }

    @Before
    public void setup() {
        handler = new ZipHandler(testZipPath, unzipPath, decider);
    }

    @After
    public void teardown() {
        final File file = new File(unzipPath);
        System.out.println(file.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            fail("Failed to clear test directory");
        }
        file.mkdir();
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
            assertTrue(new File(unzipPath + "/viikko1/Viikko1_001.Nimi/src").exists());
            assertTrue(new File(unzipPath + "/viikko1/Viikko1_001.Nimi/lib").exists());
        }
        catch (Exception ex) {
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
        catch (Exception ex) {
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
        catch (Exception ex) {
            fail("failed to unzip");
        }
    }

    @Test
    public void otherStuffIsOverwritten() {
        try {
            handler.unzip();
            File file = new File(unzipPath + "/viikko1/Viikko1_001.Nimi/build.xml");
            assertTrue(file.exists());
            helper.writeStuffToFile(file.getAbsolutePath());
            long modified = file.lastModified();
            handler.unzip();
            assertNotEquals(modified, file.lastModified());
        }
        catch (Exception ex) {
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
    /* @Test
     public void doesntOverwriteSomethingInTmcprojectYml() {
     try {
     handler.unzip();
     }
     catch (IOException | net.lingala.zip4j.exception.ZipException ex) {
     Logger.getLogger(ZipHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
     fail("Exception thrown by unzip");
     }
        
     helper.writeStuffToFile(unzipPath+ "/sdf/sdf.txt");
     }*/
}
