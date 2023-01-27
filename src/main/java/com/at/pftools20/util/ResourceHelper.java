package com.at.pftools20.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;

public class ResourceHelper {
    private static final Logger logger = Logger.getLogger(ResourceHelper.class);

    /**
     * @param resource resource path.
     * @return Resource path. Returns empty string if resource not found.
     */
    public static String getResource(@NotNull String resource) {
        final URL resourceURL = ResourceHelper.class.getResource(resource);

        if(resourceURL == null) {
            logger.log(Level.WARN, "Resource \"" + resource + "\" not found!");
            return "";
        }

        return resourceURL.toExternalForm();
    }
    
    public static @Nullable URL getResourceAsURL(@NotNull String resource) {
        final URL resourceURL = ResourceHelper.class.getResource(resource);

        if(resourceURL == null)
            logger.log(Level.WARN, "Resource \"" + resource + "\" not found!");

        return resourceURL;
    }
    
    public static String getResourcePath(@NotNull String resource) {
        final URL resourceURL = ResourceHelper.class.getResource(resource);

        if(resourceURL == null) {
            logger.log(Level.WARN, "Resource \"" + resource + "\" not found!");
            return "";
        }

        return resourceURL.getPath();
    }
    
    public static @Nullable InputStream getResourceAsStream(@NotNull String resource)
    {
    	return ResourceHelper.class.getResourceAsStream(resource);
    }
}
