object ConnectedBlocks {
  def main(args: Array[String]) {

    val blockStruct = Array.ofDim[Int](10, 10)
    blockStruct(1)(1) = 1
    blockStruct(1)(2) = 1
    blockStruct(2)(1) = 1
    blockStruct(3)(3) = 1
    blockStruct(1)(3) = 1
    blockStruct(4)(3) = 1
    blockStruct(4)(4) = 1
    blockStruct(5)(4) = 1
    blockStruct(5)(5) = 1
    blockStruct(6)(5) = 1
    blockStruct(7)(5) = 1
    blockStruct(8)(7) = 1
    blockStruct(8)(8) = 1
    blockStruct(9)(7) = 1
    blockStruct(9)(8) = 1
    blockStruct(9)(8) = 1

    //
    import scala.collection.mutable.ListBuffer
    var tuples = new ListBuffer[(Int, Int)]

    for ((x, x_index) <- blockStruct.view.zipWithIndex) {
      for ((y, y_index) <- x.view.zipWithIndex) {
        if (blockStruct(x_index)(y_index) == 1) {
          tuples += ((x_index, y_index))
        }
      }
    }

    solve(tuples.toList)
  }

  private def solve(allTuples: List[(Int, Int)]) {

    // this is messy && sloppy - there has to be a better way ... too tired right now, at this hour...
    def getNeighbors(tuple: (Int, Int), tuples: List[(Int, Int)]) : List[(Int, Int)] = {
      tuples.filter(
        x =>
          (x._1 + 1 == tuple._1 && x._2 == tuple._2) ||
            (x._1 == tuple._1 - 1 && x._2 == tuple._2) ||
            (x._1 == tuple._1 && x._2 == tuple._2 + 1) ||
            (x._1 == tuple._1 && x._2 == tuple._2 - 1) ||
            (x._1 == tuple._1 - 1 && x._2 == tuple._2 - 1) ||
            (x._1 == tuple._1 + 1 && x._2 == tuple._2 + 1) ||
            (x._1 == tuple._1 - 1 && x._2 == tuple._2 + 1) ||
            (x._1 == tuple._1 + 1 && x._2 == tuple._2 - 1)
      )
    }

    import scala.annotation.tailrec
    def solver(acc: List[List[(Int, Int)]],
               allTuples: List[(Int, Int)],
               currentNeighbors: List[(Int, Int)]): List[List[(Int, Int)]] = {
      allTuples match {
        case Nil =>
          currentNeighbors match {
            case Nil => acc
            case _ => currentNeighbors :: acc
          }
        case h :: t =>
          if (!getNeighbors(h, currentNeighbors).isEmpty)
            solver(acc, t.diff(h :: currentNeighbors), h :: currentNeighbors)
          else
            solver(currentNeighbors :: acc, t, List(h))
      }
    }

    if (allTuples != Nil) {
      val solution = solver(List(), allTuples.tail, List(allTuples.head))

      // print all structures
      println(solution)

      // get the biggest structure in the solution
      //println(solution.filter(solution.map(_.size).max == _.size).head)
    }
  }
}
