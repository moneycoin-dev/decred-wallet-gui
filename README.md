# decred-wallet
Decred Wallet GUI is a front end built in Java using the Canvas with an OpenGL pipeline for rendering graphics.
All components are made using Graphics2D and images where required, this gives us full flexability to create anything we want without the limitations of swing or other frameworks.

####Index:
* [Downloads](#download-binaries)  
* [Features](#features)
* [Build and install on Linux](#build-and-install-on-linux)
* [Build on Windows](#build-on-windows)
* [Help](#help)

___

#### Download binaries
* [Release page](https://github.com/Fsig/decred-wallet/releases)

___

#### Features

* Multithreaded
* Render control
  * When the window is out of focus it will reduce the update rate
  * This means the process will use at little resources as possible
* Cached results
  * Realtime updates on actions
* Accounts
  * View all
  * Add accounts
  * Rename accounts
* Transactions
  * List Transactions
* Contacts
  * Add unlimited
  * Remove
  * Copy details to clipboard
* Balance
  * View total by account
  * View spendable by account
  * View locked by account
* Stake mining
  * View stake info
  * Manual ticket purchase
* Send coins
  * Send from specified account
* Receive coins
  * Get new address
  * View all current addresses per account
  * Click to copy address
* Logs
  * Daemon log
  * Wallet log
  * Gui log
* Language
  * Supports multiple languge though conf files
* Network 
  * Network info
  * Peers list
  * Disconnect peers
  
___

#### Build and install on Linux

Building and installing has been tested on Debian based Linux.

###### Requirements
* JDK 1.7 or greater

###### Install
* Open a terminal and cd into **bin**
* Give the build and install scripts execute permissions **chmode +x *.sh**
* Run build **./build-nix.sh**
* Check results, should see **Sucessfully built the Decred Wallet GUI.**
* Run install **./install-nix.sh**
* Check all the returned results are **true**

___

#### Build on Windows

Building has been tested on Windows 7.

###### Requirements
* JDK 1.7 or greater

###### Install
* Open a command prompt and cd into **bin**
* Run build **./build-dos.bat**
* Check results, should see **Sucessfully built the Decred Wallet GUI Jar.**

###### Create EXE wrapper
* Download Launch4j: https://sourceforge.net/projects/launch4j/files/launch4j-3/3.8/
* Run Launch4j
* Set output path to Decred.exe
* Set jar input to DecredWallet.jar
* Set icon to resources/Decred.ico
* Choose JRE tab
* Min JRE = 1.7.0
* Max JRE = 1.9.9
* Press build

___

#### Help

* [Support/Suggestions thread](https://forum.decred.org/threads/decred-wallet-gui.1119/)
* [JDK Download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Set environment variable Windows](http://www.tutorialspoint.com/java/java_environment_setup.htm)


**Donations welcome: DsmcWt82aeraJ22bayUtMXm8dyRL8bFnBVY**
