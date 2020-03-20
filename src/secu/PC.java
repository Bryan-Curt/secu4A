
import secu.ChatServer;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
//import java.net.UnknownHostException;

public class PC {

    static ServerSocket server;
    static int clientID = 0;

    public static void main(String ard[]) {

        try {
            File f = new File("key.txt");
            System.out.println("création d'un fichier et de la clé");
            //on génère la clé
            KeyGenerator keyGen;
            SecretKey cle = null;
            try {
                keyGen = KeyGenerator.getInstance("DESede");
                keyGen.init(168);
                cle = keyGen.generateKey();
                System.out.println("cle : " + new String(cle.getEncoded()));

                String message = "pif paf pouf";
                byte[] enc = encrypteur(message, cle);
                System.out.println("texte encrypte : " + new String(enc));
                
                String dec = decrypteur(enc, cle);
                System.out.println("texte decrypte : " + dec);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //on va désormais "transformer" la clé en string pour 
            //l'écrire dans un fichier
            String k2w;
            byte[] encoded = cle.getEncoded();
            k2w = Base64.encode(encoded);
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(k2w);
            bw.close();
            fw.close();
            System.out.println("écriture réalisée de la clé : " + k2w);
            server = new ServerSocket(4444, 5);//5 connexions clientes au plus
            go();
        } catch (Exception e) {
        }
    }

    public static void go() {

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true)//
                    {
                        try {
                            Socket client = server.accept();
                            // Faire tourner le socket qui s'occupe de ce client dans son propre thread et revenir en attente de la prochaine connexion
                            // Le chat avec l'entit� connect�e est encapsul� par une instance de ChatServer
                            Thread tAccueil = new Thread(new ChatServer(client, clientID));
                            tAccueil.start();
                            clientID++;
                        } catch (Exception e) {
                        }
                    }
                }
            });
            t.start();

        } catch (Exception i) {
            System.out.println("Impossible d'�couter sur le port 4444: serait-il occup�?");
            i.printStackTrace();
        }

    }

    public static byte[] encrypteur(final String message, SecretKey cle)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, cle);
        byte[] donnees = message.getBytes();

        return cipher.doFinal(donnees);
    }

    public static String decrypteur(final byte[] donnees, SecretKey cle)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, cle);

        return new String(cipher.doFinal(donnees));
    }
}
