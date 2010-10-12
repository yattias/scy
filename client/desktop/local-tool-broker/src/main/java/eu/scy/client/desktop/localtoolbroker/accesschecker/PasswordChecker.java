package eu.scy.client.desktop.localtoolbroker.accesschecker;

public interface PasswordChecker
{
	boolean checkPassword(String userName, String configId, String password);
}
