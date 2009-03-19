package eu.scy.communications.message;

public interface IMessageTranslator {
    
    public abstract Object getObject(IScyMessage scyMessage);    
    public abstract IScyMessage getScyMessage(Object object);
    
}