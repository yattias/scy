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
public class PasswordReverse extends PasswordCheckerForGenerator
{
	public PasswordReverse()
	{
	}

	public String getPassword(String userName, String configId)
	{
		if (userName==null)
		{
			return null;
		}
		StringBuffer buffer = new StringBuffer(userName.length());
		for (int i = userName.length()-1; i>=0; i--)
		{
			buffer.append(userName.charAt(i));
		}
		return buffer.toString();
	}
}
