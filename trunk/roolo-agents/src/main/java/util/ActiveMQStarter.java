package util;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQStarter {
    
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
    }
}
