<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-tab.js' />"></script>
<script type="text/javascript">
	var tab_1_flag = false;
	var tab_2_flag = true;
	
	/* 初始点击事件 */
	$(function(){
		
		$('#myTab a').click(function (e) {
			e.preventDefault();//取消事件的默认动作
			var tabid = $(this).attr("id");
			$(this).tab('show');
			if(tabid == "resourceList"){
				alert("?");
				window.location.href = "<c:url value='/admin/sys/res/list' />";
				$("#resourceList_pane").removeClass("active").addClass("fade");
				return false;
			}else if(tabid == "resourceAdd" && tab_2_flag){
				alert("!");
				window.location.href = "<c:url value='/admin/sys/res/add' />";
				$("#resourceAdd_pane").removeClass("fade").addClas("active");
			}
		}); 
	});
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#" data-toggle="tab" id="resourceList">资源列表</a></li>
			<li><a href="#" data-toggle="tab" id="resourceAdd">增加资源</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active" id="resourceList_pane">
		    	<%@include file="/jsp_tiles/admin/system/resource/list.jsp" %>
		    </div>
		    <div class="tab-pane fade" id="resourceAdd_pane">
		    	<%@include file="/jsp_tiles/admin/system/resource/add.jsp" %>
		    </div>

		</div>
	</div>
</section>