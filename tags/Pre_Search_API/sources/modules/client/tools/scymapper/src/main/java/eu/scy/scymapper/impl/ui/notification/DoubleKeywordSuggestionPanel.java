package eu.scy.scymapper.impl.ui.notification;

import java.awt.GridLayout;
import java.util.Collection;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.Localization;


public class DoubleKeywordSuggestionPanel extends KeywordSuggestionPanel {
	
    private KeywordSuggestionPanel relationPanel;
    
    private KeywordSuggestionPanelCollide conceptPanel;
    
    public DoubleKeywordSuggestionPanel(ConceptMapActionLogger actionLogger) {
        super(actionLogger, false);        
        setLayout(new GridLayout(2,1));
        relationPanel = new KeywordSuggestionPanelCollide(actionLogger);
        conceptPanel = new KeywordSuggestionPanelCollide(actionLogger);
        conceptPanel.setTitle(Localization.getString("Mainframe.KeywordSuggestion.ConceptTitle"));
        relationPanel.setTitle(Localization.getString("Mainframe.KeywordSuggestion.RelationTitle"));
        add(conceptPanel);
        add(relationPanel);
    }
    
    @Override
    public void setActionLogger(ConceptMapActionLogger actionLogger) {
        super.setActionLogger(actionLogger);
        conceptPanel.setActionLogger(actionLogger);
        relationPanel.setActionLogger(actionLogger);
    }
    
    @Override
    public void setSuggestions(String[] keywords, String[] categories, Collection<INodeFactory> nodeFactories, String type, boolean highlightChanged) {
        if (type.equals("concept")) {
            conceptPanel.setSuggestions(keywords, categories, nodeFactories, type, highlightChanged);
        } else {
            relationPanel.setSuggestions(keywords, categories, nodeFactories, type, highlightChanged);
        }
    }
    
}
