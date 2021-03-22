package net.renfei.hadoop.inputformat;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * <p>Title: DemoInputFormat</p>
 * <p>Description: 自定义InputFormat的演示</p>
 *
 * @author RenFei
 */
public class DemoInputFormat extends FileInputFormat<Text, BytesWritable> {
    @Override
    public RecordReader createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        return new DemoRecordReader();
    }
}
