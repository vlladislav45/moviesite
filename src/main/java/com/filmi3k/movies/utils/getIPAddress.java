package com.filmi3k.movies.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static com.filmi3k.movies.config.Config.CHECK_IP;

public class getIPAddress {

    public static String getIp() throws Exception {
        URL whatsmyip = new URL(CHECK_IP);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(whatsmyip.openStream()));
            String ip = bufferedReader.readLine();

            return ip;
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
