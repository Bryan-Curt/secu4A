
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.Cipher;
//import java.net.UnknownHostException;

public class PC {

    static ServerSocket server;
    static int clientID = 0;

    public static void main(String ard[]) {

        try {
            Cipher chiffreur = Cipher.getInstance("AES");
            //chiffreur.init(Cipher.ENCRYPT_MODE, specification);
           // bytes = chiffreur.doFinal(args[1].getBytes());
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
}
