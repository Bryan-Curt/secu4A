
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ChatServer implements Runnable {

    PrintWriter out;
    BufferedReader in;
    Socket s;
    Scanner keyboard;
    int index;
    String input;
    boolean doRun = true;
    static SecretKey key;

    public ChatServer(Socket a, int u) {
        s = a;
        keyboard = new Scanner(System.in);
        index = u;
    }

    public void run() {
        File f = new File("key.txt");
        FileReader fr;
        try {
            fr = new FileReader(f.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            String cle = br.readLine();
            br.close();
            fr.close();

            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());

                System.out.println("connexion de " + s.getInetAddress().toString() + " sur le port " + s.getPort());
                String talk = in.readLine();

                while (doRun) {
                    while (talk == null) {
                        talk = in.readLine();
                    }
                    System.out.println("message encodé : " + talk);
                    String dec = decrypteur(talk, cle);
                    System.out.println("message décodé: " + dec);
                    if (talk.compareToIgnoreCase("bye") == 0) {
                        System.out.println("shutting down following remote request");
                        doRun = false;
                    } else {
                        System.out.print("to client#" + index + "> ");
                        input = keyboard.nextLine();
                       
                        out.println(input);
                        out.flush();
                        if (input.compareToIgnoreCase("bye") == 0) {
                            System.out.println("server shutting down");
                            doRun = false;
                        } else {
                            talk = in.readLine();
                        }
                    }
                }
                s.close();
            } catch (Exception e) {
                System.out.println("raaah! what did u forget this time?");
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String decrypteur(final String d, String c)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Base64DecodingException {
        byte[] donnees = Base64.decode(d);
        byte[] key = Base64.decode(c);
        SecretKey cle = new SecretKeySpec(key, 0, key.length, "DESede");

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, cle);

        return new String(cipher.doFinal(donnees));
    }
}
