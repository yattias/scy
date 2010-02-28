<!--

function logout()
{
    $.ajax({
        type: "GET",
        url: BASE_URL + "/user/logout/site/1",
        success: function(msg){
            window.location = BASE_URL;
        }
    });    
}

// utilities

function getStyle(el,styleProp)
{
    var x = document.getElementById(el);
    if (x.currentStyle) {
        var y = x.currentStyle[styleProp];      
    }
    else if (window.getComputedStyle) 
      var y = document.defaultView.getComputedStyle(x,null).getPropertyValue(styleProp);    
    return y;
}

function getStyleInt(x, styleProp)
{
    if (document.getElementById(x)) {
        var style = getStyle(x, styleProp);
        style = parseInt(style.substring(0, style.length-2));   

        return style;
    }
    else {
//        window.alert(x);
        return 0;
    }

}

function findPos(obj)
{
    var curleft = curtop = 0;
    if (obj.offsetParent) {
        do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
        } while (obj = obj.offsetParent);
    }
    return [curleft,curtop];   
}

function getElementsByClassName(oElm, strTagName, strClassName) {
        var arrElements = (strTagName == "*" && document.all)
                ? document.all : oElm.getElementsByTagName(strTagName);
        var arrReturnElements = new Array();
        strClassName = strClassName.replace(/\-/g, "\\-");
        var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
        var oElement;
        for (var i = 0; i < arrElements.length; i++) {
          oElement = arrElements[i];
          if (oRegExp.test(oElement.className)) {
              arrReturnElements.push(oElement);
          }
        }

        return arrReturnElements;
}

function showDiv(div_name)
{
  document.getElementById(div_name).style.visibility = 'visible';
  document.getElementById(div_name).style.display = 'block';  
}

function hideDiv(div_name)
{
  document.getElementById(div_name).style.visibility = 'hidden';
  document.getElementById(div_name).style.display = 'none';  
}

// highlighting edit mode functions




function loadDomainHighlights(domain, output_div, link_id)
{
//               document.getElementById(output_div).style.position = 'relative';

  var url = BASE_URL + 'highlights/view/?cz=1&domain=' + domain + '&opt=sj';
  $.ajax({
    url: url,        
    cache: true,
    dataType: "text",
    success: function (data, status) { 
//               var pos = findPos(document.getElementById(link_id));
               h_size = 140;
               var doc_top = docTop();
               y = doc_top + 20;

               if (doc_top >= h_size) {
                 y = y - h_size;
                 y += 40;
               }
               else {
                 if (doc_top != 0) {
                 y = h_size - (doc_top + y);
                 }
               }


//               x = doc_top - h_size;
               if (y <= 0) {
                 if (doc_top <= h_size) {
//                   y = h_size - doc_top;
                     y = 0;
                 }
                 else {
                   y = 0;
                 }

               }

//               alert(y + ' ' + doc_top);               
//              alert(y);
               document.getElementById(output_div).innerHTML = data; 
               document.getElementById(output_div).style.paddingTop = y + 'px';

               if (y > 120) {
//                 document.getElementById('domain_pane_message').style.visibility = 'visible';
//                 document.getElementById('domain_pane_message').style.display = 'block';
               }
               else {
                 document.getElementById('domain_pane_message').style.visibility = 'hidden';
                 document.getElementById('domain_pane_message').style.display = 'none';
               }
             }
  });  

}

function loadTagHighlights(tag, output_div, link_id)
{
  var url = BASE_URL + 'highlights/view/?cz=1&tag=' + tag + '&opt=sj';
  $.ajax({
    url: url,        
    cache: true,
    dataType: "text",
    success: function (data, status) { 
//               var pos = findPos(document.getElementById(link_id));
               h_size = 140;
               var doc_top = docTop();
               y = doc_top + 20;

               if (doc_top >= h_size) {
                 y = y - h_size;
                 y += 40;
               }
               else {
                 if (doc_top != 0) {
                 y = h_size - (doc_top + y);
                 }
               }


//               x = doc_top - h_size;
               if (y <= 0) {
                 if (doc_top <= h_size) {
//                   y = h_size - doc_top;
                     y = 0;
                 }
                 else {
                   y = 0;
                 }

               }

//               alert(y + ' ' + doc_top);               
//              alert(y);
               document.getElementById(output_div).innerHTML = data; 
               document.getElementById(output_div).style.paddingTop = y + 'px';

               if (y > 120) {
//                 document.getElementById('domain_pane_message').style.visibility = 'visible';
//                 document.getElementById('domain_pane_message').style.display = 'block';
               }
               else {
                 document.getElementById('domain_pane_message').style.visibility = 'hidden';
                 document.getElementById('domain_pane_message').style.display = 'none';
               }
             }
  });  

}


function docTop() { 
  if (document.documentElement && !document.documentElement.scrollTop) {
    return 0;
  }
  else if (document.documentElement && document.documentElement.scrollTop)  {
   return document.documentElement.scrollTop;
  }
  else if (document.body && document.body.scrollTop) {
     return document.body.scrollTop;// IE5 or DTD 3.2 
  }
}

function docBottom() { 
  if (document.body && !document.body.scrollBottom) {
    return -5;
  }
  else if (document.documentElement && document.documentElement.scrollBottom)  {
   return document.documentElement.scrollBottom;
  }
  else if (document.body && document.body.scrollBottom) {
     return document.body.scrollBottom;// IE5 or DTD 3.2 
  }
}

function showEditMode(id)
{
  var old_id = id;
  if (id.length > 0) {
    showAll(id);    
    id = "_" + id;
  }

  document.getElementById('edit_link' + id).innerHTML = 'done editing';
  document.getElementById('edit_link' + id).href = 'javascript:hideEditMode(\'' + old_id + '\')';

  var edits = getElementsByClassName(document, '*', 'edit_mode' + id);
//  alert('edit_mode' + id);
  for (var i = 0; i <= edits.length-1; i++) {
    edits[i].style.visibility = 'visible';
    edits[i].style.display = 'inline';
  }
}


function hideEditMode(id)
{
  var old_id = id;
  var link_label = 'edit your highlights';
  if (id.length > 0) {
    link_label = 'edit';
    id = '_' + id;
  }

  document.getElementById('edit_link' + id).innerHTML = link_label;
  document.getElementById('edit_link' + id).href = 'javascript:showEditMode(\'' + old_id + '\')';

  var edits = getElementsByClassName(document, '*', 'edit_mode' + id);
  for (var i = 0; i <= edits.length-1; i++) {
    edits[i].style.visibility = 'hidden';
    edits[i].style.display = 'none';
  }
}

function deleteHighlight(id)
{
  var highlight = document.getElementById('highlight_' + id);
  highlight.style.visibility = 'hidden';
  highlight.style.display = 'none';

  // send ajax request

  var url = BASE_URL + 'highlight/remove/' + id;
  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });  
}

function checkPageRemove(id, hashkey)
{
  if (confirm('Are you sure you want to remove this page?')) {
    removeURL(id, hashkey);
  }
}

function removeURL(id, hashkey)
{

  var bookmark_row = document.getElementById('bookmark_' + id);
  bookmark_row.style.visibility = 'hidden';
  bookmark_row.style.display = 'none';

  var url = BASE_URL + 'page/remove/' + hashkey;
  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });    
}

function setPrivate(url_id, hashkey)
{
  
}

function showTitleEdit(url_id)
{
  if (document.getElementById('title_edit_' + url_id)) {
    document.getElementById('title_edit_' + url_id).style.visibility = 'visible';
    document.getElementById('title_edit_' + url_id).style.display = 'block';
  }
}

function closeTitleEdit(url_id)
{
  if (document.getElementById('title_edit_' + url_id)) {
    document.getElementById('title_edit_' + url_id).style.visibility = 'hidden';
    document.getElementById('title_edit_' + url_id).style.display = 'none';
  }
}

function updateTitle(url_id, hashkey)
{
  if (document.getElementById('title_edit_input_' + url_id)) {
    var title = document.getElementById('title_edit_input_' + url_id).value;

    var url = BASE_URL + 'page/set_title/' + hashkey + '/?cig=1&title=' + encodeURIComponent(title);

    $.ajax({
      url: url,        
      cache: true,
      dataType: "text"
    });     
    }

  // notify the user

  document.getElementById('bookmark_title_link_' + url_id).innerHTML = title;
  hideDiv('title_edit_' + url_id);   

/*
//  document.getElementById('title_edit_' + url_id).innerHTML = 'Title updated!';
  showDiv('title_updated_' + url_id);
  setTimeout("hideDiv('title_updated_" + url_id + "')", 2500);
*/
}


// ordering highlights

function reorder_highlight_up(url_id, index)
{
  var id = highlights_key_pos[url_id][index];
  var second_id = highlights_key_pos[url_id][highlights_key_id[url_id][id]-1];
  swap(url_id, id, second_id);

  // ajax update order
  updateHighlightOrder(url_id);
}

function reorder_highlight_down(url_id, index)
{
  var id = highlights_key_pos[url_id][index];
  var second_id = highlights_key_pos[url_id][highlights_key_id[url_id][id]+1];
  swap(url_id, id, second_id);

  // ajax update order
  updateHighlightOrder(url_id);
}

function swap(url_id, id1, id2)
{
  var h1 = 'highlight_' + id1;
  var h2 = 'highlight_' + id2;

  document.getElementById(h1).id = 'move_temp1';
  document.getElementById(h2).id = 'move_temp2';

  var html1 = document.getElementById('move_temp1').innerHTML + '';
  var html2 = document.getElementById('move_temp2').innerHTML + '';

  document.getElementById('move_temp1').id = h2;
  document.getElementById('move_temp2').id = h1;

  document.getElementById(h1).innerHTML = html1;
  document.getElementById(h2).innerHTML = html2;

  var new_pos_1 = highlights_key_id[url_id][id2];
  var new_pos_2 = highlights_key_id[url_id][id1];
  
  highlights_key_id[url_id][id1] = new_pos_1;
  highlights_key_id[url_id][id2] = new_pos_2;
 
  highlights_key_pos[url_id][new_pos_1] = id1;
  highlights_key_pos[url_id][new_pos_2] = id2;

}

function updateHighlightOrder(url_id)
{
  var input_str = '';
  for (var i = 0; i <= highlights_key_pos[url_id].length-1; i++) {
    input_str += highlights_key_pos[url_id][i] + '_' + i + '_';
  }
  input_str = input_str.substring(0, input_str.length-1);

  var url = BASE_URL + 'highlight/setorder/' + escape(input_str);

  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });    
}

function __pos_debug()
{
  var out = '';
  for (var i = 0; i <= highlights_key_pos.length-1; i++) {
    out += "highlights_key_pos[" + i + "] = " + highlights_key_pos[i] + "\n";
  }

  out += "\n";
  for (i in highlights_key_id) {
    out += "highlights_key_id[" + i + "] = " + highlights_key_id[i] + "\n";
  }

  alert(out);
}

function apiTrack(type, hashkey)
{
  var url = BASE_URL + 'page/track/' + type + '/' + hashkey;

  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });    
}

function trackRecentView()
{
  var url = BASE_URL + 'user/recentview';

  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });    
}

function toggleDisplayShareMenu()
{
  if (document.getElementById('share_options').style.display == 'none') {
    showDiv('share_options');
    hideDiv('blog_options_link');
  }
  else {
    hideDiv('share_options');
    showDiv('blog_options_link');
  }
}

function toggle(element)
{
  var e_style = document.getElementById(element).style;

  if (e_style.visibility == 'hidden') {
    e_style.display = 'block';
    e_style.visibility = 'visible';    
  }
  else {
    e_style.display = 'none';
    e_style.visibility = 'hidden';
  }
}

// tag stuff

function showTagInput(url_id)
{
  // get position of the tag input link
 var pos = findPos(document.getElementById('tag_link_' + url_id));
 var x = pos[0] - 300;
 var y = pos[1] + 20;
  
  var tag_input_style =  document.getElementById('tag_box_'+ url_id).style;
  tag_input_style.left = x + 'px';
  tag_input_style.top = y + 'px';
  if (tag_input_style.visibility == 'visible') {
      tag_input_style.visibility = 'hidden';
      tag_input_style.display = 'none';
  }
  else {
    tag_input_style.visibility = 'visible';
    tag_input_style.display = 'block';
  }

    // set tag font style to normal

    document.getElementById('tags_' + url_id).style.fontWeight = 'normal';
    document.getElementById('tags_' + url_id).focus();
}

function saveTags(url_id, hashkey)
{  
  document.getElementById('tags_' + url_id).style.fontWeight = 'bold';

  var tags = document.getElementById('tags_' + url_id).value;   
  var url = BASE_URL + 'page/tag/' + hashkey + '/?cz=1&tags=' + tags;

    $.ajax({
        type: "GET",
        url: url       
    }); 

  setTimeout('showTagInput(' + url_id + ');', 1500);
}

function tagInputKeyPress(e,url_id,hashkey)
{
  var keynum = 0;
  if(window.event) // IE
  {
    keynum = e.keyCode
  }
  else if(e.which) // Netscape/Firefox/Opera
  {
    keynum = e.which
  } 

  if (keynum == 13) {
   saveTags(url_id,hashkey); 
  } 

  // need to clearTagBold for certain key presses
}

function clearTagBold(url_id)
{

  var input_text_style = document.getElementById('tags_' + url_id).style.fontWeight;
  if (input_text_style == 'bold') {
    document.getElementById('tags_' + url_id).style.fontWeight = 'normal';
  }
}

function setTags(url_id, tags)
{
  if (document.getElementById('tags_' + url_id)) {
    document.getElementById('tag_' + url_id).value = tags;
  }
}

function showDomain(domain)
{
  closeOpenDomains();

  document.getElementById('domain_' + domain).style.visibility = 'visible';
  document.getElementById('domain_' + domain).style.display = 'block';
}

function closeOpenDomains()
{
  var domains = getElementsByClassName(document, '*', 'domain_highlights');
  for (var i = 0; i <= domains.length-1; i++) {
    domains[i].style.visibility = 'hidden';
    domains[i].style.display = 'none';
  }
}

function updatePhoto(hashkey, tags, title)
{
  var url = BASE_URL + 'page/set_title/' + hashkey + '/?cig=1&title=' + encodeURIComponent(title);

  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });     

/*
  var url = BASE_URL + 'updateimage/' + hashkey + '/?cz=1';
  url += '&tags=' + encodeURIComponent(tags);
  url += '&title=' + encodeURIComponent(title);
*/

  var url = BASE_URL + 'page/tag/' + hashkey + '/?cz=1&tags=' + tags;

    $.ajax({
        type: "GET",
        url: url       
    }); 

  $.ajax({
    url: url,        
    cache: true,
    dataType: "text"
  });    


}

// -->
