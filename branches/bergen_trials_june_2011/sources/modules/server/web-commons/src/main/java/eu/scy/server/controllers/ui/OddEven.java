package eu.scy.server.controllers.ui;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.mar.2010
 * Time: 12:19:47
 * To change this template use File | Settings | File Templates.
 */
public class OddEven {

    private int rowCounter;
    String currentValue = ODD;

    public static final String EVEN = "tablerow-even";
    public static final String ODD = "tablerow-odd";


    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getOddEven() {
        if(getCurrentValue().equals(EVEN)) {
            setCurrentValue(ODD);
            return EVEN;
        } else if(getCurrentValue().equals(ODD)){
            setCurrentValue(EVEN);
            return ODD;
        }

        return "bibibib";

    }

    

}
