package hw1.Task

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.{IntWritable, Text}
import java.lang
import org.apache.log4j.Logger
import scala.collection.JavaConverters._

/** Reducer class, implements reduce() method of the base Reducer class.
  */
class TaskReducer
    extends Reducer[IntWritable, IntWritable, IntWritable, IntWritable] {

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
        IntWritable,
        IntWritable
      ]#Context
  ): Unit = {
    val sum = values.asScala.foldLeft(0)((x, y) => x + y.get())
    context.write(key, new IntWritable(sum))
  }
}
