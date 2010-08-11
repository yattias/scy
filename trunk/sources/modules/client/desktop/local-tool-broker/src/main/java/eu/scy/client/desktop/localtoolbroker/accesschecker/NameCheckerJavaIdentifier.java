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
public class NameCheckerJavaIdentifier implements NameChecker
{
	private int minimumNameLength;

	public NameCheckerJavaIdentifier()
	{
		minimumNameLength = 3;
	}

	public String checkAndCleanName(String name) throws IllegalArgumentException
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name may not be null");
		}
		String cleanName = name.trim().toLowerCase();
		if (cleanName.length() < getMinimumNameLength())
		{
			throw new IllegalArgumentException("Name must contain at least " + getMinimumNameLength() +
														  " characters");
		}
		if (!isValidName(cleanName))
		{
			throw new IllegalArgumentException("Name contains invalid characters");
		}
		return cleanName;
	}

	private boolean isValidName(String name)
	{
		if (!Character.isJavaIdentifierStart(name.charAt(0)))
		{
			return false;
		}
		for (int i = 1; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (!Character.isJavaIdentifierPart(c) && (c != ' '))
			{
				return false;
			}
		}
		return true;
	}

	public void setMinimumNameLength(int minimumNameLength)
	{
		if (minimumNameLength<0)
			throw new IllegalArgumentException("Minimum name length cannot be negative");
		this.minimumNameLength = minimumNameLength;
	}

	public int getMinimumNameLength()
	{
		return minimumNameLength;
	}
}
