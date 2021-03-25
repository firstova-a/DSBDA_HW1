package hw1.Task

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.{BufferedReader, InputStreamReader}
import org.apache.log4j.Logger

import scala.collection.JavaConverters._
import scala.collection.mutable

/** Mapper class, implements map() and setup() methods of the base Mapper class.
  */
class TaskMapper extends Mapper[Object, Text, IntWritable, IntWritable] {

  /** Logger object.
    */
  val logger = Logger.getLogger(this.getClass())

  /** `One` constant
    */
  val one = new IntWritable(1)

  /** Map (hash table) of areas: between coordiates of an area and its number. <(xMin, xMax, yMin, yMax) -> areaNumber>.
    */
  val areasMap = mutable.Map[(Int, Int, Int, Int), Int]()

  /** The function for setup and areas file importing, which contains areas coordinates.
    *
    * @param context The context for this Mapper
    */
  override def setup(
      context: Mapper[Object, Text, IntWritable, IntWritable]#Context
  ): Unit = {
    val areasFilePath = new Path("hdfs:/user/root/support-data/areas.txt")
    val fs = FileSystem.get(new Configuration())
    val reader = new BufferedReader(
      new InputStreamReader(fs.open(areasFilePath))
    )

    for (
      (line, index) <- reader.lines().iterator().asScala.toList.zipWithIndex
      if line != ""
    ) {
      val Array(x0, x1, y0, y1) = line.split(",").map(_.toInt)
      areasMap.put((x0, x1, y0, y1), index)
    }

    logger.info(s"Areas map: ${areasMap}")
  }

  /** Map function, which operates over single record. It maps input string to a pair:
    * <area number> -> <1 click>
    *
    * @param key  The key of a record, unused.
    * @param value  The value of a record.
    * @param context  The context.
    */
  override def map(
      key: Object,
      value: Text,
      context: Mapper[
        Object,
        Text,
        IntWritable,
        IntWritable
      ]#Context
  ): Unit = {
    val Array(x, y, userId, timestamp) = value.toString.split(",").map(_.toInt)
    val possibleAreas =
      for (
        ((x0, x1, y0, y1), areaNumber) <- areasMap
        if x >= x0 && x <= x1 && y >= y0 && y <= y1
      ) yield {
        areaNumber
      }

    possibleAreas.headOption match {
      case Some(areaNumber) =>
        context.write(
          new IntWritable(areaNumber),
          one
        )
      case None =>
        context.getCounter("Mapper", "Missed").increment(1)
    }
  }
}
