package com.hzm.hystrix.demo.command;


import com.netflix.hystrix.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Huang Zengmeng
 */
public class HelloWorldCommand extends HystrixCommand<String> {


    private final String name;

    /**
     * 定义构造函数，参数即被包装方法的输入参数
     *
     * @param name
     */
    public HelloWorldCommand(String name) {
        //定义命令组 和 方法调用超时时间
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorldCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldCommand"))
                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10).withKeepAliveTimeMinutes(100).withMaxQueueSize(15).withQueueSizeRejectionThreshold(100))
                //设置默认超时时间
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100))
        );
        this.name = name;
    }

    /**
     * 封装业务逻辑的方法体，在这里执行真正的业务逻辑。
     *
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        System.out.println(Thread.currentThread().getName());
        //例子中，模拟执行超时
        if (name.equals("TimeoutCommand")) {
            System.out.println("before sleep" + new Date());
            TimeUnit.SECONDS.sleep(10000);
            System.out.println("after sleep" + new Date());
        }
        //例子中，模拟出现异常
        if (name.equals("ExceptionCommand")) {
            throw new Exception("ExceptionCommand");
        }

        //例子中，模拟正常返回
        return "Hello " + name + "!";
    }

    /**
     * 降级方法定义，即，run方法中出现异常后应该执行的方法。包括例子中的超时。
     *
     * @return
     */
    @Override
    protected String getFallback() {
        return "Command failed!" + name;
    }
}