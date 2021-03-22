package net.renfei.hadoop.partition;

import net.renfei.hadoop.entity.DemoEntity;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * <p>Title: MyPartitioner</p>
 * <p>Description: 自定义 Partition 分区的演示</p>
 *
 * @author RenFei
 */
public class MyPartitioner extends Partitioner<Text, DemoEntity> {
    @Override
    public int getPartition(Text text, DemoEntity demoEntity, int numPartitions) {
        // 111.224.80.24 - - [17/Mar/2021:03:17:49 +0000] "GET /dictionary/gender HTTP/1.1" 200 405 "http://www.renfei.net/index.html" "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36"
        // 取 IP，使用空格切割
        String ip = text.toString().split(" ")[0];
        // 假设按照 IP 开头的区别分别分区
        if (ip.startsWith("192.")) {
            return 0;
        } else if (ip.startsWith("10.10.")) {
            return 1;
        } else if (ip.startsWith("10.0.")) {
            return 2;
        } else {
            return 3;
        }
    }
}
