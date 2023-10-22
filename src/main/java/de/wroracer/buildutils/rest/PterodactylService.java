package de.wroracer.buildutils.rest;

import de.wroracer.buildutils.domain.pterodactyl.PowerState;
import de.wroracer.buildutils.domain.pterodactyl.SignedURL;
import de.wroracer.buildutils.domain.pterodactyl.Stats;
import retrofit2.Call;
import retrofit2.http.*;

public interface PterodactylService {

    @POST("/api/client/servers/{server}/power")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Void> changePowerState(@Header("Authorization") String key, @Path("server") String server, @Body PowerState powerState);


    @GET("/api/client/servers/{server}/files/upload")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<SignedURL> getFileUploadUrl(@Header("Authorization") String key, @Path("server") String server);

    @POST("/api/client/servers/{server}/files/write")
    Call<Void> writeFile(@Header("Authorization") String key, @Path("server") String server, @Query("file") String path, @Body String content);

    @GET("/api/client/servers/{server}/resources")
    Call<Stats> getStats(@Header("Authorization") String key, @Path("server") String server);
}
