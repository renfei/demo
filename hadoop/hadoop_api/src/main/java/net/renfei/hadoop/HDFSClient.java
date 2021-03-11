package net.renfei.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>Title: HDFSClient</p>
 * <p>Description: HDFS客户端</p>
 *
 * @author RenFei
 */
public class HDFSClient {
    private final String user;
    private final URI uri;
    private final Configuration configuration;

    public HDFSClient() throws URISyntaxException {
        // HDFS 的配置，这里取默认的配置，无需修改
        this.configuration = new Configuration();
        // 配置用户名
        this.user = "renfei";
        // 配置 Hadoop 的地址
        this.uri = new URI("hdfs://n1.renfei.net:9000");
    }

    /**
     * 从在 HDFS 创建文件夹
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void mkdirs() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        fileSystem.mkdirs(new Path("/demo"));
        fileSystem.close();
    }

    /**
     * 从本地上传文件到 HDFS
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void upload() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        fileSystem.copyFromLocalFile(new Path("/Users/renfei/Downloads/demo.txt"), new Path("/demo/demo.txt"));
        fileSystem.close();
    }

    /**
     * 从 HDFS 中下载文件到本地
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void get() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        fileSystem.copyToLocalFile(new Path("/demo/demo.txt"), new Path("/Users/renfei/Downloads/demo2.txt"));
        fileSystem.close();
    }

    /**
     * 重命名 HDFS 中的文件
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void rename() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        fileSystem.rename(new Path("/demo/demo.txt"), new Path("/demo/demo2.txt"));
        fileSystem.close();
    }


    /**
     * 获取 HDFS 中的文件详情
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void listFiles() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/demo/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();
            // 文件名称
            System.out.println(status.getPath().getName());
            // 长度
            System.out.println(status.getLen());
            // 权限
            System.out.println(status.getPermission());
            // 分组
            System.out.println(status.getGroup());
            // 获取存储的块信息
            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                // 获取块存储的主机节点
                String[] hosts = blockLocation.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
            System.out.println("---------------------");
        }

        fileSystem.close();
    }

    /**
     * 从 HDFS 中删除文件
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void delete() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        // 此处第二参数 true 是指递归删除
        if (fileSystem.delete(new Path("/demo"), true)) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        fileSystem.close();
    }
}
