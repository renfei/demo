package net.renfei.demo.concurrent;

/**
 * 线程同步演示
 *
 * @author renfei
 */
public class ThreadSynchronizationDemo {
    public static void main(String[] args) throws InterruptedException {
        // ==== 售卖苹果的线程A，这是一个错误的演示，引出存在的问题 ====
        System.out.println("==== 售卖苹果的线程A，这是一个错误的演示，引出存在的问题 ====");
        SaleAppleA saleAppleA = new SaleAppleA();
        new Thread(saleAppleA, "摊贩A-A").start();
        new Thread(saleAppleA, "摊贩A-B").start();
        new Thread(saleAppleA, "摊贩A-C").start();
        // 睡眠等待 售卖苹果的线程A 执行完
        Thread.sleep(3000);
        // ==== 售卖苹果的线程B，增加了同步方法 ====
        System.out.println("==== 售卖苹果的线程B，增加了同步方法 ====");
        SaleAppleB saleAppleB = new SaleAppleB();
        new Thread(saleAppleB, "摊贩B-A").start();
        new Thread(saleAppleB, "摊贩B-B").start();
        new Thread(saleAppleB, "摊贩B-C").start();
    }

    /**
     * 售卖苹果的线程A，这是一个错误的演示，引出存在的问题
     */
    static class SaleAppleA implements Runnable {
        // 一共有 10 个苹果
        private int apple = 10;

        @Override
        public void run() {
            while (true) {
                if (this.apple > 0) {
                    try {
                        // 模拟延迟，在真实场景中这里可能是网络延迟或者业务逻辑执行延迟
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程 " + Thread.currentThread().getName() + " 售卖了苹果编号：" + this.apple--);
                } else {
                    System.out.println("线程 " + Thread.currentThread().getName() + " 售卖结束，关门了");
                    break;
                }
            }
        }
    }

    /**
     * 售卖苹果的线程B，加入同步锁机制
     */
    static class SaleAppleB implements Runnable {
        // 一共有 10 个苹果
        private int apple = 10;

        private synchronized boolean sale() {
            if (this.apple > 0) {
                try {
                    // 模拟延迟，在真实场景中这里可能是网络延迟或者业务逻辑执行延迟
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 " + Thread.currentThread().getName() + " 售卖了苹果编号：" + this.apple--);
                return false;
            } else {
                System.out.println("线程 " + Thread.currentThread().getName() + " 售卖结束，关门了");
                return true;
            }
        }

        @Override
        public void run() {
            while (true) {
                if (this.sale()) {
                    break;
                }
            }
        }
    }
}
