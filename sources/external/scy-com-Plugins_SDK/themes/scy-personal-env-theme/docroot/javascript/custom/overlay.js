//adding TopLayer
function addTopLayer(textString) {
    var height = document.body.scrollHeight + 200;
    new Element('div', {id: 'overlay'}).inject($("body"));
    $("overlay").setStyle("height", height);
    new Element('div', {id: 'overlay_loading'}).set("html", "<p style='margin-top: 5px;'>" + textString + "</p><img src='global_includes/images/misc/nolang/ajax-loader.gif' alt='loading...'>").inject($("body"));
}

//remove top layer
function removeTopLayer() {
    $("body").removeChild($("overlay_loading"));
    $("body").removeChild($("overlay"));
}

//Set text in the loading box
function setTextToTopLayer(text) {
    $("overlay_loading").innerHTML = text;
}

//add content layer for showing text or forms and so on
function addContentLayer(id, content, title) {

    var leftMargin = document.body.scrollWidth / 2 - 422;
    leftMargin = Math.round(leftMargin);
    if (content == "") {
        content = "<img style='margin-top: 30%; margin-left:46%;' src='global_includes/images/misc/nolang/ajax-loader.gif' alt='loading...'>";
    }

    hideSelects();
    var height = document.body.scrollHeight + 200;
    new Element('div', {"id": "glassLayer"}).inject($("body"));
    $("glassLayer").setStyle("height", height);
    new Element('div', {"id": id, "class": "contentLayer"}).setStyle("left", leftMargin + "px").inject($("body"));

    addTitleLine(id, title);
    new Element('div', {"id": id + "_content", "class": "contentLayer_content"}).set('html', content).inject($(id));
}

//edit the content of an content layer
function setTextToContentLayer(id, content) {
    $(id + "_content").innerHTML = content;
}

//set style to a content layer
function resizeContentLayer(id, width, height) {
    $(id).setStyle("width", width);
    $(id + "_content").setStyle("width", width - 10);

    if (height != null && height != undefined) {
        $(id).setStyle("height", height);
        $(id + "_content").setStyle("height", height);
    }

}

function maxContentLayer (id) {

    $(id).setStyle("width", 1100);
    $(id + "_content").setStyle("width", 1090);

    $(id + "_title_right").innerHTML= "<a href=\"javascript:minContentLayer('" + id + "')\">_</a>&nbsp;&nbsp;<a href=\"javascript:removeContentLayer('" + id + "')\">X</a>";

}

function minContentLayer (id) {

    $(id).setStyle("width", 800);
    $(id + "_content").setStyle("width", 790);

    $(id + "_title_right").innerHTML= "<a href=\"javascript:maxContentLayer('" + id + "')\">_</a>&nbsp;&nbsp;<a href=\"javascript:removeContentLayer('" + id + "')\">X</a>";

}

//adding title line to layer
function addTitleLine(id, title) {
    var ele1 = new Element('div', {"id": id + "_title", "class": 'contentLayer_title'}).inject($(id));
    new Element('div', {"id": id + "_title_leftt", "class": 'contentLayer_title_left'}).set('html', title).inject(ele1);
    new Element('div', {"id": id + "_title_right", "class": 'contentLayer_title_right'}).set('html', "<a href=\"javascript:removeContentLayer('" + id + "')\">&nbsp;</a>").inject(ele1);
    new Element('div', {"class": 'clear'}).set("html", "&nbsp;").inject(ele1);
}

function setTitle(id, title) {
    $(id + "_title_left").innerHTML = title;
}

//remove content layer
function removeContentLayer(id) {
    showSelects();
    $("body").removeChild($(id));
    $("body").removeChild($("glassLayer"));
}

//Create tooltip: set class "tooltip" to element und fill the "title" attribute to use the tooltip
window.addEvent('domready', function() {
    var Tips1 = new Tips($$('.tooltip'));

    $$('.tip').each(function(item, index){
        item.getParent().addClass('indexed');
    });
});

//other methods
function hideSelects() {
    var elements = document.getElementsByTagName("select");
    var length = elements.length;
    for (var i = 0; i < length; i++) {
        elements[i].style.display = "none";
    }
}

function showSelects() {
    var elements = document.getElementsByTagName("select");
    var length = elements.length;
    for (var i = 0; i < length; i++) {
        elements[i].style.display = "block";
    }
}

window.addEvent('domready', function() {

    var togglers = $$(".toggable");
    var toggler_contents = $$(".toggable_content");
    var elementCount = 0;

    togglers.each(function(toggler){

        toggler.addClass(elementCount);
    toggler.style.cursor = "pointer";
        toggler_contents[elementCount].style.display = "none";

        toggler.addEvent('click', function(){

            toggleCount = null;
            clases = this.className;

            for(i = 0; i < clases.length; i++) {
        if (clases[i].toInt() != "NaN") {
                    toggleCount = clases[i];
                }
            }

            if (toggler_contents[toggleCount].style.display == "none") {
                toggler_contents[toggleCount].style.display = "block";
            } else {
                toggler_contents[toggleCount].style.display = "none";
            }

        });

        elementCount++;
    });

});