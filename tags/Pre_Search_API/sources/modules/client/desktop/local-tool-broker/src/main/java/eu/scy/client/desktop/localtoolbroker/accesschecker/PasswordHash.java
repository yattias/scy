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
public class PasswordHash extends PasswordCheckerForGenerator
{
	private final String fillers = "~!@#%^*()";
	private final int neededBaseLength = 6;
	private LongToCompactString longToCompactString;

	public PasswordHash(String valueChars)
	{
		longToCompactString = new LongToCompactString(valueChars);
	}

	public String getPassword(String userName, String configId)
	{
		if (userName==null)
		{
			return null;
		}
		String passwordBase = userName;
		if ((configId!=null)&&(configId.length()>0))
		{
			passwordBase += configId;
		}
		if (passwordBase.length()<neededBaseLength)
		{
			int nrOfCharactersToAdd = neededBaseLength-passwordBase.length();
			passwordBase += fillers.substring(fillers.length()-nrOfCharactersToAdd);
		}
		return longToCompactString.getCompactString(passwordBase.hashCode());
	}
}
