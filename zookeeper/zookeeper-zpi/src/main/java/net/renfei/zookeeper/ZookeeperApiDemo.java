package net.renfei.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>Title: ZookeeperApiDemo</p>
 * <p>Description: Zookeeper Api 演示</p>
 *
 * @author RenFei
 */
public class ZookeeperApiDemo {
    // private static final String CONNECT_STRING = "n1.renfei.net:2181,n2.renfei.net:2181,n3.renfei.net:2181";
    private static final String CONNECT_STRING = "localhost:2181";
    private static final int SESSION_TIMEOUT = 2000;
    private ZooKeeper zkClient = null;

    @Before
    public void init() throws Exception {
        zkClient = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, watcher -> {
            // 收到事件通知后的回调函数（用户的业务逻辑）
            System.out.println(watcher.getType() + "--" + watcher.getPath());
            // 再次启动监听
            try {
                zkClient.getChildren("/", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 创建子节点
     *
     * @throws Exception
     */
    @Test
    public void create() throws Exception {
        // 参数：要创建的节点的路径； 参数2：节点数据 ； 参数3：节点权限 ；参数4：节点的类型
        String nodeCreated = zkClient.create("/renfei", "demo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(nodeCreated);
    }

    /**
     * 放置数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void set() throws KeeperException, InterruptedException {
        Stat stat = zkClient.setData("/renfei", "多大的".getBytes(), 0);
        System.out.println(stat.toString());
    }

    /**
     * 获取数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void get() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] dataBytes = zkClient.getData("/renfei", true, stat);
        // 注意这个 Version 版本号，版本不对写入不进去
        System.out.println(stat.getVersion());
        System.out.println(new String(dataBytes));
    }

    /**
     * 存在检测
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void exists() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/renfei", false);
        if (stat == null) {
            System.out.println("节点不存在");
        } else {
            System.out.println(stat.getDataLength());
        }
    }

    /**
     * 删除数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void delete() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/renfei", false);
        if (stat != null) {
            zkClient.delete("/renfei", stat.getVersion());
        }
    }

    public void register() throws KeeperException, InterruptedException {
        byte[] data = zkClient.getData("/renfei", watchedEvent -> {
            try {
                // 此处递归循环调用，一直监听变化
                register();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, new Stat());
        System.out.println(new String(data));
    }

    @Test
    public void registerTest() throws InterruptedException {
        try {
            register();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 让主线程延时阻塞，为了可以查看register()的循环递归执行
        Thread.sleep(Long.MAX_VALUE);
    }
}
