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
In index.html there is part:
		case 'elosaver':
			a=window.open("","a","width=700,height=500,toolbars=no,scrollbars=yes");
			a.document.open();a.document.write(args);a.document.close();a.focus();
//			b=window.open("http://scy.collide.info:8080/roolo-ws/webresources/saveELO?"+args,"b","");
			break;
It's the part where invoking web service can take part.
First test can be: comment lines beginning with "a=" and uncomment and modify appropriately line beginning with "b=".
If not working then maybe use some JSON library for Javascript to invoke JSON web service.
There are also some possibilities to invoke web service inside Flash. If invoking web services from javascript fails then can test solutions from Flash.

AUTHORS
Main version: Imre Raudsepp and Kristjan Adojaan
Minor changes: Kaido Hallik
