/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * action de type choice
 * TODO condition
 * uses data
 * @author Marjolaine
 */
public class InitialActionChoice extends InitialNamedAction{

    public InitialActionChoice(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat) {
        super(dbKey, code, libelle, isSetting, variable, draw, repeat);
    }

}
