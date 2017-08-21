
object Solution {

	private final val brackets = ('[', ']')
	private final val parens = ('(', ')')
	private final val curls = ('{', '}')

	def braces(values: Array[String]): Array[String] = {
		def recurse(firstChar: List[Char], chars: List[Char]): String = {

			(firstChar, chars) match {
				case (Nil, Nil) => "YES"
				case (h::t, Nil) => "NO"
				case (Nil, h::t) => return recurse(List(h), t)

				case(firstHead::firstTail, charHead::charTail) => {

					(firstHead, charHead) match{
						case (brackets._1, brackets._2) => return recurse(firstTail, charTail)
						case (brackets._1, brackets._1) => return recurse(charHead :: firstChar, charTail)
						case (brackets._1, parens._1) => return recurse(charHead :: firstChar, charTail)
						case (brackets._1, curls._1) => return recurse(charHead :: firstChar, charTail)
						case (brackets._1, _) => "NO"

						case (parens._1, parens._2) => return recurse(firstTail, charTail)
						case (parens._1, parens._1) => return recurse(charHead :: firstChar, charTail)
						case (parens._1, brackets._1) => return recurse(charHead :: firstChar, charTail)
						case (parens._1, curls._1) => return recurse(charHead :: firstChar, charTail)
						case (parens._1, _) => "NO"

						case (curls._1, curls._2) => return recurse(firstTail, charTail)
						case (curls._1, curls._1) => return recurse(charHead :: firstChar, charTail)
						case (curls._1, brackets._1) => return recurse(charHead :: firstChar, charTail)
						case (curls._1, parens._1) => return recurse(charHead :: firstChar, charTail)
						case (curls._1, _) => "NO"

						case _ => return recurse(charHead :: firstChar, charTail)
					}
				}
			}
		}

		import scala.collection.mutable.ListBuffer
		val response = new ListBuffer[String]



		for()



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
					response += recurse(Nil, stringList)
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
