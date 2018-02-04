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


public class hadoopWeatherCounter {

    public static class MaxTemperatureMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final int MISSING = 99999;

        enum INCORRECTAS {
            TOO_LOW,
            TOO_HIGH
        }

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String year = line.substring(21, 25);
            int airTemperature = Integer.parseInt(line.substring(46, 52));
            String quality = line.substring(67, 70);

            if (airTemperature != MISSING) {
                context.write(new Text(year), new IntWritable(airTemperature));
            }
            else
            {
                context.getCounter(INCORRECTAS.TOO_HIGH).increment(1);
                context.setStatus("Detected possibly corrupt record: see logs.");
                System.out.println("Incrementado contador TOO_HIGH");

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
        Job job = Job.getInstance(conf, "Max temperature counters");
        //definimos la clase q contiene nuestro mapper y reducer
        job.setJarByClass(hadoopWeatherCounter.class);
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

        long total=job.getCounters().findCounter(MaxTemperatureMapper.INCORRECTAS.TOO_HIGH).getValue();
        System.out.printf("APM Records total =" + total);


        /*
        Counters counters = job.getCounters();
        long total = counters.findCounter(MAP_INPUT_RECORDS).getValue();
        System.out.printf("Records total =" + total);
        */

    }

}
