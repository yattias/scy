dojo.provide("scy.layout.AppletPane");
dojo.experimental("scy.layout.AppletPane");

dojo.require("scy.layout.SCYPane");

dojo.declare("scy.layout.AppletPane",
	[ scy.layout.SCYPane ],
	{
	// summary:
	//		A non-modal Floating window.
	//
	// description:
	// 		Makes a `dojox.layout.ContentPane` float and draggable by it's title [similar to TitlePane]
	// 		and over-rides onClick to onDblClick for wipeIn/Out of containerNode
	// 		provides minimize(dock) / show() and hide() methods, and resize [almost]
	//
    postCreate: function() {
        this.inherited(arguments);
        if (dojo.isFF) {
            //this.bgIframe.destroy();
        }
        this.applet = dojo.query("object", this.containerNode)[0];

    },
	bringToTop: function(){
        this.inherited(arguments);
        if (dojo.isFF) {
            this._hideApplet();
            setTimeout(dojo.hitch(this, "_showApplet"), 1);
        }
	},
    _showApplet: function() {
        this.applet.style.visibility="visible";
    },
    _hideApplet: function() {
        this.applet.style.visibility="hidden";
    }
});