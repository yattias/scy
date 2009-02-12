package eu.scy.ws.example.api;

import java.util.List;

/**
 * Created: 10.feb.2009 10:44:50
 *
 * @author Bjørge Næss
 */
public interface ELO {

	void setId(Integer eloid);

	int getId();

	String getTitle();

	void setTitle(String title);

	Object getContent();
	void setContent(Object content);

	List<?extends ELO> getChildren();

	void addChildELO(ELO elo);

	void setChildren(List<? extends ELO> childELOs);
}
