package eu.scy.mobile.toolbroker;

/**
 * Created by IntelliJ IDEA.
 * User: Bj�rge N�ss
 * Date: 13.mar.2009
 * Time: 12:46:40
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceClient {
	public String getURI();
	public byte[] getRawData(String path);
}
