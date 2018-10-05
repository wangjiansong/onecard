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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value='/media/css/register/css/addrdinfo.css' />" rel="stylesheet" type="text/css" />
<title>读者注册页面</title>
<script src="<c:url value='/media/css/register/js/jquery-1.11.1.min.js' />" type="text/javascript"></script>

<script type="text/javascript">
 var validedCertify=true,spTip='';
	function validMobile(d) {
		if ($(d).val() && $(d).val().length == 11) {
			if ((/^1[3|4|5|7|8][0-9]{9}$/.test($(d).val()))) {
				$(d).attr('style','');
				return '';
			}
		}
		$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;');
		return '填写的手机号不正确。\n';
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
			var pre;
			if (pre=idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,
					2))]) {
				if(pre=='<%=res.getString("pre")%>'){
					return true;
				}else{
					spTip='抱歉，您的身份证不属于<%=res.getString("pre")%>省区域，不允许注册。';
					return false;
				}
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
	function updateBronSex(d) {
		var idCardInfo=idCardNoUtil.getIdCardInfo($(d).val());
		$("[name='rdsex']").val(idCardInfo.gender);
		$("[name='rdborndate']").val(idCardInfo.birthday);
	}
	function validRdpasswd(d){
		var v=$(d).val();
		if(v.length<6){
			$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
			return '密码长度不能少于6位。\n';
		}else{
			$(d).attr('style','')
			var rv=$('[name="rerdpasswd"]').val();
			if(rv==v){
				$('[name="rerdpasswd"]').attr('style','')
				return '';
			}else{
				$('[name="rerdpasswd"]').attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
				return '两次输入的密码不一致。\n';
			}
		}
	}
	function validEmail(d){
		var v=$(d).val();
		if(!/^[A-Za-z0-9]+([-_.][A-Za-z0-9]+)*@([A-Za-z0-9]+[-.])+[A-Za-z0-9]{2,5}$/.test(v)){
			$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
			return '输入的电子邮箱格式不正确。\n';
		}
		$(d).attr('style','')
		return '';
	}
	function isAgree() {
		if ($(".agree :checkbox").prop('checked')) {
			return '';
		} else {
			return '请点击同意<%=res.getString("LibName")%>在线实名注册使用协议。\n';
		}
	}
	
	function validcheckCode(d){
		var v=$(d).val();
		//alert(v);
		if(v.length<6){
			$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;')
			return '输入的验证码位数不正确。\n';
		}else{
			$(d).attr('style','')
			return '';
		}
	}

	function subReg() {
		if ($('[name="rdLoginId"]').val()&& $('[name="checkCode"]').val() && $('[name="rdpasswd"]').val()&& $('[name="rdname"]').val()&& $('[name="rerdpasswd"]').val()&&$('[name="rdcertify"]').val()&&$('[name="rdSort2"]').val()&&$('[name="rdSort5"]').val()&&validedCertify) {
			var msg;
			if (!(msg = isAgree() + validMobile($('[name="rdLoginId"]'))
					+validRdpasswd($('[name="rdpasswd"]'))
					+($('[name="rdemail"]').val()?validEmail($('[name="rdemail"]')):"")
					+(validcheckCode($('[name="checkCode"]'))))) {
				updateBronSex($('[name="rdcertify"]'));
				$('form').submit();
			} else {
				alert(msg);
			}
		} else {
			$('.input_info').each(function(i,d){
				if(!$(d).val().trim()){
					$(d).focus();
					return false;
				}
			})
			alert('请填写完整表单，并确认无误。');
		}
	}

function isCanRegCertify(d){
		/* var certify=$(d).val(); */
		var certify=$(d).val().trim();
		if(certify){
			if(idCardNoUtil.checkIdCardNo(certify)){
				$.ajax({
					url:"<c:url value='/portal/checkRdCertifyExist' />",
					type : "POST",
					dataType : "json",
					data:{method:'checkRdCertifyExist',rdCertify:certify},
					async:false,
					success:function(data){
						if(new Number(data.result).toString()!='NaN'){
							validedCertify=true;
							$(d).attr('style','');
							/*$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;');
							confirm("该身份证号已存在，请确认是否输入有误。");  */
						}else{
							validedCertify=false;
							/*$(d).attr('style',''); */
							 $(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;');
							confirm("您的身份证号码已被注册。");
						}
					},
					error:function(){
						validedCertify=false;
						$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;');
						confirm("验证身份证号失败，请重新输入。");
					}
				});
			}else{
				validedCertify=false;
				$(d).attr('style','  border-color: #e2a97d;outline-color: #fccedc;outline-offset: 0;');
				if(spTip){
					confirm(spTip);
					spTip='';
				}else{
					confirm("请输入正确格式的身份证号码。");
				}
			}
			d.blur();
		}
	}
	
    var interval;
	function clock(){
		var s=$('.checkCodeBtn').html();
		var i=s.substr(0,2);
		var s=s.substr(2);
		i=i-1;
		if(i<10){
			i='0'+i;
			if(i==0){
				interval=window.clearInterval(interval);
				$('.checkCodeBtn').html("获取验证码");
				$('.checkCodeBtn').attr('style','');
				return;
			}
		}
		$('.checkCodeBtn').html(i+s);
	}
	
	function vaildateCheckCode(d){
		/* var checkCode=$('#checkCode').val(); */
		var checkCode=$('#checkCode').val().trim();
		var rdLoginId=$('[name="rdLoginId"]').val();
		if(checkCode&&rdLoginId){
			//发送验证码验证是否正确
			$.ajax({
				url:"<c:url value='/portal/checkValidate' />",
				type : "POST",
				dataType : "json",
				data:{checkCode:checkCode},
				async:false,
				success:function(data){
					data = parseInt(data, 10);
	                if(data == 1){  
	                }else{
	                  confirm("短信验证码填入与实际不相符");
	                }  
				},
				error:function(){
					confirm("验证码校验失败！");
				}
			})
		}
	}
	
$(function(){
	$(document).on("click",".checkCodeBtn",function(e){
		e.stopPropagation();
		var flag=false;

		if($(this).html()=="获取验证码"&&!validMobile($('[name="rdLoginId"]'))){
			//$(this).attr('style','cursor:no-drop');
			//$(this).html('60秒可重新获取。');
			//interval=self.setInterval("clock()",1000);
			//发送验证码
			 var phone = $('[name="rdLoginId"]').val();
			 $.ajax({
				url:"<c:url value='/portal/sendValidate' />",
				type : "POST",
				data:{"phone":phone},
				dataType:"json",
				success:function(data){
				console.log(data);
					if(data.success=="success"){
						$('.checkCodeBtn').attr('style','cursor:no-drop'); 
						$('.checkCodeBtn').html('60秒可重新获取'); 
						interval=self.setInterval("clock()",1000);
					}else{
						confirm("手机号注册了...请换手机号再试");
					}
					
				},error:function(data){
					flag=true;
					confirm("ajax出错..");
					//confirm(data);
				}
			}); 
		}
	})
	
})

</script>

</head>
<body ontouchstart="" onmouseover="">
	<div class="container">
		<div class="content">
			<h1>
				您好，欢迎您注册成为<%=res.getString("LibName")%>读者！<br />为了更好的为您提供服务，请填写完整您的个人信息，谢谢配合！
			</h1><h3 style="color:red;">${message}${map.mm} </h3>
			<form action="/onecard/portal/regresult" method="post">
				<input type="hidden" name="rdType" value="<%=res.getString("rdType")%>"> 
				<input type="hidden" name="rdLibType" value="<%=res.getString("rdLibType")%>">
				<input type="hidden" name="rdLib" value="<%=res.getString("rdLib")%>">
				<p>
					<label>姓&emsp;&emsp;名:</label><input type="text" name="rdname" class='input_info'
						placeholder="请输入您的姓名。" value="" autocomplete="off"/><span class="warn">*</span>
				</p>
				<p>
					<label>密&emsp;&emsp;码:</label><input type="password" class='input_info'
						name="rdpasswd" placeholder="请输入您的密码。" value=""  autocomplete="off"/><span class="warn">*</span>
				</p>
				<p>
					<label>确认密码:</label><input type="password" name="rerdpasswd" class='input_info'
						placeholder="请输入确认密码。" value=""  autocomplete="off"/><span class="warn">*</span>
				</p>
				<p>
					<label>手机号码:</label><input type="tel" name="rdLoginId" class='input_info'
						maxlength="11" placeholder="请输入您的手机号。" value=""  autocomplete="off"/><span class="warn">*</span>
				</p>
				<p>
					<label>验&emsp;证码:</label><input type="text" name="checkCode"  id="checkCode" class='input_info'
						maxlength="6" placeholder="请输入验证码。" value=""  autocomplete="off" onblur="vaildateCheckCode(this)"/>&emsp;<a href="javascript:void(0);" class="checkCodeBtn">获取验证码</a><span class="warn">*</span>
				</p>
				<p>
					<label>身份证号:</label><input type="text" name="rdcertify" id="rdcertify" class='input_info'
						maxlength="18" placeholder="请输入您的身份证号。" value="" autocomplete="off" onblur="isCanRegCertify(this)"/><span class="warn">*</span>
				</p>
				<input type="hidden" name="rdsex" value=""> <input
					type="hidden" name="rdborndate" value="">
				<p>
					<label>电子邮箱:</label><input type="email" name="rdemail"
						placeholder="请输入您的电子邮箱。" value="" autocomplete="off"/>
				</p>
				<p>
					<label>职&emsp;&emsp;业:</label><select name="rdSort2" class='input_info'>
					    <option value="" >---请选择---</option>
						<option value="其它" >其它</option>
						<option value="公务员">公务员</option>
						<option value="科研人员">科研人员</option>
						<option value="教师">教师</option>
						<option value="文化工作者">文化工作者</option>
						<option value="工人">工人</option>
						<option value="军人">军人</option>
						<option value="医务工作者">医务工作者</option>
						<option value="公司职员">公司职员</option>
						<option value="学生">学生</option>
						<option value="个体劳动者">个体劳动者</option>
					</select><span class="warn">*</span>
				</p>
				<p>
					<label>学&emsp;&emsp;历:</label><select name="rdSort5" class='input_info'>
					    <option value="" >---请选择---</option>
						<option value="其它" >其它</option>
						<option value="高中">高中</option>
						<option value="中专">中专</option>
						<option value="大专">大专</option>
						<option value="本科">本科</option>
						<option value="博士生">博士生</option>
						<option value="研究生">研究生</option>
					</select><span class="warn">*</span>
				</p>
				<p>
					<input type="submit" value="注册" onclick="subReg();return false;">&emsp;<input
						type="reset" value="重置" /><span class="agree"><input
						type="checkbox" /><a href="./agreement.jsp" target="_blank"><span>同意协议(<%=res.getString("LibName")%>在线实名注册使用协议)</span></a></span>
				</p>
			</form>
		</div>
	</div>
</body>
</html>