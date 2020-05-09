package acceler.ocdl.utils;

import sun.misc.BASE64Encoder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import javax.crypto.Cipher;

/**
 * @author Baiyu Huo
 *
 */
public class EncryptionUtil {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    public static PublicKey publicKey;
    public static PrivateKey  privateKey;

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    synchronized public static void generateKey() {
        if(publicKey==null){
            try {
                final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
                keyGen.initialize(1024);
                final KeyPair key = keyGen.generateKeyPair();

                publicKey = key.getPublic();
                privateKey = key.getPrivate();

                System.out.println("[public key] :"+publicKey);
                System.out.println("[private key] :"+privateKey);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String testData = "123456";
        byte[] pwd = encrypt(testData, publicKey);
        System.out.println(new String(pwd));
        System.out.println("[password]: " + encrypt(testData, publicKey));
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text) {
        byte[] dectyptedText = null;

        System.out.println("[d-decrypt- private]" + privateKey);
        System.out.println("[d-decript- public]" + publicKey);
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            dectyptedText = cipher.doFinal(text);

//            int offLen = 0;
//            int i = 0;
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            while(text.length - offLen > 0){
//                byte[] cache;
//                if(text.length - offLen > 128){
//                    cache = cipher.doFinal(text,offLen,128);
//                }else{
//                    cache = cipher.doFinal(text,offLen,text.length - offLen);
//                }
//                byteArrayOutputStream.write(cache);
//                i++;
//                offLen = 128 * i;
//
//            }
//            byteArrayOutputStream.close();
//            dectyptedText = byteArrayOutputStream.toByteArray();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }


    /**
     * get key string（by base64 encoded）
     * @return
     */
    synchronized  public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }



    /**
     * Test the EncryptionUtil
     */
    public static void main(String[] args) throws Exception {

        generateKey();
        String text = "123456";
        byte[] encriptedText = encrypt(text, publicKey);
        String plainText = decrypt(encriptedText);

        System.out.println(text);
        System.out.println(new String(encriptedText));
        System.out.println(plainText);

  /*      try {*/


    /*        // Check if the pair of keys are present else generate those.
            if (!areKeysPresent()) {
                // Method generates a pair of keys using the RSA algorithm and stores it
                // in their respective files
                generateKey();
            }

            final String originalText = "Text to be encrypted ";
            ObjectInputStream inputStream = null;

            // Encrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final byte[] cipherText = encrypt(originalText, publicKey);

            // Decrypt the cipher text using the private key.
            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
            final String plainText = decrypt(cipherText, privateKey);

            // Printing the Original, Encrypted and Decrypted Text
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted: " +cipherText.toString());
            System.out.println("Decrypted: " + plainText);*/

   /*     } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
