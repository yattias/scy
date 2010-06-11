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
 * The Original Code is Highlighter.
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

//const serverURL="http://localhost:8080/ELOSaver/resources/saveELO";
//const serverURL="http://localhost:33604/ELOSaver/resources/saveELO";


var count = 0;
var updateURI = "";
var sidebar = top.window.document.getElementById("sidebar");

var highlighter = {
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
        //window.alert();
        // initialization code
        updateURI = "";
        this.initialized = true;
        this.strings = top.window.document.getElementById("highlighter-strings");
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
        //the highlighter-strings from the stringbundle
        this.strings = top.window.document.getElementById("highlighter-strings");
        
        var prompts = Components.classes["@mozilla.org/embedcomp/prompt-service;1"].getService(Components.interfaces.nsIPromptService);
        var resultOK = prompts.confirm(null,this.strings.getString("newELODialog"), this.strings.getString("newELODialogText"));

        if (resultOK){
            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;
            var summaryBox = sidebarWindow.document.getElementById('summaryBox');

            //Initialize the global storage for highlights
            Components.utils.import("resource://highlighter/highlights.jsm");
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
        
            updateURI="";
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

    storeSidebar: function(){

        var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
        var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;

        //Initialize the global storage for highlights
        Components.utils.import("resource://highlighter/highlights.jsm");
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
            Components.utils.import("resource://highlighter/highlights.jsm");

            //When starting firefox, restore is called but no URL/Source Box is available
            if(sidebarWindow.document.getElementById('summaryBox')!=null){
                summaryBox.ondrop = highlighter.onDrop;

                var summaryBox = sidebarWindow.document.getElementById('summaryBox');
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
        Components.utils.import("resource://highlighter/highlights.jsm");
        if (bullets.itemTexts.length!=bullets.nodeIDs.length){
            return false;
        }
        return true;
    },

    //Opens the Dialog for opening ELOs
    openELO: function(){

        //Registering this window as "THE" main-window

        //Open the new Overlay for selecting ELOs
        //var windowObjectReference = window.open("chrome://highlighter/content/openELOWindow.xul", "openELOWindow", "chrome,width=790,height=400,resizable=yes,scrollbars=yes, modal");
        var windowObjectReference = window.open("chrome://highlighter/content/openELOWindow.xul", "openELOWindow", "chrome,width=790,height=400,resizable=yes,scrollbars=yes");
        windowObjectReference.focus();

    //Query the webservice and get ELO-Data (String-Representations with IDs/URIs)

    },
    clearList: function(listbox){
        while(listbox.getRowCount()>0){
            listbox.removeItemAt(0);
        }
    },

    retrieveELOList: function(){
        //retrieves a list of the queried ELOs

        var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

        var eloList = document.getElementById("elolist");
        //clear the list to not append the new search-results to the old list
        this.clearList(eloList);

        //Parameters for the webservice:
        var prefs = Components.classes["@mozilla.org/preferences-service;1"]
        .getService(Components.interfaces.nsIPrefService);
        prefs = prefs.getBranch("extensions.highlighter.");
        var username = prefs.getCharPref("username");
        var password = prefs.getCharPref("password");
        var defaultaddress = prefs.getCharPref("defaultaddress");
        var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
        var address = prefs.getCharPref("address");

        //Request to the webservice: Which ELOs fit to the filter criteria
        //RESTful webservice request
        var req = new XMLHttpRequest();

        //Parameters for the webservice as JSON, stringified for transmission
        var params = {};
        params.username = username;
        //params.password = password;
        params.title = document.getElementById("filterTitle").value;
        ;
        params.author = document.getElementById("filterAuthor").value;
        params.date = document.getElementById("filterDate").value;
        ;
        params.keywords = document.getElementById("filterKeywords").value;
        ;
        var jsonParams = JSON.stringify(params);


        //the highlighter-strings from the stringbundle
        this.strings = top.window.document.getElementById("highlighter-strings");

        //Response: A List-Representation of the ELOs
        req.onreadystatechange = function (aEvt) {
            try{
                if (req.readyState == 4) {
                    if(req.status == 200){
                        //var responseText = req.responseText;
                        //var alertString = this.strings.getString(responseText);
                        //window.alert(alertString);

                        //The response as JSON has to be parsed
                        var jsObject = JSON.parse(req.responseText);
                        var elos = jsObject.elos;

                        //fill the listbox with search results:
                        for (i=0;i<elos.length;i++){

                            var titleCell = document.createElement("listcell");
                            titleCell.setAttribute("label", elos[i].title);
                            //titleCell.setAttribute("style", "text-align:right");

                            var authorCell = document.createElement("listcell");
                            authorCell.setAttribute("label", elos[i].author);

                            var dateCell = document.createElement("listcell");
                            dateCell.setAttribute("label", elos[i].date);

                            var uriCell = document.createElement("listcell");
                            uriCell.setAttribute("label", elos[i].uri);

                            var item = document.createElement("listitem");

                            item.appendChild(titleCell);
                            item.appendChild(authorCell);
                            item.appendChild(dateCell);
                            item.appendChild(uriCell);

                            eloList.appendChild(item);

                        //window.alert(item.childNodes[3].getAttribute("label"));
                        }


                        //window.alert(jsObject.errors);
                        //window.alert(jsObject.elos);




                        //window.alert(jsObject.elos[0]);
                        window.alert(top.window.document.getElementById("highlighter-strings").getString(req.responseText));
                        window.alert(req.responseText);
                    }
                    else {
                        var alertString = top.window.document.getElementById("highlighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("highlighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("highlighter-strings").getString(req.responseText);
                        window.alert(alertString);
                    }
                } else {

            }
            }catch (e) {
                var alertString = top.window.document.getElementById("highlighter-strings").getString("noServerResponse");
                window.alert(alertString);

            }
        };

        //setting the server-URL to the right resource
        var serverURL = "";
        if (usedefaultaddress){
            serverURL = defaultaddress;
        } else {
            serverURL = address;
        }
        serverURL = serverURL + "/getELOList";

        //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
        req.open('POST', serverURL, true);
        //req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        req.setRequestHeader("Content-Type","application/json");
        //req.setRequestHeader("Timeout", 0.1);
        req.send(jsonParams);


    },

    retrieveELO: function(){

    //when the user selected a specific ELO and clicks "open" in the Open-ELO Dialog, a request with the ELO ID/URI will be sent to the webservice

    //The response will be the ELO, which is a json String in case of web-ELOs
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

        //XX1
        document.getElementById("context-highlighter").hidden = gContextMenu.onImage;
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
                if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("highlighter")) {
                    var n = node.nextHighlight;
                    while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                        while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                        n = n.parentNode.removeChild(n).nextHighlight;
                    }

                    var record = n;
                    node.nextHighlight = null;  //break chain so loop can exit

                    n = record.firstNode;
                    while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                        while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                        n = n.parentNode.removeChild(n).nextHighlight;
                    }

                    record.firstNode = null;
                    record.lastNode.nextHighlight = null;
                    record.lastNode = null;

                    //everything below modifies unsafe objects on the webpage. Do this last.

                    var hi = node.ownerDocument.defaultView.highlighterInfo;
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

        if (sidebar == null){
            var urlBox = document.getElementById('urlBox');
            urlBox.value = "";
            var summaryBox = document.getElementById('summaryBox');
        } else {
            var sidebarWindow = sidebar.contentWindow;
            var urlBox = sidebarWindow.document.getElementById('urlBox');
            urlBox.value = "";
            var summaryBox = sidebarWindow.document.getElementById('summaryBox');
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
            highlighter.addSummaryItemAsImage(link);
        } else {
            count = count + 1;

            var sidebarWindow = top.window.document.getElementById("sidebar").contentWindow;

            var summaryBox = sidebarWindow.document.getElementById('summaryBox');
            var urlBox = sidebarWindow.document.getElementById('urlBox');

            var theSelection = top.window.content.document.getSelection();

            var itemText = theSelection;
            var newNodeId = "selection_" + count;

            var item = summaryBox.appendItem(itemText,newNodeId);
            item.setAttribute("tooltiptext",itemText);
            item.setAttribute("crop","center");
            var sourceURLValue = top.window.content.document.location;
            item.setAttribute("sourceURL",sourceURLValue);
            var listId = "list_"+newNodeId;
            item.setAttribute("id",listId);

            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
            //window.alert(mainWindow.document.activeElement);

            highlighter.highlightDoc();

            highlighter.correctSources();
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

        highlighter.correctSourcesFromSidebar();
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
        var brokenHighlightCandidates = highlighter.getElementsByAttributeDOM("belongsTo","*");
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
                if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("highlighter")) {
                    var n = node.nextHighlight;
                    while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                        while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                        n = n.parentNode.removeChild(n).nextHighlight;
                    }

                    var record = n;
                    node.nextHighlight = null;  //break chain so loop can exit

                    if (record!=null){
                        n = record.firstNode;
                        while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                            while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                            n = n.parentNode.removeChild(n).nextHighlight;
                        }

                        record.firstNode = null;
                        record.lastNode.nextHighlight = null;
                        record.lastNode = null;

                        //everything below modifies unsafe objects on the webpage. Do this last.

                        var hi = node.ownerDocument.defaultView.highlighterInfo;
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
        count = count + 1;
        //XXX


        var sidebarWindow = document.getElementById("sidebar").contentWindow;

        var summaryBox = sidebarWindow.document.getElementById('summaryBox');
        var urlBox = sidebarWindow.document.getElementById('urlBox');

        //XX1
        var theSelection = top.window.content.document.getSelection();
        //var theSelection = window.content.getSelection();

        if (theSelection!=""){

            var itemText = theSelection;
            var newNodeId = "selection_" + count;

            //save it too the highlights.jsm module
            //Initialize the global storage for highlights
            //Components.utils.import("resource://highlighter/highlights.jsm");
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

        //XX1
        var node = document.popupNode;
        if (node == null){
            var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
            var browser = mainWindow.gBrowser;
            win = browser.contentWindow;
        //window.alert(win.getSelection());
        //window.alert(win.getSelection());
        /*win = top.window.document;
		window.alert(win.getSelection());
		win = mainWindow;
		window.alert(win.getSelection());*/
        } else {
            var win = node.ownerDocument.defaultView;
        }
        var sel = win.getSelection();

        var fgcolor = "black";
        var bgcolor = "yellow";
        //window.alert(sel);

        //if exist highlight and no selections then change current highlight colour
        //if((sel.rangeCount == 0 || sel.getRangeAt(0).collapsed)
        //    && node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("highlighter")) {

        //  highlighter.changeHighlightColor(node, fgcolor, bgcolor);

        //} else
        if(sel.rangeCount > 0) {
            highlighter.highlightRange(win, fgcolor, bgcolor);
        }
    },

    highlightRange: function(win, fgcolor, bgcolor) {
        const record = {
            offsetY:Number.NaN,
            firstNode:null,
            lastNode:null
        };

        const wrap = win.document.createElement("span");
        wrap.setAttribute("highlighter", bgcolor);
        var belongsToValue = "selection_"+count;
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
            highlighter.highlightRange0(sel.getRangeAt(i), _createWrapper, record);
        }

        if(record.firstNode != null) {
            record.lastNode.nextHighlight = record; //connect linked list back to record

            if(!win.highlighterInfo)win.highlighterInfo = {
                highlights:[],
                dirty:true
            };

            var arr = win.highlighterInfo.highlights;
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

            win.highlighterInfo.dirty = true;

            if(highlighter.warnBeforeLeave)
                highlighter.ensureUnloadWarning(win);

            if(highlighter.shouldDisplayIcons)
                highlighter.ensurehighlighterView(win);
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
        highlighter.onMenuItemCommand(e);
    },
    openPreferences: function(){
        var windowObjectReference = window.open("chrome://highlighter/content/options.xul", "Options", "chrome");
        windowObjectReference.focus();
    },
    saveELO: function(){

        //the highlighter-strings from the stringbundle
        this.strings = top.window.document.getElementById("highlighter-strings");

        //XXX
        //var mainWindow = window.QueryInterface(Components.interfaces.nnsIInterfaces)...
        var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);

        //for testing, a c# webservice was used
        //there is also a RESTful approach

        //username and password will be received from the preferences
        var prefs = Components.classes["@mozilla.org/preferences-service;1"]
        .getService(Components.interfaces.nsIPrefService);
        prefs = prefs.getBranch("extensions.highlighter.");
        var username = prefs.getCharPref("username");
        var password = prefs.getCharPref("password");
        var defaultaddress = prefs.getCharPref("defaultaddress");
        var usedefaultaddress = prefs.getBoolPref("usedefaultaddress");
        var address = prefs.getCharPref("address");
        if ((username=="username" && password=="password")||(username=="")||(password=="")){
            window.alert(top.window.document.getElementById("highlighter-strings").getString("setUpLoginData"));
            this.openPreferences();
        } else {

            //make the XMLHttpRequest (POST)!!!

            //a problem with c# webservices was the &-symbol, which causes the transmission of the string to break
            //a solution might be to replace all occurences of & by another string

            //var rawDoc = window.content.document.documentElement.innerHTML;
            //var rawDoc = mainWindow.document.documentElement.innerHTML;

            //eventually parse the String for HTML Special Chars like &nbsp;
            //var clearedDoc = rawDoc.replace("&nbsp;", " ");


            //A marker for the &, which causes errors when submitting the html document to the server
            //var clearedDoc = rawDoc.replace(/&/g, "XXXYYYZZZ");

            //replacing src-attributes
            //var newSrcTag = "src=\""+window.content.document.location;
            //var newSrcTag = "src=\""+mainWindow.document.location;
            //Problem: replace src-tags, but only relative sources...
            //clearedDoc = clearedDoc.replace(/src\s*=\s*"/g,newSrcTag);

            //1. normalize src (remove spaces)
            //clearedDoc = clearedDoc.replace(/src\s*=\s*"/g,"src=\"");

            //2. mark every src that begins with http: with a space
            //the url shouldnt be appended at absolute Paths
            //clearedDoc = clearedDoc.replace(/src="http/g,"src =\"http");

            //3. remove paths like ../..
            //clearedDoc = clearedDoc.replace(/src="\.\.\/\.\./g,"src=\"");

            //4. append the site URL to relative Paths
            //clearedDoc = clearedDoc.replace(/src="/g,newSrcTag);


            //var htmlDoc = "<html>"+clearedDoc+"</html>";
            //window.alert(clearedDoc);

            /*
        //C# webservice!
        var req = new XMLHttpRequest();
        var url = "http://localhost:63261/Service1.asmx/saveELO";
        var params = "html="+htmlDoc;

        req.onreadystatechange = function (aEvt) {
        if (req.readyState == 4) {
            if(req.status == 200)
               window.alert(req.responseXML.getElementsByTagName("string")[0].childNodes[0].nodeValue);
            else
               window.alert("Error loading page\n");
          }
        };

        req.open('POST', url, true);
        req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        req.send(params);
        }*/

            /*//---------------------------------------------
        //create the summary-document (HTML)
		//see below for a working implementation (preview-function)

        //---------------------------------------------------*/


            //create the summaryDocument (XML)

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
                bullets = bullets + "<bullet>"+summaryBox.getItemAtIndex(i).label+"</bullet>";
            }
            summaryXML = summaryXML + "<summary>" + bullets + "</summary>";

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

            //----------------------------------------

            //var htmlDoc = mainWindow.document.documentElement.innerHTML;
            var htmlDoc = window.content.document.documentElement.innerHTML;

            //window.alert(htmlDoc);

            //----------------------------------------

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
            params.content = "<preview><![CDATA["+this.getPreview()+"]]></preview>"+"<annotations> "+summaryXML+" </annotations>"+"\n <html> \n <![CDATA["+htmlDoc+"]]> \n </html>";
            params.username = username;
            params.password = password;
            params.type = "scy/webresourcer";
            if (countryCode == null){
                params.language = languageCode;
            } else {
                params.language = languageCode;
                params.country = countryCode;
            }
            if (updateURI.length>1){
                params.uri = updateURI;
            }
            params.title = titleBox.value;
            var jsonParams = JSON.stringify(params);

            req.onreadystatechange = function (aEvt) {
                try{
                    if (req.readyState == 4) {
                        if(req.status == 200){
                            //var responseText = req.responseText;
                            //var alertString = this.strings.getString(responseText);
                            //window.alert(alertString);
                            updating = true;
                            updateURI = req.responseText;
                            window.alert(top.window.document.getElementById("highlighter-strings").getString("eloSaved"));
                        }
                        else {
                            var alertString = top.window.document.getElementById("highlighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("highlighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("highlighter-strings").getString(req.responseText);
                            window.alert(alertString);
                        }
                    } else {

                }
                }catch (e) {
                    var alertString = top.window.document.getElementById("highlighter-strings").getString("noServerResponse");
                    window.alert(alertString);

                }
            };

            var serverURL = "";
            if (usedefaultaddress){
                serverURL = defaultaddress;
            } else {
                serverURL = address;
            }
            serverURL = serverURL + "/saveELO";

            //asynchronous request per POST, Content-Type JSON --> webservice consumes JSON
            req.open('POST', serverURL, true);
            //req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            req.setRequestHeader("Content-Type","application/json");
            //req.setRequestHeader("Timeout", 0.1);
            req.send(jsonParams);
        }

    /*//JSON Request maybe useful for another platform or version
        requestNumber = JSONRequest.post(
    "http://localhost:33604/ELOSaver/resources/saveELO",
    {
        xml:"TestXML",
        username:"TestUser",
        password:"TestPassword"
    },
    function (requestNumber, value, exception) {
        window.alert(value);
        if (value) {
            window.alert(value);
        } else {
            window.alert(exception);
        }
    }
);
}*/
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
        prefs = prefs.getBranch("extensions.highlighter.");
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

        //the highlighter-strings from the stringbundle
        this.strings = top.window.document.getElementById("highlighter-strings");

        req.onreadystatechange = function (aEvt) {
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
                        var alertString = top.window.document.getElementById("highlighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("highlighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("highlighter-strings").getString(req.responseText);
                        window.alert(alertString);
                    }
                } else {

            }
            }catch (e) {
                var alertString = top.window.document.getElementById("highlighter-strings").getString("noServerResponse");
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
        prefs = prefs.getBranch("extensions.highlighter.");
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

        //the highlighter-strings from the stringbundle
        this.strings = top.window.document.getElementById("highlighter-strings");

        req.onreadystatechange = function (aEvt) {
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
                        var alertString = top.window.document.getElementById("highlighter-strings").getString("errorLoadingPage")+"\n"+document.getElementById("highlighter-strings").getString("errorCode") + req.status + "\n" + document.getElementById("highlighter-strings").getString(req.responseText);
                        window.alert(alertString);
                    }
                } else {

            }
            }catch (e) {
                var alertString = top.window.document.getElementById("highlighter-strings").getString("noServerResponse");
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
        if(node.nodeType == Node.ELEMENT_NODE && node.hasAttribute("highlighter")) {
            var selectionId = "list_"+node.getAttribute("belongsTo");

            //node.setAttribute("sourceURL","<<!!null!!>>");
            var listItem =  document.getElementById("sidebar").contentDocument.getElementById(selectionId);
            //item.setAttribute("sourceURL","<<!!null!!>>");
            var summaryBox = document.getElementById("sidebar").contentDocument.getElementById("summaryBox");
            summaryBox.removeItemAt(summaryBox.getIndexOfItem(listItem));
            var n = node.nextHighlight;
            while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                n = n.parentNode.removeChild(n).nextHighlight;
            }

            var record = n;
            node.nextHighlight = null;  //break chain so loop can exit

            n = record.firstNode;
            while(n instanceof HTMLSpanElement && n.hasAttribute("highlighter")) {
                while(n.hasChildNodes())n.parentNode.insertBefore(n.firstChild, n);
                n = n.parentNode.removeChild(n).nextHighlight;
            }

            record.firstNode = null;
            record.lastNode.nextHighlight = null;
            record.lastNode = null;

            //everything below modifies unsafe objects on the webpage. Do this last.

            var hi = node.ownerDocument.defaultView.highlighterInfo;
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
        this.strings = top.window.document.getElementById("highlighter-strings");
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

        //Loading the stringbundle from the xul document -->highlighter.properties
        this.strings = top.window.document.getElementById("highlighter-strings");
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
        this.strings = top.window.document.getElementById("highlighter-strings");
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
        this.strings = top.window.document.getElementById("highlighter-strings");
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
var dehighlighter = {
    //dehighlighter will probably not be maintained anymore. Maybe the sidebar's delete functionality is better to use.
    onLoad: function() {
        // initialization code
        this.initialized = true;
        this.strings = document.getElementById("dehighlighter-strings");
        document.getElementById("contentAreaContextMenu")
        .addEventListener("popupshowing", function(e) {
            this.showContextMenu(e);
        }, false);
    },

    showContextMenu: function(event) {
        // show or hide the menuitem based on what the context menu is on
        // see http://kb.mozillazine.org/Adding_items_to_menus
        document.getElementById("context-dehighlighter").hidden = gContextMenu.onImage;
    },
    onMenuItemCommand: function(e) {
        theSelection = window.content.getSelection();
        range = window.content.getSelection().getRangeAt(0);
        var newNode = document.createElement("span");
        newNode.setAttribute("style", "background-color: white;");
        newNode.appendChild(document.createTextNode(theSelection));
        range.deleteContents();
        range.insertNode(newNode);
    },
    onToolbarButtonCommand: function(e) {
        // just reuse the function above.  you can change this, obviously!
        dehighlighter.onMenuItemCommand(e);
    }

};
//Components.utils.import("resource://highlighter/highlights.jsm");
//General eventlistener
window.addEventListener("beforeunload", function(e) {
    highlighter.onBeforeUnload(e);
}, false);
window.addEventListener("afterunload", function(e) {
    highlighter.onAfterUnload(e);
}, false);
window.addEventListener("unload", function(e) {
    highlighter.onUnload(e);
}, false);
window.addEventListener("load", function(e) {
    highlighter.onLoad(e);
}, false);

//Adding listener for Tab-events
var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIWebNavigation).QueryInterface(Components.interfaces.nsIDocShellTreeItem).rootTreeItem.QueryInterface(Components.interfaces.nsIInterfaceRequestor).getInterface(Components.interfaces.nsIDOMWindow);
var container = mainWindow.gBrowser.tabContainer;
container.onselect = highlighter.findBrokenHighlights;


