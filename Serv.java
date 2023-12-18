import java.io.*;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Serv {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        if (port < 1024 || port > 65535) {
            System.err.println("•  Invalid port number. Port must be between 1024 and 65535.");
            System.exit(1);
        }

        String keystoreFile = "key.p12";
        char[] keystorePassword = "sakshi".toCharArray();

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(keystoreFile), keystorePassword);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keystore, keystorePassword);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
        while (true) {
    
            //System.out.println("➤  Server is running on port " + port + '\n');

            SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
            System.out.println("•  Server has been successfully connected to Client." + '\n');

            while (true) {
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String userData = in .readLine();
                    String[] splited = userData.split(" ");
                    String hashpasswd = hashPassword(splited[1]);

                    if (infoExist(splited[0], hashpasswd)) {
                        out.println("1");
                        System.out.println("•  Client has been login successfully.\n");
                        break;
                    } else {
                        try {
                            out.println("0");
                            System.out.println("•  Failed to login.\n");
                        } catch (Exception e) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }


    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());

        return new BigInteger(1, hashedBytes).toString(16);
    }

    static boolean infoExist(String id, String hashpasswd) throws IOException {
        try (FileReader reader = new FileReader("hashpasswd"); Scanner myReader = new Scanner(reader)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splited = data.split(" ");
                if (splited[0].equals(id) && splited[1].equals(hashpasswd)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}