/* ***** BEGIN LICENSE BLOCK *****
 *   Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is scylighter.
 *
 * The Initial Developer of the Original Code is
 * Sven Manske.
 * Portions created by the Initial Developer are Copyright (C) 2009
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

if ("undefined" == typeof(scylighterOptions)) {
    var scylighterOptions = {

        onLoad: function(e) {
            // initialization code
            //            window.alert(scylighterCommons.test());
            scylighterOptions.fillForm();
            this.initialized = true;
        },
        
        getCommons: function(){
            Components.utils.import("resource://scylighter/commons.jsm");
            return scylighterCommons;
        },

        onDialogAccept : function(){
            scylighterOptions.addLogin();
        },
        getElementsByAttributeDOM: function (strAttributeName, strAttributeValue){
            //only select Span-Tags, because only these tags are highlighted
            var arrElements = top.window.content.document.getElementsByTagName("span");
            //window.alert(arrElements.length);
            var arrReturnElements = new Array();
            var oAttributeValue = (typeof strAttributeValue != "undefined")? new RegExp("(^|\\s)" + strAttributeValue + "(\\s|$)", "i") : null;
            var oCurrent;
            var oAttribute;
            for(var i=0; i<arrElements.length; i++){
                oCurrent = arrElements[i];
                oAttribute = oCurrent.getAttribute && oCurrent.getAttribute(strAttributeName);
                if(typeof oAttribute == "string" && oAttribute.length > 0){
                    if(typeof strAttributeValue == "undefined" || (oAttributeValue && oAttributeValue.test(oAttribute))){
                        arrReturnElements.push(oCurrent);
                    }
                }
            }
            return arrReturnElements;
        },

        addLogin: function(){
            var loginManager = scylighterOptions.getLoginManager();
            var newLogin = scylighterOptions.createLoginFromForm();
            try{
                loginManager.addLogin(newLogin);
            } catch(e){
                //login already added -> modify login
                scylighterOptions.editLogin(newLogin);
            }
        },
        editLogin: function(newLogin){
            //get old login
            //overwrite with new login
            var loginManager = scylighterOptions.getLoginManager();
            var oldLogin = scylighterOptions.getLogin();
            loginManager.modifyLogin(oldLogin, newLogin);
        //loginManager.
        },
        fillForm: function(){
            var login = scylighterOptions.getLogin();
            var passwordBox = document.getElementById("textpassword");
            var usernameBox = document.getElementById("textuser");
            passwordBox.value = login.password;
            usernameBox.value = login.username;
        },
        getLogin: function(){
            var hostname = scylighterOptions.getHostname();
            var formSubmitURL = hostname;
            var httprealm = null;

            try {
                // Get Login Manager
                var myLoginManager = Components.classes["@mozilla.org/login-manager;1"].
                getService(Components.interfaces.nsILoginManager);
				  
                // Find users for the given parameters
                var logins = myLoginManager.findLogins({}, hostname, formSubmitURL, httprealm);
                // Find user from returned array of nsILoginInfo objects
                if(logins.length>0){
                    return logins[logins.length-1];
                }
                return null;
            }
            catch(ex) {
                // This will only happen if there is no nsILoginManager component class
                window.alert(ex);
            }
        },

        createLoginFromForm: function(){
            var hostname = scylighterOptions.getHostname();
            var password = document.getElementById("textpassword").value;
            var username = document.getElementById("textuser").value;
            var nsLoginInfo = new Components.Constructor("@mozilla.org/login-manager/loginInfo;1", Components.interfaces.nsILoginInfo,"init");
            var loginInfo = new nsLoginInfo(hostname, hostname, null, username, password, "uname", "pword");
            //var loginInfo = new nsLoginInfo('http://www.example.com',
            //'http://login.example.com', null,
            //'joe', 'SeCrEt123', 'uname', 'pword');
            return loginInfo;
        },

        getLoginManager: function(){
            var loginManager = Components.classes["@mozilla.org/login-manager;1"]
            .getService(Components.interfaces.nsILoginManager);
            return loginManager;
        },
        getHostname: function(){
            var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var defaultaddress = prefs.getCharPref("defaultaddress");
            var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
            var address = prefs.getCharPref("address");

            var serverURL = "";
            if (usedefaultaddress){
                serverURL = defaultaddress;
            } else {
                serverURL = address;
            }
            if(serverURL.charAt(serverURL.length-1)!="/"){
                serverURL = serverURL + "/";
            }
            return serverURL;
        }
    };

    window.addEventListener("load", function(e) {
        scylighterOptions.onLoad(e);
    }, false);
}