package com.gigaspaces.web_application.web;

import com.gigaspaces.annotation.SupportCodeChange;
import org.openspaces.core.executor.Task;
import org.openspaces.core.executor.TaskRoutingProvider;

@SupportCodeChange(id="1")
public class CPUAlertTask implements Task<Integer>, TaskRoutingProvider {
    private int value;

    public CPUAlertTask(int value) {
        this.value = value;
    }

    public Integer execute() {
        System.out.println("CPU: execute version1");
        int i= 0;
        while(true){
           // System.out.println("value is" + i);
            /*try {
                //Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //++i;
            //i = i+2;
        }
    }

    public Integer getRouting() {
        return this.value;
    }
}
