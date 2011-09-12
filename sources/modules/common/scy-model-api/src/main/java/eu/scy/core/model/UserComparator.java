/**
 * 
 */
package eu.scy.core.model;

import java.util.Comparator;


/**
 * @author giemza
 *
 */
public class UserComparator implements Comparator<User> {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(User user1, User user2) {
        return user1.getUserDetails().getUsername().compareTo(user2.getUserDetails().getUsername());
    }

}
