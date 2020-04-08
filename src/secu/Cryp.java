import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.util.Base64.*;


public class Cryp {

	/**
	 * @param args
	 */
	
	final String encryptedValue = "I saw the real you" ;
	String secKey = "ubutru";

	
	public static void main(String[] args) {
		
		String encryptedVal = null;
		String valueEnc = "aazzaa"; //chaine de caractère initiale

	    try {
	    		    	
	    	 KeyGenerator generator = KeyGenerator.getInstance("AES"); //On utilise l'algorithme de chifrement AES
	            generator.init(128); //init de la clé d'une longueur de 128 bits
	            SecretKey key = generator.generateKey(); //la clé est créée
	            Cipher cipher = Cipher.getInstance("AES"); //on précise l'algorithme
	            cipher.init(Cipher.ENCRYPT_MODE, key); // on s'initie ne mode chiffrement
	            byte[] res = cipher.doFinal(valueEnc.getBytes()); //on transforme la chaine de caractère initiale en tablea d'octets
	            String res_str =  Base64.encode(res); // transforme lle tableau d'octet en string (on a donc ici la chaine de caractère encodée (illisible à moins d'avoir la clé)
	           
	            cipher.init(Cipher.DECRYPT_MODE, key); //on passe ne mode déchiffrement
	               
	            byte[] res2 = cipher.doFinal(Base64.decode(res_str)); //on décode la chaine de caractère cryptée juste avant
	                //byte[] res2 = cipher.doFinal(res_str.getBytes("utf-8"));
	            String res_str2 =  new String(res2); //et on affiche la chaine de caractère décodée (si tout vva bien on a donc ici le message que le message initial)
	           
	            System.out.println("source:"+valueEnc); //affichage du string source
	            System.out.println("enc:"+res_str); //affichage du string encodé
	            System.out.println("dec:"+res_str2); //affichage du string décodé
	           
	            byte[] enck = key.getEncoded(); //récupération de la clé sous forme de tableau d'octet
	            System.out.println(Base64.encode(enck)); //transformation de la clé en string et affichage textuel de la clé
	           
	           
	            String encoded = "r1peJOWYRRod8IibmrYoPA=="; //misterieux message encodé
	            String key_str = "+WHQtDsr9LJQ05/2MHZkQQ=="; //clé perméttant de déchiffrer le message juste au dessus
	           
	            byte[] kb = Base64.decode(key_str.getBytes()); //transformation du string de la clé en tableau d'octet
	            SecretKeySpec ksp = new SecretKeySpec(kb, "AES"); //génération de la clé à partir de la clé sous forme de chaine de caractère
	           
	            Cipher cipher2 = Cipher.getInstance("AES"); //passage en mode AES
	            cipher2.init(Cipher.DECRYPT_MODE, ksp); //passage en mode décryptage
	            byte[] res3 = cipher2.doFinal(Base64.decode(encoded)); //décodage du message crypté sous forme de tablea d'octet
	            String res_str3 =  new String(res3); //convesion du message décrypté en string
	            System.out.println("obtained: "+res_str3); //affichage du message décrypté
	        
	       
	    } catch(Exception ex) {
	        System.out.println("The Exception is=" + ex);
	    }

	    

	}

}
