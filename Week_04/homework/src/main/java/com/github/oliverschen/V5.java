package com.github.oliverschen;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ck
 * Semaphore 实现，这个实现感觉不是很理想，最后使用了 join() 操作
 */
public class V5 extends AbstractBase {

    public static void main(String[] args) {
        new V5().template();
    }

    @Override
    public int asyncInvoke() throws Exception {
        Semaphore semaphore = new Semaphore(1);
        AtomicInteger result = new AtomicInteger();
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire(1);
                result.set(sum());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        });
        thread.start();
        thread.join();
        return result.get();
    }
}