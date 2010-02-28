/*
 * CalculatorModel.fx
 *
 * Created on Dec 30, 2008, 5:56:49 PM
 */

package eu.scy.elobrowser.tool.calculator;

import java.lang.Double;

/**
 * @author dean
 */
public class KeyModel {
    public-init var character: String;
    public-init var description: String;
    public-init var action: function():Void;
}

public class CalculatorModel {
    public-init var displayLength = 9;
    public-read var display = "0" on replace {
        if( display.length() >= displayLength ) {
            display = display.substring(0, displayLength);
        }
    }

    public-read var keys = [
        createCharacterKey( "0" ),
        createCharacterKey( "1" ),
        createCharacterKey( "2" ),
        createCharacterKey( "3" ),
        createCharacterKey( "4" ),
        createCharacterKey( "5" ),
        createCharacterKey( "6" ),
        createCharacterKey( "7" ),
        createCharacterKey( "8" ),
        createCharacterKey( "9" ),
        createOperatorKey( "+", "plus", add ),
        createOperatorKey( "-", "minus", subtract ),
        createOperatorKey( "x", "multiply", multiply ),
        createOperatorKey( "\u00f7", "divide", divide ),
        createCharacterKey( "." ),
        KeyModel {
            character: "="
            description: "equals"
            action: function() {
                performOp( add );
                display = if (register == (register as Integer))
                    "{register as Integer}"
                else
                    "{register}";
                register = 0;
            }
        }
    ];

    var register = 0.0;
    var operation = add;
    var clearDisplayOnNextCharacter = true;

    package function appendToDisplay( character: String ) {
        if( character.matches( "[0-9\\.]" ) )
        display = "{display}{character}";
    }

    function createCharacterKey( character: String ) {
        KeyModel {
            character: character
            description: character
            action: function() {
                if (clearDisplayOnNextCharacter) {
                    display = "";
                    clearDisplayOnNextCharacter = false;
                }
                appendToDisplay( character );
            }
        }
    }

    function createOperatorKey( character:String, description:String, nextOp:function() ) {
        KeyModel {
            character: character
            description: description
            action: function() {
                performOp( nextOp );
            }
        }
    }

    function performOp( nextOp: function() ) {
        operation();
        operation = nextOp;
        clearDisplayOnNextCharacter = true;
    }

    function add(): Void {
        register = register + Double.parseDouble( display );
    }

    function subtract(): Void {
        register = register - Double.parseDouble( display );
    }

    function multiply(): Void {
        register = register * Double.parseDouble( display );
    }

    function divide(): Void {
        register = register / Double.parseDouble( display );
    }
}
