import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.Job;


public class hadoopParser {

    public static class ParserMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            line=line.replaceAll("9","#");
            Integer c = Integer.parseInt(line.substring(1,4));
            IntWritable x = new IntWritable(c);
            context.write(new Text(line), x);

        }
    }

    public static class ParserReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int maxValue = Integer.MIN_VALUE;
            for (IntWritable value : values) {
                maxValue = Math.max(maxValue, value.get());
            }

            context.write(key, new IntWritable(maxValue));
        }
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage : hadoopparser <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        conf.set("fs.defaultFS", "hdfs://namenode:8020");
        //conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resourcemanager.address", "resourcemanager:8032");

        Job job = Job.getInstance(conf, "My hadoop parser");
        //definimos la clase q contiene nuestro mapper y reducer
        job.setJarByClass(hadoopParser.class);
        //definir las clases que haran mapeo y reduce
        job.setMapperClass(ParserMapper.class);
        job.setReducerClass(ParserReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //definir paths de hdfs como entrada y salida del job, eran parametrizados
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //lanzar trabajo asincronamente
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}
