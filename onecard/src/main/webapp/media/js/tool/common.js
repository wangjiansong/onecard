/******* 常用JS方法 *******/

/* 给String注册清空首尾空格的方法 */
String.prototype.trim = function() {
	return this.replace(/^\s+/g, "").replace(/\s+$/g, "");
};

/* 给String注册替换全部的方法 */
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
};

/* 字符串转成json对象 */
function decode(str) {
	return (typeof (str) != "string") ? str : (str.trim() == '') ? '' : eval('(' + str + ')');
}

/* 模拟ext的decode方法 */
try {
	if (!Ext.util.JSON.decode)
		var Ext = {
			util : {
				JSON : {
					decode : function(str) {
						return decode(str);
					}
				}
			}
		};
} catch (e) {
	var Ext = {
		util : {
			JSON : {
				decode : function(str) {
					return decode(str);
				}
			}
		}
	};
}

/* 如果对象未定义，或者为空，或者是空字符串，返回真，否则返回假 */
function isEmpty(obj) {
	if (typeof (obj) == "undefined" || obj == null
			|| (typeof (obj) != "object" && (obj + "").replace(/ /g, "") == "")) { // ||obj.length==0
		return true;
	}
	return false;
}

/* 获取浏览器类型与版本 */
function getBrowserType() {
	var browserType = "";
	navigator.appName;
	var ua = navigator.userAgent.toLowerCase();
	if (window.ActiveXObject) {
		browserType = "IE " + ua.match(/msie ([\d.]+)/)[1];
	} else if (document.getBoxObjectFor) {
		browserType = "FIREFOX " + ua.match(/firefox\/([\d.]+)/)[1];
	} else if (window.MessageEvent && !document.getBoxObjectFor) {
		browserType = "FIREFOX " + ua.match(/mozilla\/([\d.]+)/)[1];
	} else if (window.MessageEvent && !document.getBoxObjectFor) {
		browserType = "CHROME " + ua.match(/chrome\/([\d.]+)/)[1];
	} else if (window.opera) {
		browserType = "OPERA " + ua.match(/opera.([\d.]+)/)[1];
	} else if (window.openDatabase){
		browserType = "SAFARI " + ua.match(/version\/([\d.]+)/)[1];
	}
	return browserType;
}

/* 时间对象格式化 */
Date.prototype.format = function(format) {
    var o = {
    	"M+" : this.getMonth()+1, //month
	    "d+" : this.getDate(),    //day
	    "h+" : this.getHours(),   //hour
	    "m+" : this.getMinutes(), //minute
	    "s+" : this.getSeconds(), //second
	    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
	    "S" : this.getMilliseconds() //millisecond
    };
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
    RegExp.$1.length==1 ? o[k] :
    ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
};