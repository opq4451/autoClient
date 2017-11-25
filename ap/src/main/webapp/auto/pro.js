const rangeBigExist = { '01' : 's' , '02' : 's' , '03' : 's' ,'04' : 's' ,'05' : 's' ,  
						'06' : 'b' , '07' : 'b' , '08' : 'b' ,'09' : 'b' ,'10' : 'b' ,

};

const bsCode = {
		'b' : ['06','07','08','09','10'] , 
		's' : ['01','02','03','04','05'] ,
		
}

const rangeSingExist = {
		'01' : 'sin' , '03' : 'sin' , '05' : 'sin' ,'07' : 'sin' ,'09' : 'sin' ,  
		'02' : 'dou' , '04' : 'dou' , '06' : 'dou' ,'08' : 'dou' ,'10' : 'dou' ,
			 

};

const sdCode = {
		'sin' : ['01','03','05','07','09'] , 
		'dou' : ['02','04','06','08','10'] ,
		
}



function reallyOtherBet(sn,amount,betphase,c,codeList,type){
	$.ajax({ url:  u + "/betBS?user="+document.getElementById("user").value +
			"&sn="+sn+"&amount="+amount+"&betphase="+betphase+"&c="+c+"&codeList="+codeList+"&type="+type,  
     async: false,
     success: function(data) {
    	 
     	 }
    });
	
}

function getBetMin(){
	let min = 0;
	$.ajax({ url:  u + "/getMin",  
	 async: false,
	 success: function(data) {
		 min=data;
 	}
	
	});
	return min;
}


function connectURL(){
	var frameUrl = "http://203.160.143.110/www_new/index_new.php?username="+document.getElementById("user").value+"&usertype=a&langx=zh-cn&mid="+
	document.getElementById("mid").value+"&uid="+document.getElementById("uid").value;
	 
	window.open(
			frameUrl,
			  '_blank' // <- This is what makes it open in a new window.
			);
}