#! /bin/sh
rm -f DecredWallet.jar
uudecode $0
java -jar DecredWallet.jar
rm -f DecredWallet.jar
exit
