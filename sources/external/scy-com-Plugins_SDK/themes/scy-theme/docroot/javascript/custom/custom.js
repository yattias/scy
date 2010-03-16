function openWindowWithResolution(locHref, message, width, height, fullscreen) {
    // Codiert von Thomas Sojka am 18.04.2006
    // cit_06eli102_SCOFenstergroesse
    // JSFunktion zum öffnen des WBT Fensters mit einer zuvor definierten Größe
    if (parentAktiv) {
        parentAktiv = false;
        href = locHref;
        var check = false;

        // Überprüfung das message gesetzt ist
        if (message != undefined && message != "undefined" && message != "") {
            startsWithWarning = true;
            check = confirm(message);
        } else {
            check = true;
        }
        if (check) {
            if (fullscreen) {
                SCOWindow = window.open(href + "&doWait=true", "SCOWindow", "width=" + screen.availWidth + ",height=" + screen.availHeight + ",left=0,top=0,scrollbars=auto,resizable=yes,  fullscreen=yes, toolbar=no, location=no, directories=no, status=no, menubar=no");
                //SCOWindow = window.open(href+"&doWait=true","SCOWindow","width="+screen.availWidth+",height="+screen.availHeight+",left=0,top=0,scrollbars=auto,resizable=1");
                SCOWindow.moveTo(0, 0);
            } else {
                SCOWindow = window.open(href + "&doWait=true", "SCOWindow", "width=" + width + ",height=" + height + ",left=0,top=0,location=no,scrollbars=auto,resizable=yes, menubar=no, status=no, toolbar=no");
            }
            startInterval = setInterval("startSCO()", 3000);
        } else {
            startsWithWarning = false;
            parentAktiv = true;
        }
    }else{
        if(SCOWindow){
            SCOWindow.focus();
        }
    }
}

function openDemo() {
    w = window.open("http://grippeschutz-portal.reflact.com/content/sco1/index_offline.html", "demo", "width=300,height=300,left=50,top=50,resizable=true");
    w.focus();
}

function openFull(userId) {
    jQuery.get("http://grippeschutz-portal.reflact.com/letstrain/LoginAction.do?pass=start1&login=pascal.gehring", function(data) {
        openWindowWithResolution('http://grippeschutz-portal.reflact.com/letstrain/scoExec/2fslgyhg/sco1/index.html?launch=213747&user='+userId+'&item=213425','',1024,768,false)
    });
}

function openWBT(href,courseId,userId,itemId) {
    jQuery.get("http://grippeschutz-portal.reflact.com/letstrain/LoginAction.do?pass=start1&login=pascal.gehring", function(data) {
        openWindowWithResolution('http://grippeschutz-portal.reflact.com/letstrain/scoExec/'+href+'?launch='+courseId+'&user='+userId+'&item='+itemId,'',800,600,false)
    });
}