
import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rodri
 */
public class PromptComboBoxRenderer extends BasicComboBoxRenderer {
    
    private String prompt;

    /*
     *  Set the text to display when no item has been selected.
     */
    public PromptComboBoxRenderer(String prompt)
    {
        this.prompt = prompt;
    }

    /*
     *  Custom rendering to display the prompt text when no item is selected
     */
    public Component getListCellRendererComponent(
        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null)
            setText( prompt );

        return this;
    }
 
        
}
