package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.FileAttribute;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ZipHandler {

    private String zipPath;
    private String unzipDestination;
    private Path tmpPath;

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

    public void unzip() {
        try {
            tmpPath = Files.createTempDirectory("tmc-temp", new FileAttribute[0]);
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(tmpPath.toString());
            moveDirectory(tmpPath);
        }
        catch (ZipException | IOException ex) {
            Logger.getLogger(ZipHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void moveDirectory(Path path) throws IOException {
        File directory = path.toFile();
        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                makeDir(f.getAbsolutePath());
                moveDirectory(f.toPath());
            } else {
                moveFileToDestination(f.getAbsolutePath());
            }
        }
    }

    private boolean isOverwritable(String path) {
        return !(path.contains("src") && new File(path).exists());
    }
    
    private void makeDir(String path) {
        String relativePath = path.substring(tmpPath.toString().length()); // remove /tmp/yadayada.../
        String realPath = unzipDestination + relativePath;
        new File(realPath).mkdir();
    }

    private void moveFileToDestination(String filePath) {
        String relativePath = filePath.substring(tmpPath.toString().length()); // remove /tmp/yadayada.../
        String realPath = unzipDestination + relativePath;
        if (isOverwritable(realPath)) {
            writeFile(filePath, realPath);
            System.out.println("moving: " + relativePath);
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
