compile:
	javac GenPasswd.java
	javac Serv.java
	javac Cli.java
run-genpasswd: compile
	java GenPasswd.java 
run-ser: compile
	java Serv.java 8122
run-cli: compile
	java Cli.java remote07.cs.binghamton.edu 8122
clean:
	rm -r *.class

	