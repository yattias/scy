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

//const serverURL="http://localhost:8080/ELOSaver/resources/saveELO";
//const serverURL="http://localhost:33604/ELOSaver/resources/saveELO";

var overlay = {

  onLoad: function() {
    // initialization code
	var sidebar = top.window.document.getElementById("sidebar-box");
	if (sidebar.hidden){
		//disable (de-)highlight-command on context-menu
		document.getElementById("context-scylighter").disabled = true;
		document.getElementById("context-descylighter").disabled = true;
                document.getElementById("menu-scy-new").disabled=true;
                document.getElementById("menu-scy-save").disabled=true;
                document.getElementById("menu-scy-preview").disabled=true;
                document.getElementById("menu-scy-print").disabled=true;
	} else {
		//enable (de-)highlight-command on context-menu
		document.getElementById("context-scylighter").disabled = false;
		document.getElementById("context-descylighter").disabled = false;
                document.getElementById("menu-scy-new").disabled=false;
                document.getElementById("menu-scy-save").disabled=false;
                document.getElementById("menu-scy-preview").disabled=false;
                document.getElementById("menu-scy-print").disabled=false;
	}
    this.initialized = true;
    //this.strings = top.window.document.getElementById("scylighter-strings");
    //document.getElementById("contentAreaContextMenu").addEventListener("popupshowing", function(e) { this.showContextMenu(e); }, false);
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

	sidebarExists:function(){
	var sidebar = top.window.document.getElementById("sidebar");
	if (sidebar == null) {
		//window.alert("sidebarExists = false");
		return false;
	} else {
		//window.alert("sidebarExists = true");
		return true;
	}
  },
  onSidebarChanged: function(){
	//toggleSidebar("viewSidebar");
	var sidebar = top.window.document.getElementById("sidebar-box");
	if (sidebar.hidden){
		//disable (de-)highlight-command on context-menu
		document.getElementById("context-scylighter").disabled = true;
		document.getElementById("context-descylighter").disabled = true;
                document.getElementById("menu-scy-new").disabled=true;
                document.getElementById("menu-scy-save").disabled=true;
                document.getElementById("menu-scy-preview").disabled=true;
                document.getElementById("menu-scy-print").disabled=true;
	} else {
		//enable (de-)highlight-command on context-menu
		document.getElementById("context-scylighter").disabled = false;
		document.getElementById("context-descylighter").disabled = false;
                document.getElementById("menu-scy-new").disabled=false;
                document.getElementById("menu-scy-save").disabled=false;
                document.getElementById("menu-scy-preview").disabled=false;
                document.getElementById("menu-scy-print").disabled=false;
	}
  },

  showContextMenu: function(event) {
    // show or hide the menuitem based on what the context menu is on
    // see http://kb.mozillazine.org/Adding_items_to_menus
    document.getElementById("context-scylighter").hidden = gContextMenu.onImage;
   },
  onMenuItemCommand: function(e) {

},

  onToolbarButtonCommand: function(e) {
    // just reuse the function above.  you can change this, obviously!
    scylighter.onMenuItemCommand(e);
  },

};

window.addEventListener("load", function(e) {overlay.onLoad(e);}, false);
