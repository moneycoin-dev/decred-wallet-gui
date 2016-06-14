@echo off

rem Cleanup old files
if exist "decred-wallet/com" ( 
	del "decred-wallet/*" 
)

if exist "DecredWallet.jar" ( 
	del "DecredWallet.jar" 
)

rem Move into source folder
cd "../src"

rem Complie java files
javac com/hosvir/decredwallet/DecredWallet.java -d ../bin/decred-wallet/

rem Add files to jar
rem jar cvfe ../bin/DecredWallet.jar com.hosvir.decredwallet.DecredWallet resources -C ../bin/decred-wallet/ .

rem Now using ANT for required jars
ant -buildfile ../bin/resources/DecredWallet-ANTSCRIPT.xml

rem Move into bin folder
cd "../bin"

rem Manually use Launch4j to create Windows executable


rem Check all went well
if exist "DecredWallet.jar" ( 
	echo 
	echo Successfully built the Decred Wallet GUI Jar.
	echo You now need to run Launch4j to wrap the Jar in an EXE if desirec.
)else (
	echo 
	echo Build failed, post your error to the Thread on Decred Fourms for help.
)
