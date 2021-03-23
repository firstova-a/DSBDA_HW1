package hw1

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.fs.Path
import Task.{TaskMapper, TaskReducer}
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat
import org.apache.log4j.Logger
import org.apache.hadoop.io.Text

/** Main class for program run (driver class).
  */
object Main {

  /** Logger object.
    */
  val logger = Logger.getLogger(this.getClass)

  /** Main function.
    *
    * @param args Sequence of arguments, must contain input and output path for mapreduce job.
    */
  def main(args: Array[String]): Unit = {
    val inputFilePath = args(0)
    val outputFilePath = args(1)

    val config = new Configuration()
    val job = Job.getInstance(config, "hw1")

    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[TaskMapper])
    job.setReducerClass(classOf[TaskReducer])

    job.setMapOutputKeyClass(classOf[IntWritable])
    job.setMapOutputValueClass(classOf[IntWritable])

    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[Text])

    job.setOutputFormatClass(
      classOf[SequenceFileOutputFormat[Text, Text]]
    )

    FileInputFormat.addInputPath(job, new Path(inputFilePath))
    FileOutputFormat.setOutputPath(job, new Path(outputFilePath))

    val status = job.waitForCompletion(true)

    val counter = job.getCounters().findCounter("Mapper", "Missed")
    logger.info(s"Missed points (out of screen borders): ${counter.getValue()}")

    System.exit(if (status) 0 else 1)
  }
}
