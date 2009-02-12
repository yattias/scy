package eu.scy.ws.example.api;

/**
 * Created: 11.feb.2009 09:54:29
 *
 * @author Bjørge Næss
 */
public interface ELOContent {

	int getType();

	void setType(int type);

	Object getContent();

	void setContent(Object content);
}
