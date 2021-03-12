package net.renfei.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p>Title: WordCountMapReduce</p>
 * <p>Description: WordCount的MapReduce案例</p>
 *
 * @author RenFei
 */
public class WordCountMapReduce {
    /**
     * 程序的入口
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());
        // 告知框架我们的类路径
        job.setJarByClass(WordCountMapReduce.class);
        // 告知框架mapper和reducer
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        // 告知框架mapper和reducer的输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 告知框架输入输出数据路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 提交job
        boolean exc = job.waitForCompletion(true);
        System.exit(exc ? 0 : 1);
    }

    /**
     * Map（映射）
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final Text word = new Text();
        private final IntWritable intWritable = new IntWritable(1);

        /**
         * Mapper 的业务逻辑需要写在 map() 里，我们这里重写父类的 map()
         *
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 拿到一行数据
            String line = value.toString();
            // 按照空格切分单词
            String[] words = line.split(" ");
            // 遍历单词
            for (String word : words
            ) {
                this.word.set(word);
                // 把Map（映射）好的东西返回给框架
                context.write(this.word, this.intWritable);
            }
        }
    }

    /**
     * Reduce（归约）
     */
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable total = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            // 遍历我们上面 Map（映射）出的 同 Key 的 Value
            for (IntWritable value : values
            ) {
                // 对 Value 求和
                sum += value.get();
            }
            // 装箱包装一下
            this.total.set(sum);
            // 把 Reduce（归约）结果返回给框架
            context.write(key, this.total);
        }
    }
}
