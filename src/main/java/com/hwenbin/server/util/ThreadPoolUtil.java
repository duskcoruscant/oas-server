package com.hwenbin.server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 自定义线程池
 *
 * @author hwb
 * @date 2022/04/08 16:04
 */
@Component
public class ThreadPoolUtil {

    @Value("${customize-thread-pool.core-pool-size}")
    private int corePoolSize;

    @Value("${customize-thread-pool.maximum-pool-size}")
    private int maximumPoolSize;

    private static ThreadPoolExecutor executor;

    @PostConstruct
    public void initProcessorThreadPool() {
        executor = new ThreadPoolExecutor(
                corePoolSize
                , maximumPoolSize
                // 设置空闲线程存活时间（秒）
                , 60
                , TimeUnit.SECONDS
                // 公平策略,FIFO
                , new SynchronousQueue<>(true)
                /*
                 * 拒绝处理策略
                 * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
                 * AbortPolicy()：直接抛出异常。
                 * DiscardPolicy()：直接丢弃。
                 * DiscardOldestPolicy()：丢弃队列中最老的任务。
                 */
                /*
                 * 特殊说明：
                 * 1. 这里个人项目，非实际环境，拒绝策略咱们采用抛出异常
                 * 2.真实业务场景会把缓存队列的大小会设置大一些，
                 * 如果，提交的任务数量超过最大线程数量或将任务环缓存到本地、redis、mysql中,保证消息不丢失
                 * 3.如果项目比较大的话，异步通知种类很多的话，建议采用MQ做异步通知方案
                 */
                , new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public static void submit(Runnable runnable) {
        executor.submit(runnable);
    }

}
