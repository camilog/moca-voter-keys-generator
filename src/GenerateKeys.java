import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;

public class GenerateKeys {

    private static String bulletinBoardAddress = "";
    private static String votersPublicKeysSubDomain = "/voters_public_keys";

    // TODO: Implement user and pass verification in order to upload the voter public key
    // private static String user, pass;

    // Function to generate RSA Keys for the voter
    static protected void generateKeysAndUploadPublicKey(String id) throws NoSuchAlgorithmException, WriterException, IOException {

        // If there's already a public key of this id uploaded to the BB, delete it
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

        // Encode to save private key into a string to generate the QR-Code later
        String stringPrivateKey = new BigInteger(privateKey.getEncoded()).toString();

        // Generate QR code images for private key
        BitMatrix privateKeyBitMatrix = new QRCodeWriter().encode(stringPrivateKey, BarcodeFormat.QR_CODE, 400, 400);

        // TODO: Change this to work with a dialog of JavaFX
        // Display privateKey QR to the voter
        BufferedImage img = MatrixToImageWriter.toBufferedImage(privateKeyBitMatrix);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        JOptionPane.showMessageDialog(null, imgLabel);

        // Upload PublicKey to BB
        String stringPublicKey = new BigInteger(publicKey.getEncoded()).toString();
        upload(id, stringPublicKey);
    }

    // Check if there's a public key uploaded to the BB, if so is necessary to delete it
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

        // Serialize the JSON response to an Object (VoterPublicKeyResponse)
        String jsonString = response.toString();
        Gson gson = new Gson();
        VoterPublicKeyResponse voterPublicKeyResponse = gson.fromJson(jsonString, VoterPublicKeyResponse.class);

        // Check if there's already a key on the BB, if so, delete it
        if (voterPublicKeyResponse.error == null) {

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

    // Function to retrieve the bulletin board address
    protected static String getBBAddress() {
        return bulletinBoardAddress;
    }

}
