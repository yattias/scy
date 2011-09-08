package i18n2xsl.console

import org.junit.Before
import org.junit.Test
import org.junit.Assert._

class TestAdvancedCommandHandlerFinder {
  val advancedCommandHandlerFinder = new AdvancedCommandHandlerFinder()

  @Before
  def setUp(): Unit = {
  }

  @Test
  def testAnalyseCommand() {
	  assertEquals(("one", false),advancedCommandHandlerFinder.analyseCommand("one"))
	  assertEquals(("[one", false),advancedCommandHandlerFinder.analyseCommand("[one"))
	  assertEquals(("one]", false),advancedCommandHandlerFinder.analyseCommand("one]"))
	  assertEquals(("one", true),advancedCommandHandlerFinder.analyseCommand("[one]"))
  }

  @Test
  def testCheckCommandHandler(): Unit = {
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one", ""))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one", "zero"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two", "zero"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two three", "zero"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one", "zero one"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two", "zero one two"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two three", "zero one two three"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two", "one"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one two three", "one two"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("one [two] three", "one two"))
    assertEquals(None, advancedCommandHandlerFinder.checkCommandHandler("[one] [two] three", "one two"))
    testCheckCommandHandlerMethod("", 0, "one", "one")
    testCheckCommandHandlerMethod("", 0, "one two", "one two")
    testCheckCommandHandlerMethod("", 0, "one two three", "one two three")
    testCheckCommandHandlerMethod("", 0, "one two three", "one  two     three")
    testCheckCommandHandlerMethod("two", 0, "one", "one two")
    testCheckCommandHandlerMethod("two three four", 0, "one", "one two three four")
    testCheckCommandHandlerMethod("three", 0, "one two", "one two three")
    testCheckCommandHandlerMethod("three four", 0, "one two", "one two three four")
    testCheckCommandHandlerMethod("four", 0, "one two three", "one two three four")
    testCheckCommandHandlerMethod("", 0, "one two three four", "one two three four")
    testCheckCommandHandlerMethod("", 1, "one", "on")
    testCheckCommandHandlerMethod("", 1, "one", "o")
    testCheckCommandHandlerMethod("", 1, "one two", "on two")
    testCheckCommandHandlerMethod("", 2, "one two", "on t")
    testCheckCommandHandlerMethod("", 1, "one two three", "one two thre")
    testCheckCommandHandlerMethod("", 1, "one two three", "one tw three")
    testCheckCommandHandlerMethod("", 2, "one two three", "on tw three")
    testCheckCommandHandlerMethod("", 3, "one two three", "o  t     thre")
    testCheckCommandHandlerMethod("", 0, "one [two]", "one two")
    testCheckCommandHandlerMethod("", 0, "one [two]", "one")
    testCheckCommandHandlerMethod("three", 0, "one [two]", "one two three")
    testCheckCommandHandlerMethod("three", 0, "one [two]", "one three")
    testCheckCommandHandlerMethod("three", 0, "[one] two", "one two three")
    testCheckCommandHandlerMethod("three", 0, "[one] two", "two three")
    testCheckCommandHandlerMethod("six", 0, "[one] two [three] [four] five", "two five six")
    testCheckCommandHandlerMethod("six", 2, "[one] two [three] [four] five", "o two t five six")
    testCheckCommandHandlerMethod("six", 4, "[one] two [three] [four] five", "o two t f fi six")
  }

  private def testCheckCommandHandlerMethod(answer: String, nrOfExtends: Int, command: String, entered: String): Unit = {
    val answerSeq = answer.split(" ").filterNot(_.isEmpty).toSeq
    val expectedAnswer = Some(new CheckResult(nrOfExtends, answerSeq))
    assertEquals(expectedAnswer, advancedCommandHandlerFinder.checkCommandHandler(command, entered))
  }
  
}