#! /bin/bash

#Cleanup old files
rm -R decred-wallet/*
rm DecredWallet.jar

#Move into source folder
cd ../src

#Complie java files
javac com/hosvir/decredwallet/DecredWallet.java -d ../bin/decred-wallet/

#Add files to jar
jar cvfe ../bin/DecredWallet.jar com.hosvir.decredwallet.DecredWallet resources -C ../bin/decred-wallet/ .

#Set permissions on jar
chmod +x ../bin/DecredWallet.jar

