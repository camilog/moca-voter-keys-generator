import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.screen.Screen;

public class GenerateKeys extends Window {

    private static final String ftpServer = "cjgomez.duckdns.org";
    private static final String user = "pi";
    private static final String pass = "CamiloGomez";

    public GenerateKeys() {
        super("Generate Voter Keys");

        // Add button to generate keys
        addComponent(new Button("Generate keys", () -> {
            // Retrieve ID of the voter, used later to verify the signature
            String id = com.googlecode.lanterna.gui.dialog.TextInputDialog.showTextInputBox(getOwner(), "Parameters", "ID of the Voter", "", 10);

            try {
                // Generate keys with the id given
                generateKeysAndUploadPublicKey(id);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    // Function to generate RSA Keys for Voters
    static private void generateKeysAndUploadPublicKey(String id) throws NoSuchAlgorithmException, WriterException, IOException {

        // Set instance RSA for generation of keys
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        // Configuration of 512 length-bit of modulus and a secure random number
        keyGen.initialize(512, new SecureRandom());

        // Generation of the keyPair
        KeyPair keyPair = keyGen.generateKeyPair();

        // Save in different files, the public and private key
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Encode to save keys in a string to generate QR-Codes later
        String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Tutorial to obtain publicKey from String
        /*
        byte[] publicKeyBytes = Base64.getDecoder().decode(stringPublicKey.getBytes("utf-8"));
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        PublicKey newPublicKey = publicKeyFactory.generatePublic(publicSpec);

        // Tutorial to obtain privateKey from String
        byte[] privateKeyBytes = Base64.getDecoder().decode(stringPrivateKey.getBytes("utf-8"));
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey newPrivateKey = privateKeyFactory.generatePrivate(privateSpec);
        */

        // Generate QR code images for public and private keys
        BitMatrix publicKeyBitMatrix = new QRCodeWriter().encode(stringPublicKey, BarcodeFormat.QR_CODE, 300, 300);
        BitMatrix privateKeyBitMatrix = new QRCodeWriter().encode(stringPrivateKey, BarcodeFormat.QR_CODE, 300, 300);

        // Create directories to stores keys and qrCode-images of the different keys
        File dir1 = new File("publicKeys_Key");
        File dir2 = new File("publicKeys_QR");
        File dir3 = new File("privateKeys_QR");
        File dir4 = new File("privateKeys_Key");
        dir1.mkdir();
        dir2.mkdir();
        dir3.mkdir();
        dir4.mkdir();

        // Set-up the OutputStreams to save in a file the different keys
        ObjectOutputStream publicStreamKey = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("publicKeys_Key/" + id + "publicKey.key")));
        ObjectOutputStream privateStreamKey = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("privateKeys_Key/" + id + "privateKey.key")));
        FileOutputStream publicStream = new FileOutputStream("publicKeys_QR/" + id + "publicKey" + ".png");
        FileOutputStream privateStream = new FileOutputStream("privateKeys_QR/" + id + "privateKey" + ".png");

        // Write in those OutputStreams the correspondent objects
        publicStreamKey.writeObject(publicKey);
        privateStreamKey.writeObject(privateKey);
        MatrixToImageWriter.writeToStream(publicKeyBitMatrix, "png", publicStream);
        MatrixToImageWriter.writeToStream(privateKeyBitMatrix, "png", privateStream);

        // Close every file
        publicStreamKey.close();
        privateStreamKey.close();
        publicStream.close();
        privateStream.close();

        // Upload PublicKey to BB
        File publicKeyFile = new File("publicKeys_Key/" + id + "publicKey.key");
        upload(ftpServer, user, pass, "votersPublicKeys/" + id + "_publicKey.key", publicKeyFile);

    }

    static public void main(String[] args) throws NoSuchAlgorithmException, WriterException, IOException {

        // Create window to display options
        GenerateKeys myWindow = new GenerateKeys();
        GUIScreen guiScreen = TerminalFacade.createGUIScreen();
        Screen screen = guiScreen.getScreen();

        // Start and configuration of the screen
        screen.startScreen();
        guiScreen.showWindow(myWindow, GUIScreen.Position.CENTER);
        screen.refresh();

        // Stopping screen at finalize application
        screen.stopScreen();

    }

    // Upload of the file
    /**
     * Upload a file to a FTP server. A FTP URL is generated with the
     * following syntax:
     * ftp://user:password@host:port/filePath;type=i.
     *
     * @param ftpServer , FTP server address (optional port ':portNumber').
     * @param user , Optional user name to login.
     * @param password , Optional password for user.
     * @param fileName , Destination file name on FTP server (with optional
     *            preceding relative path, e.g. "myDir/myFile.txt").
     * @param source , Source file to upload.
     * @throws IOException on error.
     */
    public static void upload( String ftpServer, String user, String password,
                               String fileName, File source ) throws IOException
    {
        if (ftpServer != null && fileName != null && source != null)
        {
            StringBuffer sb = new StringBuffer( "ftp://" );
            // check for authentication else assume its anonymous access.
            if (user != null && password != null)
            {
                sb.append( user );
                sb.append( ':' );
                sb.append( password );
                sb.append( '@' );
            }
            sb.append( ftpServer );
            sb.append( '/' );
            sb.append( fileName );
         /*
          * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file directory
          * listing
          */
            sb.append( ";type=i" );

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try
            {
                URL url = new URL( sb.toString() );
                URLConnection urlc = url.openConnection();

                bos = new BufferedOutputStream( urlc.getOutputStream() );
                bis = new BufferedInputStream( new FileInputStream( source ) );

                int i;
                // read byte by byte until end of stream
                while ((i = bis.read()) != -1)
                {
                    bos.write( i );
                }
            }
            finally
            {
                if (bis != null)
                    try
                    {
                        bis.close();
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                if (bos != null)
                    try
                    {
                        bos.close();
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
            }
        }
        else
        {
            System.out.println( "Input not available." );
        }
    }

}