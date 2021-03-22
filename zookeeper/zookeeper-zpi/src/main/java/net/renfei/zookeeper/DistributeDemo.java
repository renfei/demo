package net.renfei.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * <p>Title: DistributeDemo</p>
 * <p>Description: 监听服务器节点动态上下线</p>
 * <p>主节点可以有多台，可以动态上下线，任意一台客户端都能实时感知到主节点服务器的上下线</p>
 *
 * @author RenFei
 */
public class DistributeDemo {
    // private static final String CONNECT_STRING = "n1.renfei.net:2181,n2.renfei.net:2181,n3.renfei.net:2181";
    private static final String CONNECT_STRING = "localhost:2181";
    private static final int SESSION_TIMEOUT = 2000;
    private static final String PARENT_NODE = "/servers";

    /**
     * 服务器端向Zookeeper注册
     */
    public static class DistributeServer {
        private ZooKeeper zkClient = null;


        // 创建到zk的客户端连接
        public void getConnect() throws IOException {
            zkClient = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, event -> System.out.println("DistributeServer默认的回调函数"));
        }

        // 注册服务器
        public void registServer(String hostname) throws Exception {
            String create = zkClient.create(PARENT_NODE + "/server", hostname.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(hostname + " is online " + create);
        }

        // 业务功能
        public void business(String hostname) throws Exception {
            System.out.println(hostname + " is working ...");
            Thread.sleep(Long.MAX_VALUE);
        }

        public static void main(String[] args) throws Exception {
            // 获取zk连接
            DistributeServer server = new DistributeServer();
            server.getConnect();

            Stat stat = server.zkClient.exists("/servers", false);
            if (stat == null) {
                server.zkClient.create("/servers", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // 利用zk连接注册服务器信息，因为要注册多个服务器，所以使用参数的方式，我们可以调用多次
            server.registServer(args[0]);

            // 启动业务功能
            server.business(args[0]);
        }
    }

    public static class DistributeClient {
        private ZooKeeper zk = null;

        // 创建到zk的客户端连接
        public void getConnect() throws IOException {
            zk = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, event -> {
                // 再次启动监听
                try {
                    getServerList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 获取服务器列表信息
        public void getServerList() throws Exception {
            // 1获取服务器子节点信息，并且对父节点进行监听
            List<String> children = zk.getChildren(PARENT_NODE, true);
            // 2存储服务器信息列表
            ArrayList<String> servers = new ArrayList<>();
            // 3遍历所有节点，获取节点中的主机名称信息
            for (String child : children) {
                byte[] data = zk.getData(PARENT_NODE + "/" + child, false, null);

                servers.add(new String(data));
            }
            // 4打印服务器列表信息
            System.out.println(servers);
        }

        // 业务功能
        public void business() throws Exception {
            System.out.println("client is working ...");
            Thread.sleep(Long.MAX_VALUE);
        }

        public static void main(String[] args) throws Exception {
            // 1获取zk连接
            DistributeClient client = new DistributeClient();
            client.getConnect();

            // 2获取servers的子节点信息，从中获取服务器信息列表
            client.getServerList();

            // 3业务进程启动
            client.business();
        }
    }
}
