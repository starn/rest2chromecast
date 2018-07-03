rest2chromecast
===============

Rest2chromecast is a pseudo-rest application to control chromecast though http request.
I tested it on windows 10 and linux, but it might works on other operating system.
I created this application to drive my chromecast with amazon echo and domoticz, but you can find many other usages !

How to use
===========
- You must have a java runtime environnement installed
- download the file ./target/rest2chromecast-1.0-SNAPSHOT-jar-with-dependencies.jar
- run:
 java -jar rest2chromecast-1.0-SNAPSHOT-jar-with-dependencies.jar 8080
 - the rest server will run on port 8080 (you can change the parameter to any port you want).
 - It will scan your network to find chromecast each minute. wait one minute after you plug a chromecast
 before using it.
 
 when rest2chromecast is running you can execute following request with a browser, a wget or any application that can execute http requests:
 - pause the movie: http://localhost:8080/?action=pause
 - play the movie: http://localhost:8080/?action=play
 - seek 30 seconds after: http://localhost:8080/?action=seek
 - force refreshing chromecasts: http://localhost:8080/?action=findChromecasts
 - next movie/audio title: http://localhost:8080/?action=next
 - kill the server (be carefull, you will have to restart it manualy after that): http://localhost:8080/?action=kill
 
 for developper
 ==============
 - git clone https://github.com/starn/rest2chromecast.git
 - mvn clean install
 => done :)
 
 