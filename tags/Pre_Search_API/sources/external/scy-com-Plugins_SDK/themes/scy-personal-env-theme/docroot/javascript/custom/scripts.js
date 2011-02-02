function assignUser(userPK) {
    document.forms[0].action.value = 'assign';
    document.forms[0].userPK.value = userPK;
    document.forms[0].submit();
}

function assignGroup(Gid) {
    document.forms[0].dispatch.value = 'assign';
    document.forms[0].gid.value = Gid;
    document.forms[0].submit();
}

function submitForm() {
    this.document.forms[0].dispatch.value = "commit";
    this.document.forms[0].submit();
}

function assignParticipantUser(oid) {
    document.forms[0].dispatch.value = 'assign';
    document.forms[0].oid.value = oid;
    document.forms[0].submit();
}

function assignParticipantGroup(participantGroupPK) {
    document.forms[0].dispatch.value = 'assign';
    document.forms[0].participantGroupId.value = participantGroupPK;
    document.forms[0].submit();
}

function commentPopup(url) {
    F1 = window.open(url, "comment", "toolbar=no,menubar=no,resizable=no,scrollbars=yes,status=no,width=400,height=300");
    F1.focus();
}

function keyPopup(url) {
    F1 = window.open(url, "key", "toolbar=no,menubar=no,resizable=no,scrollbars=no,status=no,width=400,height=400");
    F1.focus();
}

function editAnswersPopup(url) {
    F1 = window.open(url, "editAnswers", "toolbar=no,menubar=no,resizable=no,scrollbars=no,status=no,width=400,height=150");
    F1.focus();
}

function statisticSettingPopup(url) {
    F1 = window.open(url, "statisticSettingPopup", "toolbar=no,menubar=no,resizable=no,scrollbars=no,status=no,width=450,height=200");
    F1.focus();
}

function textAnswerPopup(url) {
    F1 = window.open(url, "textAnswerPopup", "toolbar=no,menubar=no,resizable=yes,scrollbars=yes,status=no,width=600,height=600");
    F1.focus();
}

function AdminPopup(url) {
    F1 = window.open(url, "Administration", "toolbar=no,menubar=no,resizable=yes,scrollbars=yes,status=no,width=800,height=600");
    F1.focus();
}

function ReportPopup(url) {
    F1 = window.open(url, "Report", "toolbar=no,menubar=no,resizable=yes,scrollbars=yes,status=no,width=800,height=600");
    F1.focus();
}

function contextHelpPopup(url) {
    F1 = window.open(url, "contextHelpPopup", "toolbar=no,menubar=no,resizable=no,scrollbars=auto,status=no,width=380,height=400");
    F1.focus();
}

function legendPopup(url) {
    F1 = window.open(url, "legendPopup", "toolbar=no,menubar=no,resizable=no,left=100,scrollbars=yes,status=no,width=350,height=550");
    F1.focus();
}

function FAQ(url) {
    F1 = window.open(url, "FAQ", "toolbar=no,menubar=no,resizable=no,scrollbars=yes,status=no,width=700,height=500");
    F1.focus();
}

function showExport(url) {
    F1 = window.open(url, "showExport", "toolbar=no,menubar=no,resizable=yes,scrollbars=yes,status=no,width=770,height=600");
    F1.focus();
}

function printView(url) {
    F1 = window.open(url, "printView", "toolbar=no,menubar=no,resizable=no,scrollbars=yes,status=no,width=770,height=600");
    F1.focus();
}


function firstLogin() {
    F1 = window.open('HomeAction.do?dispatch=showFirstLogin', 'firstLogin', 'width=360,height=270,left=220,top=290');
}

function viewMessage(url) {
    F1 = window.open(url, "viewMessage", "toolbar=no,menubar=no,resizable=no,scrollbars=yes,status=no,width=770,height=600");
    F1.focus();
}


function activationLogin(url) {
    F1 = window.open(url, 'Login', 'width=360,height=310,left=220,top=290');
    F1.focus();
}

function showHint(url) {
    F1 = window.open(url, 'Hint', 'toolbar=no,menubar=no,resizable=yes,scrollbars=yes,status=no,width=360,height=310,left=220,top=290');
    F1.focus();
}

function activateCourse(url) {
    if (url != undefined) {
        opener.openWindow(url)
        this.close();
    }
}


function changeLockLevelPopup(url) {
    F1 = window.open(url, 'changeLockLevel', 'width=360,height=310,left=220,top=290');
    F1.focus();
}

function refresh_close(url)
{
    window.opener.location.href = url;
    window.opener.focus();
    window.close();
}

var blinkColTbl = new Array();
blinkColTbl[0] = "red";
blinkColTbl[1] = "black";

var blinkTimeout = 1500;

function blinky() {
    var blinkTimeout = 1500;
    blink();
}
function zucki() {
    var blinkTimeout = 1000;
    blink();
}

var blinkIdx = 0;
function blink() {
    if (document.all && document.all.blink) {
        blinkIdx = (blinkIdx += 1) % 2;
        var color = blinkColTbl [ blinkIdx ];
        if (document.all.blink.length) {
            for (i = 0; i < document.all.blink.length; i++)
                document.all.blink[i].style.color = color;
        } else
            document.all.blink.style.color = color;
        setTimeout("blink();", blinkTimeout);
    }
}

function selectAll(formindex) {
    for (var i = 0; i < document.forms[formindex].all.length; i++) {
        if (document.forms[formindex].all[i].type == "checkbox")
            document.forms[formindex].all[i].checked = "checked";
    }
}


var SCOWindow;


function openSCOWindow(href, message) {
    var check = true;
    if (message != undefined && message != "undefined") {
        check = confirm(message);
    }
    if (check) {
        SCOWindow = window.open(href, "SCOWindow", "width=788,height=542,left=0,top=0,scrollbars=auto,resizable=1");
        SCOWindow.focus();
    }
}

function closeWindow() {
    if (SCOWindow != null) {
        if (typeof SCOWindow != "undefined" && !SCOWindow.closed) {
            SCOWindow.blur();
            SCOWindow.close();
        }
    }
}

//
// Diese Funktion verhindert gleichzeitiges Starten von mehreren WBT's.
// Aufruf erfolgt im Body-Tag. ( <body onFocus="checkPopi()" ...)
function checkPopi() {
    if (SCOWindow != null) {
        if (typeof SCOWindow != "undefined" && !SCOWindow.closed) {
            SCOWindow.focus();
        }
    }

}

//function ermöglicht refresh

function reloadFormularPage() {
    document.forms[0].submit();
}

//function which shows a wait text in the front while loading content
function WaitBox() {
    document.writeln("<div class=\"preloader\" id=\"preload\" style=\"left: 0px;width: 745px;position: absolute; top: 40%;\" align=\"center\">");
    document.writeln("<span style=\"background-color: #fff;border: 1px solid #036;padding: 5px;display: block;width: 200px;height: 32px;font-family: Verdana;font-size: 12px;\"><img src=\"global_includes\\images\\misc\\nolang\\loading.gif\" width=\"30\" height=\"30\" title=\"loading\" style=\"float: left;border: 1px solid #036;\">" + loadingWait + "</span>");
    document.writeln("</div>");
}
function preload() {
    document.getElementById('preload').visibility = "hidden";
}

function submitChangeAction(action) {
    var chgAction = document.createElement("input");
    chgAction.setAttribute("type", "hidden");
    chgAction.setAttribute("name", "changeAction");
    chgAction.setAttribute("id", "changeAction");
    chgAction.setAttribute("value", action);
    document.forms[0].appendChild(chgAction);
    document.forms[0].submit();
}

var activeFormElements = true;

function deactivateFormElementsWhileLoading() {
    for (var i = 0; i < document.forms[0].all.length; i++) {
        document.forms[0].all[i].disabled = true;
    }
    activeFormElements = false;
}


function activateFormElementsWhileLoading() {
    for (var i = 0; i < document.forms[0].all.length; i++) {
        document.forms[0].all[i].disabled = true;
    }
    activeFormElements = true;
}



