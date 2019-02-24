package acceler.ocdl.utils;

import java.io.File;

public interface CommandHelper {

    void runCommand(File workspaceDirectory, String commands);

    void runCommand(File workspaceDirectory, String commands, StringBuilder stdout, StringBuilder stderrout);
}
