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

/** Main class for program run.
  */
object Main {
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
    job.setOutputKeyClass(classOf[IntWritable])
    job.setOutputValueClass(classOf[IntWritable])

    job.setOutputFormatClass(
      classOf[SequenceFileOutputFormat[IntWritable, IntWritable]]
    )

    FileInputFormat.addInputPath(job, new Path(inputFilePath))
    FileOutputFormat.setOutputPath(job, new Path(outputFilePath))

    val status = job.waitForCompletion(true)

    val counter = job.getCounters().findCounter("Mapper", "Missed")
    logger.info(s"Missed points (out of screen borders): ${counter.getValue()}")

    System.exit(if (status) 0 else 1)
  }
}
