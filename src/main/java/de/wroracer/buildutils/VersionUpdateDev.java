package de.wroracer.buildutils;

import de.wroracer.buildutils.version.VersionUpdater;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.versions.SetMojo;

@Mojo(name = "version-update-dev")
public class VersionUpdateDev extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "version.updater.devPrefix", required = true)
    private String devPrefix;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String version = project.getVersion();
        String newVersion = "";

        if (version.contains("-" + devPrefix)) {
            String[] split = version.split("-" + devPrefix);
            int devVersion = Integer.parseInt(split[1]);
            devVersion++;
            newVersion = split[0] + "-" + devPrefix + devVersion;
        } else {
            version = VersionUpdater.updatePatch(version);
            newVersion = version + "-" + devPrefix + "1";
        }
        project.getProperties().setProperty("newVersion", newVersion);

        System.setProperty("newVersion", newVersion);

        getLog().info("Next Version is " + newVersion);

        //execute versions:set
        //find version:set plugin


        //update version in pom file
    }
}
