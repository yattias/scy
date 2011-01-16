package eu.scy.client.tools.formauthor;
import java.awt.Container;

public class DataFormElement {
	DataFormElementModel dfem;
	DataFormElementView dfev;
	DataFormElementController dfec;

	public DataFormElement(DataFormElementModel dfem,Container container,DataFormModel dfm) {
		this.dfem = dfem;
		dfev = new DataFormElementView(dfem, container);
		dfec = new DataFormElementController(dfem, dfev,dfm);
	}

}
