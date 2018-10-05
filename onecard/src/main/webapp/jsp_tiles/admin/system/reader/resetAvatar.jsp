<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	var IMAGETYPES = [".JPG",".JPEG",".PNG",".BMP",".GIF"];
	
	function preview(localPath) {
		var fileObj=document.getElementById("fileObj");
		var avatar=document.getElementById("avatar");
		var tip = document.getElementById("tip");
		if(fileObj.files && fileObj.files[0]){
			//火狐,Chrome:直接设img属性
			//avatar.src = fileObj.files[0].getAsDataURL();
			//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
			var suffix = localPath.substring(localPath.lastIndexOf("."),localPath.length).toUpperCase();
			var isPermit = false;
			for(var i=0; i<IMAGETYPES.length; i++){
				if(suffix == IMAGETYPES[i]){
					isPermit = true;
					break;
				}
			}
			if(isPermit == false){
				document.getElementById("localPath").value = "";
				tip.innerHTML = "只支持&nbsp;jpg&nbsp;|&nbsp;jpeg&nbsp;|&nbsp;png&nbsp;|&nbsp;bmp&nbsp;|&nbsp;gif&nbsp;格式的图片！";
				tip.style.display = "inline";
				return;
			}
			tip.style.display = "none";
			document.getElementById("localPath").value = localPath;
			avatar.src = window.URL.createObjectURL(fileObj.files[0]);
		}else{
			//IE:使用滤镜
			fileObj.select();
			//获取用户选择文本
			var thisPath = document.selection.createRange().text;
			var suffix = localPath.substring(localPath.lastIndexOf("."),localPath.length).toUpperCase();
			var isPermit = false;
			for(var i=0; i<IMAGETYPES.length; i++){
				if(suffix == IMAGETYPES[i]){
					isPermit = true;
					break;
				}
			}
			if(isPermit == false){
				document.getElementById("localPath").value = "";
				tip.innerHTML = "只支持&nbsp;jpg&nbsp;|&nbsp;jpeg&nbsp;|&nbsp;png&nbsp;|&nbsp;bmp&nbsp;|&nbsp;gif&nbsp;格式的图片！";
				tip.style.display = "inline";
				return;
			}
			document.getElementById("localPath").value = localPath;
			var previewDivForIE = document.getElementById("previewDivForIE");
			//必须设置初始大小
			previewDivForIE.style.width = "139px";
			previewDivForIE.style.height = "139px";
			//图片异常的捕捉，防止用户修改后缀来伪造图片
			try{
				previewDivForIE.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				previewDivForIE.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = thisPath;
			}catch(e){
				tip.innerHTML = "您上传的图片格式不正确，请重新选择！";
				tip.style.display = "inline";
				return;
			}
			tip.style.display = "none";
			avatar.style.display = "none";
			document.selection.empty();
		}
	}
	
	function getBack() {
		window.location.href = "<c:url value='/admin/reader/detailReader/${rdId}' />";
	}
</script>
<div class="page-header">
	<span style="font: bold 27px arial,sans-serif;">修改头像</span>
	<span id="tip" class="alert alert-error" style="margin-left:70px;display: none;"></span>
</div>
<div>
	<div id="avatarDiv">
		<img id="avatar" class="img-polaroid" style="width: 130px;height: 130px;" 
			src="<c:url value="/admin/reader/showAvatar/${rdId}" />" />
	</div>
	<div id="previewDivForIE">
	</div>
	<div style="position: relative;width: 370px;" >
		<form name="dataform" action="<c:url value='/admin/reader/uploadAvatar/${rdId}' />" 
			method="post" enctype="multipart/form-data" target="hidden_frame">
			<input type="text" id="localPath" name="localPath" style="height: 22px;width: 180px;margin-top: 10px;" />  
			<input type="file" id="fileObj" name="fileObj" style="position: absolute;top: 11px;left: 0px;
				width: 252px;height: 30px;filter: alpha(opacity:0);opacity: 0;"
				onchange="javascript:preview(this.value);" />
			<input type="button" class="btn btn-primary" value="浏览" />
			<input type="button" class="btn btn-success" value="上传" 
				onclick="javascript:uploadAvatar();" />
			<input type="button" class="btn btn-inverse" value="返回" 
				onclick="javascript:getBack();" />
			<input type="hidden" name="rdId" value="${rdId}" />
			<iframe id="hidden_frame" name="hidden_frame" style="display: none;">
			</iframe>
		</form>
	</div>
</div>
<script type="text/javascript">
	function uploadAvatar() {
		var tip = document.getElementById("tip");
		var localPath = document.getElementById("localPath").value;
		if(localPath){
			tip.style.display = "none";
			document.forms[0].submit();
		}else{
			tip.innerHTML = "请选择图片！";
			tip.style.display = "inline";
			return false;
		}
	}
	
	function callback(msg) {
		var tip = document.getElementById("tip");
		if(msg == "error" || msg == "ohno"){
			tip.className = "A_MESSAGEBOX_LOADING alert alert-error";
			tip.innerHTML = "上传失败！";
			tip.style.display = "block";
			return false;
		}else{
			tip.className = "A_MESSAGEBOX_LOADING alert alert-success";
			tip.innerHTML = "上传成功！";
			tip.style.display = "block";
			setTimeout(function(){
				getBack();
			},1000);
		}
	}
</script>