/*
 * 读者操作的一些方法 ：恢复 验证 挂失 补办 ...等
 * 
 */
	function getRdid(){
		var val=$('input:radio[name="id_radio"]:checked').val();
		if(val==null){
			return "";
		}
		else{
			return val;
		}
	}
	//1.恢复读者证
	var payed = 0.0;
	function renew() {//var rdId = getRdid();
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		$.ajax({
			type : "POST",
			url : "renew",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var deposity = jsonData.deposity;
					var rdDeposity = jsonData.rdDeposity;
					var totalfee = jsonData.totalfee;
					var tip = "";
					tip += " 读者当前押金：" + rdDeposity + "元";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(deposity != 0) {
						tip += " 押金 ：" + deposity + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							renew();
							payed = 0.0;
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					return;
				} else if(success == 2) {
					showResult("该证不需要恢复!");
					payed = 0.0;
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else {
					alert("证操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				}
				
			},
			cache: false
		});
	}
	
	//读者证注销操作
	function rdLogout() {
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var rdCFState = $("#rdstate").text();
		if(rdCFState=="注销"){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经被注销, 是否继续注销？"))return;
				isContinue = true;
			}
		}
		$.ajax({
			type : "POST",
			url : "logout",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							rdLogout();
							payed = 0.0;
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					return;
				} else if(success == 1) {
					alert("证注销操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -2) {
					alert("证注销操作成功!同步失败！");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -3) {
					var message = jsonData.message;
					alert(message);
					return;
				}
				
			},
			cache: false
		});
	}
	//读者证信用有效操作
	function credit() {
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
//		var rdCFState = $("#rdstate").text();
//		if(rdCFState=="信用有效"){
//			if(!isContinue) {
//				if(!confirm("该证已是信用有效状态，请检查是否已经, 是否继续注销？"))return;
//				isContinue = true;
//			}
//		}
		$.ajax({
			type : "POST",
			url : "credit",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							rdLogout();
							payed = 0.0;
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					return;
				} else if(success == 1) {
					alert("证信用有效状态操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -2) {
					alert("证信用有效状态操作成功!同步失败！");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -3) {
					var message = jsonData.message;
					alert(message);
					return;
				}
				
			},
			cache: false
		});
	}
	//验证读者证
	function check() {//#cardOp cardDrop var rdId = $("#rdId").attr("value");
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		$.ajax({
			type : "POST",
			url : "check",
			data : {rdId: rdId, payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							check();
							payed = 0.0;
						}
					}
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					payed = 0.0;
					return;
				} else if(success == 1) {
					alert("验证操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -2) {
					alert("验证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				}
				
			},
			cache: false
		});
	}
	
	function cardOperation(option) {//#cardOp cardDrop var rdId = $("#rdId").attr("value");
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "cardOperation",
			data : {rdId: rdId, option : option},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					alert("证操作成功!");
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == 2) {
					showResult("该证无须进行本操作!");
					return;
				} else if(success == -2 ) {
					var message = jsonData.message;
					alert(message);
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
				}
				
				
			},
			cache: false
		});
	}
	
	//退证操作
	function quit() {// var rdId = $("#rdId").attr("value");
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var rdCFState = $("#rdstate").text();
		if(rdCFState=="注销"){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经被退证, 是否继续退证？"))return;
				isContinue = true;
			}
		}
		$.ajax({
			type : "POST",
			url : "quit",
			data : {rdId: rdId,payed : payed},
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
				//	message.append("\"prepay\":" + prepay + ", "); //应退预付款
				//	message.append("\"deposity\":" + rdDeposity ); //押金
					var prepay = jsonData.prepay;
					var deposity = jsonData.deposity;
					var tip = "应交 ";
					if(checkfee > 0) {
						tip += " 验证费【" + checkfee + "】元";
					}
					if(servicefee > 0) {
						tip += " 服务费 【" + servicefee + "】元";
					}
					if(idfee > 0) {
						tip += " 工本费 【" + idfee + "】元";
					}
					if(prepay > 0) {
						tip += " 应退还";
						tip += " 一卡通余额【" + prepay + "】元";
					}
					if(deposity > 0) {
						tip += " 应退还";
						tip += " 押金【" + deposity + "】元";
					}
					if(totalfee > 0) {
						tip += " 计算后共应交" + totalfee + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							quit();
							payed = 0.0;
						}
					} else {
						tip += " 计算后共应退还" + (0 - totalfee) + "元。";
						if(confirm(tip)) {
							payed = totalfee;
							quit();
							payed = 0.0;
						}
					}
					
				} else if(success == -1) {
					var totalfee = jsonData.totalfee;
					var prepay = jsonData.prepay;
					alert("读者账户余额("+ prepay +"元), 不足以完成此次("+ totalfee +"元)扣费!");
					return;
				} else if(success == 1) {
					alert("退证操作成功!");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -2) {
					alert("退证操作成功!同步失败");
					payed = 0.0;
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				} else if(success == -3) {
					var message = jsonData.message;
					alert(message);
					return;
				} else if(success == 2) {
					
				}
				
			},
			cache: false
		});
	}
	
	//同步读者操作
	function syncReader() {
		//var rdId = $("#rdId").attr("value");
		var rdId = getRdid();
		$("#syncButton").attr("disabled", "true");
		$("#syncButton").html("正在同步...");
		
		if(rdId == "") {
			showResult("请先选择读者!");
			$("#syncButton").removeAttr("disabled");
			$("#syncButton").html("手动同步");
			return;
		}
		$.ajax({
			type : "POST",
			url : "syncReader/" + rdId,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 1) {
					showResult("同步成功!");
					$("#syncButton").removeAttr("disabled");
					$("#syncButton").html("手动同步");
					return;
				} else if(success == 0) {
					var message = jsonData.message;
					showResult(message);
					$("#syncButton").removeAttr("disabled");
					$("#syncButton").html("手动同步");
					return;
				} else {
					var message = jsonData.message;
					var exp = jsonData.exception;
					showResult(message + ", <br />异常：" + exp);
					$("#syncButton").removeAttr("disabled");
					$("#syncButton").html("手动同步");
				}
			},
			cache:false
		});
	}
	
	function searchReader() {
		var fieldValue = getRdid();
		var fieldName = "rdId";
		var obj = null;
		var params = {fieldName : fieldName,fieldValue : fieldValue};
		$.ajax({
			type : "POST",
			url : "searchReader",
			data : params,
			async: false, //设置同步 异步ajax 回调是拿不到该对象
			dataType : "json",
			success : function(backData){
				var result = backData.result;
				if(result && result=="none") {
					showResult("未查询到读者信息！");
				} else if(result && result=="more") {
					showResult("查询结果大于一条，请细化查询条件！");
				} else {
					obj = eval(backData);//eval(backData);
				}
			},
			error : function(){
				showResult("未查询到读者信息！");
			}
		});
		return obj;
	}
	
	var isContinue = false;
	//读者补办选择 是卡补办还是证号补办 2014-12-12
	function repairSelect(){
		if(confirm("是补办读者证号吗?")){
			repair();
			return;
		}
		if(confirm("是补办读者卡号吗?")){
			cardRepair();
			return;
		}
	}
	//补办 在来证的账号多加P
	var paySign = "0";
	var newRdid = "";
	function repair(){
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var reader = searchReader();
		if(reader==null){
			showResult("查找不到该读者证！");return;
		}
		var rdCFState=reader.rdCFState;
		//到后台数据库查询该读者信息
		if(!(rdCFState==3 || rdCFState==5)){
			showResult("只有挂失、注销状态的读者才能补办读者证！");return;
		}
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经补办读者证, 是否继续补办？"))return;
				isContinue = true;
			}
		}
		if(rdId.charAt(rdId.length-1)=="P"){
			if(!confirm("此读者证已经是补办证！是否继续？"))return;
		}
		//基本信息start
		var rdType = reader.rdType;
		var rdGlobal;
		var rdLibType;
		rdGlobal = reader.rdGlobal;
		rdLibType = reader.rdLibType;
		var libUser = reader.libUser;
		var rdStartDate = reader.rdStartDateStr;
		
		var rdEndDate = reader.rdEndDateStr;
		var rdInTime = reader.rdInTime;
		var rdLib = reader.rdLib;
		var rdSort1 = reader.rdSort1;
		var rdSort2 = reader.rdSort2;
		var rdSort3 = reader.rdSort3;
		var rdSort4 = reader.rdSort4;
		var rdSort5 = reader.rdSort5;
		
		var rdName = reader.rdName;
		var rdPasswd = reader.rdPasswd;

		var rdCertify = reader.rdCertify;

		var rdSex = reader.rdSex;
		var rdNation = reader.rdNation;
		var rdBornDate = reader.rdBornDateStr;
		var rdAge = reader.rdAge;
		var rdLoginId = reader.rdLoginId;
		var rdPhone = reader.rdPhone;
		var rdUnit = reader.rdUnit;
		var rdAddress = reader.rdAddress;
		var rdPostCode = reader.rdPostCode;
		var rdEmail = reader.rdEmail;
		var rdNative = reader.rdNative;
		var rdInterest = reader.rdInterest;
		var rdRemark = reader.rdRemark;
		var cardId = reader.cardId;
		//基本信息end
		if(newRdid == "") {
			newRdid = prompt("请输入新读者证号，系统默认如下：", rdId + "P");//更换新账号
			var inputRdid = newRdid;
			if (inputRdid == "") {
				showResult("新证号不能是空");
				return;
			} else if(inputRdid == null) {
				paySign = "0";
				return;
			}
		}
		
		var oldRdpasswd = reader.oldRdpasswd;

		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId : cardId,oldRdpasswd: oldRdpasswd};
		$.ajax({
			type : "POST",
			url : "repair",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							paySign = "1";
							
							repair();
							paySign = "0";
							newRdid == "";
							isContinue = false;
						}
					} else {
						paySign = "1";
						repair();
						paySign = "0";
						newRdid == "";
						isContinue = false;
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					isContinue = false;

					return;
				} else if(success == 2) {
					showResult("该证不需要补办!");
					isContinue = false;
					return;
				} else if(success == -2) {
					showResult("证操作成功!同步失败");
					isContinue = false;
					window.location.href = "index";//"/sso/admin/reader/add/"+newRdid;
					return;
				} else {
					showResult("证操作成功!");
					isContinue = false;
					window.location.href = "index";//"/sso/admin/reader/add/"+newRdid;
					return;
				}
				
			},
			cache: false
		});
	}
	
	//卡补办 cardRepair
	var newCardid = ""; //更换的新卡号，证号不变
	function cardRepair(){
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var reader = searchReader();
		if(reader==null){
			showResult("查找不到该读者证！");return;
		}
		var rdCFState=reader.rdCFState;
		//到后台数据库查询该读者信息
		if(!(rdCFState==3 || rdCFState==5)){
			showResult("只有挂失、注销状态的读者才能补办读者证！");return;
		}
		if(rdCFState==5){
			if(!isContinue) {
				if(!confirm("该证已注销，请检查是否已经补办读者证, 是否继续补办？"))return;
				isContinue = true;
			}
		}
		if(newCardid == "") {
			newCardid = prompt("请输入该读者新卡号：", ""); //更换的新卡号，证号不变
	        if(newCardid==""){
	        	showResult("新卡号不能是空");
	        	return;
	        } else if(newCardid == null) {
	        	paySign = "0";
	        	return;
	        }
		}
		
		
		//基本信息start
		var rdType = reader.rdType;
		var rdGlobal;
		var rdLibType;
		rdGlobal = reader.rdGlobal;
		rdLibType = reader.rdLibType;
		var libUser = reader.libUser;
		var rdStartDate = reader.rdStartDateStr;
		
		var rdEndDate = reader.rdEndDateStr;
		var rdInTime = reader.rdInTime;
		var rdLib = reader.rdLib;
		var rdSort1 = reader.rdSort1;
		var rdSort2 = reader.rdSort2;
		var rdSort3 = reader.rdSort3;
		var rdSort4 = reader.rdSort4;
		var rdSort5 = reader.rdSort5;
		
		var rdName = reader.rdName;
		var rdPasswd = reader.rdPasswd;

		var rdCertify = reader.rdCertify;

		var rdSex = reader.rdSex;
		var rdNation = reader.rdNation;
		var rdBornDate = reader.rdBornDateStr;
		var rdAge = reader.rdAge;
		var rdLoginId = reader.rdLoginId;
		var rdPhone = reader.rdPhone;
		var rdUnit = reader.rdUnit;
		var rdAddress = reader.rdAddress;
		var rdPostCode = reader.rdPostCode;
		var rdEmail = reader.rdEmail;
		var rdNative = reader.rdNative;
		var rdInterest = reader.rdInterest;
		var rdRemark = reader.rdRemark;
		var cardId = reader.cardId;
		//基本信息end
		
		var newRdid = reader.newRdid;
		//var paySign = "0";
		var oldRdpasswd = reader.oldRdpasswd;
		

		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId : cardId,oldRdpasswd: oldRdpasswd,newCardid:newCardid};
		$.ajax({
			type : "POST",
			url : "cardRepair",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							paySign = "1";
							cardRepair();
							paySign = "0";
							newCardid = "";
							isContinue = false;
						}
					} else {
						paySign = "1";
						
				        cardRepair();
				        paySign = "0";
				        newCardid = "";
						isContinue = false;
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					isContinue = false;

					return;
				} else if(success == 2) {
					showResult("该证不需要恢复!");
					isContinue = false;
					return;
				} else if(success == -2) {
					showResult("证操作成功!同步失败");
					isContinue = false;
					window.location.href = "index";
					return;
				} else {
					showResult("证操作成功!");
					isContinue = false;
					window.location.href = "index";
					return;
				}
				
			},
			cache: false
		});
	}
	
	//换证
	function change(){
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var reader = searchReader();
		if(reader==null){
			showResult("查找不到该读者证！");return;
		}
		var rdCFState=reader.rdCFState;
		
		//基本信息start
		var rdType = reader.rdType;
		var rdGlobal = reader.rdGlobal;
		var rdLibType = reader.rdLibType;
		var libUser = reader.libUser;
		var rdStartDate = reader.rdStartDateStr;
		
		var rdEndDate = reader.rdEndDateStr;
		var rdInTime = reader.rdInTime;
		var rdLib = reader.rdLib;
		var rdSort1 = reader.rdSort1;
		var rdSort2 = reader.rdSort2;
		var rdSort3 = reader.rdSort3;
		var rdSort4 = reader.rdSort4;
		var rdSort5 = reader.rdSort5;
		
		var rdName = reader.rdName;
		var rdPasswd = reader.rdPasswd;

		var rdCertify = reader.rdCertify;

		var rdSex = reader.rdSex;
		var rdNation = reader.rdNation;
		var rdBornDate = reader.rdBornDateStr;
		var rdAge = reader.rdAge;
		var rdLoginId = reader.rdLoginId;
		var rdPhone = reader.rdPhone;
		var rdUnit = reader.rdUnit;
		var rdAddress = reader.rdAddress;
		var rdPostCode = reader.rdPostCode;
		var rdEmail = reader.rdEmail;
		var rdNative = reader.rdNative;
		var rdInterest = reader.rdInterest;
		var rdRemark = reader.rdRemark;
		var cardId = reader.cardId;
		//基本信息end
		
		//var paySign = reader.paySign;//$("#paySign").val();
		if(newRdid == "") {
			newRdid = prompt("请输入新读者证号，系统默认如下：", rdId + "P"); //将输入的内容赋给变量新账号
	        if (newRdid == "") {
	        	showResult("新证号不能是空");
	        	return;
	        } else if(newRdid == null) {
	        	paySign = "0";
	        	return;
	        }
		}
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, newRdid: newRdid, cardId:cardId};
		$.ajax({
			type : "POST",
			url : "change",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							paySign = "1";
							
					        change();
					        paySign = "0";
					        newRdid = "";
						}
					} else {
						paySign = "1";
						
						change();
						paySign = "0";
						newRdid = "";
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					paySign = "0";
					newRdid = "";
					return;
				} else if(success == 2) {
					return;
				} else if(success == -2) {
					alert("证操作成功!同步失败");
					window.location.href = "index";//"/sso/admin/reader/add/"+newRdid;
					return;
				} else {
					alert("证操作成功!");
					window.location.href = "index";//"/sso/admin/reader/add/"+newRdid;
					return;
				}
				
			},
			cache: false
		});
	}
	
	//延期
	function defer() {
		var rdId = getRdid();
		if(rdId == "") {
			showResult("请先选择读者!");
			return;
		}
		var reader = searchReader();
		if(reader==null){
			showResult("查找不到该读者证！");return;
		}
		var rdCFState=reader.rdCFState;
		if(rdCFState==5){
			if(!isContinue) {
				showResult("注销的读者证不能延期！");return;
				//if(!confirm("该证已注销，请检查是否已经被注销, 是否继续注销？"))return;
				//isContinue = true;
			}
		}
		
		//基本信息start
		var rdType = reader.rdType;
		var rdGlobal = reader.rdGlobal;
		var rdLibType = reader.rdLibType;
		var libUser = reader.libUser;
		var rdStartDate = reader.rdStartDateStr;
		
		var rdEndDate = reader.rdEndDateStr;
		var rdInTime = reader.rdInTime;
		var rdLib = reader.rdLib;
		var rdSort1 = reader.rdSort1;
		var rdSort2 = reader.rdSort2;
		var rdSort3 = reader.rdSort3;
		var rdSort4 = reader.rdSort4;
		var rdSort5 = reader.rdSort5;
		
		var rdName = reader.rdName;
		var rdPasswd = reader.rdPasswd;

		var rdCertify = reader.rdCertify;

		var rdSex = reader.rdSex;
		var rdNation = reader.rdNation;
		var rdBornDate = reader.rdBornDateStr;
		var rdAge = reader.rdAge;
		var rdLoginId = reader.rdLoginId;
		var rdPhone = reader.rdPhone;
		var rdUnit = reader.rdUnit;
		var rdAddress = reader.rdAddress;
		var rdPostCode = reader.rdPostCode;
		var rdEmail = reader.rdEmail;
		var rdNative = reader.rdNative;
		var rdInterest = reader.rdInterest;
		var rdRemark = reader.rdRemark;
		//基本信息end
		var deferDate = reader.deferDate;//延期的默认时间
		deferDate = prompt("请输入延期终止日期，格式YYYY-MM-DD，系统默认为：当前日期+读者有效期：", rdEndDate);//更新延期默认时间
		var params = {rdId : rdId,rdCFState : rdCFState,rdType : rdType,rdGlobal : rdGlobal,rdLibType : rdLibType,
				rdStartDate : rdStartDate,rdEndDate : rdEndDate,rdInTime : rdInTime,rdLib : rdLib,rdSort1 : rdSort1,
				rdSort2 : rdSort2,rdSort3 : rdSort3,rdSort4 : rdSort4,rdSort5 : rdSort5,rdName : rdName,
				rdPasswd : rdPasswd,rdCertify : rdCertify,rdSex : rdSex,rdNation : rdNation,rdBornDate : rdBornDate,
				rdAge : rdAge,rdLoginId : rdLoginId,rdPhone : rdPhone,rdUnit : rdUnit,rdAddress : rdAddress,
				rdPostCode : rdPostCode,rdEmail : rdEmail,rdNative : rdNative,rdInterest : rdInterest, paySign:paySign, 
				libUser:libUser, rdRemark:rdRemark, deferDate:deferDate};
		$.ajax({
			type : "POST",
			url : "defer",
			data : params,
			dataType : "json",
			success : function(jsonData){
				var success = jsonData.success;
				if(success == 0) {
					var checkfee = jsonData.checkfee;
					var servicefee = jsonData.servicefee;
					var idfee = jsonData.idfee;
					var totalfee = jsonData.totalfee;
					var tip = "";
					if(checkfee != 0) {
						tip += " 验证费： " + checkfee + "元";
					}
					if(servicefee != 0) {
						tip += " 服务费 ：" + servicefee + "元";
					}
					if(idfee != 0) {
						tip += " 工本费 ：" + idfee + "元";
					}
					if(totalfee != 0) {
						tip += " 共应交" + totalfee + "元。";
						if(confirm(tip)) {
							paySign = "1";
							deferDate = prompt("请输入延期终止日期，格式YYYY-MM-DD，系统默认为：当前日期+读者有效期：", rdEndDate);
					        if (inputDefer == "") {
					        	showResult("终止日期不能是空");
					        	return;
					        } else if(inputDefer == null) {
					        	paySign = "0";
					        	return;
					        }
					        defer();
					        paySign = "0";
						}
					} else {
						paySign = "1";
						deferDate = prompt("请输入延期终止日期，格式YYYY-MM-DD，系统默认为：当前日期+读者有效期：", rdEndDate);
				        if (inputDefer == "") {
				        	showResult("终止日期不能是空");
				        	return;
				        } else if(inputDefer == null) {
				        	paySign = "0";
				        	return;
				        }
						defer();
						paySign = "0";
					}
				} else if(success == -1) {
					var message = jsonData.message;
					showResult(message);
					return;
				} else if(success == -2) {
					showResult("证操作成功!同步失败");
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					
					return;
				} else {
					showResult("证操作成功!");
					window.location.href = "index";//"/sso/admin/reader/add/"+rdId;
					return;
				}
				
			},
			cache: false
		});
	}
	
	//计算年龄
	function caculateAge(birthday) {
		if(!birthday){
			return;
		}
		birthday=new Date(birthday.replace(/-/g, "\/")); 
		var d=new Date(); 
		var age = d.getFullYear()-birthday.getFullYear()-((d.getMonth()<birthday.getMonth()|| d.getMonth()==birthday.getMonth() && d.getDate()<birthday.getDate())?1:0);
		document.getElementById("rdAge").value = age;
	}
	

	