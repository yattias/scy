/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping;

/**
 *
 * @author sikkenj
 */
public interface XmlMetadataDisplayMappingConverter
{

	public String toXmlString(MetadataDisplayMapping metadataDisplayMapping);

	public MetadataDisplayMapping fromXmlString(String xmlString);
}
