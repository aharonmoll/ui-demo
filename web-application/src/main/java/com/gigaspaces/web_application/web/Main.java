package com.gigaspaces.web_application.web;


public class Main {
    public static void main(String[] args) {
        WebApplication webApp = new WebApplication("space", "efratGroup");
        //webApp.doCPUAlert();

       webApp.doRAMAlert();
    }
}
