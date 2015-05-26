package hy.tmc.cli.zipping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipperTest {

    Zipper zipper;
    String mockPath = "testResources/mockProject";
    String mockUnzipPath = "testResources/mockProject.zip";

    @Before
    public void setUp() {
        zipper = new Zipper();
    }

    @After
    public void tearDown() {
        new File(mockUnzipPath).delete();
    }

    /**
     * Test of zip method, of class Zipper.
     */
    @Test
    public void zipsFolder() {
        try {
            zipper.zip(mockPath, mockUnzipPath);
            ZipFile file = new ZipFile(mockUnzipPath);
            List<FileHeader> filesInZip = file.getFileHeaders();
            List<String> names = new ArrayList();

            for (FileHeader header : filesInZip) {
                names.add(header.getFileName());
            }
            assertEquals(5, names.size());
            assertTrue(names.contains("mockProject/root/.tmcproject.yml"));
            assertTrue(names.contains("mockProject/root/"));

        }
        catch (ZipException ex) {
            fail("Couldn't zip");
        }
    }

    @Test
    public void overridesPreviousExistingZip() throws IOException {
        String newFileName = "best.txt";
        try {
            zipper.zip(mockPath, mockUnzipPath);

            ZipFile zip = new ZipFile(mockUnzipPath);
            File newFile = new File(newFileName);
            newFile.createNewFile();
            zip.addFile(newFile, new ZipParameters());

            assertEquals(newFileName, zip.getFileHeader(newFileName).getFileName());
            assertTrue(new File(mockUnzipPath).exists());

            zipper.zip(mockPath, mockUnzipPath);
            zip = new ZipFile(mockUnzipPath);
            FileHeader header = zip.getFileHeader(newFileName);
            assertEquals(newFileName, header.getFileName());

        }
        catch (ZipException ex) {
            for (Path file : Files.newDirectoryStream(Paths.get("testResources/"))) {
                System.out.println(file.getFileName());
            }
            fail(ex.getMessage());
        }

    }
}
