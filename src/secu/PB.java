
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PB {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException {
        InetAddress addr;
        Socket client;
        PrintWriter out;
        BufferedReader in;
        String input;
        String userInput;
        boolean doRun = true;
        //lecture de la clé
        File f = new File("key.txt");
        FileReader fr = new FileReader(f.getAbsoluteFile());
        BufferedReader br = new BufferedReader(fr);
        String cle = br.readLine();
        br.close();
        fr.close();
        //--

        Scanner k = new Scanner(System.in);

        try {
            client = new Socket("localhost", 4444);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            
            byte[] key = Base64.decode(cle);
            SecretKey key2 = new SecretKeySpec(key, 0, key.length, "DESede");
            

            System.out.print("enter msg> ");
            userInput = k.nextLine();
            
            byte[] u = encrypteur(userInput,key2);
            String output = Base64.encode(u);
            
            out.println(output);
            out.flush();
            System.out.println("done");
            
            

            if (userInput.compareToIgnoreCase("bye") == 0) {
                System.out.println("shutting down");
                doRun = false;
            } else {
                while (doRun) {
                    input = in.readLine();
                    while (input == null) {
                        input = in.readLine();
                    }

                    System.out.println(input);
                    if (input.compareToIgnoreCase("bye") == 0) {
                        System.out.println("client shutting down from server request");
                        doRun = false;
                    } else {
                        System.out.print("enter msg> ");
                        userInput = k.nextLine();
                        out.flush();
                        if (userInput.compareToIgnoreCase("bye") == 0) {
                            System.out.println("shutting down");
                            doRun = false;
                        }

                    }
                }
            }
            client.close();
            k.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
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
}
