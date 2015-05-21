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

    protected List<String> unoverwritablePaths;
    private ProjectRootFinder finder;

    public DefaultMoveDecider(ProjectRootDetector detector) {
        this.finder = new ProjectRootFinder(detector);
        this.unoverwritablePaths = new ArrayList<>();
    }

    @Override
    public boolean shouldMove(String path) {
        for (String unOverwritable : this.unoverwritablePaths) {
            if (path.endsWith(unOverwritable)) {
                return false;
            }
        }
        return !(path.contains("src") && new File(path).exists());
    }

    private void setUnoverwritablePaths(Path tmpPath) {
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
        this.unoverwritablePaths = map.get("extra_student_files");
    }

    @Override
    public void readTmcprojectYml(Path zipRoot) {
        this.setUnoverwritablePaths(zipRoot);
    }

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
