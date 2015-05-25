package hy.tmc.cli.zipping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

public class DefaultMoveDecider implements MoveDecider {

    protected List<String> additionalStudentFiles;
    private final ProjectRootFinder finder;

    /**
     * Default movedecider, which decides whether something can be overwritten
     * in a Maven or an Ant project
     * @param detector 
     */
    public DefaultMoveDecider(ProjectRootDetector detector) {
        this.finder = new ProjectRootFinder(detector);
        this.additionalStudentFiles = new ArrayList<>();
    }

    /**
     * Decide whether this file should be moved or not, that is, does it contain
     * work by students. Studentfiles are all files in the source directory, in this case
     * src or src/main. Also files specified by .tmcproject.yml in the project
     * root are studentfiles
     * 
     * @param path path of the file
     * @return true iff this file should be overwritten 
     */
    @Override
    public boolean shouldMove(String path) {
        for (String unOverwritable : this.additionalStudentFiles) {
            if (path.endsWith(unOverwritable)) {
                return false;
            }
        }
        return !(path.contains("src") && new File(path).exists());
    }
    
    /**
     * Find and read .tmcproject.yml. The ziphandler will invoke this method
     * 
     * @param tmpPath path that the project has been unzipped to initially
     */
    @Override
    public void readTmcprojectYml(Path tmpPath) {
        if (tmpPath.toString().isEmpty()) {
            return;
        }
        Path root = finder.getRootDirectory(tmpPath);
        File specFile = findTmcprojectYmlFile(root);
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
        this.additionalStudentFiles = map.get("extra_student_files");
    }

    /**
     * Try to find .tmcproject.yml
     * 
     * @param path path of the root directory
     * @return the file, or null if it is not found
     */
    private File findTmcprojectYmlFile(Path path) {
        File dir = path.toFile();
        for (File file : dir.listFiles()) {
            if (file.getName().equals(".tmcproject.yml")) {
                return file;
            }
        }
        return null;
    }
}
