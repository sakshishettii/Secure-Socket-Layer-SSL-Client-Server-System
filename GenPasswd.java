import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GenPasswd {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        getInfo();
    }
     static void getInfo() throws IOException, NoSuchAlgorithmException {
        try (Scanner scanner = new Scanner(System.in)) {
            String ans;

             while (true) {
                 System.out.print("•  Enter the Id: ");
                 String id = scanner.nextLine();
                 if (!id.matches("^[a-z]+$")) {
                    System.out.println("The ID should only contain lower-case letters.");
                    continue;
                 }

                 if (infoExist(id)) {
                    System.out.println("The ID already exists.\n");
                    System.out.print("Would you like to enter another ID and password (Y/N)? ");
                    ans = scanner.nextLine().toUpperCase();
                    if (ans.equals("N")) {
                        System.exit(0);
                    }
                    continue;
                }

                 System.out.print("•  Enter the password: ");
                 String passwd = scanner.nextLine();
                 if (passwd.length() < 8) {
                     System.out.println("The password should contain at least 8 characters.\n");
                     continue;
                 }

                 String hashpass = hashPassword(passwd);
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String timestamp = sdf.format(new Date());
                 saveToFile(id, hashpass, timestamp);
                 System.out.println("ID and password has been successfully entered.\n");;

                 System.out.print("Would you like to enter another ID and password (Y/N)? ");
                 ans = scanner.nextLine().toUpperCase();
                 if (ans.equals("N")) {
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
    
     static void saveToFile(String id, String passwd, String date) {
         try (FileWriter fw = new FileWriter("hashpasswd", true);
              BufferedWriter br = new BufferedWriter(fw)) {
             String hash = id + " " + passwd + " " + date;
             br.write(hash);
             br.newLine();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }

     static boolean infoExist(String id) throws IOException {
        File f = new File("hashpasswd");
        if(!f.exists()){
            f.createNewFile();
            return false;
        } 
        try (FileReader reader = new FileReader("hashpasswd");
              Scanner myReader = new Scanner(reader)) {
             while (myReader.hasNextLine()) {
                 String data = myReader.nextLine();
                 String[] splited = data.split(" ");
                 if (splited[0].equals(id)) {
                     return true;
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return false;
     }
}