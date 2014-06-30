surfer.js
=======

A javascript frontend for surf (Java based renderer for algebraic surfaces) based on GWT

#Requirements

Development version (without gradle integration)
Requires
* Oracle-java (>=7)
* Eclipse (>=3.8.1)
* Google Plugin 3.5.1 
* GWT SDK 2.5.0 

Installing GWT SDK 2.5.0 in Eclipse has to be done manually:
1. download this file https://code.google.com/p/google-web-toolkit/downloads/detail?name=gwt-2.5.0.zip&can=2&q=

2. setup GWT SDK 2.5.0 on windows>>preferences>>Google>>Web Toolkit.


#Instuctions for compiling/running



1. in workspace dir ```git clone git@github.com:kenarab/jsurf.git jssurf ```(jssurf includes vecmath modified sources for running in js)
2. in eclipse, compile GWT project.
3. start the class JettyServer under jetty-src folder, it will run a web server (servlet container) in 8081 port.

Then you have to access to this URL: 
http://localhost:8081/surf.js/surfer-new-design-03.html



