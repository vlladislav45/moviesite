package com.filmi3k.movies.utils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static com.filmi3k.movies.config.Config.*;

public class FileParser {
    private static FileParser instance = null;

    private final URL url = getClass().getResource(WHITELIST);

    private final File file = new File(url.getFile());


    public FileParser() { }

    public static FileParser initialize() {
        if(instance == null) {
            instance = new FileParser();

            return instance;
        }
        return null;
    }

    public void parseBannedIPAddresses(List<String> bannedIPs) throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                bannedIPs.add(scanner.nextLine());
            }
        }  finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addBannedIPAddress(String ipAddress) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file,true));

            if(instance.checkIPIfNotExist(ipAddress)) writer.write("\n" + ipAddress);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkIPIfNotExist(String ipAddress) throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                if(ipAddress.equals(scanner.next())) {
                    return false;
                }
            }
        }  finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
