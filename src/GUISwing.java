/*
import com.google.zxing.WriterException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GUISwing extends JFrame {

    public GUISwing() {
        initComponents();
    }

    private void initComponents() {

        setTitle("Voters Keys Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton configurationButton = new JButton("Configure Bulletin Board address");
        JButton generateKeysButton = new JButton("Generate Voter Keys");

        configurationButton.addActionListener(e -> showConfigurationWindow());

        generateKeysButton.addActionListener(e -> showKeysGenerationWindow());

        setLayout(new GridLayout(2, 1));
        add(configurationButton);
        add(generateKeysButton);
        setLocationRelativeTo(null);
        pack();

    }

    private void showConfigurationWindow() {
        final JFrame frame = new JFrame("Configure Bulletin Board address");

        JLabel addressLabel = new JLabel("Ingrese dirección del Bulletin Board");
        final JTextField addressTextField = new JTextField();

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            String newAddress = addressTextField.getText();
            GenerateKeys.setBBAddress(newAddress);
            frame.setVisible(false);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.add(addressLabel);
        panel.add(addressTextField);

        frame.setLayout(new GridLayout(2, 1));
        frame.add(panel);
        frame.add(okButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void showKeysGenerationWindow() {

        JFrame frame = new JFrame("Generate Voter Keys");

        JLabel idLabel = new JLabel("Enter voter ID:");
        JTextField idTextField = new JTextField();

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> {
            String id = idTextField.getText();

            try {
                GenerateKeys.generateKeysAndUploadPublicKey(id);
            } catch (WriterException | IOException | NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }

            // TODO: Avisar que la clave fue creada

            frame.setVisible(false);

        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.add(idLabel);
        panel.add(idTextField);

        frame.setLayout(new GridLayout(2, 1));
        frame.add(panel);
        frame.add(okButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new GUISwing().setVisible(true));

    }

}
*/
