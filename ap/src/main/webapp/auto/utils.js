function getUrl(){
	
	return "http://localhost:9999";
}


function logout(){
 
	 window.location.href="/index.html";
}

function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}

function parse_query_string(query) {
	  var vars = query.split("&");
	  var query_string = {};
	  for (var i = 0; i < vars.length; i++) {
	    var pair = vars[i].split("=");
	    // If first entry with this name
	    if (typeof query_string[pair[0]] === "undefined") {
	      query_string[pair[0]] = decodeURIComponent(pair[1]);
	      // If second entry with this name
	    } else if (typeof query_string[pair[0]] === "string") {
	      var arr = [query_string[pair[0]], decodeURIComponent(pair[1])];
	      query_string[pair[0]] = arr;
	      // If third or later entry with this name
	    } else {
	      query_string[pair[0]].push(decodeURIComponent(pair[1]));
	    }
	  }
	  return query_string;
}


function getRate(latter){
	if(latter == "A")
		return "9.918";
	else if(latter == "B")
		return "9.818";
	else if(latter == "C")
		return "9.718";
	else 
		return "9.618";
}

//pk10
const betMinute = [4,5,6,9,10,11,14,15,16,19,20,21,24,25,26,29,30,31,34,35,36,39,40,41,44,45,46,49,50,51,54,55,56,59,0,1];
const falseMinute = [2,7,12,17,22,27,32,37,42,47,52,57];

const clearMinute = [6,11,16,21,26,31,36,41,46,51,56,1];

//boat
const betMinute_boat = [6,7,8,11,12,13,16,17,18,21,22,23,26,27,28,31,32,33,36,37,38,41,42,43,46,47,48,51,52,53,56,57,58,1,2,3];
const falseMinute_boat = [4,9,14,19,24,29,34,39,44,49,54,59];

const clearMinute_boat = [8,13,18,23,28,33,38,43,48,53,58,3];


function checkFalseMinute(m){ 
	for(var i in falseMinute){
		if(falseMinute[i] == m)
			return true;
	}
	return false
}

function checkBetMinute(m){ 
	for(var i in betMinute){
		if(betMinute[i] == m)
			return true;
	}
	return false
}

function checkClearMinute(m){ 
	for(var i in clearMinute){
		if(clearMinute[i] == m)
			return true;
	}
	return false
}




function checkFalseMinute_boat(m){ 
	for(var i in falseMinute_boat){
		if(falseMinute_boat[i] == m)
			return true;
	}
	return false
}

function checkBetMinute_boat(m){ 
	for(var i in betMinute_boat){
		if(betMinute_boat[i] == m)
			return true;
	}
	return false
}

function checkClearMinute_boat(m){ 
	for(var i in clearMinute_boat){
		if(clearMinute_boat[i] == m)
			return true;
	}
	return false
}