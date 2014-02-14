object SpreadsheetIndex {

  def main(args: Array[String]) {
    val input = readLine()

    if (input == null || !input.matches("^[a-zA-Z]+$")) {
      println( """Please pass in something in the form of `^[azAZ]+$` !""")
    } else {
      println(getHeaderIndex(input.toUpperCase.toList.reverse))
    }
  }

  private def getHeaderIndex(charList: List[Char]): BigInt = {
    val multiplesOf26 = BigInt(26) // we are dealing with 26 letters

    def calculateIndex(letter: Char, index: Int): BigInt = {
      (letter - 64) /* make it into a corresponding ASCII integer */ * multiplesOf26.pow(index)
    }

    import scala.annotation.tailrec
    @tailrec
    def getIndex(list: List[Char], accumulator: BigInt, indexOfLetterInString: Int): BigInt = {
      list match {
        case Nil => accumulator
        case h :: t => getIndex(t, accumulator + calculateIndex(h, indexOfLetterInString), indexOfLetterInString + 1)
      }
    }

    getIndex(charList, 0, 0)
  }
}
