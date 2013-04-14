Dependencies
============

* java jdk \(tested with oracle java jdk 1.7.0_17\)
* maven (\packages exist for most linux distros, tested with version 3.0.5\)
* bitcoinj, install it from [source](http://code.google.com/p/bitcoinj/wiki/UsingMaven)

Features
========

Extract bitcoin address from private key
----------------------------------------

``mvn clean compile exec:java -Dexec.mainClass=ooq.asdf.Main -Dexec.args="-a convert"``

Test it with a private key from [bitaddress.org](https://www.bitaddress.org/)
