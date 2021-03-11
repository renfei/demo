package net.renfei.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>Title: HDFSStreamClient</p>
 * <p>Description: HDFS客户端(数据流的方式)</p>
 *
 * @author RenFei
 */
public class HDFSStreamClient {
    private final String user;
    private final URI uri;
    private final Configuration configuration;

    public HDFSStreamClient() throws URISyntaxException {
        // HDFS 的配置，这里取默认的配置，无需修改
        this.configuration = new Configuration();
        // 配置用户名
        this.user = "renfei";
        // 配置 Hadoop 的地址
        this.uri = new URI("hdfs://n1.renfei.net:9000");
    }

    /**
     * 以流的方式上传文件
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void upload() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        // 创建一个文件输入流
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/renfei/Downloads/demo.txt"));
        // 获取输出流
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/demo/demo.txt"));
        // 流拷贝
        IOUtils.copyBytes(fileInputStream, fsDataOutputStream, configuration);
        // 关闭资源
        IOUtils.closeStream(fsDataOutputStream);
        IOUtils.closeStream(fileInputStream);
        fileSystem.close();
    }

    /**
     * 以流的方式下载文件
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void get() throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(uri, configuration, user);
        // 获取输入流
        FSDataInputStream fsDataOutputStream = fileSystem.open(new Path("/demo/demo.txt"));
        // 获取输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/renfei/Downloads/demo2.txt"));
        // 流拷贝
        IOUtils.copyBytes(fsDataOutputStream, fileOutputStream, configuration);
        // 关闭资源
        IOUtils.closeStream(fsDataOutputStream);
        IOUtils.closeStream(fileOutputStream);
        fileSystem.close();
    }
}
