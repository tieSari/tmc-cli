package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.FileAttribute;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Handles unpacking zip files downloaded from TMC.
 * 
 */

public class ZipHandler {

    private String zipPath;
    private String unzipDestination;
    private Path tmpPath;

    /**
     * Creates ziphandler with specified zip path and unzip location
     * @param path for zip to unpack
     * @param unzipLocation place to unzip to
     */
    public ZipHandler(String path, String unzipLocation) {
        this.zipPath = path;
        this.unzipDestination = unzipLocation;
    }

    public String getUnzipLocation() {
        return unzipDestination;
    }

    public void setUnzipLocation(String unzipDestination) {
        this.unzipDestination = unzipDestination;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }
    
    /**
     * Unzips zip to specified location
     * @throws IOException if cannot write to file
     * @throws ZipException If specified zip is not found
     */

    public void unzip() throws IOException, ZipException {
       
            tmpPath = Files.createTempDirectory("tmc-temp", new FileAttribute[0]);
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(tmpPath.toString());
            moveDirectory(tmpPath);
        }
      
  

    private void moveDirectory(Path path) throws IOException {
        File directory = path.toFile();
        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                new File(getFullDestinationPath(f.getAbsolutePath())).mkdir();
                moveDirectory(f.toPath());
            } else {
                moveFileToDestination(f.getAbsolutePath());
            }
        }
    }

    private boolean isOverwritable(String path) {
        return !(path.contains("src") && new File(path).exists());
    }
    
    private String getFullDestinationPath(String filePath){
        String relativePath = filePath.substring(tmpPath.toString().length()); // remove /tmp/yadayada.../
        return unzipDestination + relativePath;
        
    }

    private void moveFileToDestination(String filePath) {
        String realPath = getFullDestinationPath(filePath);
        if (isOverwritable(realPath)) {
            writeFile(filePath, realPath);
        }
    }

    private void writeFile(String src, String dest) {
        try {
            Files.move(Paths.get(src), Paths.get(dest), REPLACE_EXISTING);
        }
        catch (IOException e) {
            System.out.println();
        }
    }

}
