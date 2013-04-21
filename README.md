## About this project

The project goal is to create an easy-reading code with not too many lines. The client shall not save an unencrypted private key to disk. All disk-persistent user settings are in $BITJ_HOME/bitj.properties where BITJ_HOME is $USER_HOME/bitj by default. We're using chain / store implementation from bitcoinj internally. I'm not very much into GUI programming with java so I'll keep the UI as simple as possible, using JOptionPane for input and System.out for output.

Files used:

* ~/bitj/bitj.properties user settings (read-only)
* ~/bitj/blocks.h2.db


### Dependencies


* java jdk (tested with oracle java jdk 1.7.0_17)
* maven (packages exist for most linux distros, tested with version 3.0.5)
* bitcoinj, install it from [source](http://code.google.com/p/bitcoinj/wiki/UsingMaven)

### Features

List of things that **should** work

#### Extract bitcoin address from private key

``mvn clean compile exec:java -Dexec.mainClass=ooq.asdf.Main -Dexec.args='a c'``

Test it with a private key from [bitaddress.org](https://www.bitaddress.org/)

Code: [https://github.com/methylene/bitj/blob/master/src/main/java/ooq/asdf/view/PrivateToPublicPopup.java](https://github.com/methylene/bitj/blob/master/src/main/java/ooq/asdf/view/PrivateToPublicPopup.java)


#### getbalance (work in progress)

``mvn clean compile exec:java -Dexec.mainClass=ooq.asdf.Main -Dexec.args='a b'``

Show balance for given private (todo: public) key, a la [blockexplorer](http://blockexplorer.com/q/addressbalance)

Code: [https://github.com/methylene/bitj/blob/master/src/main/java/ooq/asdf/view/BalancePopup.java](https://github.com/methylene/bitj/blob/master/src/main/java/ooq/asdf/view/BalancePopup.java)
