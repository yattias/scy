package eu.scy.agents.api;

public interface IPersistentStorage {
    
    public <T> void put(String key, T object);
    
    public <T> T get(String key);
    
}
