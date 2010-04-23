/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.copex.utilities;
/**
 * create material
 * @author Marjolaine
 */
public interface ActionAddMaterial {
    /* clic sur hide/show*/
    public void actionHideAddMaterial();
    public void actionShowAddMaterial();
    /* sauvegarde du texte de l'ajout de material*/
    public void saveText();
    /* met le texte de l'ajout de material*/
    public void setMaterial();
}
