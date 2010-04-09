/**
 * 
 */
package eu.scy.common.smack;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jivesoftware.smack.packet.PacketExtension;

import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza
 *
 */
public class SmacketExtension implements PacketExtension {
	
	private SCYPacketTransformer transformer;
	
	public SmacketExtension(SCYPacketTransformer transformer) {
		this.transformer = transformer;
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName() {
		return transformer.getElementname();
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return transformer.getNamespace();
	}
	
	public SCYPacketTransformer getTransformer() {
		return transformer;
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#toXML()
	 */
	@Override
	public String toXML() {
		try {
			return transformer.toXML();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
