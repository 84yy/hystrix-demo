package com.hzm.hystrix.demo.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Huang Zengmeng
 */
public class HelloWorld {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Future<String> future = new HelloWorldCommand("normal").queue();
        String s1 = future.get();
        System.out.println(sdf.format(new Date()) + "->" + s1);

        for (int i = 100; i > 0; i--) {
            new HiWorldCommand("TimeoutCommand").queue();
            System.out.println("================ " + i + " ==================");
        }
    }
}