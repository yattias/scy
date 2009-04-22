package eu.scy.client.tools.drawing;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

public class DialogUncaughtExceptionHandler implements UncaughtExceptionHandler
{

	public void uncaughtException(Thread t, Throwable e)
	{
		CharArrayWriter chars = new CharArrayWriter();
		PrintWriter printWriter = new PrintWriter(chars);
		printWriter.println("Uncaught exception in thread: " + t.getName());
		printWriter.println("Stack trace:");
		e.printStackTrace(printWriter);
		JOptionPane.showMessageDialog(null, chars.toString());
	}

}
