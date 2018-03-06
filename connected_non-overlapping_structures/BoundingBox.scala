import scala.collection.mutable.ArrayBuffer

object BoundingBox {
  private val token: Char = '*'

  /**
    * Main entry into the program.
    *
    * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    *
    * Please note that there is no input checking done - I am trusting the person
    * running this program is nice, and only is trying to feed good input to get
    * an answer, and check it for correctness!
    *
    * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // line index starts at 1
    var inputLine = 1
    val input: ArrayBuffer[(Int, Int)] = ArrayBuffer.empty

    import scala.util.control.Breaks._
    breakable {
      for (ln <- io.Source.stdin.getLines) {
        if (ln == "") break
        val chars = ln.toArray

        for ((ch, ch_index) <- chars.zip(Stream from 1)) { // index starts at 1 for each char in line
          if (ch == token) input += ((inputLine, ch_index))
        }

        inputLine += 1
      }
    }

    solve(input.toList)
  }


  /**
    * "Solve" the problem
    *
    * @param allTuples All of the coordinates (as Tuple2) which correspond to the `*` input
    */
  private def solve(allTuples: List[(Int, Int)]): Unit = {

    /**
      * Find the immediate neighbors of the given point (tuple)
      *
      * @param tuple  The pair of coordinates to find the immediate neighbors of
      * @param tuples All of pairs
      * @return The filterd tuples that are immediate neighboths of `tuple`
      */
    def getNeighbors(tuple: (Int, Int),
                     tuples: List[(Int, Int)]): List[(Int, Int)] = {
      tuples.filter(
        x =>
          (x._1 == tuple._1 + 1 && x._2 == tuple._2) ||
            (x._1 == tuple._1 - 1 && x._2 == tuple._2) ||
            (x._1 == tuple._1 && x._2 == tuple._2 + 1) ||
            (x._1 == tuple._1 && x._2 == tuple._2 - 1)
      )
    }

    import scala.annotation.tailrec

    /**
      * Construct a list of clusters, where each cluster (list) is an immediate neighbor
      * of any/all of the elements in said cluster
      *
      * @param acc              The accumulator
      * @param allTuples        All of the tuples
      * @param currentNeighbors Current collection of neighbors being processed
      * @return A list of grouped tuples (list) that are neighbors
      */
    @tailrec
    def constructClusters(acc: List[List[(Int, Int)]],
                          allTuples: List[(Int, Int)],
                          currentNeighbors: List[(Int, Int)]): List[List[(Int, Int)]] = {
      allTuples match {
        case Nil => currentNeighbors match {
          case Nil => acc
          case _ => currentNeighbors :: acc
        }
        case h :: t =>
          val allNeighbors = getAllNeighbors(t, List.empty, List(h))
          if (allNeighbors.isEmpty) {
            if (currentNeighbors.isEmpty)
              constructClusters(acc, t, List.empty)
            else
              constructClusters(currentNeighbors :: acc, t, List.empty)
          } else {
            val updatedAllTuples = allTuples.diff(allNeighbors) // this is inefficient! I have to substitute `allTuples` with a hash-backed data structure! So that its runtime is constant
            constructClusters(allNeighbors :: acc, updatedAllTuples, List.empty)
          }
      }
    }

    /**
      * Get all of the neighbors of a given cluster
      *
      * @param allTuples         All the tuples to draw potential neighbors from
      * @param neighborAcc       The accumulator
      * @param neighborToProcess The current cluster of tuples (neighbors) that are being processed
      * @return A cluster (list) of tuples that belong together
      */
    @tailrec
    def getAllNeighbors(allTuples: List[(Int, Int)],
                        neighborAcc: List[(Int, Int)],
                        neighborToProcess: List[(Int, Int)]): List[(Int, Int)] = {
      neighborToProcess match {
        case Nil => neighborAcc
        case h :: t =>
          val newNeighbors = getNeighbors(h, allTuples)
          getAllNeighbors(allTuples.diff(newNeighbors), h :: neighborAcc, t ::: newNeighbors)
      }
    }

    /**
      * Get the minimum bounding box for a set of coordinates
      *
      * @param acc        The accumulator (List(x1,y1,x2,y2))
      * @param clusters   The clusters to get the bounding boxes for
      * @return A list of bounding boxes
      */
    @tailrec
    def extractBoundingBoxes(acc: List[(Int, Int, Int, Int)],
                             clusters: List[List[(Int, Int)]]): List[(Int, Int, Int, Int)] = {
      clusters match {
        case Nil => acc
        case h :: t =>
          extractBoundingBoxes((h.minBy(_._1)._1, h.minBy(_._2)._2, h.maxBy(_._1)._1, h.maxBy(_._2)._2) :: acc, t)
      }
    }

    /**
      * Filter overlapping bounding boxes.
      *
      * @param acc                The accumulator
      * @param sortedBoundedBoxes The bounding boxes.  THE HAVE TO BE SORTED!!!!
      * @return Only the non-overlapping bounding boxes
      */
    @tailrec
    def filterOverlappingClusters(acc: List[(Int, Int, Int, Int)],
                                  sortedBoundedBoxes: List[(Int, Int, Int, Int)]): List[(Int, Int, Int, Int)] = {
      sortedBoundedBoxes match {
        case Nil => acc
        case h :: t =>
          acc match {
            case Nil => filterOverlappingClusters(h :: acc, t)
            case acc_head :: acc_tail =>
              if (!doBoundingBoxesOverlap(acc_head, h))
                filterOverlappingClusters(h :: acc, t)
              else
                filterOverlappingClusters(acc_tail, t)
          }
      }
    }

    /**
      * Given the two bounding boxes, figure out if they overlap
      *
      * @param leftBox This is Tuple4(x1,y1,x2,y2)
      * @param rightBox Tuple4(x1,y1,x2,y2)
      * @return If overlapping true, false otherwise
      */
    def doBoundingBoxesOverlap(leftBox: (Int, Int, Int, Int), rightBox: (Int, Int, Int, Int)): Boolean =
      !(leftBox._1 > rightBox._3 || rightBox._1 > leftBox._3 || leftBox._2 > rightBox._4 || rightBox._2 > leftBox._4)

    //
    // Solve all this goodness, step by step.
    //
    // All of this could have been done compressed into a couple of lines, but for readability's
    // sake, it's been expanded as such so it's (hopefully) easier to follow
    //
    if (allTuples.nonEmpty) {
      // find all of the connected clusters
      val allClusters: List[List[(Int, Int)]] = constructClusters(List.empty, allTuples, List.empty)

      // now find the bounding boxes of the clusters
      val boundingBoxes: List[(Int, Int, Int, Int)] = extractBoundingBoxes(List.empty, allClusters)

      // let us sort them by first(x-plane), then by second (y-plane) value
      val sortedBoundingBoxes: List[(Int, Int, Int, Int)] = boundingBoxes.sortBy(x => (x._1, x._2))

      // get filtered bounding boxes
      val filteredSortedBoundingBoxes: List[(Int, Int, Int, Int)] =
        filterOverlappingClusters(List(sortedBoundingBoxes.head), sortedBoundingBoxes.tail)

      // sort bounding boxes by largest area
      val sortedFilteredSortedBoundingBoxes: List[(Int, Int, Int, Int)] =
        filteredSortedBoundingBoxes.sortBy(x => (x._3 - x._1) * (x._4 - x._2))

      // now print all of them
      for (x <- sortedFilteredSortedBoundingBoxes) {
        println(s"(${x._1},${x._2})(${x._3},${x._4})")
      }
    } else { // trying to pull a fast one!?
      println("Please check your input, and try again!")
    }
  }
}
