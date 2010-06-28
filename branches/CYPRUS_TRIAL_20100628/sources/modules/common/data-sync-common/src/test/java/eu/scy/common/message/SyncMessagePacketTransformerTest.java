/**
 * 
 */
package eu.scy.common.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.scy.common.datasync.SyncObject;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Type;


/**
 * @author giemza
 *
 */
public class SyncMessagePacketTransformerTest {

	private DataSyncMessagePacketTransformer transformer;
	
	private SyncMessage message;
	
	private String path1;
	
	private String path2;
	
	private String path3;
	
	@Before
	public void setUp() {
		transformer = new DataSyncMessagePacketTransformer();
		message = new SyncMessage();
		message.setType(Type.answer);
		message.setEvent(Event.queryall);
		message.setToolId("scysim");
		message.setUserId("merkel@scy.collide.info/SMACK");
		
		SyncObject obj = null;
		for (int i = 0; i < 2; i++) {
			obj = new SyncObject();
			obj.setCreator("merkel@scy.collide.info/SMACK");
			obj.setToolname("scysim");
			obj.setProperty("value1", "lecker");
			obj.setProperty("value2", "Wurst");
			obj.setProperty("counter", Integer.toString(i));
			message.getSyncObjects().add(obj);
		}
		transformer.setObject(message);
		
		path1 = "/" + SyncMessage.PATH + "/" + "userId";
		path2 = "/" + SyncMessage.PATH + "/" + SyncObject.PATH + "s" + "/" + SyncObject.PATH + "[0]@" + "toolname";
		path3 = "/" + SyncMessage.PATH + "/" + SyncObject.PATH + "s" + "/" + SyncObject.PATH + "[1]/properties/value2";
	}
	
	@Test
	public void testXPaths() throws Exception {
		String[] paths = transformer.getXPaths();
		
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = false;
		
		for (String string : paths) {
			if(string.equals(path1)) {
				found1 = true;
			} else if(string.equals(path2)) {
				found2 = true;
			} else if(string.equals(path3)) {
				found3 = true;
			}
		}
		assertTrue("Path not found: " + path1, found1);
		assertTrue("Path not found: " + path2, found2);
		assertTrue("Path not found: " + path3, found3);
	}
	
	@Test
	public void testGetValueForXPath() throws Exception {
		assertEquals("merkel@scy.collide.info/SMACK", transformer.getValueForXPath(path1));
		assertEquals("scysim", transformer.getValueForXPath(path2));
		assertEquals("Wurst", transformer.getValueForXPath(path3));
	}
	
	@Test
	public void testSimpleMessage() throws Exception {
		message.getSyncObjects().clear();
		transformer.setObject(message);
		assertEquals("merkel@scy.collide.info/SMACK", transformer.getValueForXPath(path1));
	}
}
