package i18n2xsl.console
import org.junit.Before
import org.junit.Test;
import org.junit.Assert._

class TestSimpleCommandHandlerFinder {

  val simpleCommandHandlerFinder = new SimpleCommandHandlerFinder()

  @Before
  def setUp(): Unit = {
  }

  @Test
  def test(): Unit = {
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one", ""))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one", "zero"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two", "zero"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two three", "zero"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one", "zero one"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two", "zero one two"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two three", "zero one two three"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two", "one"))
    assertEquals(None, simpleCommandHandlerFinder.checkCommandHandler("one two three", "one two"))
    testCheckCommandHandlerMethod("", "one","one")
    testCheckCommandHandlerMethod("", "one two","one two")
    testCheckCommandHandlerMethod("", "one two three","one two three")
    testCheckCommandHandlerMethod("", "one two three","one  two     three")
    testCheckCommandHandlerMethod("two", "one","one two")
    testCheckCommandHandlerMethod("two three four", "one","one two three four")
    testCheckCommandHandlerMethod("three", "one two","one two three")
    testCheckCommandHandlerMethod("three four", "one two","one two three four")
    testCheckCommandHandlerMethod("four", "one two three","one two three four")
    testCheckCommandHandlerMethod("", "one two three four","one two three four")
  }
  
  private def testCheckCommandHandlerMethod(answer: String, command: String, entered: String): Unit = {
    val answerSeq = answer.split(" ").filterNot(_.isEmpty).toSeq
    assertEquals(Some(answerSeq), simpleCommandHandlerFinder.checkCommandHandler(command,entered))
  }

}