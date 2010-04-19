function resize_iframe() {
	/*
	 * if (parseInt(navigator.appVersion)>3) { if
	 * (navigator.appName=="Netscape") { winW = window.innerWidth-16; winH =
	 * window.innerHeight-16; } if (navigator.appName.indexOf("Microsoft")!=-1) {
	 * winW = document.body.offsetWidth-20; winH =
	 * document.body.offsetHeight-20; winH = document.offsetHeight; } }
	 */

	if (self.innerWidth) {
		frameWidth = self.innerWidth;
		frameHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientWidth) {
		frameWidth = document.documentElement.clientWidth;
		frameHeight = document.documentElement.clientHeight;
	} else if (document.body) {
		frameWidth = document.body.clientWidth;
		frameHeight = document.body.clientHeight;
	}

	// height = winH;
	height = frameHeight - 20;

	// resize the iframe according to the size of the
	// window (all these should be on the same line)

	// alert(height - document.getElementById("frame").offsetTop);
	document.getElementById("frame").height = parseInt(height
			- document.getElementById("frame").offsetTop - 8)
			+ "px";
	document.getElementById("frame").style.height = parseInt(height
			- document.getElementById("frame").offsetTop - 8)
			+ "px";
}

function addNote() {
	var view_iframe = window.frames['frame'];
	if (view_iframe.an) {
		view_iframe.an.AddNote('IcQ7xFdNx');
	}
}

function highlightText() {
	// define iframe
	// var view_iframe = document.getElementById('frame').document;
	var view_iframe = window.frames['frame'];
	// alert(view_iframe);
	view_iframe.Webliter.getSelectedText();
}


/*
function showColorPicker() {
	var color_style = document.getElementById('color_picker').style;
	if (color_style.visibility == 'visible') {
		color_style.visibility = 'hidden';
		color_style.display = 'none';
	} else {
		color_style.visibility = 'visible';
		color_style.display = 'block';
	}
}

function pickColor(color) {
	// set the HIGHLIGHT_COLOR and Webliter.highlight_color variables on the
	// awurl

	// need to try/catch here to make sure this succeeds. If it doesn't display
	// an error to the user.
	var view_iframe = window.frames['frame'];
	view_iframe.HIGHLIGHT_COLOR = color;
	// view_iframe.Webliter.highlight_color = color;

	// remove borders from all the colors then put a border around the selected
	// color
	this.selectHighlightedColor(color);

	var color = color.replace('#', '.');
	var url = 'http://www.awesomehighlighter.com/' + 'user/color/' + color;
	$.ajax( {
		url :url,
		cache :true,
		dataType :"text"
	});
}

function selectHighlightedColor(color) {
	var check_color = color.replace('#', '');

	var color_pickers = getElementsByClassName(document, "*", "color_choice");

	for ( var i = 0; i <= color_pickers.length - 1; i++) {
		if (color_pickers[i].id == 'color_' + check_color) {
			color_pickers[i].style.border = '2px solid black';
		} else {
			color_pickers[i].style.border = 'none';
		}
	}
}

function showHelpText() {
	var help_style = document.getElementById('iframe_help_text').style;
	if (help_style.visibility == 'visible') {
		help_style.visibility = 'hidden';
		help_style.display = 'none';
	} else {
		help_style.visibility = 'visible';
		help_style.display = 'block';
	}
}

*/

function bugReport() {
	window
			.open('http://www.awesomehighlighter.com/main/contact/',
					'Bug Report');
}

function saveHighlights() {
	window.location = 'http://www.awesomehighlighter.com/page/display/IcQ7xFdNx/1';
}

function closeBrowserMessage() {
	document.getElementById('browser_message').style.visibility = 'hidden';
	document.getElementById('browser_message').style.display = 'none';
}

function showBrowserMessage() {
	document.getElementById('browser_message').style.visibility = 'visible';
	document.getElementById('browser_message').style.display = 'block';
}



var WEBLITER_NO_CHECK = 1;
var AWESOME_FRAME = 1;
var MOUSE_MODE = 1;
var FRAME_IS_OK = 1;
