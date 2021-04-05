package net.renfei.demo.concurrent;

/**
 * Runnable 多线程初体验
 *
 * @author renfei
 */
public class RunnableDemo {
    public static void main(String[] args) {
        Thread threadA = new Thread(new MyThread("线程A"));
        Thread threadB = new Thread(new MyThread("线程B"));
        Thread threadC = new Thread(new MyThread("线程C"));
        threadA.start();
        threadB.start();
        threadC.start();
    }

    /**
     * 通过实现 Runnable 接口的方式实现多线程
     */
    static class MyThread implements Runnable {
        private String myName;

        public MyThread(String name) {
            this.myName = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(this.myName + " :: 打印 i = " + i);
            }
        }
    }
}
