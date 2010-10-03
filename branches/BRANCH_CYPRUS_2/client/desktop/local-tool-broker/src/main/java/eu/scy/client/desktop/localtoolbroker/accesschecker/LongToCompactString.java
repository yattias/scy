package eu.scy.client.desktop.localtoolbroker.accesschecker;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LongToCompactString
{
	private final long mask32 = 0x0FFFFFFFFL;
	private final String valueChars;
	private final long radix;

	public LongToCompactString(String valueChars)
	{
		if ((valueChars==null)||(valueChars.length()<2))
		{
			throw new IllegalArgumentException("valueChars must contain at least 2 characters");
		}
		this.valueChars = valueChars;
		radix = valueChars.length();
	}

	public String getCompactString(long value)
	{
		if (value<0)
		{
			throw new IllegalArgumentException("value may not be negative, "+value);
		}
		StringBuffer buffer = new StringBuffer();
		long valueLeft = value;
		while (valueLeft!=0)
		{
			int charValue = (int)(valueLeft%radix);
			valueLeft /= radix;
			buffer.append(valueChars.charAt(charValue));
		}
		if (buffer.length()==0)
		{
			buffer.append(valueChars.charAt(0));
		}
		else
		{
			buffer.reverse();
		}
		return buffer.toString();
	}

	public String getCompactString(int value)
	{
		long longValue = value&mask32;
		return getCompactString(longValue);
	}
}
