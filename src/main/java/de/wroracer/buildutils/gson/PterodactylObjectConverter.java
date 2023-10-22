package de.wroracer.buildutils.gson;

import com.google.gson.*;
import de.wroracer.buildutils.domain.pterodactyl.PterodactylObject;
import de.wroracer.buildutils.domain.pterodactyl.SignedURL;
import de.wroracer.buildutils.domain.pterodactyl.Stats;

import java.lang.reflect.Type;

public class PterodactylObjectConverter<T extends PterodactylObject> implements JsonDeserializer<T>, JsonSerializer<T> {
    private static Gson gson;

    public static void register(GsonBuilder builder) {
        gson = builder.create();
        builder.registerTypeAdapter(SignedURL.class, new PterodactylObjectConverter<SignedURL>());
        builder.registerTypeAdapter(Stats.class, new PterodactylObjectConverter<Stats>());
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        T ret = null;
        if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            if (object.has("object") && object.has("attributes")) {
                String objectType = object.get("object").getAsString();
                JsonElement attributes = object.get("attributes");
                ret = switch (objectType) {
                    case "signed_url" -> (T) gson.fromJson(attributes, SignedURL.class);
                    case "stats" -> (T) gson.fromJson(attributes, Stats.class);
                    default -> ret;
                };
            }
        }
        return ret;
    }

    @Override
    public JsonElement serialize(T pterodactylObject, Type type, JsonSerializationContext jsonSerializationContext) {
        if (pterodactylObject instanceof SignedURL) {
            JsonObject object = new JsonObject();
            object.addProperty("object", "signed_url");
            object.add("attributes", jsonSerializationContext.serialize(pterodactylObject));
            return object;
        } else if (pterodactylObject instanceof Stats) {
            JsonObject object = new JsonObject();
            object.addProperty("object", "stats");
            object.add("attributes", jsonSerializationContext.serialize(pterodactylObject));
            return object;
        }
        return null;
    }
}
