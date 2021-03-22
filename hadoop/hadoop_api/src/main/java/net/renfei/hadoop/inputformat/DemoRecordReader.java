package net.renfei.hadoop.inputformat;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * <p>Title: DemoRecordReader</p>
 * <p>Description: 自定义RecordReader，处理一个文件，把数据变成 [K,V] 值</p>
 * 因为是演示，Text 也就是 Key 我们就取文件路径了，没什么意义
 *
 * @author RenFei
 */
public class DemoRecordReader extends RecordReader<Text, BytesWritable> {
    private boolean readed = false;
    private Text key = new Text();
    private BytesWritable value = new BytesWritable();
    private FileSplit fileSplit;
    private FSDataInputStream inputStream;

    /**
     * 初始化方法，初始化的时候会被调用一次
     *
     * @param split
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        // 切片类型转为文件切片
        // 此处强转 FileSplit 是因为 net.renfei.hadoop.inputformat.DemoInputFormat 继承了 org.apache.hadoop.mapreduce.lib.input.FileInputFormat
        fileSplit = (FileSplit) split;
        // 获取切片路径
        Path path = fileSplit.getPath();
        // 通过路径获取文件系统
        FileSystem fileSystem = path.getFileSystem(context.getConfiguration());
        // 打开文件流
        inputStream = fileSystem.open(path);
    }

    /**
     * 读取下一组 KV 值
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (readed) {
            return false;
        } else {
            // 读取数据
            // 读 Key，因为是演示，Text 也就是 Key 我们就取文件路径了，没什么意义
            key.set(fileSplit.getPath().toString());
            // 读 Value，直接一次读取完，所以是 fileSplit.getLength()
            byte[] buf = new byte[(int) fileSplit.getLength()];
            inputStream.read(buf);
            value.set(buf, 0, buf.length);
            readed = true;
            return true;
        }
    }

    /**
     * 获取当前读取到的Key
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    /**
     * 读取当前的Value
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    /**
     * 当前数据读取的进度
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return readed ? 1 : 0;
    }

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        IOUtils.closeStream(inputStream);
    }
}
