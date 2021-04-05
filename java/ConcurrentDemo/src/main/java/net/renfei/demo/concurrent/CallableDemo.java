package net.renfei.demo.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Callable 多线程初体验
 *
 * @author renfei
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable("我们实现的Callable"));
        Thread threadA = new Thread(futureTask);
        threadA.start();
        System.out.println("main 获得线程返回的值：" + futureTask.get());
    }

    static class MyCallable implements Callable<Integer> {
        private String myName;

        public MyCallable(String name) {
            this.myName = name;
        }

        @Override
        public Integer call() throws Exception {
            for (int i = 0; i < 10; i++) {
                System.out.println(this.myName + " :: 打印 i = " + i);
            }
            return 100;
        }
    }
}
