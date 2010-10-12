package eu.scy.scymapper.impl.configuration;

import eu.scy.core.model.impl.pedagogicalplan.ToolImpl;
import eu.scy.core.model.pedagogicalplan.Tool;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 11:31:06
 */
public class SCYMapperTool extends ToolImpl implements Tool {
	private String id = "scymapper";
	private String name = "SCYMapper";
	private String description = "SCYMapper is a concept mapping tool";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
