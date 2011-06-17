function CustomAssetPager(tableName, itemsPerPage , local, countedCommunityItems) {
    this.tableName = tableName;
    this.itemsPerPage = itemsPerPage;
    this.currentPage = 1;
    this.pages = 0;
    this.inited = false;
    this.position = "";
    this.upperPosition = "";
    
    this.local = local;
    this.countedCommunityItems = countedCommunityItems;
    this.selectAll = true;
    this.communityItemArray = new Array();
    
    // Total number of Items.
    this.total = 0;
    
    this.showRecords = function(from, to) {       
    	
    	if(this.selectAll==true){
	        var rows = document.getElementById(this.tableName).rows;
	        // i starts from 1 to skip table header row
	        for (var i = 1; i < rows.length; i++) {      	
	            if (i < from || i > to)  
	                rows[i].style.display = 'none';
	            else
	                rows[i].style.display = '';
	        }
    	}
    	else{
    		var rows = document.getElementById(this.tableName).rows;
    		for(var i = 1; i < this.communityItemArray.length; i++){
    			if (i < from || i > to)  
    				rows[this.communityItemArray[i]].style.display = 'none';
    			else{
    				rows[this.communityItemArray[i]].style.display = '';
    			}
    		}
    	}
    }
    
    this.showPage = function(pageNumber) {
    	if (! this.inited) {
    		alert("not inited");
    		return;
    	}    
    	
        var oldPageAnchor = document.getElementById('pg'+this.currentPage);
        if(oldPageAnchor != null){
        	oldPageAnchor.className = 'pg-normal';
        }
        
        this.currentPage = pageNumber;
        var newPageAnchor = document.getElementById('pg'+this.currentPage);
        if(newPageAnchor != null){
        	newPageAnchor.className = 'pg-selected';
        }

        var from = (pageNumber - 1) * this.itemsPerPage + 1; 
        
        // Without these counterproductive multiplications the Result could be a string concatenation!
        var to = (from*1) + ((this.itemsPerPage *1) - 1) *1; 
        
        if(to>this.total){
        	to = this.total;
        }
        
        this.showActualPos();
        this.showRecords(from, to);
        this.showPageNav('customAssetPager', this.position, this.upperPosition);
    }   
    
    this.changeSize = function(){
    	this.itemsPerPage = document.getElementById('setPagingSizeForCustomAsset').options[document.getElementById('setPagingSizeForCustomAsset').selectedIndex].value;
        this.currentPage = 1;
        this.pages = 0;
        this.inited = false;
    	this.init();
    	this.showPageNav('customAssetPager', this.position, this.upperPosition);
    	this.showPage(1);
    }
    
    this.changeCommunity = function(){
    	var test = document.getElementById('setCommunityForCustomAsset').options[document.getElementById('setCommunityForCustomAsset').selectedIndex].value;    
    	if(test == "all"){
    		this.selectAll = true;
    	}
    	else{
    		this.selectAll = false;
    		
            var rows = document.getElementById(this.tableName).rows;
            var counter = 1;
            
            // i starts from 1 to skip table header row
            for (var i = 1; i < rows.length; i++) {   
            	
            	// Make ALL Items invisible!
            	rows[i].style.display = 'none';
            	
            	if(rows[i].id=="isFromCommunity"){
            		rows[i].style.display = '';
            		this.communityItemArray[counter] = i;
            		counter = counter + 1;
            	}
            }
    		
    	}    		
    	this.changeSize();
    }
    
    this.prev = function() {
        if (this.currentPage > 1)
            this.showPage(this.currentPage - 1);
    }
    
    this.next = function() {
        if (this.currentPage < this.pages) {
            this.showPage(this.currentPage + 1);
        }
    }                        
    
    this.init = function() {
    	if(this.selectAll==true){
	        var rows = document.getElementById(this.tableName).rows;
	        // Count all records and substract the Header.
	        var records = (rows.length - 1); 
	        this.pages = Math.ceil(records / this.itemsPerPage);
	        this.inited = true;
	        
	        this.total = records;
    	}
    	else{
    		this.pages = Math.ceil((this.communityItemArray.length - 1) / this.itemsPerPage);
    		this.inited = true;
    		
    		this.total = this.communityItemArray.length - 1;
    		
    		// If there are NO Items, then this will catch a Negativ result.
    		if(this.total < 0){
    			this.total = 0;
    		}
    	}
    }
    
    this.showActualPos = function() {
    	
    	var currentElementHtml = '';
    	var currentPageMult = this.currentPage*this.itemsPerPage;
    	
    	if(this.currentPage==1){
    		if(this.total > 0){
    			currentElementHtml = currentElementHtml + '1';
    		}
    		else{
    			currentElementHtml = currentElementHtml + '0';
    		}

    	}
    	else{
    		currentElementHtml = currentElementHtml + (currentPageMult - this.itemsPerPage + 1);
    	}
    	
    	if(this.currentPage == 1){
        	if(this.total >= this.itemsPerPage){
        		currentElementHtml = currentElementHtml + ' - ' + this.itemsPerPage;
        	}
        	else{
        		currentElementHtml = currentElementHtml + ' - ' + this.total;
        	}
    	}
    	else{
        	if(this.total - currentPageMult >= 0){
        		currentElementHtml = currentElementHtml + ' - ' + currentPageMult;
        	}
        	else{
        		currentElementHtml = currentElementHtml + ' - ' + this.total;
        	}
    	}
    	
    	var change = document.getElementById('customAssetActualPosition');
    	var temp = change.innerHTML;
    	
		temp = temp.replace(/[0-9][0-9]* - [0-9][0-9]*/,currentElementHtml);
		temp = temp.replace(/\d*\./,this.total+"."); 
    	
    	change.innerHTML = temp;
    }

    this.showPageNav = function(pagerName, positionId, upperPostionId) {
    	if (! this.inited) {
    		alert("not inited");
    		return;
    	}
    	var element = document.getElementById(positionId);
    	var upperElement =  document.getElementById(upperPostionId);
    	
    	this.position = positionId;
    	this.upperPosition = upperPostionId;
    	
    	// Choose the local Language.
    	var prev = "Prev";
    	var next = "Next";
    	if(this.local.toLowerCase().indexOf("de")!=-1){
    		prev = "Zurück";
    		next = "Weiter";
    	}
    	
    	var pagerHtml = '<span onclick="' + pagerName + '.prev();" class="pg-normal"> &#171 ' + prev + ' </span> | ';
    	// Deactivate the Link, if the first page is shown.
    	if(this.currentPage != 1){
    		pagerHtml = '<a onclick="' + pagerName + '.prev();" class="pg-normal" href="#"> &#171 ' + prev + ' </a> | ';
    	}
    	
        for (var page = 1; page <= this.pages; page++) {
        	if (page == this.currentPage) {
        		pagerHtml += '<a id="pg' + page + '" class="pg-normal" href="#" onclick="' + pagerName + '.showPage(' + page + ');">' + '<b>' + page + '</b>' + '</a> | ';
        	} else {
            	pagerHtml += '<a id="pg' + page + '" class="pg-normal" href="#" onclick="' + pagerName + '.showPage(' + page + ');">' + page + '</a> | ';
        	}
        }
        
        if(this.currentPage != this.pages){
        	pagerHtml += '<a onclick="'+pagerName+'.next();" class="pg-normal" href="#"> ' + next + ' &#187;</a>';  
        }
        else{
        	pagerHtml += '<span onclick="'+pagerName+'.next();" class="pg-normal"> ' + next + ' &#187;</span>';  
        }                
        
        element.innerHTML = pagerHtml;
        upperElement.innerHTML = pagerHtml;
    }
}

function Sorting(tableName) {
    this.tableName = tableName;
    this.header = new Array();
    this.tableContent = new Array();
    this.length = 0;
    this.sortetContent = new Array();
    
    
    this.init = function(){
    	this.getContentFromTable();
    }
    
    function mySort0(objectOne, objectTwo)
    {
    	// Sort by Title. 
    	
	    /* 
	   	 The Regex looks like :
	   	 >XXXX</a> 
	   	 where XXXX is everything even line breaks.
	   	*/
	   	var regex = ">(.|[\r\n|\n|\r])*?</a>";
	   	
	   	var objectOneTitle = objectOne[0].toLowerCase();
	   	// Check if it is a link to an Image.
	   	if(objectOneTitle.search(regex)!=-1){
	   		var from = (objectOneTitle.search(regex) + 1);
	   		var till = (objectOneTitle.indexOf('</a>', from) - from); 	
	   		objectOneTitle = objectOneTitle.substr(from,till);		
	   	}
	   	objectOneTitle = objectOneTitle.replace(/ä/g,"a");
	   	objectOneTitle = objectOneTitle.replace(/ö/g,"o");
	   	objectOneTitle = objectOneTitle.replace(/ü/g,"u");
	   	objectOneTitle = objectOneTitle.replace(/ß/g,"s");
	
	   	var objectTwoTitle = objectTwo[0].toLowerCase();
	   	// Check if it is a link to an Image.
	   	if(objectTwoTitle.search(regex)!=-1){
	   		var from = (objectTwoTitle.search(regex) + 1);
	   		var till = (objectTwoTitle.indexOf('</a>', from) - from); 	
	   		objectTwoTitle = objectTwoTitle.substr(from,till);	
	   	}
	   	objectTwoTitle = objectTwoTitle.replace(/ä/g,"a");
	   	objectTwoTitle = objectTwoTitle.replace(/ö/g,"o");
	   	objectTwoTitle = objectTwoTitle.replace(/ü/g,"u");
	   	objectTwoTitle = objectTwoTitle.replace(/ß/g,"s");
	   	
	   	return(objectOneTitle==objectTwoTitle) ? 0 : (objectOneTitle>objectTwoTitle) ? 1 : -1;
    }
    
    function mySort1(objectOne, objectTwo )
    {
    	// Sort by Description. 
    	
    	/* 
    	 The Regex looks like :
    	 alt="XXXX" 
    	 where XXXX is everything except a " (even line breaks).
    	*/
    	var regex = "alt=\"([\r\n|\n|\r]|.)+?\"";
    	
    	var objectOneDescription  = objectOne[1].toLowerCase();
    	// Check if it is a link to an Image.
    	if(objectOneDescription.search(regex)!=-1){
    		var from = (objectOneDescription.search(regex) + 5);
    		var till = (objectOneDescription.indexOf('"', from) - from); 	
    		objectOneDescription = objectOneDescription.substr(from,till);		
    	}
    	objectOneDescription = objectOneDescription.replace(/ä/g,"a");
    	objectOneDescription = objectOneDescription.replace(/ö/g,"o");
    	objectOneDescription = objectOneDescription.replace(/ü/g,"u");
    	objectOneDescription = objectOneDescription.replace(/ß/g,"s");

    	var objectTwoDescription = objectTwo[1].toLowerCase();
    	// Check if it is a link to an Image.
    	if(objectTwoDescription.search(regex)!=-1){
    		var from = (objectTwoDescription.search(regex) + 5);
    		var till = (objectTwoDescription.indexOf('"', from) - from); 	
    		objectTwoDescription = objectTwoDescription.substr(from,till);		
    	}
    	objectTwoDescription = objectTwoDescription.replace(/ä/g,"a");
    	objectTwoDescription = objectTwoDescription.replace(/ö/g,"o");
    	objectTwoDescription = objectTwoDescription.replace(/ü/g,"u");
    	objectTwoDescription = objectTwoDescription.replace(/ß/g,"s");

    	return(objectOneDescription==objectTwoDescription) ? 0 : (objectOneDescription>objectTwoDescription) ? 1 : -1;
    }
    
    function mySort2(objectOne, objectTwo )
    {
    	// Sort by Typ. 
    	
    	/* 
    	 The Regex looks like :
    	 alt="XXXX" 
    	 where XXXX is everything except a " (WITHOUT line breaks).
    	*/
    	var regex = "alt=\".+?\"";
    	
    	var objectOneTyp = objectOne[2].toLowerCase();
    	// Check if it is a link to an Icon. If no Icon exist then it is the Filename.
    	if(objectOneTyp.search(regex)!=-1){
    		var from = (objectOneTyp.search(regex) + 5);
    		var till = (objectOneTyp.indexOf('"', from) - from); 	
    		objectOneTyp = objectOneTyp.substr(from,till);		
    	}  	

    	var objectTwoTyp = objectTwo[2].toLowerCase();
    	// Check if it is a link to an Icon. If no Icon exist then it is the Filename.
    	if(objectTwoTyp.search(regex)!=-1){
    		var from = (objectTwoTyp.search(regex) + 5);
    		var till = (objectTwoTyp.indexOf('"', from) - from); 	
    		objectTwoTyp = objectTwoTyp.substr(from,till);		
    	}

    	return(objectOneTyp==objectTwoTyp) ? 0 : (objectOneTyp>objectTwoTyp) ? 1 : -1;
    }
    
    function mySort3(objectOne, objectTwo )
    {
    	// Sort by Modified-Date.
    	
    	// Get the Date.
    	var tempObjectOne = objectOne[3].split("&nbsp;");
    	var tempObjectTwo = objectTwo[3].split("&nbsp;");

    	/*
    	 The Modified-Date has the following Form:
    	 DD-MM-YYYY
    	*/ 
		var dateA = tempObjectOne[0].split("-");
		var dateB = tempObjectTwo[0].split("-");
		
		if(dateA[2]-dateB[2]+dateA[1]-dateB[1]+dateA[0]-dateB[0]<0){
			return 1;
		}
		else{
			if(dateA[2]-dateB[2]+dateA[1]-dateB[1]+dateA[0]-dateB[0]==0){
				return 0;
			}
			else{
				return -1;
			}
		}
    }
    
    this.getContentFromTable = function(){
    	
    	var table = document.getElementById(tableName);
	    var rows = table.rows;
	    // The first row is the Header of the Table.
	    this.length = (rows.length - 1);    
	        
	    for (var i = 0; i < rows.length; i++) {
	    	// table header row
	    	if(i == 0){
	    		// Get all Header Informations.
	    		this.header = new Array();
	    		this.header[0]= rows[0].cells[0].innerHTML;
	    		this.header[1]= rows[0].cells[1].innerHTML;
	    		this.header[2]= rows[0].cells[2].innerHTML;
	    		this.header[3]= rows[0].cells[3].innerHTML;
	    	}
	    	else{
	    		// Get the Content of the Table and save it in an Array.
	    		this.tableContent[i] = new Array();
		    	this.tableContent[i][0] = rows[i].cells[0].innerHTML;
		    	this.tableContent[i][1] = rows[i].cells[1].innerHTML;
		    	this.tableContent[i][2] = rows[i].cells[2].innerHTML;
		    	this.tableContent[i][3] = rows[i].cells[3].innerHTML;
		    	this.tableContent[i][4] = rows[i].id;
	    	}
	    }
    }
    
    this.sortTable = function(sortBy){
    	
    	if(sortBy == 0){
    		// Sort by Title. 
    		this.sortetContent = this.tableContent.sort(mySort0);
    	}
    	else{
        	if(sortBy == 1){
        		// Sort by Description. 
        		this.sortetContent = this.tableContent.sort(mySort1);
        	}
        	else{
            	if(sortBy == 2){
            		// Sort by Typ. 
            		this.sortetContent = this.tableContent.sort(mySort2);
            	}
            	else{
                	if(sortBy == 3){
                		// Sort by Modified-Date.
                		this.sortetContent = this.tableContent.sort(mySort3);
                	}
            	}
        	}	
    	}
    	
    	// If the Table is not sorted by Title, then sort it secondary by Title.
    	if(sortBy != 0){
    		var comparator;
    		var notFirst = false;
    		var change = true;
    		var from = 0;
    		var subArray; 
    			
    		for (var i = 0; i < this.length; i++) {
    			
    			if(change){
    				comparator = this.sortetContent[i];
    				from = i;
    				change = false;
    			}
    			
    			if(notFirst){
    				// Sort by Description and secondary by Title.
        			if(sortBy == 1){
        				if(mySort1(comparator,this.sortetContent[i])==0){
        					change = false;
        				}
        				// Is the following Element different OR the last Element
        				if(mySort1(comparator,this.sortetContent[i])!=0 | (i+1) == this.sortetContent.length ){
        					change = true;
        					// clear the subArray
        					subArray = new Array();
        					// Get the new subArray
        					for (var j = from; j < i; j++) { 
        						subArray[j - from] = this.sortetContent[j];
        					}
        					// Sort the subArray by Title.
        					subArray = subArray.sort(mySort0);
        					// Change the sortetContent with the sorting of subArray.
        					for (var j = from; j < i; j++) {
        						this.sortetContent[j] = subArray[j - from];
        					}
        				}
        			}
        			else{
        				// Sort by Typ and secondary by Title.
            			if(sortBy == 2){
            				if(mySort2(comparator,this.sortetContent[i])==0){
            					change = false;
            				}
            				// Is the following Element different OR the last Element
            				if(mySort2(comparator,this.sortetContent[i])!=0 | (i+1) == this.sortetContent.length ){
            					change = true;
            					// clear the subArray
            					subArray = new Array();
            					// Get the new subArray
            					for (var j = from; j < i; j++) {
            						subArray[j - from] = this.sortetContent[j];
            					}
            					// Sort the subArray by Title.
            					subArray = subArray.sort(mySort0);
            					// Change the sortetContent with the sorting of subArray.
            					for (var j = from; j < i; j++) {
            						this.sortetContent[j] = subArray[j - from];
            					}
            				}
            			}
            			else{
            				// Sort by Modified-Date and secondary by Title.
                			if(sortBy == 3){
                				if(mySort3(comparator,this.sortetContent[i])==0){
                					change = false;
                				}
                				// Is the following Element different OR the last Element
                				if(mySort3(comparator,this.sortetContent[i])!=0 | (i+1) == this.sortetContent.length ){
                					change = true;
                					// clear the subArray
                					subArray = new Array();
                					// Get the new subArray
                					for (var j = from; j < i; j++) {
                						subArray[j - from] = this.sortetContent[j];
                					}
                					// Sort the subArray by Title.
                					subArray = subArray.sort(mySort0);
                					// Change the sortetContent with the sorting of subArray.
                					for (var j = from; j < i; j++) {
                						this.sortetContent[j] = subArray[j - from];
                					}
                				}
                			}
            			}
        			}
    			}
    			notFirst = true;
    		}
    	}
    	
    	var newHtml = "<colgroup><col width='30%'><col width='40%'><col width='10%'><col width='20%'></colgroup>";
    	newHtml = newHtml +	"<tr><th onclick='sort.sortTable(0)'>" + this.header[0] + "</th><th onclick='sort.sortTable(1)'>" + this.header[1] + "</th><th onclick='sort.sortTable(2)'>" + this.header[2] + "</th><th onclick='sort.sortTable(3)'>" + this.header[3] + "</th></tr>";
    		
    	for (var i = 0; i < this.length; i++) {
    		if(this.sortetContent[i][4].toString().length > 0){
        		newHtml = newHtml + "<tr id='" + this.sortetContent[i][4].toString() + "'>";
    		}
    		else{
        		newHtml = newHtml + "<tr>";
    		}

    		newHtml = newHtml + "<td>" + this.sortetContent[i][0] + "</td>";
    		newHtml = newHtml + "<td>" + this.sortetContent[i][1] + "</td>";
    		newHtml = newHtml + "<td>" + this.sortetContent[i][2] + "</td>";
    		newHtml = newHtml + "<td>" + this.sortetContent[i][3] + "</td>";
    		newHtml = newHtml + "</tr>";
    	}
    	  	
    	newHtml = newHtml + "</table>";
    	document.getElementById(tableName).innerHTML = newHtml;
    	
    	customAssetPager.changeCommunity();
    }
}