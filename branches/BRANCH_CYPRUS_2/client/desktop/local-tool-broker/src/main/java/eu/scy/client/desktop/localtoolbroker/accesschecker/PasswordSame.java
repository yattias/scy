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
public class PasswordSame extends PasswordCheckerForGenerator
{
	public PasswordSame()
	{
	}

	public String getPassword(String userName, String configId)
	{
		return userName;
	}
}
