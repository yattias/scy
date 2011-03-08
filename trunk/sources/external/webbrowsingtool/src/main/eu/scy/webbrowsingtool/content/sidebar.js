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

//var count = 0;
//var updateURI = "";
//var sidebar = top.window.document.getElementById("sidebar");

if ("undefined" == typeof(scylighter)) {
    var scylighter = {
        updateURI:"",
        count:0,
        sidebar: top.window.document.getElementById("sidebar"),
        sidebarVisible : false,
        toggleMenuState: false,
        autoHighlightMode: false,
        shouldDisplayIcons: false,
        warnBeforeLeave: false,
        lastColors: {
            fgcolor:"",
            bgcolor:""
        },

        onLoad: function(e) {
            //this.sidebarExists();
            // initialization code
            this.updateURI = "";
            this.initialized = true;
            this.strings = top.window.document.getElementById("scylighter-strings");
            this.restoreSidebar();
            this.correctSources();
            document.getElementById("contentAreaContextMenu").addEventListener("popupshowing", function(e) {
                this.showContextMenu(e);
            }, false);

        },
        sidebarExists:function(){
            var sidebar = top.window.document.getElementById("sidebar");
            if (sidebar == null) {
                return false;
            } else {
                return true;
            }
        },
        onUnload: function(e){
        },
        onBeforeUnload: function(e){
            this.storeSidebar();
        },
        newELO:function(){
            //the scylighter-strings from the stringbundle
            this.strings = top.window.document.getElementById("scylighter-strings");
        
            var prompts = Components.classes["@mozilla.org/embedcomp/prompt-service;1"].getService(Components.interfaces.nsIPromptService);
            var resultOK = prompts.confirm(null,this.strings.getString("newELODialog"), this.strings.getString("newELODialogText"));

            if (resultOK){
                var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
                var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;
                var summaryBox = sidebarWindow.document.getElementById('summaryBox');

                //Initialize the global storage for highlights
                Components.utils.import("resource://scylighter/highlights.jsm");
                bullets.itemTexts = new Array();
                bullets.nodeIDs = new Array();
                bullets.sourceURLs = new Array();
                commentsStore.value = "";
                titleStore.value = "";
                sourcesStore.value = "";

                //read sequentially from the sidebar and store it to the highlights.jsm module (only for this session)

                var urlBox = sidebarWindow.document.getElementById('urlBox');
                var titleBox = sidebarWindow.document.getElementById('titleBox');
                var commentBox = sidebarWindow.document.getElementById('commentBox');
                this.clearList(summaryBox);
        
                this.updateURI="";
                urlBox.value="";
                titleBox.value="";
                commentBox.value="";
                //hack!
                //        mainWindow.toggleSidebar('viewSidebar');
                //        mainWindow.toggleSidebar('viewSidebar');
                this.findBrokenHighlights();
                this.correctSources();
            //this will remove highlights and delete the sources
            }
        },

        generateHash: function(str){
            var converter = Components.classes["@mozilla.org/intl/scriptableunicodeconverter"].createInstance(Components.interfaces.nsIScriptableUnicodeConverter);
            // UTF-8 is also the encoding in the webservice
            converter.charset = "UTF-8";
            // result is an out parameter,
            // result.value will contain the array length
            var result = {};
            // data is an array of bytes
            var data = converter.convertToByteArray(str, result);
            var ch = Components.classes["@mozilla.org/security/hash;1"].createInstance(Components.interfaces.nsICryptoHash);
            ch.init(ch.MD5);
            ch.update(data, data.length);
            var hash = ch.finish(false);

            // return the two-digit hexadecimal code for a byte
            function toHexString(charCode)
            {
                return ("0" + charCode.toString(16)).slice(-2);
            }

            // convert the binary hash data to a hex string.
            var s = [toHexString(hash.charCodeAt(i)) for (i in hash)].join("");
            // s now contains your hash in hex: should be
            // 5eb63bbbe01eeed093cb22bb8f5acdc3
            return s;
        //        return hash;
        },
        getWebserviceRequest: function(params, servicePath){
            var req = new XMLHttpRequest();
            var jsonParams = JSON.stringify(params);
            req.jsonParams = jsonParams;
            req.servicePath = servicePath;
            return req;
        },

        sendRequest: function(req){
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
            if(serverURL.charAt(serverURL.length-1)=="/"){
                serverURL = serverURL + req.servicePath;
            } else {
                serverURL = serverURL + "/"+ req.servicePath;
            }

            //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
            req.open('POST', serverURL, true);
            req.setRequestHeader("Content-Type","application/json");
            //            req.send(jsonParams);
            req.send(req.jsonParams);
        },

        serverChallengeRequest: function(authCallback){

            var sc;
            var cc = new Date().getTime();
            //FIXME change password to real password when usermanagement is connected
            var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var username = prefs.getCharPref("username");
            //remark: password is never sent to a server!
//            var password = "secret pass"; //FIXME fixed password
            //TODO use loginManager
            var password = prefs.getCharPref("password");

            //Parameters for the webservice as JSON, stringified for transmission
            var params = {};
            params.username = username;
            params.cc = cc;
            params.type = "cc";

            req = this.getWebserviceRequest(params, "serverChallenge");

            req.onreadystatechange = function (aEvt) {
                var alertString="";
                try{
                    if (req.readyState == 4) {
                        if(req.status == 200){
                            //ok
                            sc = req.responseText;
                            scylighter.serverResponseRequest(sc, cc, password, authCallback);
                        }
                        else {
                            alertString = top.window.document.getElementById("scylighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("scylighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("scylighter-strings").getString(req.responseText);
                            window.alert(alertString);
                        }
                    } else {
                }
                }catch (e) {
                    window.alert(e);
                    alertString = top.window.document.getElementById("scylighter-strings").getString("noAuth");
                    window.alert(alertString);
                }
            };
            this.sendRequest(req);
        },

        serverResponseRequest: function(sc, cc, password, authCallback){
            var cr = this.calculateCr(cc, sc, password);
            //Parameters for the webservice as JSON, stringified for transmission

            var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var username = prefs.getCharPref("username");
            var params = {};
            params.cr = cr;
            params.username = username;
            params.type = "sr";

            req = this.getWebserviceRequest(params, "serverResponse");

            req.onreadystatechange = function (aEvt) {
                var alertString="";
                try{
                    if (req.readyState == 4) {
                        if(req.status == 200){
                            //ok
                            sr = req.responseText;
                            //TODO calculate expected sr, match it against sr
                            //expected sr = hash(sc + cc + secret)
                            expectedSr = scylighter.generateHash(sc + cc + password);
                            if(sr==expectedSr){
                                authCallback();
                            } else {
                                alertString = scylighter.strings.getString("authFailed");
                                window.alert(alertString);
                            }
                        }
                        else {
                            alertString = scylighter.strings.getString("errorLoadingPage")+"\n"+document.getElementById("scylighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("scylighter-strings").getString(req.responseText);
                            window.alert(alertString);
                        }
                    } else {
                }
                }catch (e) {
                    alertString = top.window.document.getElementById("scylighter-strings").getString("noServerResponse");
                    window.alert(alertString);
                }
            };
            this.sendRequest(req);
        },

        calculateCr: function(cc, sc, password){
            hash = cc+sc+password;
            cr = this.generateHash(hash);
            return cr;
        },

        storeSidebar: function(){

            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;

            //Initialize the global storage for highlights
            Components.utils.import("resource://scylighter/highlights.jsm");
            bullets.itemTexts = new Array();
            bullets.nodeIDs = new Array();
            bullets.sourceURLs = new Array();

            //read sequentially from the sidebar and store it to the highlights.jsm module (only for this session)

            var summaryBox = sidebarWindow.document.getElementById('summaryBox');
            var urlBox = sidebarWindow.document.getElementById('urlBox');
            var titleBox = sidebarWindow.document.getElementById('titleBox');
            var commentBox = sidebarWindow.document.getElementById('commentBox');
            var item;
            if(summaryBox.getRowCount()>0){
                for (i = 0; i<summaryBox.getRowCount();i++){
                    item = summaryBox.getItemAtIndex(i);
                    bullets.itemTexts.push(item.label);
                    bullets.nodeIDs.push(item.value);
                    bullets.sourceURLs.push(item.getAttribute("sourceURL"));
                }
            }
            commentsStore.value = commentBox.value;
            titleStore.value = titleBox.value;
            sourcesStore.value = urlBox.value;
        },

        restoreSidebar: function(){
            //Adding drop-handler to the summary-listbox
            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;
            var summaryBox = sidebarWindow.document.getElementById("summaryBox");
            if (summaryBox == null){
                summaryBox = document.getElementById("summaryBox");
            }

            if(this.checkStoredData()){
                //load storage from the highlights.jsm module
                Components.utils.import("resource://scylighter/highlights.jsm");

                //When starting firefox, restore is called but no URL/Source Box is available
                if(sidebarWindow.document.getElementById('summaryBox')!=null){
                    summaryBox.ondrop = scylighter.onDrop;

                    summaryBox = sidebarWindow.document.getElementById('summaryBox');
                    var urlBox = sidebarWindow.document.getElementById('urlBox');
                    var titleBox = sidebarWindow.document.getElementById('titleBox');
                    var commentBox = sidebarWindow.document.getElementById('commentBox');
                    var item;
                    var nodeId;
                    var itemText;
                    var listId;
                    var sourceURL;

                    //Store information for restoring them on-load
                    if(bullets.itemTexts.length>0){
                        for(i=0; i<bullets.itemTexts.length; i++){
                            itemText = bullets.itemTexts[i];
                            nodeId = bullets.nodeIDs[i];
                            sourceURL = bullets.sourceURLs[i];
                            item = summaryBox.appendItem(itemText,nodeId);
                            item.setAttribute("tooltiptext",itemText);
                            item.setAttribute("crop","center");
                            item.setAttribute("sourceURL",sourceURL);
                            listId = "list_"+nodeId;
                            item.setAttribute("id",listId);

                        }
                    }
                    if (commentsStore.value!=""||titleStore.value!=""||sourcesStore.value!="") {
                        commentBox.value = commentsStore.value;
                        titleBox.value = titleStore.value;
                        urlBox.value = sourcesStore.value;
                    }

                }
            } else{ //Stored data is checked and inconsistent
                window.alert("Stored Data corrupted.");
            }

        },

        checkStoredData: function(){
            Components.utils.import("resource://scylighter/highlights.jsm");
            if (bullets.itemTexts.length!=bullets.nodeIDs.length){
                return false;
            }
            return true;
        },

        clearList: function(listbox){
            while(listbox.getRowCount()>0){
                listbox.removeItemAt(0);
            }
        },

        getElementsByAttributeDOM: function (strAttributeName, strAttributeValue){

            //XX2
            //var mainWindow = window.QueryInterface()....
            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

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
            //window.alert("arrReturnElements.length: "+arrReturnElements.length);
            return arrReturnElements;
        },

        showContextMenu: function(event) {
            // show or hide the menuitem based on what the context menu is on
            // see http://kb.mozillazine.org/Adding_items_to_menus

            document.getElementById("context-scylighter").hidden = gContextMenu.onImage;
        },
        deleteSelection: function(){
            //deleteSelection is performed from within the sidebar ("delete selected"-button)
            //it searched for elements, which are marked as belonging to the listitem and changes the background-color

            var list = document.getElementById('summaryBox');
            //window.alert("list==null: " + (list==null));
            var count = list.selectedCount;

            while (count--){
                var item = list.selectedItems[0];
                var unhighlightNodes=this.getElementsByAttributeDOM("belongsTo",item.value);
                //window.alert("item.value: " + item.value);
                //window.alert("unhighlightNodes.length: " + unhighlightNodes.length);
                //Deleting the listitem before restoring the html because this can cause an nullpointer which prevents the listitem from being cleared
                var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
                var undoNode = top.window.content.document.getElementById(item.value);
                //window.alert("undoNode: "+undoNode);
                //undoNode.style.backgroundColor = "white";
                if (undoNode!=null){
                    undoNode.style.backgroundColor = undoNode.parentNode.style.backgroundColor;
                }
                list.removeItemAt(list.getIndexOfItem(item));

                this.correctSourcesFromSidebar();

                //Now, clean the html
                for(i=0;i<unhighlightNodes.length;i++){
                    //NEW STUFF causes an error
                    var node = unhighlightNodes[i];
                    //window.alert(node);
                    if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("scylighter")) {
                        var n = node.nextHighlight;
                        while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                            while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                            n = n.parentNode.removeChild(n).nextHighlight;
                        }

                        var record = n;
                        node.nextHighlight = null;  //break chain so loop can exit

                        n = record.firstNode;
                        while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                            while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                            n = n.parentNode.removeChild(n).nextHighlight;
                        }

                        record.firstNode = null;
                        record.lastNode.nextHighlight = null;
                        record.lastNode = null;

                        //everything below modifies unsafe objects on the webpage. Do this last.

                        var hi = node.ownerDocument.defaultView.scylighterInfo;
                        for(var i=hi.highlights.length-1; i>=0; i--) {
                            if(hi.highlights[i] == record) {
                                hi.highlights.splice(i, 1);
                                break;
                            }
                        }
                        hi.dirty = true;
                    }


                //OLD STUFF, but works!
                /*if (unhighlightNodes[i]!=null){
		//window.alert("unhighlightNode "+i+" is not null");
		unhighlightNodes[i].style.backgroundColor = "white";
		//unhighlightNodes[i].setAttribute("sourceURL","<<!!null!!>>");

		}*/
                }
            //this.correctSources();
            }

        //window.alert("End reached");

        },
        correctSources:function(){
            var sidebar = document.getElementById("sidebar");
            var urlBox;
            var summaryBox;
            if (sidebar == null){
                urlBox = document.getElementById('urlBox');
                urlBox.value = "";
                summaryBox = document.getElementById('summaryBox');
            } else {
                var sidebarWindow = sidebar.contentWindow;
                urlBox = sidebarWindow.document.getElementById('urlBox');
                urlBox.value = "";
                summaryBox = sidebarWindow.document.getElementById('summaryBox');
            }
            if (summaryBox.itemCount>0){
                urlBox.value = summaryBox.getItemAtIndex(0).getAttribute("sourceURL");
            }
            for(i = 1; i < summaryBox.itemCount; i++){
                var actualSource = summaryBox.getItemAtIndex(i).getAttribute("sourceURL");
                if (urlBox.value.search(actualSource)!=-1){
                    //the url exists
                    urlBox.value = urlBox.value;
                }
                else {
                    //the url doesnt exist
                    urlBox.value = urlBox.value +"\n"+ actualSource;
                }
            }


        },
        onDrop: function(event){
            var link = event.dataTransfer.getData("URL");
            var splitted = link.split(".");
            var ending = splitted[splitted.length-1];
            ending = ending.toLowerCase();
            //Link must have a valid ending (image!)
            if (ending=="gif"||ending=="jpg"||ending=="jpeg"||ending=="png"){
                //window.alert("|"+ending+"|");
                //add listitem for that
                scylighter.addSummaryItemAsImage(link);
            } else {
                this.count = this.count + 1;

                var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;

                var summaryBox = sidebarWindow.document.getElementById('summaryBox');
                var urlBox = sidebarWindow.document.getElementById('urlBox');

                var theSelection = top.window.content.document.getSelection();

                var itemText = theSelection;
                var newNodeId = "selection_" + this.count;

                var item = summaryBox.appendItem(itemText,newNodeId);
                item.setAttribute("tooltiptext",itemText);
                item.setAttribute("crop","center");
                var sourceURLValue = top.window.content.document.location;
                item.setAttribute("sourceURL",sourceURLValue);
                var listId = "list_"+newNodeId;
                item.setAttribute("id",listId);

                var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
                //window.alert(mainWindow.document.activeElement);

                scylighter.highlightDoc();

                scylighter.correctSources();
            }


        },
        addSummaryItemAsImage:function(imageURL){
            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;
            var summaryBox = sidebarWindow.document.getElementById("summaryBox");
            var urlBox = sidebarWindow.document.getElementById('urlBox');

            var newNodeId = imageURL;

            var item = summaryBox.appendItem(imageURL,newNodeId);
            //item.setAttribute("tooltiptext",imageURL);
            item.setAttribute("crop","center");
            item.setAttribute("tooltiptext",imageURL);
            var sourceURLValue = top.window.content.document.location;
            item.setAttribute("sourceURL",sourceURLValue);
            var listId = "list_"+newNodeId;
            item.setAttribute("id",listId);
            item.setAttribute("itemType","image-link");

            /*
	//TODO custom tooltip for images
	var imageTooltip = document.createElement("tooltip");
	imageTooltip.setAttribute("id",imageURL);
	imageTooltip.label = "BAM!";
//	item.tooltip = imageTooltip;
	item.setAttribute("tooltip",imageURL);
	//window.alert(item.tooltip);*/

            scylighter.correctSourcesFromSidebar();
        },

        findBrokenHighlights: function(){
            //If highlights are deleted from the Sidebar, they can only be deleted from the current-DOM-tree.
            //If the highlight was in another tab, it wont be found by the delete-function and will persist highlighted
            //On changing to this tab, the function will de-highlight it

            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;
            var summaryBox = sidebarWindow.document.getElementById("summaryBox");

            var belongsTos = new Array();
            for(i = 0; i < summaryBox.getRowCount(); i++){
                belongsTos.push(summaryBox.getItemAtIndex(i).getAttribute("value"));
            }
            var brokenHighlightCandidates = scylighter.getElementsByAttributeDOM("belongsTo","*");
            for (i=0;i<brokenHighlightCandidates.length;i++){
                var selectionBroken = true;
                var candidate = brokenHighlightCandidates[i];
                for (j=0;j<belongsTos.length;j++){
                    if (candidate.getAttribute("belongsTo")==belongsTos[j]){
                        selectionBroken = false;
                    }
                }
                if (selectionBroken){
                    //window.alert("Selection broken");
                    var node = candidate;
                    if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("scylighter")) {
                        var n = node.nextHighlight;
                        while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                            while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                            n = n.parentNode.removeChild(n).nextHighlight;
                        }

                        var record = n;
                        node.nextHighlight = null;  //break chain so loop can exit

                        if (record!=null){
                            n = record.firstNode;
                            while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                                while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                                n = n.parentNode.removeChild(n).nextHighlight;
                            }

                            record.firstNode = null;
                            record.lastNode.nextHighlight = null;
                            record.lastNode = null;

                            //everything below modifies unsafe objects on the webpage. Do this last.

                            var hi = node.ownerDocument.defaultView.scylighterInfo;
                            for(var i=hi.highlights.length-1; i>=0; i--) {
                                if(hi.highlights[i] == record) {
                                    hi.highlights.splice(i, 1);
                                    break;
                                }
                            }
                            hi.dirty = true;
                        }
                    }
                }
            }

        },
        correctSourcesFromSidebar:function(){
            //Check the mapping between highlights and selections in the sidebar. Delete  sources if not used anymore

            var urlBox = document.getElementById('urlBox');
            urlBox.value = "";
            var summaryBox = document.getElementById('summaryBox');
            if (summaryBox.itemCount>0){
                urlBox.value = summaryBox.getItemAtIndex(0).getAttribute("sourceURL");
            }
            for(i = 1; i < summaryBox.itemCount; i++){
                var actualSource = summaryBox.getItemAtIndex(i).getAttribute("sourceURL");
                if (urlBox.value.search(actualSource)!=-1){
                    //the url exists
                    urlBox.value = urlBox.value;
                }
                else {
                    //the url doesnt exist
                    urlBox.value = urlBox.value +"\n"+ actualSource;
                }
            }
        },
        onMenuItemCommand: function(e) {
            this.count = this.count + 1;

            var sidebarWindow = document.getElementById("sidebar").contentWindow;

            var summaryBox = sidebarWindow.document.getElementById('summaryBox');
            var urlBox = sidebarWindow.document.getElementById('urlBox');

            var theSelection = top.window.content.document.getSelection();

            if (theSelection!=""){

                var itemText = theSelection;
                var newNodeId = "selection_" + this.count;

                //save it too the highlights.jsm module
                //Initialize the global storage for highlights
                //Components.utils.import("resource://scylighter/highlights.jsm");
                //Store information for restoring them on-load
                //window.alert("summaryBox.getRowCount():" +summaryBox.getRowCount());
                //bullets.itemTexts[summaryBox.getRowCount()]=itemText;
                //bullets.NodeIDs[summaryBox.getRowCount()]=NodeId;

                var item = summaryBox.appendItem(itemText,newNodeId);
                item.setAttribute("tooltiptext",itemText);
                item.setAttribute("crop","center");
                var sourceURLValue = top.window.content.document.location;
                item.setAttribute("sourceURL",sourceURLValue);
                var listId = "list_"+newNodeId;
                item.setAttribute("id",listId);

                this.highlightDoc();

                this.correctSources();
            }


        },
        highlightDoc: function() {

            var node = document.popupNode;
            if (node == null){
                var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
                var browser = mainWindow.gBrowser;
                win = browser.contentWindow;
            } else {
                var win = node.ownerDocument.defaultView;
            }
            var sel = win.getSelection();

            var fgcolor = "black";
            var bgcolor = "yellow";
            //window.alert(sel);

            //if exist highlight and no selections then change current highlight colour
            //if((sel.rangeCount == 0 || sel.getRangeAt(0).collapsed)
            //    && node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("scylighter")) {

            //  scylighter.changeHighlightColor(node, fgcolor, bgcolor);

            //} else
            if(sel.rangeCount > 0) {
                scylighter.highlightRange(win, fgcolor, bgcolor);
            }
        },

        highlightRange: function(win, fgcolor, bgcolor) {
            const record = {
                offsetY:Number.NaN,
                firstNode:null,
                lastNode:null
            };

            const wrap = win.document.createElement("span");
            wrap.setAttribute("scylighter", bgcolor);
            var belongsToValue = "selection_"+this.count;
            var sourceURLValue = top.window.content.document.location;
            //	wrap.setAttribute("sourceURL",sourceURLValue);
            wrap.setAttribute("belongsTo",belongsToValue)
            //wrap.style.color = fgcolor;
            wrap.style.backgroundColor = bgcolor;

            const _createWrapper = function(n) {
                var e = wrap.cloneNode(false);

                if(!record.firstNode)record.firstNode = e;
                if(record.lastNode)record.lastNode.nextHighlight = e;
                record.lastNode = e;

                //XXX getBoxObjectFor(node)
                var boxObject = n.parentNode.getBoundingClientRect();

                var posTop = boxObject.top;
                //var posTop = win.document.getBoxObjectFor(n.parentNode).y;
                var pageTop = parseInt(win.pageYOffset);
                if(!posTop || posTop < pageTop) {
                    record.offsetY = pageTop;
                } else {
                    if(!(posTop > record.offsetY))record.offsetY = posTop;
                }

                return e;
            };

            //Highlight Range0
            for(var i=0, sel=win.getSelection(); i<sel.rangeCount; i++) {
                scylighter.highlightRange0(sel.getRangeAt(i), _createWrapper, record);
            }

            if(record.firstNode != null) {
                record.lastNode.nextHighlight = record; //connect linked list back to record

                if(!win.scylighterInfo)win.scylighterInfo = {
                    highlights:[],
                    dirty:true
                };

                var arr = win.scylighterInfo.highlights;
                //do an insertion sort insertion
                if(arr.length > 0) {
                    for(var i=arr.length-1; i>=-1; i--) {
                        if(i < 0) {
                            arr.splice(0, 0, record);
                        } else if(arr[i].offsetY <= record.offsetY) {
                            arr.splice(i + 1, 0, record);
                            break;
                        }
                    }
                } else arr.push(record);

                win.scylighterInfo.dirty = true;

                if(scylighter.warnBeforeLeave)
                    scylighter.ensureUnloadWarning(win);

                if(scylighter.shouldDisplayIcons)
                    scylighter.ensurescylighterView(win);
            }
        },

        highlightRange0: function(range, _createWrapper, record) {

            if(range.collapsed)  //(startContainer == endContainer && startOffset == endOffset)
                return;

            var startSide = range.startContainer;
            var endSide = range.endContainer;
            var ancestor = range.commonAncestorContainer;
            var dirIsLeaf = true;

            if(range.endOffset == 0) {  //nodeValue = text | element
                while(!endSide.previousSibling && endSide.parentNode != ancestor) {
                    endSide = endSide.parentNode;
                }
                endSide = endSide.previousSibling;
            //dump("\nendOffset=0, endSide="+endSide+" goto root="+endSide+" ancestor="+ancestor);
            } else if(endSide.nodeType == Node.TEXT_NODE) {
                if(range.endOffset < endSide.nodeValue.length) {
                    endSide.splitText(range.endOffset);
                //dump("\nendSide is textnode, split text, endSide="+endSide);
                }
            } else if(range.endOffset > 0) {  //nodeValue = element
                endSide = endSide.childNodes.item(range.endOffset - 1);
            //dump("\nendOffset > 0, select indexed="+endSide);
            }

            if(startSide.nodeType == Node.TEXT_NODE) {
                if(range.startOffset == startSide.nodeValue.length) {
                    //dump("\nstartOffset=len, noop");
                    dirIsLeaf = false;
                } else if(range.startOffset > 0) {
                    startSide = startSide.splitText(range.startOffset);
                    if(endSide == startSide.previousSibling)endSide = startSide;
                //dump("\nstartOffset > 0, split text, startSide="+startSide);
                }
            } else if(range.startOffset < startSide.childNodes.length) {
                startSide = startSide.childNodes.item(range.startOffset);
            //dump("\nstartOffset < len, select indexed="+startSide);
            } else {
                //dump("\nstartOffset=len, noop");
                dirIsLeaf = false;
            }

            range.setStart(range.startContainer, 0);
            range.setEnd(range.startContainer, 0);

            //dump("\nstartSide="+startSide+"\nendSide="+endSide);
            var done = false;
            var node = startSide;
            var tmp;

            do {
                if(dirIsLeaf && node.nodeType == Node.TEXT_NODE &&
                    !((tmp = node.parentNode) instanceof HTMLTableElement ||
                        tmp instanceof HTMLTableRowElement ||
                        tmp instanceof HTMLTableColElement ||
                        tmp instanceof HTMLTableSectionElement)) {
                    var wrap = node.previousSibling;
                    if(!wrap || wrap != record.lastNode) {
                        wrap = _createWrapper(node);
                        node.parentNode.insertBefore(wrap, node);
                    }
                    wrap.appendChild(node);

                    node = wrap.lastChild;
                    dirIsLeaf = false;
                }

                if(node == endSide && (!endSide.hasChildNodes() || !dirIsLeaf)) {
                    done = true;
                }

                if(node instanceof HTMLScriptElement ||
                    node instanceof HTMLStyleElement ||
                    node instanceof HTMLSelectElement) {  //never parse their children
                    dirIsLeaf = false;
                }


                if(dirIsLeaf && node.hasChildNodes()) {
                    node = node.firstChild;  //dump("-> firstchild ");

                } else if(node.nextSibling != null) {
                    node = node.nextSibling;  //dump("-> nextSibling ");
                    dirIsLeaf = true;

                } else if(node.nextSibling == null) {
                    node = node.parentNode;  //dump("-> parent ");
                    dirIsLeaf = false;
                }
            //if(node == ancestor.parentNode)dump("\nHALT shouldn't face ancestor");
            } while(!done);
        },

        onToolbarButtonCommand: function(e) {
            // just reuse the function above.  you can change this, obviously!
            scylighter.onMenuItemCommand(e);
        },
        openPreferences: function(){
            var windowObjectReference = window.open("chrome://scylighter/content/options.xul", "Options", "chrome");
            windowObjectReference.focus();
        },
        createXML: function(){
            var summaryXML = "<document>";

            //append title
            var titleBox = document.getElementById('titleBox');
            if (titleBox == null){
                titleBox = sidebar.contentDocument.getElementById('titleBox');
            }
            summaryXML = summaryXML + "<title>"+ titleBox.value + "</title>";

            //append summary from the summmaryBox
            var summaryBox = document.getElementById('summaryBox');
            if (summaryBox == null){
                summaryBox = sidebar.contentDocument.getElementById('summaryBox');
            }
            var bullets = "";
            for(i = 0; i < summaryBox.itemCount; i++){
                bullets = bullets + "<quote>"+summaryBox.getItemAtIndex(i).label+"</quote>";
            }
            summaryXML = summaryXML + "<quotes>" + bullets + "</quotes>";

            //append comments
            //Maybe it is a good idea to split multiple comments by /n and branch the comments-tag
            var commentBox = document.getElementById('commentBox');
            if (commentBox == null){
                commentBox = sidebar.contentDocument.getElementById('commentBox');
            }
            var comments = commentBox.value.split("\n");
            var commentsContent = "";
            for (i=0; i<comments.length;i++) {
                commentsContent = commentsContent + "<comment>" + comments[i] + "</comment>";
            }
            summaryXML = summaryXML + "<comments>" + commentsContent + "</comments>";

            //append sources
            //Maybe it is a good idea to split multiple sources by /n and branch the sources-tag

            var urlBox = document.getElementById('urlBox');
            if (urlBox == null){
                urlBox = sidebar.contentDocument.getElementById('urlBox');
            }
            var sources = urlBox.value.split("\n");
            var sourcesContent = "";
            for (i=0; i<sources.length;i++) {
                sourcesContent = sourcesContent + "<source>" + sources[i] + "</source>";
            }

            summaryXML = summaryXML + "<sources>" + sourcesContent + "</sources>";

            //close the document tag
            summaryXML = summaryXML + "</document>";
            return summaryXML;
        },
        saveEloRequest: function(){
            //the scylighter-strings from the stringbundle
            this.strings = top.window.document.getElementById("scylighter-strings");

            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

            //username and password will be received from the preferences
            var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var username = prefs.getCharPref("username");
            var password = prefs.getCharPref("password");
            var defaultaddress = prefs.getCharPref("defaultaddress");
            var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
            var address = prefs.getCharPref("address");
            if ((username=="username" && password=="password")||(username=="")||(password=="")){
                window.alert(top.window.document.getElementById("scylighter-strings").getString("setUpLoginData"));
                this.openPreferences();
            } else {
                //create the summaryDocument (XML)
                var summaryXML = scylighter.createXML();
                //----------------------------------------

                //for now, html isnt used because there is no usecase for wrapping the whole html of a page as a backup inside the ELO
                //                var htmlDoc = window.content.document.documentElement.innerHTML;

                // extract language and country:
                var lang = navigator.language;
                var languageCode = "en";
                var countryCode = null;
                if (lang !=null){
                    if (lang.length > 2){
                        languageCode = lang.substring(0,2);
                        countryCode = lang.substring(3,5);
                    } else {
                        languageCode = lang;
                    }
                }

                //RESTful webservice request
                var req = new XMLHttpRequest();

                //Parameters for the webservice as JSON, stringified for transmission
                var params = {};
                params.content = "<webresource><preview><![CDATA["+scylighter.getPreview()+"]]></preview>"+"<annotations> "+summaryXML+" </annotations>"+"\n <html>\n</html></webresource>";
                params.username = username;
                params.type = "scy/webresourcer";
                if (countryCode == null){
                    params.language = languageCode;
                } else {
                    params.language = languageCode;
                    params.country = countryCode;
                }
                if (scylighter.updateURI.length>1){
                    params.uri = this.updateURI;
                }
                var titleBox = document.getElementById('titleBox');
                if (titleBox==null){
                    titleBox = sidebar.contentDocument.getElementById('titleBox');
                }
                params.title = titleBox.value;
                params.authType = "challenge/response";
                var jsonParams = JSON.stringify(params);

                req.onreadystatechange = function (aEvt) {
                    var alertString="";
                    try{
                        if (req.readyState == 4) {
                            if(req.status == 200){
                                updating = true;
                                scylighter.updateURI = req.responseText;
                                window.alert(top.window.document.getElementById("scylighter-strings").getString("eloSaved"));
                            }
                            else {
                                alertString = scylighter.strings.getString("errorLoadingPage")+"\n"+scylighter.strings.getString("errorCode") + req.status + "\n";
                                window.alert(alertString);
                            }
                        } else {

                    }
                    }catch (e) {
                        window.alert(e);
                        alertString = top.window.document.getElementById("scylighter-strings").getString("noServerResponse");
                        window.alert(alertString);
                    }
                };

                var serverURL = "";
                if (usedefaultaddress){
                    serverURL = defaultaddress;
                } else {
                    serverURL = address;
                }
                if(serverURL.charAt(serverURL.length-1)=="/"){
                    serverURL = serverURL + "saveELO";
                } else {
                    serverURL = serverURL + "/saveELO";
                }

                //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
                req.open('POST', serverURL, true);
                //req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
                req.setRequestHeader("Content-Type","application/json");
                //req.setRequestHeader("Timeout", 0.1);
                req.send(jsonParams);
            }
        },
        saveELO: function(){
            this.serverChallengeRequest(this.saveEloRequest);
        },
        showPreviewELOWindow : function() {

            var elolist = document.getElementById("elolist");
            var selectedItem = elolist.getSelectedItem(0);
            var uri = selectedItem.childNodes[3].getAttribute("label");
            //window.alert("URI: "+uri);

            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

            //username and password will be received from the preferences
            var prefs = Components.classes["@mozilla.org/preferences-service;1"]
            .getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var username = prefs.getCharPref("username");
            var password = prefs.getCharPref("password");
            var defaultaddress = prefs.getCharPref("defaultaddress");
            var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
            var address = prefs.getCharPref("address");

            //RESTful webservice request
            var req = new XMLHttpRequest();

            //Parameters for the webservice as JSON, stringified for transmission
            var params = {};
            params.uri = uri;
            var jsonParams = JSON.stringify(params);

            //the scylighter-strings from the stringbundle
            this.strings = top.window.document.getElementById("scylighter-strings");

            req.onreadystatechange = function (aEvt) {
                var alertString="";
                try{
                    if (req.readyState == 4) {
                        if(req.status == 200){
                            //The response as JSON has to be parsed
                            var jsObject = JSON.parse(req.responseText);
                            var preview = jsObject.preview;
                            //window.alert(preview);
                            //opens a new Browser-Window without Navigation and sets the preview to the documents content
                            myPreviewWindow = window.open('','','resizable=yes,scrollbars=yes,width=700,height=520');
                            myPreviewWindow.document.body.innerHTML = preview;
                        //var previewELOWindow = window.open('','','resizable=yes,scrollbars=yes,width=700,height=600');
                        //previewELOWindow.top.window.document.body.innerHTML = preview;
                        }
                        else {
                            alertString = top.window.document.getElementById("scylighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("scylighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("scylighter-strings").getString(req.responseText);
                            window.alert(alertString);
                        }
                    } else {

                }
                }catch (e) {
                    alertString = top.window.document.getElementById("scylighter-strings").getString("noServerResponse");
                    window.alert(alertString);

                }
            };

            var serverURL = "";
            if (usedefaultaddress){
                serverURL = defaultaddress;
            } else {
                serverURL = address;
            }
            serverURL = serverURL + "/getELOPreview";

            //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
            req.open('POST', serverURL, true);
            //req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            req.setRequestHeader("Content-Type","application/json");
            //req.setRequestHeader("Timeout", 0.1);
            req.send(jsonParams);

            elolist.clearSelection();
        },

        showELOWindow : function() {

            var elolist = document.getElementById("elolist");
            var selectedItem = elolist.getSelectedItem(0);
            var uri = selectedItem.childNodes[3].getAttribute("label");
            //window.alert("URI: "+uri);

            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

            //username and password will be received from the preferences
            var prefs = Components.classes["@mozilla.org/preferences-service;1"]
            .getService(Components.interfaces.nsIPrefService);
            prefs = prefs.getBranch("extensions.scylighter.");
            var username = prefs.getCharPref("username");
            var password = prefs.getCharPref("password");
            var defaultaddress = prefs.getCharPref("defaultaddress");
            var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
            var address = prefs.getCharPref("address");

            //RESTful webservice request
            var req = new XMLHttpRequest();

            //Parameters for the webservice as JSON, stringified for transmission
            var params = {};
            params.uri = uri;
            var jsonParams = JSON.stringify(params);

            //the scylighter-strings from the stringbundle
            this.strings = top.window.document.getElementById("scylighter-strings");

            req.onreadystatechange = function (aEvt) {
                var alertString="";
                try{
                    if (req.readyState == 4) {
                        if(req.status == 200){
                            //The response as JSON has to be parsed
                            var jsObject = JSON.parse(req.responseText);
                            var preview = jsObject.preview;
                            //window.alert(preview);
                            //opens a new Browser-Window without Navigation and sets the preview to the documents content
                            myPreviewWindow = window.open('','','resizable=yes,scrollbars=yes,width=700,height=520');
                            myPreviewWindow.document.body.innerHTML = preview;
                        //var previewELOWindow = window.open('','','resizable=yes,scrollbars=yes,width=700,height=600');
                        //previewELOWindow.top.window.document.body.innerHTML = preview;
                        }
                        else {
                            alertString = top.window.document.getElementById("scylighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("scylighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("scylighter-strings").getString(req.responseText);
                            window.alert(alertString);
                        }
                    } else {

                }
                }catch (e) {
                    alertString = top.window.document.getElementById("scylighter-strings").getString("noServerResponse");
                    window.alert(alertString);

                }
            };

            var serverURL = "";
            if (usedefaultaddress){
                serverURL = defaultaddress;
            } else {
                serverURL = address;
            }
            serverURL = serverURL + "/getELOPreview";

            //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
            req.open('POST', serverURL, true);
            //req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            req.setRequestHeader("Content-Type","application/json");
            //req.setRequestHeader("Timeout", 0.1);
            req.send(jsonParams);

            elolist.clearSelection();
        },

        removeHighlight: function() {

            var node = document.popupNode;
            if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("scylighter")) {
                var selectionId = "list_"+node.getAttribute("belongsTo");

                //node.setAttribute("sourceURL","<<!!null!!>>");
                var listItem =  document.getElementById("sidebar").contentDocument.getElementById(selectionId);
                //item.setAttribute("sourceURL","<<!!null!!>>");
                var summaryBox = document.getElementById("sidebar").contentDocument.getElementById("summaryBox");
                summaryBox.removeItemAt(summaryBox.getIndexOfItem(listItem));
                var n = node.nextHighlight;
                while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                    while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                    n = n.parentNode.removeChild(n).nextHighlight;
                }

                var record = n;
                node.nextHighlight = null;  //break chain so loop can exit

                n = record.firstNode;
                while(n instanceof HTMLSpanElement && n.hasAttribute("scylighter")) {
                    while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                    n = n.parentNode.removeChild(n).nextHighlight;
                }

                record.firstNode = null;
                record.lastNode.nextHighlight = null;
                record.lastNode = null;

                //everything below modifies unsafe objects on the webpage. Do this last.

                var hi = node.ownerDocument.defaultView.scylighterInfo;
                for(var i=hi.highlights.length-1; i>=0; i--) {
                    if(hi.highlights[i] == record) {
                        hi.highlights.splice(i, 1);
                        break;
                    }
                }
                hi.dirty = true;
            }
            this.correctSources();
        },

        undo: function(){
            //NOT used at the moment
            if (highlightCount>0) {
                var nodeID = "highlight_" + (highlightCount-1)+"_" + "background-color: FFFF";
                var oldNode = document.getElementById(nodeID).node;
                oldNode.setAttribute("style", "background-color: white;");
            }
            window.alert("Undo not possible");
        },
        openHelp: function(){
            this.strings = top.window.document.getElementById("scylighter-strings");
            var helpFileDirectory =this.strings.getString("helpFileDirectory");
            if (document.getElementById('titleBox')==null){
                //help accessed via menubar
                //needs other relative address
                helpFileDirectory=this.strings.getString("helpFileDirectoryAlternative");
            }
            var windowObjectReference = window.open(helpFileDirectory, "Help", "chrome,resizable=yes,width=600,height=400");
            windowObjectReference.focus();
    
        },
        getPreview: function(){

        
            //creates a HTML representation of the document for preview

            //Loading the stringbundle from the xul document -->scylighter.properties
            this.strings = top.window.document.getElementById("scylighter-strings");
            //window.alert(this.strings==null);


            //---------------------------------------------------

            var titleBox = document.getElementById('titleBox');
            if (titleBox==null){
                titleBox = sidebar.contentDocument.getElementById('titleBox');
            }
            var summaryHTML = "<document>";

            //The header especially contains the styles of the summary
            var header = 	"<head> <title>"+titleBox.value+"</title> <style type=\"text/css\">"+
            "h1{font-family:Georgia,\"Times New Roman\",Times,serif; font-weight:normal; border-bottom:3px solid #E2E1DE; font-size:200%; margin-bottom:0.5em;} " +
            "h2{ font-family:Georgia,\"Times New Roman\",Times,serif; font-weight:normal; font-size:130%; } " +
            "p{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; padding:0;} " +
            "ul{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; list-style-type: disc;} " +
            "li{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; margin-bottom:0.25em; } " +
            "a{ font-family:Verdana,Sans-Serif;} " +
            "</style></head>";

            summaryHTML = summaryHTML + header + "<body>";
            //append title
            summaryHTML = summaryHTML + "<h1>"+ titleBox.value + "</h1>";

            //append summary from the summmaryBox
            var summaryBox = document.getElementById('summaryBox');
            if (summaryBox==null){
                summaryBox = sidebar.contentDocument.getElementById('summaryBox');
            }
            var bullets = "";
            //window.alert("summaryBox.value==null");
            for(i = 0; i < summaryBox.itemCount; i++){
                var summaryItem = summaryBox.getItemAtIndex(i);
                if (summaryItem.getAttribute("itemType")=="image-link"){
                    var imageTag = "<img src=\""+summaryItem.label+"\">";
                    bullets = bullets + "<li> <br>"+imageTag;
                }else {
                    bullets = bullets + "<li>"+summaryItem.label;
                }

            }
            summaryHTML = summaryHTML +"<h2>"+ this.strings.getString("summary") +"</h2>"+ "<ul>" + bullets + "</ul>";

            //append comments
            //Maybe it is a good idea to split multiple comments by /n and branch the comments-tag
            var commentBox = document.getElementById('commentBox');
            if (commentBox==null){
                commentBox = sidebar.contentDocument.getElementById('commentBox');
            }
            summaryHTML = summaryHTML + "<h2>"+this.strings.getString("comments")+"</h2>" + "<p>" + commentBox.value + "</p>";

            //append sources
            //Maybe it is a good idea to split multiple sources by /n and branch the sources-tag
            var urlBox = document.getElementById('urlBox');
            if (urlBox==null){
                urlBox = sidebar.contentDocument.getElementById('urlBox');
            }
            //split at \n for multiple sources and create hyperlinks
            var sources = urlBox.value.split("\n");
            summaryHTML = summaryHTML + "<h2>"+this.strings.getString("sources")+"</h2>" + "<p>";
            for (i=0; i<sources.length;i++) {
                summaryHTML = summaryHTML + "<a href=\"" + sources[i] + "\" target=\"_blank\">" + sources[i] + "</a> <br>";
            }
            summaryHTML = summaryHTML + "</p>";

            //close the document tag
            summaryHTML = summaryHTML + "</body> </document>";

            //window.alert("summaryHTML: \n --------------------------------------------\n"+summaryHTML);

            //---------------------------------------------------
            return summaryHTML;
        },
        preview: function(){
            var summary = this.getPreview();
            this.strings = top.window.document.getElementById("scylighter-strings");
            var titleBox = document.getElementById('titleBox');
            if (titleBox==null){
                titleBox = sidebar.contentDocument.getElementById('titleBox');
            }
            //opens a new Browser-Window without Navigation and sets the preview to the documents content
            myWindow = window.open('',"preview",'resizable=yes,scrollbars=yes,width=600,height=520');
            myWindow.document.body.innerHTML = summary;
            myWindow.document.title = titleBox.value;
        },
        print: function(){
            var summary = this.getPreview();
            this.strings = top.window.document.getElementById("scylighter-strings");
            var titleBox = document.getElementById('titleBox');
            if (titleBox==null){
                titleBox = sidebar.contentDocument.getElementById('titleBox');
            }
            var printerTitle = this.strings.getString("printPreview")+titleBox.value;
            //opens a new Browser-Window without Navigation and sets the preview to the documents content
            myWindow = window.open('',"preview",'resizable=yes,scrollbars=yes,width=600,height=520');
            myWindow.document.body.innerHTML = summary;
            myWindow.document.title = printerTitle;
            myWindow.print();
        },
        onAfterUnload:function(e){
            window.alert("AfterUnload called");
        }
    };


    //Components.utils.import("resource://scylighter/highlights.jsm");
    //General eventlistener
    window.addEventListener("beforeunload", function(e) {
        scylighter.onBeforeUnload(e);
    }, false);
    window.addEventListener("afterunload", function(e) {
        scylighter.onAfterUnload(e);
    }, false);
    window.addEventListener("unload", function(e) {
        scylighter.onUnload(e);
    }, false);
    window.addEventListener("load", function(e) {
        scylighter.onLoad(e);
    }, false);

    //Adding listener for Tab-events
    var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
    var container = mainWindow.gBrowser.tabContainer;
    container.onselect = scylighter.findBrokenHighlights;
}