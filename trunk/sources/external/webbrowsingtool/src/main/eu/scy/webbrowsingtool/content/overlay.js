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

if ("undefined" == typeof(scylighterOverlay)) {
    var scylighterOverlay = {

        onLoad: function() {
            // initialization code
            var sidebarBox = document.getElementById("sidebar-box");
            sidebarBox.addEventListener("DOMContentLoaded",function(e) {
                scylighterOverlay.changeMenuitemState();
            }, false);
            scylighterOverlay.changeMenuitemState();
            this.initialized = true;
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

        getSidebar: function(){
            return top.window.document.getElementById("sidebar");
        },

        getSidebarBox: function(){
            return top.window.document.getElementById("sidebar-box");
        },
       
        isSidebarValid: function(){
            var sidebar = scylighterOverlay.getSidebar();
            const location = "chrome://scylighter/content/sidebar.xul";
            if(sidebar!=null){
                var sidebarWindow = sidebar.contentWindow;
                if(sidebarWindow.location.href == location){
                    return true;
                } 
            }
            return false;
        },

        changeMenuitemState: function(){
            var sidebarBox = scylighterOverlay.getSidebarBox();
            if (sidebarBox.hidden || !scylighterOverlay.isSidebarValid()){
                //disable (de-)highlight-command on context-menu
                document.getElementById("context-scylighter").disabled = true;
                document.getElementById("context-descylighter").disabled = true;
                document.getElementById("menu-scy-new").disabled=true;
                document.getElementById("menu-scy-save").disabled=true;
                document.getElementById("menu-scy-preview").disabled=true;
                document.getElementById("menu-scy-print").disabled=true;
                document.getElementById("app-menu-scy-new").disabled=true;
                document.getElementById("app-menu-scy-save").disabled=true;
                document.getElementById("app-menu-scy-preview").disabled=true;
                document.getElementById("app-menu-scy-print").disabled=true;
            } else if(scylighterOverlay.isSidebarValid()){
                //enable (de-)highlight-command on context-menu
                //and only enable if it is scylighter sidebar
                document.getElementById("context-scylighter").disabled = false;
                document.getElementById("context-descylighter").disabled = false;
                document.getElementById("menu-scy-new").disabled=false;
                document.getElementById("menu-scy-save").disabled=false;
                document.getElementById("menu-scy-preview").disabled=false;
                document.getElementById("menu-scy-print").disabled=false;
                document.getElementById("app-menu-scy-new").disabled=false;
                document.getElementById("app-menu-scy-save").disabled=false;
                document.getElementById("app-menu-scy-preview").disabled=false;
                document.getElementById("app-menu-scy-print").disabled=false;
            }
        },

        showContextMenu: function(event) {
            // show or hide the menuitem based on what the context menu is on
            // see http://kb.mozillazine.org/Adding_items_to_menus
            document.getElementById("context-scylighter").hidden = gContextMenu.onImage;
            document.getElementById("context-descylighter").hidden = gContextMenu.onImage;
        }
    };

    window.addEventListener("load", function(e) {
        scylighterOverlay.onLoad(e);
    }, false);
}