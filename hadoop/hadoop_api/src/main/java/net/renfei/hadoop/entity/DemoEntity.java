package net.renfei.hadoop.entity;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>Title: DemoEntity</p>
 * <p>Description: </p>
 *
 * @author RenFei
 */
public class DemoEntity implements WritableComparable<DemoEntity> {
    private String ip;
    private String path;
    private int port;

    /**
     * 序列化方法
     *
     * @param dataOutput 框架给我们的数据出口
     * @throws IOException
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(ip);
        dataOutput.writeUTF(path);
        dataOutput.writeInt(port);
    }

    /**
     * 反序列化方法
     *
     * @param dataInput 框架给我们的数据来源
     * @throws IOException
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        ip = dataInput.readUTF();
        path = dataInput.readUTF();
        port = dataInput.readInt();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int compareTo(DemoEntity o) {
        return Integer.compare(o.getPort(), this.port);
    }
}
