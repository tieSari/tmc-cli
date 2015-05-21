package hy.tmc.cli.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * Handles unpacking zip files downloaded from TMC.
 *
 */
public class ZipHandler {

    private String zipPath;
    private String unzipDestination;
    private Path tmpPath;
    private List<String> unoverwritablePaths;

    /**
     * Creates ziphandler with specified zip path and unzip location
     *
     * @param zipSourcePath for zip to unpack
     * @param unzipLocation place to unzip to
     */
    public ZipHandler(String zipSourcePath, String unzipLocation) {
        this.zipPath = zipSourcePath;
        this.unzipDestination = unzipLocation;
        this.unoverwritablePaths = new ArrayList<>();
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
    
    private void setUnoverwritablePaths(){
        if (tmpPath.toString().isEmpty()){
            return;
        }
        File specFile = findTmcprojectYmlFile(tmpPath);
        if (specFile == null) {
            return;
        }
        String contents;
        try {
            contents = FileUtils.readFileToString(specFile);
        }
        catch (IOException ex) {
            Logger.getLogger(ZipHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, List<String>> map = (Map<String, List<String>>) yaml.load(contents);
        this.unoverwritablePaths = map.get("extra_student_files");
    }

    /**
     * Unzips zip to specified location
     *
     * @throws IOException if cannot write to file
     * @throws ZipException If specified zip is not found
     */
    public void unzip() throws IOException, ZipException {
        tmpPath = Files.createTempDirectory("tmc-temp", new FileAttribute[0]);
        ZipFile zipFile = new ZipFile(zipPath);
        zipFile.extractAll(tmpPath.toString());
        this.setUnoverwritablePaths();
        moveDirectory(tmpPath);
    }
    
    private File findTmcprojectYmlFile(Path path) {
        File file = path.toFile();
        
        if (! file.isDirectory()) {
            if (file.getName().equals(".tmcproject.yml")){
                return file;
            }
            return null;
        }
        
        File tmcproject = null;
        
        for (File f : file.listFiles()) {
            if (tmcproject != null){
                break;
            }
            tmcproject = findTmcprojectYmlFile(f.toPath());
        }
        return tmcproject;
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
        for (String unOverwritable : this.unoverwritablePaths){
            if (path.endsWith(unOverwritable)){
                return false;
            }
        }
        return !(path.contains("src") && new File(path).exists());
    }

    private String getFullDestinationPath(String filePath) {
        String relativePath = filePath.substring(tmpPath.toString().length()); 
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
