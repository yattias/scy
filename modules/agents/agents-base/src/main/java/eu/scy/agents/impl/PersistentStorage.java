package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.IPersistentStorage;

public class PersistentStorage implements IPersistentStorage {
    
    private static final Object PERSISTENT_STORAGE = "persistent_storage_1_0";
    private TupleSpace tupleSpace;
    
    public PersistentStorage() {
        try {
            tupleSpace = new TupleSpace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        try {
            Tuple t = tupleSpace.read(new Tuple(PERSISTENT_STORAGE, key, Field.createWildCardField()));
            return (T) t.getField(2).getValue();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public <T> void put(String key, T value) {
        try {
            tupleSpace.write(new Tuple(PERSISTENT_STORAGE, key, value));
        } catch (TupleSpaceException e) {
            throw new RuntimeException(e);
        }
    }
}
