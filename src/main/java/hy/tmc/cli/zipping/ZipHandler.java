package hy.tmc.cli.zipping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

/**
 * Handles unpacking zip files downloaded from TMC.
 */
public class ZipHandler {

    private String zipPath;
    private String unzipDestination;
    private MoveDecider movedecider;
    private String specFileName = ".tmcproject.yml";

    /**
     * Creates ziphandler with specified zip path and unzip location
     *
     * @param zipSourcePath for zip to unpack
     * @param unzipLocation place to unzip to
     * @param movedecider a class which helps decide which files may be overwritten
     */
    public ZipHandler(String zipSourcePath, String unzipLocation, MoveDecider movedecider) {
        this.zipPath = zipSourcePath;
        this.unzipDestination = unzipLocation;
        this.movedecider = movedecider;
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

    private void extractYml(ZipFile zipFile) throws ZipException {
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();

        for (FileHeader fileHeader : fileHeaders) {
            if (fileHeader.getFileName().endsWith(specFileName)) {
                zipFile.extractFile(fileHeader, unzipDestination);
                this.movedecider.readTmcprojectYml(Paths.get(unzipDestination + "/" + fileHeader.getFileName()));
            }
        }
    }

    /**
     * Unzips zip to specified location
     *
     * @throws IOException if cannot write to file
     * @throws ZipException If specified zip is not found
     */
    public void unzip() throws IOException, ZipException {

        ZipFile zipFile = new ZipFile(zipPath);
        extractYml(zipFile);

        List<FileHeader> fileHeaders = zipFile.getFileHeaders();

        for (FileHeader fileHeader : fileHeaders) {
            String fullFileName = unzipDestination + "/" + fileHeader.getFileName();
            if (movedecider.canBeOverwritten(fullFileName)) {
                zipFile.extractFile(fileHeader, unzipDestination);
            }
        }
    }
}
