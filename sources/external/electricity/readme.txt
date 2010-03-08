DESCRIPTION

Flash sourcecode:
 * korteren.fla
 * JSON.as
Model created with Adobe Flash CS3 Professional (Flash version 9.0)
Publish settings:
 * Flash actionscript 2.0
 * Flash player 7

Web interface control file: index.html
Web interface flash file: korteren.swf
Web interface uses also files:
 * abi.gif
 * bl.gif
 * olmeelekter_abi.htm (help file for model users)
 * pkell.gif
 * rkell.gif
 * tagasi.gif
 * timer.gif

REQUIREMENTS
Flash versions see above.
Web interface: needs http web server.

INSTALL
Put all web interface files into one appropriate folder in web server. See file list above. Thats all :)

ELO SAVE LOGIC

Model output in JSON format. In top level there are elements:
 * content - part "content" in ELO
 * username - constant "username"
 * password - constant "password"
 * language - constant "en"
 * country - constant "country"
 * title - title enetered by user
 * description - description entered by user
 * type - constant "scy/dataset"

How to invoke SCY JSON web service?
There is row in index.html:
			makePOSTRequest('http://scy.collide.info:8080/roolo-ws/webresources/saveELOelectricity', args);
Change web service address to correct address.
Possible issue: "same origin policy" problems can arise. If all in one server then no problems. If Roolo web service and electricity web in different servers then possible security problems can be (server can block and some browsers can block something).

AUTHORS
Main version: Imre Raudsepp and Kristjan Adojaan
Minor changes: Kaido Hallik
