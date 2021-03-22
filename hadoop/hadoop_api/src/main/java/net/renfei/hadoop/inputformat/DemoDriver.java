package net.renfei.hadoop.inputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p>Title: DemoDriver</p>
 * <p>Description: 自定义InputFormat的演示</p>
 *
 * @author RenFei
 */
public class DemoDriver {
    /**
     * 程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(DemoDriver.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        // 设置使用我们自定义的 DemoInputFormat
        job.setInputFormatClass(DemoInputFormat.class);

        // 因为 DemoInputFormat 继承了 FileInputFormat，所以可以使用 FileInputFormat 设置
        FileInputFormat.setInputPaths(job, new Path("/Users/renfei/Downloads/demo.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/renfei/Downloads/demoout"));
    }
}
