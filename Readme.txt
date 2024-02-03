
➛ Author Information
    ➛ Name: Sakshi Shetty
    ➛ Programming Language Used: Java
    ➛ Tested on remote.cs.binghamton.edu: Yes

➛ Description
This project implements a secure client-server system utilizing SSL for encrypted communication. The system includes a server and a client enabling secure transmission of user credentials.

➛ Server Functionality
    ➛ Server Program: Serv.java
    ➛ Execution Command: java Serv <server_port>
    ➛ Description: The server receives connection requests from clients on a specified port. Upon connection, it validates the received user ID and password against stored hashed passwords in the "hashpasswd"          file. If the credentials match, it sends "correct ID and password" to the client; otherwise, prompts for re-entry.

➛ Client Functionality
    ➛ Client Program: Cli.java
    ➛ Execution Command: java Cli <server_domain> <server_port>
    ➛ Description: The client connects to the server via SSL. It prompts the user to input their ID and password securely. After sending this data to the server, it awaits a response. Upon receiving "correct ID         and password" from the server, it terminates; otherwise, it requests re-entry of credentials.

➛ Generating 'hashpasswd' File
    ➛ File Generation Program: GenPasswd.java
    ➛ Execution Command: java GenPasswd
    ➛ Description: This program prompts the user to enter an ID and password. After validating the inputs, it computes the hash of the password and saves the ID, hashed password, and creation timestamp into the         "hashpasswd" file.

➛ File Structure
    ➛ Source Files: Cli.java
                    Serv.java
                    GenPasswd.java
    ➛ Makefile: Makefile
