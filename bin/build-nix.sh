#! /bin/bash

#Cleanup old files
if [ -f "decred-wallet/com" ]; then 
	rm -R decred-wallet/* 
fi

if [ -f "DecredWallet.jar" ]; then 
	rm DecredWallet.jar 
fi

if [ -f "decred" ]; then 
	rm decred 
fi

#Move into source folder
cd ../src

#Complie java files
javac -cp ../bin/libraries/*:. com/hosvir/decredwallet/DecredWallet.java -d ../bin/decred-wallet/

#Copy resources to working dir
cp -R resources ../bin/decred-wallet/

#Add files to jar
#jar cvfe ../bin/DecredWallet.jar com.hosvir.decredwallet.DecredWallet resources -C ../bin/decred-wallet/ .

#Now using ANT for required jars
ant -buildfile ../bin/resources/DecredWallet-ANTSCRIPT.xml

#Move into bin folder
cd ../bin

#Copy embed script
cp resources/embed.sh decred

#Append jar to executable shell
uuencode DecredWallet.jar DecredWallet.jar >> decred

#Set permissions
chmod +x DecredWallet.jar
chmod +x decred

#Manually use Launch4j to create Windows executable


#Check all went well
if [ -f "DecredWallet.jar" ]; then 
	echo ""
	echo "Successfully built the Decred Wallet GUI."
else
	echo ""
	echo "Build failed, post your error to the Thread on Decred Fourms for help."
fi
