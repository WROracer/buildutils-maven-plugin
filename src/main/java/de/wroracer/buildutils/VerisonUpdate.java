package de.wroracer.buildutils;

import de.wroracer.buildutils.version.VersionUpdater;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;


@Mojo(name = "version-update")
public class VerisonUpdate extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String version = project.getVersion();

        if (version.contains("-")) {
            version = version.split("-")[0];
        } else {
            version = VersionUpdater.updatePatch(version);
        }
        project.getProperties().setProperty("newVersion", version);
        System.setProperty("newVersion", version);

        getLog().info("Next Version is " + version);
    }
}
