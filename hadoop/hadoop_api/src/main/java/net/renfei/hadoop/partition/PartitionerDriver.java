package net.renfei.hadoop.partition;

import net.renfei.hadoop.entity.DemoEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * <p>Title: PartitionerDriver</p>
 * <p>Description: 自定义 Partition 分区的演示</p>
 * 只是演示，不保证运行成功
 *
 * @author RenFei
 */
public class PartitionerDriver {
    /**
     * 程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(PartitionerDriver.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DemoEntity.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DemoEntity.class);

        // 此处只演示 自定义 Partition 分区 的使用
        // 设置 ReduceTasks 是 4 个，因为我们分区为 0、1、2、3
        job.setNumReduceTasks(4);
        job.setPartitionerClass(MyPartitioner.class);
    }
}
