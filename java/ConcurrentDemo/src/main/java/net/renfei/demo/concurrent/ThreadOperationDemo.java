package net.renfei.demo.concurrent;

/**
 * 线程操作演示
 *
 * @author renfei
 */
public class ThreadOperationDemo {
    static Thread mainThread = Thread.currentThread();

    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        // ======= 线程的命名操作 =======
        Thread threadA = new Thread(myThread, "线程A");
        Thread threadB = new Thread(myThread);
        Thread threadC = new Thread(myThread, "线程C");
        // ======= 线程的优先级操作 =======
        threadC.setPriority(Thread.MAX_PRIORITY);
        threadA.start();
        threadB.start();
        threadC.start();
        Thread.sleep(100);
        // 判断 threadA 是否被中断
        if (!threadA.isInterrupted()) {
            // 中断 threadA 的线程，线程的中断操作
            threadA.interrupt();
        }
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("[main]主线程的执行 i = " + i);
        }
    }

    static class MyThread implements Runnable {
        @Override
        public void run() {
            // 通过 Thread.currentThread() 获取当前线程
            System.out.println("当前线程名称：" + Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                System.out.println("当前线程名称：" + Thread.currentThread().getName() + " for i = " + i);
                try {
                    // 让线程休眠 1 秒
                    Thread.sleep(1000);
                    if (i > 3) {
                        // join 操作，如果 i 大于 3 让主线程先执行
                        System.out.println("线程join，当前线程：" + Thread.currentThread().getName());
                        mainThread.join();
                    }
                    if (i == 6) {
                        // 线程 yield 礼让操作
                        System.out.println("线程yield：" + Thread.currentThread().getName());
                        Thread.yield();
                    }
                } catch (InterruptedException e) {
                    // 线程中断异常
                    System.out.println("！！！当前线程：" + Thread.currentThread().getName() + "被中断");
                }
            }
        }
    }
}
