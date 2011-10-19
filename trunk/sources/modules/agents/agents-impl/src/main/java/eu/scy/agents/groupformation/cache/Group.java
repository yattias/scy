package eu.scy.agents.groupformation.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/** @author fschulz */
public class Group implements Iterable<String> {

    private static final Group EMPTY_GROUP = new Group();

    private Set<String> group;
    private Object data;
    private String id;

    public Group() {
        group = new LinkedHashSet<String>();
    }

    public Group(Group toClone) {
        this();
        for ( String user : toClone ) {
            group.add(user);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return group.iterator();
    }

    public boolean isEmpty() {
        return group.isEmpty();
    }

    public void remove(String userToRemove) {
        group.remove(userToRemove);
    }

    public int size() {
        return group.size();
    }

    public void add(String user) {
        group.add(user);
    }

    public boolean contains(String user) {
        return group.contains(user);
    }

    public static Group emptyGroup() {
        return EMPTY_GROUP;
    }

    public Set<String> asSet() {
        return Collections.unmodifiableSet(group);
    }

    public void clear() {
        group.clear();
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        Group group1 = (Group) o;

        if ( group != null ? !group.equals(group1.group) : group1.group != null ) {
            return false;
        }
        if ( id != null ? !id.equals(group1.id) : group1.id != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + ( id != null ? id.hashCode() : 0 );
        return result;
    }
}
