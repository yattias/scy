package eu.scy.core.openfire;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 15:23:14
 * To change this template use File | Settings | File Templates.
 */
public interface OpenFireService {
    @Override
    XMPPConnection getConnection(String userName1, String password1) throws XMPPException;

    String getUsernameWithHost(String username);

    @Override
    String getHost();

    @Override
    void setHost(String host);
}
