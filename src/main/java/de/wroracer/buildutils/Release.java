package de.wroracer.buildutils;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "release", defaultPhase = LifecyclePhase.COMPILE)
public class Release extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String version = project.getVersion();

        System.out.println(version);

        project.setVersion(version + "-SNAPSHOT");

        File pomFile = project.getFile();
        System.out.println(pomFile.getAbsolutePath());
        //Create copy of pom

        File pomFileCopy = new File(pomFile.getAbsolutePath().replace(".xml", "-copy.xml"));

        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;
        try {
            model = reader.read(new FileReader(pomFile));
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }

        model.setVersion(version + "-SNAPSHOT");

        MavenXpp3Writer writer = new MavenXpp3Writer();
        try {
            writer.write(new FileWriter(pomFile), model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //change version in original pom

    }
}
