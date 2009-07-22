package eu.scy.agents.roolo.elo.drawing;

public class RuntimeTest {

    public static void main(String[] args) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        long freeMemory = Runtime.getRuntime().freeMemory();

        System.out.println("Hello "+System.getProperty("user.name") +"! You have " +availableProcessors + " processors and " + freeMemory + " free Memory");
        System.out.println("By the way: Isn't it awesome to use such a great "+System.getProperty("os.name")+" on a "+System.getProperty("os.arch")+" architectur?");
    }
}
