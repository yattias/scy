dojo.provide("scy.layout.AppletPane");
dojo.experimental("scy.layout.AppletPane");

dojo.require("dojox.layout.FloatingPane");

dojo.declare("scy.layout.AppletPane",
	[ dojox.layout.FloatingPane ],
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

    },
	bringToTop: function(){
		// summary: bring this FloatingPane above all other panes
		var windows = dojo.filter(
			this._allFPs,
			function(i){
				return i !== this;
			},
		this);
		windows.sort(function(a, b){
			return a.domNode.style.zIndex - b.domNode.style.zIndex;
		});
		windows.push(this);

        // Loop through the other windows in the page
		dojo.forEach(windows, function(w, x){
            if (w.declaredClass == "scy.layout.SCYPane" && w.bgIframe) {
                // deattach the background iframe from this window
                // so that this window appears on top of it
                w.bgIframe.destroy();
            }
			w.domNode.style.zIndex = this._startZ + (x * 2);
			dojo.removeClass(w.domNode, "dojoxFloatingPaneFg");
		}, this);
		dojo.addClass(this.domNode, "dojoxFloatingPaneFg");
	}
});