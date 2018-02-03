import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class testHadoop {

    public static void main(String[] args) throws Exception {

        Text txt = new Text("(106, 0043012650999992014032418004N9+0500001N9+00035+99999999999N9+[1])");

        String line = txt.toString();
        String year = line.substring(21, 25);
        int airTemperature = Integer.parseInt(line.substring(46, 52));
        String quality = line.substring(67, 70);


        System.out.println(year);
        System.out.println(airTemperature);
        System.out.println(quality);



    }

    }
