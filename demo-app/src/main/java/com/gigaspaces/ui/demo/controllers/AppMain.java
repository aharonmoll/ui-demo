package com.gigaspaces.ui.demo.controllers;

import com.gigaspaces.rest.client.java.invoker.ApiException;

public class AppMain {
    public static void main (String args[]) {
        DemoController controller = new DemoController();
        try {
            controller.terminateAndStartContainer("ProductsCatalog","ProductsCatalog~2_1", "60" );
        } catch (ApiException e) {
            e.printStackTrace();
        }

        try {
            controller.terminateAndStartContainer("Mirror","Mirror~1", "5" );
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
