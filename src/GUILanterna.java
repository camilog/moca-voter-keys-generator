/*
import java.io.*;
import java.security.*;

import com.google.zxing.WriterException;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.dialog.TextInputDialog;
import com.googlecode.lanterna.screen.Screen;

public class GUILanterna extends Window {

    public GUILanterna() {
        super("Generate Voter Keys");

        // Panel with the current bulletin board address
        Panel addressPanel = new Panel("Current Bulletin Board address");
        addressPanel.addComponent(new Label());

        // Add Bulletin-Board-address panel
        addComponent(addressPanel);
        updateAddressLabel((Label) addressPanel.getComponentAt(0));

        // Add button to set up BB address
        addComponent(new Button("Configure Bulletin Board address", () -> {
            // Retrieve string of the bulletin board address
            String newAddress = TextInputDialog.showTextInputBox(getOwner(),
                    "Bulletin Board address", "New Bulletin Board address", "", 20);

            // Set new bulletin board address and update label showing it
            GenerateKeys.setBBAddress(newAddress);
            updateAddressLabel((Label) addressPanel.getComponentAt(0));

            // Final message in case of success
            MessageBox.showMessageBox(getOwner(),
                    "Finalizado", "Nueva dirección del Bulletin Board exitosamente guardada.");
        }));

        // Add button to generate keys
        addComponent(new Button("Generate keys", () -> {
            // Retrieve ID of the voter, used later to verify the signature
            String id = TextInputDialog.showTextInputBox(getOwner(),
                    "Parameters", "ID of the Voter", "", 10);

            // Generate keys with the id given
            try {
                GenerateKeys.generateKeysAndUploadPublicKey(id);
            } catch (NoSuchAlgorithmException | WriterException | IOException e) {
                e.printStackTrace();
            }

            // Final message in case of success
            MessageBox.showMessageBox(getOwner(),
                    "Finalizado", "Se han generado exitosamente las claves privada y pública.\nEntregar imagen de clave privada y pública al votante.");
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

    private void updateAddressLabel(Label label) {
        label.setText(GenerateKeys.getBBAddress());
    }

    static public void main(String[] args) throws NoSuchAlgorithmException, WriterException, IOException {

        // Create window to display options
        GUILanterna myWindow = new GUILanterna();
        GUIScreen guiScreen = TerminalFacade.createGUIScreen();
        Screen screen = guiScreen.getScreen();

        // Start and configuration of the screen
        screen.startScreen();
        guiScreen.showWindow(myWindow, GUIScreen.Position.CENTER);
        screen.refresh();

        // Stopping screen at finalize application
        screen.stopScreen();

    }



}*/
