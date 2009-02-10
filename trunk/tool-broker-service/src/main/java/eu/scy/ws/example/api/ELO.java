package eu.scy.ws.example.api;

import java.util.List;

/**
 * Created: 10.feb.2009 10:44:50
 *
 * @author Bjørge Næss
 */
public interface ELO {
	String getTitle();

	void setTitle(String title);

	void setId(Integer eloid);

	int getId();

	String getContent();

	void setContent(String content);

	List<ELO> getChildren();

	void addChildELO(ELO elo);

	void setChildren(List<ELO> childELOs);
}
