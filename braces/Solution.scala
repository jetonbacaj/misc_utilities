
object Solution {

	private final val brackets = ('[', ']')
	private final val parens = ('(', ')')
	private final val curls = ('{', '}')

	def braces(values: Array[String]): Array[String] = {
		def recurse(firstChar: List[Char], chars: List[Char]): String = {
			chars match {
				case h :: t => {
					//println("\tfirstChar = " + firstChar + " chars = " + chars)
					if (firstChar == Nil) {
						return recurse(List(h), t)
					}

					if (h == brackets._2) {
						if (firstChar.head != brackets._1) {
							//println("firstChar.head != brackets._1")
							"NO"
						} else if (firstChar.head == brackets._1) {
							//println("firstChar.head == brackets._1")
							return recurse(firstChar.tail, t)
						}
					} else if (h == parens._2) {
						if (firstChar.head != parens._1) {
							//println("firstChar.head != parens._1")
							"NO"
						} else if (firstChar.head == parens._1) {
							//println("firstChar.head == parens._1")
							return recurse(firstChar.tail, t)
						}
					} else if (h == curls._2) {
						if (firstChar.head != curls._1) {
							//println("firstChar.head != curls._1")
							"NO"
						} else if (firstChar.head == curls._1) {
							//println("firstChar.head == curls._1")
							return recurse(firstChar.tail, t)
						}
					}

					return recurse(h :: firstChar, t)
				}
				case Nil => {
					//println("\tfirstChar = " + firstChar + " chars = Nil")
					if (firstChar == Nil) {
						"YES"
					} else {
						"NO"
					}
				}
			}
		}

		import scala.collection.mutable.ListBuffer
		val response = new ListBuffer[String]

		//println("Input: ")
		for (x <- values) {
			//println(x)
			if (x == null || x == "" || x.trim == "" || x.length < 2) {
				response += "NO"
			} else {
				val stringList = x.toList
				if (stringList.head == brackets._2 || stringList.head == parens._2 || stringList.head == curls._2) {
					response += "NO"
				} else {
					response += recurse(List(stringList.head), stringList.tail)
				}
			}
		}

		response.toArray;
	}

	//
	// Substitute with your own input.  `braces` will only hande brackets, curly braces, and parenthesis.
	// It will deal with null and empty strings, but it will NOT deal with any other characters.
	//
	def main(args: Array[String]) {
		//println("Answers: ")
		for (x <- braces(Array(null, "" /*empty string!*/ , "(", ")", "{}[]()", "{[}]}", "{{{([]{}())}}}", "{[(({([])}))]}}", "({[(({([])}))]}"))) println(x)
	}
}
