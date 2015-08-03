import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.util.Base64;

public class GenerateKeys {

    private static String bulletinBoardAddress = "";

    // Function to set up the bulletin board address
    protected static void setBBAddress(String newAddress) {
        bulletinBoardAddress = newAddress;
    }

    protected static String getBBAddress() {
        return bulletinBoardAddress;
    }

    // Function to generate RSA Keys for Voters
    static protected void generateKeysAndUploadPublicKey(String id) throws NoSuchAlgorithmException, WriterException, IOException {

        // Set instance RSA for generation of keys
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        // Configuration of 512 length-bit of modulus and a secure random number
        keyGen.initialize(512, new SecureRandom());

        // Generation of the keyPair
        KeyPair keyPair = keyGen.generateKeyPair();

        // Create the variables for the public and private keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Encode to save keys in a string to generate the private key QR-Code later
        String stringPrivateKey = new BigInteger(privateKey.getEncoded()).toString();

        // Tutorial to obtain publicKey from String (Change the line of Base64 to the string format)
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

        // Generate QR code images for private keys
        BitMatrix privateKeyBitMatrix = new QRCodeWriter().encode(stringPrivateKey, BarcodeFormat.QR_CODE, 400, 400);

        // Create directories to stores keys and qrCode-images of the different keys
        // File dir1 = new File("publicKeys_Key");
        // File dir2 = new File("publicKeys_QR");
        // File dir3 = new File("privateKeys_QR");
        // File dir4 = new File("privateKeys_Key");
        // dir1.mkdir();
        // dir2.mkdir();
        // dir3.mkdir();
        // dir4.mkdir();

        // Show privateKey QR to the voter
        // TODO: Cambiar esto a que funcione en ambiente Lanterna -> Es necesario que este programa esté en Lanterna?
        BufferedImage img = MatrixToImageWriter.toBufferedImage(privateKeyBitMatrix);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        JOptionPane.showMessageDialog(null, imgLabel);

        // Set-up the OutputStreams to save in a file the different keys
        // ObjectOutputStream publicStreamKey = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("publicKeys_Key/" + id + "publicKey.key")));
        // ObjectOutputStream privateStreamKey = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("privateKeys_Key/" + id + "privateKey.key")));
        // FileOutputStream publicStream = new FileOutputStream("publicKeys_QR/" + id + "publicKey" + ".png");
        // FileOutputStream privateStream = new FileOutputStream("privateKeys_QR/" + id + "privateKey" + ".png");

        // Write in those OutputStreams the correspondent objects
        // publicStreamKey.writeObject(publicKey);
        // privateStreamKey.writeObject(privateKey);
        // MatrixToImageWriter.writeToStream(publicKeyBitMatrix, "png", publicStream);
        // MatrixToImageWriter.writeToStream(privateKeyBitMatrix, "png", privateStream);

        // Close every file
        // publicStreamKey.close();
        // privateStreamKey.close();
        // publicStream.close();
        // privateStream.close();

        // Upload PublicKey to BB
        String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        upload(bulletinBoardAddress, id, stringPublicKey);
    }

    // Upload of the publicKey as a JSON to the bbServer
    static private void upload(String publicKeyServer, String voterId, String publicKey) throws IOException {
        // Set the URL where to POST the public key
        URL obj = new URL(publicKeyServer);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Create JSON with the parameters
        String urlParameters = "{\"public_key\":{\"voter\":" + voterId + ",\"key\":\"" + publicKey + "\"}}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        con.getResponseCode();
    }

}
