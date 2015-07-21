import java.io.*;
import java.security.*;

import com.google.zxing.WriterException;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.screen.Screen;

public class GenerateKeys_light extends Window {

    public GenerateKeys_light() {
        super("Generate Voter Keys");

        // Add button to generate keys
        addComponent(new Button("Generate keys", () -> {
            // Retrieve ID of the voter, used later to verify the signature
            String id = com.googlecode.lanterna.gui.dialog.TextInputDialog.showTextInputBox(getOwner(), "Parameters", "ID of the Voter", "", 10);

            try {
                // Generate keys with the id given
                GenerateKeys_CORE.generateKeysAndUploadPublicKey(id);
            } catch (NoSuchAlgorithmException | WriterException | IOException e) {
                e.printStackTrace();
            }

            // Final message in case of success
            MessageBox.showMessageBox(getOwner(), "Finalizado", "Se han generado exitosamente las claves privada y pública.\nEntregar imagen de clave privada y pública al votante.");
        }));

        // Add button to finalize application
        addComponent(new Button("Exit application", () -> {
            // Close window properly and finalize application
            getOwner().getScreen().clear();
            getOwner().getScreen().refresh();
            getOwner().getScreen().setCursorPosition(0, 0);
            getOwner().getScreen().refresh();
            getOwner().getScreen().stopScreen();
            System.exit(0);
        }));
    }

    static public void main(String[] args) throws NoSuchAlgorithmException, WriterException, IOException {

        // Create window to display options
        GenerateKeys_light myWindow = new GenerateKeys_light();
        GUIScreen guiScreen = TerminalFacade.createGUIScreen();
        Screen screen = guiScreen.getScreen();

        // Start and configuration of the screen
        screen.startScreen();
        guiScreen.showWindow(myWindow, GUIScreen.Position.CENTER);
        screen.refresh();

        // Stopping screen at finalize application
        screen.stopScreen();

    }



}