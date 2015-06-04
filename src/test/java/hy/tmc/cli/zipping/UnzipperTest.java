package hy.tmc.cli.zipping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import hy.tmc.cli.testhelpers.FileWriterHelper;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UnzipperTest {

    Unzipper handler;
    FileWriterHelper helper = new FileWriterHelper();
    String testZipPath = "testResources/test.zip";
    String unzipPath = "testResources/unzips";
    String projectPath = unzipPath + "/viikko1/Viikko1_001.Nimi";
    String javaFile = projectPath + "/src/Nimi.java";
    UnzipDecider decider;

    public UnzipperTest() {
        decider = new DefaultUnzipDecider();
    }

    @Before
    public void setup() {
        handler = new Unzipper(testZipPath, unzipPath, decider);
    }

    /**
     * Deletes files used in tests.
     */
    @After
    public void teardown() throws IOException {
        final File file = new File(unzipPath);
        FileUtils.deleteDirectory(file);
        file.mkdir();
    }

    @Test
    public void findsFile() {
        assertTrue(new File(testZipPath).exists());
    }

    @Test
    public void oneFileUnzips() throws IOException, ZipException {
        handler.unzip();
        assertTrue(new File(unzipPath + "/viikko1").exists());
        assertTrue(new File(unzipPath + "/viikko1/Viikko1_001.Nimi/src").exists());
        assertTrue(new File(unzipPath + "/viikko1/Viikko1_001.Nimi/lib").exists());
    }

    @Test
    public void sourceFolderUnzips() throws IOException, ZipException {
        assertFalse(new File(javaFile).exists());
        handler.unzip();
        assertTrue(new File(javaFile).exists());
    }

    @Test
    public void sourceFolderDoesntOverride() throws ZipException, IOException {
        handler.unzip();
        File file = new File(javaFile);
        assertTrue(file.exists());
        helper.writeStuffToFile(javaFile);
        long modified = file.lastModified();
        handler.unzip();
        assertEquals(modified, file.lastModified());
    }

    @Test
    public void otherStuffIsOverwritten() throws IOException, ZipException {
        handler.unzip();
        File file = new File(unzipPath + "/viikko1/Viikko1_001.Nimi/build.xml");
        assertTrue(file.exists());
        helper.writeStuffToFile(file.getAbsolutePath());
        long modified = file.lastModified();
        handler.unzip();
        assertNotEquals(modified, file.lastModified());
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
        } catch (IOException ex) {
            fail("Didn't work");
        } catch (net.lingala.zip4j.exception.ZipException ex) {
            //ok
        }
    }

    @Test
    public void doesntOverwriteSomethingInTmcprojectYml() throws IOException, ZipException {
        String studentTestFile = projectPath + "/test/StudentTest.java";

        handler.unzip();

        helper.writeStuffToFile(studentTestFile);
        long lastMod = new File(studentTestFile).lastModified();
        handler.unzip();

        assertEquals(lastMod, new File(studentTestFile).lastModified());

    }

}
