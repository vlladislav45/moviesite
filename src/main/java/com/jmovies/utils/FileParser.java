package com.jmovies.utils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static com.jmovies.config.Config.*;

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
        return instance;
    }

    public void parseBannedIPAddresses(List<String> bannedIPs) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                bannedIPs.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBannedIPAddress(List<String> ipAddresses) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file,true));

            /*
             It must be TESTED
             */
            for(int i = 0; i < ipAddresses.size(); i++) {
                if (instance.checkIPIfNotExist(ipAddresses.get(i))) writer.write("\n" + ipAddresses.get(i));
            }

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
