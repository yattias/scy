package eu.scy.tools.math.doa.json;

import java.util.ArrayList;

public class ToolbarCollection {

    ArrayList<IToolbarShape> shapes = new ArrayList<IToolbarShape>();

    public void add(IToolbarShape shape) {
    	shapes.add(shape);
    }
}
