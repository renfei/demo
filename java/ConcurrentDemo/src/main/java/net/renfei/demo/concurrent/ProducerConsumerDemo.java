package net.renfei.demo.concurrent;

/**
 * 生产者与消费者的经典模型
 *
 * @author renfei
 */
public class ProducerConsumerDemo {

    public static void main(String[] args) {
        // 装载苹果的仓库内存空间
        Warehouse warehouse = new Warehouse();
        // 生产者开始生产
        Thread threadProducer = new Thread(new Producer(warehouse), "Producer");
        // 消费者开始消费
        Thread threadConsumer = new Thread(new Consumer(warehouse), "Consumer");
        threadProducer.start();
        threadConsumer.start();
        try {
            // 让子线程们执行 4 秒
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 优雅的停止线程，仓库停止营业了
        warehouse.setRun(false);
    }

    /**
     * 仓库，装载苹果的内存空间
     */
    static class Warehouse {
        private Apple apple;
        // 优雅的停止线程标识，仓库是不是还在营业
        private boolean run = true;

        static class Apple {
        }

        public synchronized void set(Apple apple) {
            if (this.apple != null) {
                // 无法放入，需要被消费
                try {
                    super.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // 放入仓库，唤醒等待的线程
                this.apple = apple;
                super.notify();
            }
        }

        public synchronized Apple get() {
            if (this.apple == null) {
                // 仓库空了，无法消费，需要生产
                try {
                    super.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                return this.apple;
            } finally {
                // 被消费，唤醒等待线程
                this.apple = null;
                super.notify();
            }
        }

        public boolean isRun() {
            return run;
        }

        public void setRun(boolean run) {
            this.run = run;
        }
    }

    /**
     * 生产者，苹果没了就生产
     */
    static class Producer implements Runnable {
        private Warehouse warehouse;

        public Producer(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            while (warehouse.isRun()) {
                try {
                    // 模拟延迟，模拟生产比消费快一点
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("生产者：生产一个苹果");
                this.warehouse.set(new Warehouse.Apple());
            }
        }
    }

    /**
     * 消费者，有苹果就消费
     */
    static class Consumer implements Runnable {
        private Warehouse warehouse;

        public Consumer(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            while (warehouse.isRun()) {
                try {
                    // 模拟延迟，模拟消费比生产慢一点
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.warehouse.get();
                System.out.println("消费者：消费一个苹果");
            }
        }
    }
}
