package org.laxio.piston.sticky;

import org.json.JSONObject;
import org.laxio.piston.piston.Piston;
import org.laxio.piston.piston.versioning.PistonModule;
import org.laxio.piston.piston.versioning.Version;
import org.laxio.piston.protocol.v001.StickyProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.laxio.piston.piston.versioning.PistonModuleType.*;

/**
 * Sets up Piston Modules
 * @see org.laxio.piston.piston.versioning.PistonModule
 */
public class StickyInitiator {

    private static final Object lock = new Object();

    /**
     * Creates PistonModules for all available
     * @see org.laxio.piston.piston.versioning.PistonModuleType
     */
    public static void setup() throws IOException {
        synchronized (lock) {
            /*
             * org.laxio.piston.ignition.json       / Impl / Ignition version     / Req: false
             * org.laxio.piston.piston.json         / Spec / Piston API version   / Req: true
             * org.laxio.piston.sticky.json         / Impl / StickyPiston version / Req: true
             * org.laxio.piston.protocol.{ver}.json / Impl / Protocol version     / Req: true
             * org.laxio.piston.protocol.{ver}.json / Spec / Minecraft version    / Req: true
             */

            try {
                JSONObject json = getJSON("/org.laxio.piston.ignition.json");
                PistonModule ignition = build(json, "Implementation");
                IGNITION.setModule(ignition);
            } catch (Exception ex) {
                // ignore, not required
            }

            JSONObject json = getJSON(Piston.class);
            PistonModule piston = build(json, "Specification");
            PISTON.setModule(piston);

            json = getJSON(StickyPistonServer.class);
            PistonModule stickypiston = build(json, "Implementation");
            STICKYPISTON.setModule(stickypiston);

            json = getJSON(StickyProtocol.class);
            PistonModule protocol = build(json, "Implementation");
            PROTOCOL.setModule(protocol);

            json = getJSON(StickyProtocol.class);
            PistonModule minecraft = build(json, "Specification");
            MINECRAFT.setModule(minecraft);
        }
    }

    /**
     * Builds a JSON object based on the package name of the supplied class
     * @param cls The class to get the package name from
     * @return The JSON object for that class
     * @throws IOException
     */
    private static JSONObject getJSON(Class<?> cls) throws IOException {
        return getJSON("/" + cls.getPackage().getName() + ".json");
    }


    /**
     * Builds a JSON object using the supplied path
     * @param path The path
     * @return The JSON object for the supplied path
     * @throws IOException
     */
    private static JSONObject getJSON(String path) throws IOException {
        return new JSONObject(read(StickyInitiator.class.getResourceAsStream(path)));
    }

    /**
     * Builds a module using the supplied JSON object and tag
     * @param json The JSON object to look in
     * @param tag The prefix tag, usually Specification or Implementation
     * @return A constructed module for the supplied parameters
     */
    private static PistonModule build(JSONObject json, String tag) {
        String title = json.getString(tag + "-Title");
        String version = json.getString(tag + "-Version");
        String vendor = json.getString(tag + "-Vendor");

        Version ver = Version.parse(version);
        return new PistonModule(title, ver, vendor);
    }

    /**
     * Translates a stream into a single string
     * @param input The stream to translate
     * @return The complete string
     * @throws IOException
     */
    private static String read(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

}
