package net.renfei.demo.concurrent;

/**
 * Thread 多线程初体验
 *
 * @author renfei
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Thread threadA = new MyThread("线程A");
        Thread threadB = new MyThread("线程B");
        Thread threadC = new MyThread("线程C");
        threadA.start();
        threadB.start();
        threadC.start();
    }

    /**
     * 通过继承 Thread 的方式实现多线程
     */
    static class MyThread extends Thread {
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
