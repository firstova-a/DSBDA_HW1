package hw1

import hw1.Task.{TaskMapper, TaskReducer}
import org.apache.hadoop.io.Text
import org.scalatest._
import org.apache.hadoop.io.IntWritable
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.mockito.Mockito._
import collection.JavaConverters._
import org.apache.hadoop.mapreduce.Counter

/** Main class for unit tests. Responsible for:
  * 1) TaskMapper.map() correctness check
  * 2) TaskMapper counters usage check
  * 3) TaskReducer.reduce() correctness check
  */
class MapReduceTest extends AnyFlatSpec with MockitoSugar {
  "mapper" should "map correctly" in {
    val mapper = new TaskMapper()
    val context = mock[mapper.Context]

    for {
      (x, areaNumber) <- List(0, 10).zipWithIndex
      y <- List(0, 10)
    } {
      mapper.areasMap.put((x, x + 10, y, y + 10), areaNumber)
    }

    mapper.map(None, new Text("10,13,10,1615797254"), context)

    verify(context).write(new IntWritable(1), new IntWritable(1))
  }

  "mapper" should "use counters" in {
    val mapper = new TaskMapper()
    val context = mock[mapper.Context]
    val counter = mock[Counter]

    when(context.getCounter("Mapper", "Missed")) thenReturn counter

    for {
      (x, areaNumber) <- List(0, 10).zipWithIndex
      y <- List(0, 10)
    } {
      mapper.areasMap.put((x, x + 10, y, y + 10), areaNumber)
    }

    mapper.map(None, new Text("10,50,10,1615797254"), context)

    verify(counter).increment(1)
  }

  "reducer" should "reduce corectly" in {
    val reducer = new TaskReducer()
    val context = mock[reducer.Context]

    val key = new IntWritable(1)
    val values = List(
      new IntWritable(1),
      new IntWritable(2),
      new IntWritable(3),
      new IntWritable(4),
      new IntWritable(5)
    )

    reducer.reduce(key, values.asJava, context)

    verify(context).write(new Text("area-1"), new Text("15 (warm)"))
  }
}
