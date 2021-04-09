package net.renfei.demo.concurrent;

/**
 * 线程间，资源互持，死锁演示
 *
 * @author renfei
 */
public class DeadlockDemo implements Runnable {
    private Money money = new Money();
    private Apple apple = new Apple();

    public static void main(String[] args) {
        new DeadlockDemo();
    }

    /**
     * main 线程实例化的时候，占有钱对象，需要拿到苹果，才能付钱
     */
    public DeadlockDemo() {
        new Thread(this).start();
        money.buy(apple);
    }

    /**
     * 子线程占有苹果，需要拿到钱，才能销售苹果
     */
    @Override
    public void run() {
        apple.sale(money);
    }
}

/**
 * 钱的资源
 */
class Money {
    /**
     * 买苹果
     *
     * @param apple 苹果
     */
    public synchronized void buy(Apple apple) {
        System.out.println("线程 " + Thread.currentThread().getName() + "：给我苹果。我就给你钱");
        apple.ok();
    }

    public synchronized void ok() {
        System.out.println("线程 " + Thread.currentThread().getName() + "：购买苹果成功。");
    }
}

/**
 * 苹果资源
 */

class Apple {
    /**
     * 销售苹果
     *
     * @param money 钱
     */
    public synchronized void sale(Money money) {
        System.out.println("线程 " + Thread.currentThread().getName() + "：给我钱。我就给你苹果");
        money.ok();
    }

    public synchronized void ok() {
        System.out.println("线程 " + Thread.currentThread().getName() + "：销售苹果成功。");
    }
}
