import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.util.Base64;

public class GenerateKeys {

    private static String bulletinBoardAddress = "";
    private static String votersPublicKeysSubDomain = "/voters_public_keys";
    private static String user, pass;

    // Function to generate RSA Keys for Voters
    static protected void generateKeysAndUploadPublicKey(String id) throws NoSuchAlgorithmException, WriterException, IOException {

        checkAndDeletePreviousRecordOnBB(id);

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

        // TODO: Change this to work with a dialog of JavaFX
        // Show privateKey QR to the voter
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
        //String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String stringPublicKey = new BigInteger(publicKey.getEncoded()).toString();
        upload(id, stringPublicKey);
    }

    private static void checkAndDeletePreviousRecordOnBB(String voterId) throws IOException {
        // Set the URL where to POST the public key
        URL obj = new URL(bulletinBoardAddress + votersPublicKeysSubDomain + "/" + voterId);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request header
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.getResponseCode();

        // Receive the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        String jsonString = response.toString();
        Gson gson = new Gson();
        VoterPublicKeyResponse voterPublicKeyResponse = gson.fromJson(jsonString, VoterPublicKeyResponse.class);

        if (voterPublicKeyResponse.error == null) {
            // DELETE entry on BB
            // Set the URL where to DELETE the public key
            obj = new URL(bulletinBoardAddress + votersPublicKeysSubDomain + "/" + voterId + "?rev=" + voterPublicKeyResponse._rev);
            con = (HttpURLConnection) obj.openConnection();

            // Add request header
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");
            con.getResponseCode();
        }

    }

    // Upload of the publicKey as a JSON to the bbServer
    static private void upload(String voterId, String publicKey) throws IOException {
        // Set the URL where to POST the public key
        URL obj = new URL(bulletinBoardAddress + votersPublicKeysSubDomain + "/" + voterId);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request header
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        // Create JSON with the parameters
        String urlParameters = "{\"voter_id\":" + voterId + ",\"value\":" + publicKey + "}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        con.getResponseCode();
    }

    // Function to set up the bulletin board address
    protected static void setBBAddress(String newAddress) {
        bulletinBoardAddress = newAddress;
    }

    protected static String getBBAddress() {
        return bulletinBoardAddress;
    }

}
