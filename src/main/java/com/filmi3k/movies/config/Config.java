package com.filmi3k.movies.config;

/**
 * @author Vladislav Enev
 */
public class Config {
    //Main resource directory
    public static final String BASE_DIR = "/static";

    //Movie stream properties
    public static final String VIDEO = BASE_DIR + "/movies/";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;

    // Poster quality
    public static final float IMAGE_QUALITY = 0.3f; //default is 30 percent from 100

    //Whitelist
    public static final String WHITELIST = "/whitelist.txt";

    //User properties
    public static final String BASE_THEME = "BASE_THEME";
    public static final String DARK_THEME = "DARK_THEME";

    public Config() { }
}
