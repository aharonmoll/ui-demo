package com.gigaspaces.ui.demo.controllers;


import com.gigaspaces.rest.client.java.invoker.ApiException;

public class AppMain {
    public static void main (String args[]) {
        DemoController controller = new DemoController();
        /*try {
            System.out.println(controller.markInstanceUnavailable("ProductsCatalog","ProductsCatalog~2_2", 30 ));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/
        /*try {
            System.out.println(controller.markInstanceUnavailable("Mirror","Mirror~1", 30 ));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/

       /* try {
            System.out.println(controller.triggerCPUAlertOnService("ProductsCatalog", 60));
        } catch (ApiException e) {
            e.printStackTrace();
        }*/

        try {
            System.out.println(controller.triggerMemoryAlertOnService("ProductsCatalog", 90));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
