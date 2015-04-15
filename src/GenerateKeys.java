import java.io.*;
import java.security.*;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateKeys {

    static private void generateKeys(String id) throws NoSuchAlgorithmException, WriterException, IOException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        keyGen.initialize(512, new SecureRandom());

        KeyPair keyPair = keyGen.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Obtener PublicKey a partir de String
        /*
        byte[] publicKeyBytes = Base64.getDecoder().decode(stringPublicKey.getBytes("utf-8"));
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        PublicKey newPublicKey = publicKeyFactory.generatePublic(publicSpec);

        // Obtener PrivateKey a partir de String
        byte[] privateKeyBytes = Base64.getDecoder().decode(stringPrivateKey.getBytes("utf-8"));
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey newPrivateKey = privateKeyFactory.generatePrivate(privateSpec);
        */

        BitMatrix publicKeyBitMatrix = new QRCodeWriter().encode(stringPublicKey, BarcodeFormat.QR_CODE, 300, 300);
        BitMatrix privateKeyBitMatrix = new QRCodeWriter().encode(stringPrivateKey, BarcodeFormat.QR_CODE, 300, 300);

        File dir1 = new File("publicKeys_Key");
        File dir2 = new File("publicKeys_QR");
        File dir3 = new File("privateKeys_QR");
        dir1.mkdir();
        dir2.mkdir();
        dir3.mkdir();

        ObjectOutputStream publicStreamKey = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("publicKeys_Key/" + id + "publicKey.key")));
        FileOutputStream publicStream = new FileOutputStream("publicKeys_QR/" + id + "publicKey" + ".png");
        FileOutputStream privateStream = new FileOutputStream("privateKeys_QR/" + id + "privateKey" + ".png");

        publicStreamKey.writeObject(publicKey);
        MatrixToImageWriter.writeToStream(publicKeyBitMatrix, "png", publicStream);
        MatrixToImageWriter.writeToStream(privateKeyBitMatrix, "png", privateStream);

        publicStreamKey.close();
        publicStream.close();
        privateStream.close();

    }

    static public void main(String[] args) throws NoSuchAlgorithmException, WriterException, IOException {
        System.out.println("Bienvenido al programa generador de claves.");
        System.out.print("Ingrese RUT (id) del votante (sin puntos ni guión, con dígito verificador): ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String id = br.readLine();
        generateKeys(id);

        System.out.println("Se han generado exitosamente las claves privada y pública");
        System.out.println("Entregar imagen de clave privada y pública al votante");
    }

}