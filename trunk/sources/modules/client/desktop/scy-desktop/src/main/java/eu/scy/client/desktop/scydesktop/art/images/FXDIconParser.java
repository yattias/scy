package eu.scy.client.desktop.scydesktop.art.images;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FXDIconParser {

    public FXDIconParser() {
	File file = new File("..\\..\\src\\main\\java\\eu\\scy\\client\\desktop\\scydesktop\\art\\images\\content.fxd");
	ArrayList<String> group = null;
	BufferedReader reader = null;

	try {
	    reader = new BufferedReader(new FileReader(file));
	    String text = null;
	    String name = null;

	    while ((text = reader.readLine()) != null) {
		if (text.contains("Group {")) {
		    text = reader.readLine();
		    group = new ArrayList<String>();
		    name = text.substring(8, text.length()-1);
		    name = name.replace(" ", "_");
		    name = name.substring(0,1).toUpperCase()+name.substring(1)+"Icon";
		    System.out.println("parsing: " + name);
		    while (!(text.contains("]") && text.contains("},"))) {
			group.add(text);
			text = reader.readLine();
		    }
		    group.add("},");
		    createClass(group, name);
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (reader != null) {
		    reader.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void main(String[] args) {
	FXDIconParser fXDIconParser = new FXDIconParser();
    }

    private void createClass(ArrayList<String> group, String name) {
	try {
	    FileWriter outFile = new FileWriter("..\\..\\src\\main\\java\\eu\\scy\\client\\desktop\\scydesktop\\art\\eloicons\\"+name+".fx");
	    PrintWriter out = new PrintWriter(outFile);
	    writeHeader(out, name);
	    for (String s : group) {
		if (s.contains("id:") || s.contains("visible:")) {
		    // doing nothing, skip these lines
		} else {
		    out.println(s);
		}
	    }
	    writeFooter(out);
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void writeHeader(PrintWriter out, String name) {
	out.println("package eu.scy.client.desktop.scydesktop.art.eloicons;");
	out.println();
	out.println("import javafx.scene.CustomNode;");
	out.println("import javafx.scene.Node;");
	out.println("import javafx.scene.Group;");
	out.println("import javafx.scene.shape.*;");
	out.println("import javafx.scene.paint.Color;");
	out.println("import javafx.scene.transform.Transform;");
	out.println("import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;");
	out.println("/**");
	out.println(" * @author lars");
	out.println(" */");
	out.println("public class "+name+" extends CustomNode {");
	out.println();
	out.println("public var windowColorScheme: WindowColorScheme;");
	out.println();
	out.println("public function createNode(): Node {");
	out.println();
	out.println("return Group {");
	out.println();
    }

    private void writeFooter(PrintWriter out) {
	out.println("]");
	out.println("}");
	out.println("}");
	out.println("}");
    }
}
