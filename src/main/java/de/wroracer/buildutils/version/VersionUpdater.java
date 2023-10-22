package de.wroracer.buildutils.version;

public class VersionUpdater {

    public static String updatePatch(String version) {
        String[] split = version.split("\\.");
        int patch = Integer.parseInt(split[2]);
        patch++;
        return split[0] + "." + split[1] + "." + patch;
    }
}
