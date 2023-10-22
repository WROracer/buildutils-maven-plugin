package de.wroracer.buildutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wroracer.buildutils.domain.pterodactyl.PowerState;
import de.wroracer.buildutils.domain.pterodactyl.Stats;
import de.wroracer.buildutils.gson.PterodactylObjectConverter;
import de.wroracer.buildutils.rest.PterodactylService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;

@Mojo(name = "pterodactyl-release", defaultPhase = LifecyclePhase.PACKAGE)
public class PterodactylRelease extends AbstractMojo {

    private final String tempAPIKey = "ptlc_GxUJzk93XNi3X41fwOdEJf8tmcFqmzJsO6Ua0Ac2XBI";
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "pterodactyl.url", required = true)
    private String petrodactylUrl;

    @Parameter(property = "pterodactyl.key", required = true)
    private String petrodactylKey;

    @Parameter(property = "pterodactyl.server", required = true)
    private String petrodactylServer;

    @Parameter(property = "pterodactyl.folder")
    private String petrodactylFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File packageFile = project.getArtifact().getFile();
        if (packageFile == null) {
            getLog().error("No package file found in this build. Aborting...");
            return;
        }

        getLog().info("Found package file: " + packageFile.getAbsolutePath());

        String apiKey = "Bearer " + petrodactylKey;

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        PterodactylObjectConverter.register(builder);

        if (apiKey == null || apiKey.isEmpty()) {
            getLog().error("No API Key found. Please set the pterodactyl.key property in your pom.xml");
            return;
        }
        if (petrodactylServer == null || petrodactylServer.isEmpty()) {
            getLog().error("No Server ID found. Please set the pterodactyl.server property in your pom.xml");
            return;
        }
        if (petrodactylUrl == null || petrodactylUrl.isEmpty()) {
            getLog().error("No URL found. Please set the pterodactyl.url property in your pom.xml");
            return;
        }
        if (petrodactylFile == null) {
            petrodactylFile = "";
        }

        Gson gson = builder.create();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(petrodactylUrl)
                .build();

        PterodactylService service = retrofit.create(PterodactylService.class);

        //Check if server is Online
        boolean isOnline = false;
        try {
            Response<Stats> res = service.getStats(apiKey, petrodactylServer).execute();
            if (!res.isSuccessful()) {
                getLog().error("Failed to get server stats: " + res.errorBody().string());
            } else {
                assert res.body() != null;
                isOnline = res.body().getServerState().equals("running") || res.body().getServerState().equals("starting");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean wasOnline = isOnline;
        if (isOnline) {
            getLog().info("Stopping server...");
            try {
                Response<Void> res = service.changePowerState(apiKey, petrodactylServer, PowerState.STOP).execute();
                if (!res.isSuccessful()) {
                    getLog().error("Failed to stop server: " + res.errorBody().string());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int serverOfflineTry = 0;

            while (isOnline) {
                try {
                    Response<Stats> res = service.getStats(apiKey, petrodactylServer).execute();
                    if (!res.isSuccessful()) {
                        getLog().error("Failed to get server stats: " + res.errorBody().string());
                    } else {
                        assert res.body() != null;
                        isOnline = res.body().getServerState().equals("running") || res.body().getServerState().equals("starting");
                    }
                    //Sleep for 5 seconds
                    Thread.sleep(5000);
                    serverOfflineTry++;
                    getLog().info("Server is still online. Waiting 5 seconds... (" + serverOfflineTry + "/10)");
                    if (serverOfflineTry > 10) {
                        getLog().error("Server is still online after 10 tries. Aborting...");
                        return;
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        String fileContent = FileUtils.readFile(packageFile);

        try {
            getLog().info("Uploading to Pterodactyl...");
            Response<Void> res2 = service.writeFile(apiKey, petrodactylServer, petrodactylFile + "/" + packageFile.getName(), fileContent).execute();
            if (!res2.isSuccessful()) {
                getLog().error("Failed to upload file: " + res2.errorBody().string());
            }

            //check if server is stopped

            if (wasOnline) {
                Thread.sleep(5000);
                getLog().info("Starting server...");

                Response<Void> res3 = service.changePowerState(apiKey, petrodactylServer, PowerState.START).execute();
                if (!res3.isSuccessful()) {
                    getLog().error("Failed to start server: " + res3.errorBody().string());
                }
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        getLog().info("Done!");

    }
}
