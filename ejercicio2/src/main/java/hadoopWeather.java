import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class hadoopWeather {

    public static class MaxTemperatureMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final int INCORRECTO = 99999;


        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String year = line.substring(21, 25);
            int airTemperature = Integer.parseInt(line.substring(46, 52));
            String quality = line.substring(67, 70);

            if (airTemperature != INCORRECTO) {
                context.write(new Text(year), new IntWritable(airTemperature));
            }

        }
    }

    public static class MaxTemperatureReducer
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
            System.err.println("Usage : MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Max temperature");
        //definimos la clase q contiene nuestro mapper y reducer
        job.setJarByClass(hadoopWeather.class);
        //definir las clases que haran mapeo y reduce
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //definir paths de hdfs como entrada y salida del job, eran parametrizados
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //lanzar trabajo asincronamente
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}
