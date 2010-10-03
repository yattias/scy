/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

/**
 * interface saisie commentaires
 * @author Marjolaine
 */
public interface ActionComment {
    /* action de commenter*/
    public void actionComment();
    /* sauvegarde du texte des commentaires*/
    public void saveComment();
    /* met le texte  commentaires*/
    public void setComment();
    /* enter key pressed */
    public void enterKeyPressed();
}
