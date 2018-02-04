import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.io.*;
//import org.apache.mrunit.mapreduce.MapDriver;
import org.junit.*;
import java.io.IOException;

public class testHadoop {

    @Test
    public static void main(String[] args) throws Exception {

        //Text txt = new Text("(318, 0043012650999992006032418004N9+0500001N9+00031+99999999999N9+[1])");
        Text txt = new Text("(318, 0043012650999992016032418004N9+0500001N9+00038+99999999999N9+[1])");
        /*
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new hadoopWeatherCounter.MaxTemperatureMapper())
                .withInput(new LongWritable(0), txt)
                .withOutput(new Text("2016"), new IntWritable(31))
                .runTest();
*/
        }

    }
