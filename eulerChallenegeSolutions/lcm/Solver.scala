

object Solver{

  private val primes: List[Double] = List(
    2, 3,  5,   7,  11,
    13,  17,   19,  23,
    29,  31,  37,  41,
    43,  47,  53,  59,
    61,  67,  71,  73,
    79,  83,  89,  97,
    101,  103, 107,  109,
    113,  127,  131,  137,
    139,  149,  151,  157,
    163,  167,  173,  179,
    181,  191,  193,  197,
    199,  211,  223,  227,
    229,  233,  239,  241,
    251,  257,  263,  269,
    271,  277,  281,  283,
    293,  307,  311,  313,
    317,  331,  337,  347,
    349,  353,  359,  367,
    373,  379,  383,  389,
    397,  401,  409,  419,
    421,  431,  433,  439,
    443,  449,  457,  461,
    463,  467,  479,  487,
    491,  499, 503,  509,
    521,  523, 541)


  def main(args: Array[String]): Unit = {

    val upperBound = args(0).toInt

    if (upperBound > 542 || upperBound < 2)
      println("Please stay within [2, 542]")
    else {
      val tic = System.currentTimeMillis()
      solve(upperBound)
      val toc = System.currentTimeMillis()
      println("Time it took, in ms = " + (toc - tic))
    }
  }

  private def solve(upperBound: Int) : Unit = {
    iter(1, primes.head, primes.tail)

    def iter(acc: Double, currentPrime: Double, restOfPrimes: List[Double]): Unit = {
      if (currentPrime > upperBound)
        println(acc)
      else
        iter(acc * (Math.pow(currentPrime, Math.floor(logarithm(currentPrime, upperBound)))), restOfPrimes.head, restOfPrimes.tail)
    }
  }

  private def logarithm(base: Double, x: Double) : Double = Math.log(x)/Math.log(base)
}