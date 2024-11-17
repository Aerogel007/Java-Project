package JavaFX;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/*
this has all the functionality realted to copying the text in the output textbox to clipboard
 */
public class ClipBoard {

    /**
     * Copies the given text to the system clipboard.
     *
     * @param text The text to be copied to the clipboard.
     */
    public static void copyToClipboard(String sentence) {
        String text = sentence;
        if (text == null || text.isEmpty()) {
            System.out.println("No text provided to copy.");
            return;
        }
        else{
        System.out.println("text :"+ text);
        // Create a StringSelection object with the provided text
        StringSelection stringSelection = new StringSelection(text);

        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Set the text into the clipboard
        clipboard.setContents(stringSelection, null);

        System.out.println("Text copied to clipboard!");
        }
    }
    
     /**
     * Extracts the first sentence without the numerical bullet from the given TextArea.
     * 
     * @param textArea The TextArea containing text with numbered bullets.
     * @return The first sentence without the numerical bullet.
     */
   

}
