package com.smeup.kokos;

import org.apache.camel.main.Main;

public class FolderMonitor {

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.configure().addRoutesBuilder(new FolderMonitorRoute());

        System.out.println("Starting Camel context...");
        main.run(args);

    }
}
