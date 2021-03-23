package hw1.Task

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.{IntWritable, Text}
import java.lang
import org.apache.log4j.Logger
import scala.collection.JavaConverters._

/** Reducer class, implements reduce() method of the base Reducer class.
  */
class TaskReducer extends Reducer[IntWritable, IntWritable, Text, Text] {

  /** Map between range of click number and temperature.
    */
  val tempDict = Map(
    (0, 10) -> "cold",
    (10, 30) -> "warm",
    (30, 100) -> "hot"
  )

  /** Reduce function, which sums all values for respectful key. Emits:
    * <area number> -> <number of clicks>
    *
    * @param key  The key of a record.
    * @param values The sequence of values for the record.
    * @param context  The context.
    */
  override def reduce(
      key: IntWritable,
      values: lang.Iterable[IntWritable],
      context: Reducer[
        IntWritable,
        IntWritable,
        Text,
        Text
      ]#Context
  ): Unit = {
    val sum = values.asScala.foldLeft(0)((x, y) => x + y.get())
    val temp =
      (for (((lowerBound, upperBound), temperature) <- tempDict) yield {
        if (sum >= lowerBound && sum < upperBound) {
          Some(temperature)
        } else {
          None
        }
      }).flatten.head

    val resultKey = new Text(s"area-$key")
    val resultValue = new Text(s"$sum ($temp)")

    context.write(resultKey, resultValue)
  }
}
