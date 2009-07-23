package eu.scy.agents.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IThreadedAgent;

/**
 * This agents encapsulates an agent that is not written in Java.
 * 
 * @author weinbrenner
 * 
 */
public abstract class AbstractForeignAgent implements IThreadedAgent {

    private String name;
    
    private String id;

    private Process process;

    /**
     * Finds the absolute path to an executable file in the path. It is also possible to search for several names, which is particularly useful if it should work for multiple operating systems.
     * 
     * @param exeNames
     *            an array containing all file names to be searched
     * @return the path to a given executable
     */
    protected static File findExecutable(String... exeNames) {
        HashSet<String> exes = new HashSet<String>(Arrays.asList(exeNames));
        String pathVar = System.getenv("PATH");
        System.out.println(pathVar);
        String[] pathComps = pathVar.split(File.pathSeparator);
        for (String p : pathComps) {
            File f = new File(p);
            if (f.listFiles() != null) {
                for (File f1 : f.listFiles()) {
                    if (exes.contains(f1.getName())) {
                        return f1;
                    }
                }
            }
        }
        return null;
    }

    public AbstractForeignAgent(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public void start() throws AgentLifecycleException {
        new Thread(this).start();
    }

    public void run() {
        String[] cmdarray = getCommandline();
        try {
            process = Runtime.getRuntime().exec(cmdarray);
            final PrintStream errOs = routeErrTo();
            final PrintStream outOs = routeOutTo();
            if (outOs != null) {
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String buffer = null;
                            while ((buffer = br.readLine()) != null) {
                                outOs.println(buffer);
                            }
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            if (errOs != null) {
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                            String buffer = null;
                            while ((buffer = br.readLine()) != null) {
                                errOs.println(buffer);
                            }
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void kill() {
        process.destroy();
    }

    /**
     * Returns an array containing the commandline call for the agents. It must be implemented by all foreign agents.
     * 
     * @return an array containing the commandline call
     */
    protected abstract String[] getCommandline();

    /**
     * Returns the printstream to which STDERR should be routed. This method should be overridden if a programmer wants to interpret the output in some way. The simplest way to just print the output of the agent to a console is to pass System.err here.
     * 
     * @return the printstream to which STDERR should be routed.
     */
    protected PrintStream routeErrTo() {
        return null;
    }

    /**
     * Returns the printstream to which STDOUT should be routed. This method should be overridden if a programmer wants to interpret the output in some way. The simplest way to just print the output of the agent to a console is to pass System.in here.
     * 
     * @return the printstream to which STDOUT should be routed.
     */
    protected PrintStream routeOutTo() {
        return null;
    }

    @Override
    public boolean isStopped() {
        return !isRunning();
    }

    @Override
    public boolean isRunning() {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            // no other generic way of finding out if the process is still alive
            return true;
        }
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getId() {
        return id;
    }

}
