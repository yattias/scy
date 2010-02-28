dojo.provide("scy.layout.SCYPane");
dojo.experimental("scy.layout.SCYPane");

dojo.require("dojox.layout.FloatingPane");

dojo.declare("scy.layout.SCYPane",
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
	bringToTop: function(){
		this.inherited(arguments);
        // Remove my background iframe
        this.bgIframe.destroy();
        // Then, add a new iframe to the DOM, ensuring that it appears on top of all other elements
        this.bgIframe = new scy.BackgroundIframe(this.domNode);
	}
});

scy.BackgroundIframe = function(/* DomNode */node){
	// summary:
	//		For IE z-index schenanigans. id attribute is required.
	//
	// description:
	//		new dijit.BackgroundIframe(node)
	//			Makes a background iframe as a child of node, that fills
	//			area (and position) of node

	if(!node.id){ throw new Error("no id"); }
    var iframe = dijit._frames.pop();
    node.appendChild(iframe);
    if(dojo.isIE){
        iframe.style.setExpression("width", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetWidth");
        iframe.style.setExpression("height", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetHeight");
    }
    this.iframe = iframe;
};

dojo.extend(scy.BackgroundIframe, {
	destroy: function(){
		//	summary: destroy the iframe
		if(this.iframe){
			dijit._frames.push(this.iframe);
            dojo.destroy(this.iframe);
			delete this.iframe;
		}
	}
});
