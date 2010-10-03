/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.scyelo;

/**
 *
 * @author SikkenJ
 */
public interface ScyEloContentCreator<E> {

   public E createScyEloContent(ScyElo scyElo);

   public void updateEloContent(ContentTypedScyElo<E> scyElo);
}
