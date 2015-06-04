package hy.tmc.cli.zipping;

import com.google.common.base.Optional;
import java.nio.file.Path;

public interface RootFinder {

    public Optional<Path> getRootDirectory(Path zipRoot);

}
