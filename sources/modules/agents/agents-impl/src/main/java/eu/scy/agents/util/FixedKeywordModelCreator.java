package eu.scy.agents.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: fschulz
 * Date: 27.09.11
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public class FixedKeywordModelCreator {

    public static final String FIXED_KEYWORD_MODEL_DAT = "fixedKeywordModel.dat";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (!checkArguments(args)) {
            return;
        }

        Scanner scanner = new Scanner(new FileInputStream(args[0]));
        scanner.useDelimiter("\n");
        Set<String> keywordSet = new HashSet<String>();
        while (scanner.hasNext()) {
            String keyword = scanner.next();
            keywordSet.add(keyword);
        }

        String mission = args[1];
        String language = args[2];
        String prefix = ".";
        if (args.length > 3) {
            prefix = args[3];
        }
        String outputDir = createPath(prefix, mission, language);
        File outFile = new File(outputDir, FIXED_KEYWORD_MODEL_DAT);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
        out.writeObject(keywordSet);
        out.close();
        System.out.println("Saved keyword model in: " + outFile.getAbsolutePath());

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                new File(outputDir, FIXED_KEYWORD_MODEL_DAT)));
        Set<String> keywordsIn = (Set<String>) in.readObject();
        in.close();

        System.out.println("Test:");
        for (String keyword : keywordsIn) {
            System.out.println(keyword);
        }
    }

    private static boolean checkArguments(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage:");
            System.out.println("command <inputFile> <missionNumber> <language>");
            System.out.println("\t<missionNumber> = mission1|mission2|mission3|mission4|mission5");
            System.out.println("\t<language> = two letter code e.g. en, fr, el, no, nl");
            return false;
        }
        return true;
    }

    private static String createPath(String prefix, String mission, String language) {
        StringBuilder pathBuffer = new StringBuilder();
        pathBuffer.append(prefix);
        pathBuffer.append("/");
        pathBuffer.append(mission);
        pathBuffer.append("/");
        pathBuffer.append(language);
        return pathBuffer.toString();
    }

}
