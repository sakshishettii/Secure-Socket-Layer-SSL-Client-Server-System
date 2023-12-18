import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class Cli {
    public static void main(String[] args) throws Exception {
        //boolean status = false;
        if (args.length != 2) {
            System.exit(1);
        }

        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        if (serverPort < 1024 || serverPort > 65535) {
            System.err.println("•  Invalid port number. Port must be between 1024 and 65535.");
            System.exit(1);
        }
        
        String[] validHosts = {
            "remote01.cs.binghamton.edu",
            "remote02.cs.binghamton.edu",
            "remote03.cs.binghamton.edu",
            "remote04.cs.binghamton.edu",
            "remote05.cs.binghamton.edu",
            "remote06.cs.binghamton.edu",
            "remote07.cs.binghamton.edu"
        };

        if (!isValidServerHost(serverHost, validHosts)) {
            System.err.println("•  Invalid server host.");
            System.exit(1);
        }

        String truststoreFile = "truststore.p12";
        char[] truststorePassword = "sakshi".toCharArray();
        
        KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(new FileInputStream(truststoreFile), truststorePassword);
        
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(truststore);
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        SSLSocket clientSocket = (SSLSocket) sslSocketFactory.createSocket(serverHost, serverPort);
            while (true) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                
                System.out.print("•  Enter ID: ");
                String id = userInput.readLine();
                System.out.print("•  Enter password: ");
                String password = userInput.readLine();
                System.out.println();
                String hash = id +" "+password;
                out.println(hash);
                
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String serverResponse = in.readLine();
                if(serverResponse.equals("0")){
                    //System.out.println('\n');
                    System.out.println("•  The ID/password is incorrect");
                    System.out.println("Please try again.\n");
                    
                    //System.out.println("Client is closed."); 
                }
                else{
                    System.out.println("•  Correct ID and password.");
                    System.out.println("Client is closed.\n");
                    clientSocket.close();
                    break;
                }
            }
    }
    private static boolean isValidServerHost(String serverHost, String[] validHosts) {
        for (String validHost : validHosts) {
            if (serverHost.equalsIgnoreCase(validHost)) {
                return true;
            }
        }
        return false;
    }
}
