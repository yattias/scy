function toggleContent(nodeId, shortText, longText) {
	var node = document.getElementById(nodeId).firstChild;
  var currText = node.nodeValue;
  if (currText.length == shortText.length) {
	  node.nodeValue = longText;
  } else {
	  node.nodeValue = shortText;
  }
}
