<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	pageContext.setAttribute("basePath", request.getContextPath());
    pageContext.setAttribute("username",request.getSession().getAttribute("username"));
    //pageContext.setAttribute("mobilePhone",request.getSession().getAttribute("mobilePhone").equals("null")?"":request.getSession().getAttribute("mobilePhone"));

 // properties 配置文件名称
    ResourceBundle res = ResourceBundle.getBundle("sysConfig");

%>
<!DOCTYPE html>
<html>
<head>
	
	<meta name="renderer" content="webkit|ie-comp|ie-stand">

    <link href="<c:url value='/media/css/addrdinfo.css' />" media="all" rel="stylesheet" type="text/css" />
    <title>补全读者信息</title>
    <script type="text/javascript">
    
	 function validMobile(d){
	    if($(d).val()&&$(d).val().length==11){
	        if((/^1[3|4|5|7|8][0-9]{9}$/.test($(d).val()))){
	      	    return '';
	      	}
	    }
	    return '填写手机号不正确。\n';
	}
	 function validRdBornDate(d){
		    if($(d).val()>18&&$(d).val()<70){
		    	$('[name="rdBornDate"]').val(new Date().getFullYear()-$(d).val()+"-01-01")
		        return '';
		    }
		    return '填写年龄不正确。';
	}
	 function isAgree(){
		 if($(".agree :checkbox").prop('checked')){
			 return '';
		 }else{
			 return '请点击同意<%=res.getString("LibName")%>在线实名注册使用协议。\n';
		 }
	 }
	 
	 
	 var idCardNoUtil = {
		/*省,直辖市代码表*/
		provinceAndCitys : {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		},

		/*每位加权因子*/
		powers : [ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9",
				"10", "5", "8", "4", "2" ],

		/*第18位校检码*/
		parityBit : [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" ],

		/*性别*/
		genders : {
			male : "男",
			female : "女"
		},

		/*校验地址码*/
		checkAddressCode : function(addressCode) {
			var check = /^[1-9]\d{5}$/.test(addressCode);
			if (!check)
				return false;
			if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,
					2))]) {
				return true;
			} else {
				return false;
			}
		},

		/*校验日期码*/
		checkBirthDayCode : function(birDayCode) {
			var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/
					.test(birDayCode);
			if (!check)
				return false;
			var yyyy = parseInt(birDayCode.substring(0, 4), 10);
			var mm = parseInt(birDayCode.substring(4, 6), 10);
			var dd = parseInt(birDayCode.substring(6), 10);
			var xdata = new Date(yyyy, mm - 1, dd);
			if (xdata > new Date()) {
				return false;//生日不能大于当前日期
			} else if ((xdata.getFullYear() == yyyy)
					&& (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
				return true;
			} else {
				return false;
			}
		},

		/*计算校检码*/
		getParityBit : function(idCardNo) {
			var id17 = idCardNo.substring(0, 17);
			/*加权 */
			var power = 0;
			for (var i = 0; i < 17; i++) {
				power += parseInt(id17.charAt(i), 10)
						* parseInt(idCardNoUtil.powers[i]);
			}
			/*取模*/
			var mod = power % 11;
			return idCardNoUtil.parityBit[mod];
		},

		/*验证校检码*/
		checkParityBit : function(idCardNo) {
			var parityBit = idCardNo.charAt(17).toUpperCase();
			if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
				return true;
			} else {
				return false;
			}
		},

		/*校验15位或18位的身份证号码*/
		checkIdCardNo : function(idCardNo) {
			//15位和18位身份证号码的基本校验
			var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
			if (!check)
				return false;
			//判断长度为15位或18位 
			if (idCardNo.length == 15) {
				return idCardNoUtil.check15IdCardNo(idCardNo);
			} else if (idCardNo.length == 18) {
				return idCardNoUtil.check18IdCardNo(idCardNo);
			} else {
				return false;
			}
		},

		//校验15位的身份证号码
		check15IdCardNo : function(idCardNo) {
			//15位身份证号码的基本校验
			var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/
					.test(idCardNo);
			if (!check)
				return false;
			//校验地址码
			var addressCode = idCardNo.substring(0, 6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if (!check)
				return false;
			var birDayCode = '19' + idCardNo.substring(6, 12);
			//校验日期码
			return idCardNoUtil.checkBirthDayCode(birDayCode);
		},

		//校验18位的身份证号码
		check18IdCardNo : function(idCardNo) {
			//18位身份证号码的基本格式校验
			var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/
					.test(idCardNo);
			if (!check)
				return false;
			//校验地址码
			var addressCode = idCardNo.substring(0, 6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if (!check)
				return false;
			//校验日期码
			var birDayCode = idCardNo.substring(6, 14);
			check = idCardNoUtil.checkBirthDayCode(birDayCode);
			if (!check)
				return false;
			//验证校检码  
			return idCardNoUtil.checkParityBit(idCardNo);
		},

		formateDateCN : function(day) {
			var yyyy = day.substring(0, 4);
			var mm = day.substring(4, 6);
			var dd = day.substring(6);
			return yyyy + '-' + mm + '-' + dd;
		},

		//获取信息
		getIdCardInfo : function(idCardNo) {
			var idCardInfo = {
				gender : "", //性别
				birthday : "" // 出生日期(yyyy-mm-dd)
			};
			if (idCardNo.length == 15) {
				var aday = '19' + idCardNo.substring(6, 12);
				idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
				if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
					idCardInfo.gender = idCardNoUtil.genders.female;
				} else {
					idCardInfo.gender = idCardNoUtil.genders.male;
				}
			} else if (idCardNo.length == 18) {
				var aday = idCardNo.substring(6, 14);
				idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
				if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
					idCardInfo.gender = idCardNoUtil.genders.female;
				} else {
					idCardInfo.gender = idCardNoUtil.genders.male;
				}

			}
			return idCardInfo;
		},

		/*18位转15位*/
		getId15 : function(idCardNo) {
			if (idCardNo.length == 15) {
				return idCardNo;
			} else if (idCardNo.length == 18) {
				return idCardNo.substring(0, 6) + idCardNo.substring(8, 17);
			} else {
				return null;
			}
		},

		/*15位转18位*/
		getId18 : function(idCardNo) {
			if (idCardNo.length == 15) {
				var id17 = idCardNo.substring(0, 6) + '19'
						+ idCardNo.substring(6);
				var parityBit = idCardNoUtil.getParityBit(id17);
				return id17 + parityBit;
			} else if (idCardNo.length == 18) {
				return idCardNo;
			} else {
				return null;
			}
		}
	};
	function validRrdcertify(d) {
		if (idCardNoUtil.checkIdCardNo($(d).val())) {
			var idCardInfo=idCardNoUtil.getIdCardInfo($(d).val());
			$("[name='rdsex']").val(idCardInfo.gender);
			$("[name='rdborndate']").val(idCardInfo.birthday);
			$(d).attr('style','')
			return '';
		}
		$("[name='rdsex']").val('');
		$("[name='rdborndate']").val('');
		$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
		$('#id').removeAttr("readOnly");
		return '填写的身份证号不正确。\n';
	}
	  
	function subRdInfo(){
		 
		if ($('[name="rdLoginId"]').val() &&$('[name="rdCertify"]').val()&&$('[name="rdSort2"]').val()&&$('[name="rdSort5"]').val()) {
			var msg;
			if (!(msg = isAgree() + validMobile($('[name="rdLoginId"]'))+ validRrdcertify($('[name="rdCertify"]')))) {
				alert("您的证号已激活成功，请重新登陆。");
				$('form').submit();
				//window.history.back();
			} else {
				alert(msg);
			}
		} else {
			$('.input_info').each(function(i,d){
				if(!$(d).val().trim()){
					$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
					$(d).focus();
					return false;
				}
			})
			alert('请填写完整表单(或确认身份证号已验证通过)。');
		} 
		 
	 }
	
	function isCanRegCertify(d){
	
		var certify=$(d).val();
		if(certify.length==15||certify.length==18){
			$("#id").attr("readOnly","true");
			
		}
	}
	
	 
    </script>
</head>
<body>
    <div class="container">
    <div class="content">
             <h1><%-- <font color="#9f9f9f">${READER_SESSION.reader.rdName}</font> --%>
             	您好，欢迎成为<%=res.getString("LibName")%>读者！<br/>为了更好的为您提供服务，请补全您的真实个人信息，谢谢配合！</h1>
		    <form action="<c:url value='/portal/doindex' />" method="POST" name="loginform">
		        <input type="hidden" name="rdType" value="${reader!=null?reader.rdType:netreader.readerType}  ">
		        <input type="hidden" name="rdLibType" value="${reader!=null?reader.rdLibType:null}">
		        <input type="hidden" name="rdLib" value="${reader!=null?reader.rdLib:netreader.readerLib}">
		        <input type="hidden" name="rdId" value="${rdId}" id="rdid">
		        <input type="hidden" name="rdPasswd" value="${reader!=null?reader.rdPasswd:netreader.readerPassword}">
		        <p>
		        	<label>姓&emsp;&emsp;名:</label><input type="text" name="rdName" class='input_info'
		        		placeholder="请输入您的姓名。" value="${reader!=null?reader.rdName:netreader.readerName}" id="rdNameid"  onblur="isCanRegName(this)"/>
		        </p>
		        <p><label>手机号码:</label><input type="tel" name="rdLoginId" class='input_info' maxlength="11" placeholder="请输入您的手机号。" value="${reader!=null?reader.rdLoginId:netreader.readerMobile}"/></p>
				<p>
					<label>身份证号:</label><input type="text" name="rdCertify" class='input_info'
						maxlength="18" placeholder="请输入您的身份证号。" value="${reader!=null?reader.rdCertify:netreader.readerCertify}" id="id"  onblur="isCanRegCertify(this)"/>
				</p>
				<input type="hidden" name="rdsex" value=""> 
				<input type="hidden" name="rdborndate" value="">
				<p><label>电子邮箱:</label><input type="email" name="rdEmail" placeholder="请输入您的电子邮箱。" value="${reader!=null?reader.rdEmail:netreader.readerEmail}"/></p>
                <p><label>职&emsp;&emsp;业:</label><select name="rdSort2" class='input_info'>
                <option value="" selected = "selected">--请选择--</option>
		        <option value="其它" >其它</option>
		        <option value="公务员" >公务员</option>
		        <option value="科研人员">科研人员</option>
		        <option value="教师">教师</option>
		        <option value="文化工作者">文化工作者</option>
		        <option value="工人">工人</option>
		        <option value="军人">军人</option>
		        <option value="医务工作者">医务工作者</option>
		        <option value="公司职员">公司职员</option>
		        <option value="学生">学生</option>
		        <option value="个体劳动者">个体劳动者</option>
		        </select></p>
		        <p><label>文&emsp;&emsp;化:</label><select name="rdSort5" class='input_info'>
		        <option value="" selected = "selected">--请选择--</option>
		        <option value="其它" >其它</option>
		        <option value="高中">高中</option>
		        <option value="中专">中专</option>
		        <option value="博士生">博士生</option>
		        <option value="本科">本科</option>
		        <option value="研究生">研究生</option>
		        <option value="大专">大专</option>
		        </select></p>
		        <p><input type="submit"  value="确定" onclick="subRdInfo();return false;">&emsp;&emsp;<input type="reset" value="重置"/><span class="agree" ><input type="checkbox" /><a href="./agreement.jsp" target="_blank" ><span>同意协议(<%=res.getString("LibName")%>在线实名注册使用协议)</span></a></span></p>
		    </form>
		</div>
		</div>
</body>
<script src="<c:url value='/media/js/jquery/jquery-1.8.3.min.js' />" type="text/javascript"></script>
<script type="text/javascript">
	function isCanRegName(d){
		var name=$(d).val();
		var rdid = document.getElementById("rdid").value;
		if(name != rdid && name.length != 0){
			$("#rdNameid").attr("readOnly","true");
		}
	} 
</script>
</html>