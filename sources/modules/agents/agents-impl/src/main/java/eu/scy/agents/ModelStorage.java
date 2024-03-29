package eu.scy.agents;

import info.collide.sqlspaces.client.TupleSpace;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;

import java.io.*;

/**
 * The implementation of {@link IPersistentStorage}. Saves things as ELOs
 *
 * @author Florian Schulz
 */
public class ModelStorage implements IPersistentStorage {
    private static final Logger logger = Logger
            .getLogger(MissionUriMapping.class.getName());

    private MissionUriMapping mapping;

    private StringBuffer pathBuffer;

    /**
     * Create a new {@link ModelStorage} instance.
     */
    public ModelStorage(TupleSpace tupleSpace) {
        mapping = new MissionUriMapping(tupleSpace);
        pathBuffer = new StringBuffer();
    }

    private byte[] getByteArray(String missionName, String language, String key) {
        String path = computePath(missionName, language, key);
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        InputStream resourceAsStream = getClass().getClassLoader()
                .getResourceAsStream(path);
        if (resourceAsStream == null) {
            logger.debug("mode not found " + path);
            return null;
        }
        try {
            StreamUtils.copy(resourceAsStream, bytesOut);
        } catch (IOException e) {
            logger.warn("exception occured", e);
            return null;
        }
        byte[] byteArray = bytesOut.toByteArray();
        if (byteArray.length > 0) {
            return byteArray;
        }
        logger.debug("byte array for model " + path + " empty");
        return null;
    }

    private String computePath(String mission, String language, String key) {
        pathBuffer.setLength(0);
        pathBuffer.append(Mission.getForName(mission).getId());
        pathBuffer.append("/");
        pathBuffer.append(language);
        pathBuffer.append("/");
        pathBuffer.append(key);
        pathBuffer.append(".dat");
        return pathBuffer.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String mission, String language, String key) {
        if (mission.isEmpty()) {
            return null;
        }
        if (language.isEmpty()) {
            return null;
        }
        if (key.isEmpty()) {
            return null;
        }

        byte[] bytes = getByteArray(mission, language, key);
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    new ByteArrayInputStream(bytes));
            return (T) inputStream.readObject();
        } catch (IOException e) {
            // exception is allowed but returns null.
        } catch (ClassNotFoundException e) {
            // exception is allowed but returns null.
        }
        return null;
    }

    @Override
    public <T> void put(String mission, String language, String key, T object) {
        // not implemented
    }

}
