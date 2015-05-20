package hy.tmc.cli.testhelpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import static org.junit.Assert.fail;

public class FileWriterHelper {

    FileWriter writer;

    public void writeStuffToFile(String path) {
        try {
            writer = new FileWriter(new File(path));
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                writer.append(random.nextBoolean()+ "\n");
            }
        }
        catch (IOException ex) {
            fail("writer failed to init");
        }
    }
}
