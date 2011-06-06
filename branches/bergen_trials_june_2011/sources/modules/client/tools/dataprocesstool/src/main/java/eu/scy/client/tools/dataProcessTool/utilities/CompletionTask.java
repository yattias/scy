/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

/**
 *
 * @author Marjolaine
 */
public class CompletionTask  implements Runnable{
    private String completion;
    private int position;
    private MyTextFieldCompletion field;

    public CompletionTask(MyTextFieldCompletion field, String completion, int position) {
        this.completion = completion;
        this.position = position;
        this.field = field;
    }


    @Override
    public void run() {
        field.insert(completion, position);
        field.setCaretPosition(position + completion.length());
        field.moveCaretPosition(position);
        field.setMode(MyTextFieldCompletion.Mode.COMPLETION);
    }

}
