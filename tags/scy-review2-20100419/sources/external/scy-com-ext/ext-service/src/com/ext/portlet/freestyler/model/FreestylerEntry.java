package com.ext.portlet.freestyler.model;

import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;

/**
 * Model for the hole freestyler document composition. An freestyler entry has
 * information about the freestyler images, the original xml file and freestyler
 * folder.
 * 
 * @see FreestylerImage
 * @see FreestylerFolder
 * @see com.ext.portlet.freestyler.model.FreestylerEntryModel
 * @see com.ext.portlet.freestyler.model.impl.FreestylerEntryImpl
 * @see com.ext.portlet.freestyler.model.impl.FreestylerEntryModelImpl
 * 
 */
public interface FreestylerEntry extends FreestylerEntryModel, ResourceViewInterface {
}
