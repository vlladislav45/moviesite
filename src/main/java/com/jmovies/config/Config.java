package com.jmovies.config;

/**
 * @author Vladislav Enev
 */
public class Config {
    //Main resource directory
    public static final String BASE_DIR = "/static";

    //Movie stream properties
    public static final String VIDEO = BASE_DIR + "/movies/";

    /*Properties for movie response
      default byte range -> 1024
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;

    // Poster quality
    public static final float IMAGE_QUALITY = 0.3f; //default is 30 percent from 100

    /*Whitelist
      People who are banned
     */
    public static final String WHITELIST = "/whitelist.txt";

    /*User properties
        If someone's name is changed
     */
    public static final String BASE_THEME = "BASE_THEME";
    public static final String DARK_THEME = "DARK_THEME";

    /*Min & max vote for each user
      (review for every movie)
     */
    public static final double MIN_VOTE = 0.0;
    public static final double MAX_VOTE = 5.0;

    /*Token expired
        Default expire 24 hours
        It is used in JwtUtil
     */
    public static final long TOKEN_EXPIRED = 24;

    /*Company email
        It is used in EmailService
     */
    public static final String companyEmail = "omegatwentyone@gmail.com";
}
