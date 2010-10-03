package eu.scy.client.desktop.localtoolbroker.accesschecker;

abstract public class PasswordCheckerForGenerator implements PasswordGenerator
{

	public boolean checkPassword(String userName, String configId, String password)
	{
		String realPassword = getPassword(userName,configId);
		if (realPassword!=null)
			return realPassword.equals(password);
		return false;
	}

}
