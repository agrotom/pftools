package com.at.pftools20.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.net.URL;

public class Helper {
    private static final Logger logger = Logger.getLogger(Helper.class);

    /**
     * @param resource resource path.
     * @return Resource path. Returns empty string if resource not found.
     */
    public static String getResource(String resource) {
        if(resource == null)
            return "";

        final URL resourceURL = Helper.class.getResource(resource);

        if(resourceURL == null) {
            logger.log(Level.WARN, "Resource \"" + resource + "\" not found!");
            return "";
        }

        return resourceURL.toExternalForm();
    }
}
