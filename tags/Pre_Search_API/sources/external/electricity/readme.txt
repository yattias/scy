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
Web interface uses also files under directory help

REQUIREMENTS
Flash versions see above.
Web interface: needs http web server.

INSTALL
Put all web interface files into one appropriate folder in web server. See file list above. Thats all :)

ELO SAVE LOGIC

Model output in JSON format. In top level there are elements:
 * content - part "content" in ELO
 * username - entered by user
 * password - constant "password"
 * language - constant "en"
 * country - constant "country"
 * title - title enetered by user
 * description - description entered by user
 * type - constant "scy/dataset"

MULTI-LANGUAGE HELP

To use help in different languages you should create folder <language> under folder "help" and create file index.html there with help content and then register this language in main index.html file (see code snapshot below), currently "en" and "fr" are registered:
    if (lang_qs=='en' || lang_qs=='fr') {
Invoking different language version: add ?lang=<language> to URL, for example: ?lang=fr
Default language is en (english).

AUTHORS
Main version: Imre Raudsepp and Kristjan Adojaan
Minor changes: Kaido Hallik
