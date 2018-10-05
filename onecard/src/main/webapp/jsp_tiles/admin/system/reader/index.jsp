<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="<c:url value='/media/bootstrap/plugin/bootstrap-tab.js' />"></script>
<script type="text/javascript">
	var tab_1_flag = false;
	var tab_2_flag = true;
	var tab_3_flag = true;
	
	/* 初始点击事件 */
	$(function(){
		var defaultTab = "${defaultTab}";
		if(defaultTab == "" || defaultTab == "readertype"){
			$('#myTab a:first').tab('show');
		}else if(defaultTab == "libtype"){
			$('#myTab a[href="#libtype"]').tab('show');
// 			$('#myTab li:eq(1) a').tab('show');
		}else if(defaultTab == "libcode"){
			$('#myTab a:last').tab('show');
		}
		
		$('#myTab a').click(function (e) {
			e.preventDefault();//取消事件的默认动作
			var tabid = e.target.hash;
			$(this).tab('show');
			if(tabid == "#readertype"){
				window.location.href = "<c:url value='/system/reader/readertype/readerTypeIndex' />";
				return false;
			}else if(tabid == "#libtype" && tab_2_flag){
// 				window.location.href = "<c:url value='/system/reader/libtype/libTypeIndex' />";
				tab_2_flag = false;
			}else if(tabid == "#libcode" && tab_3_flag){
				window.location.href = "<c:url value='/system/reader/libcode/libCodeIndex' />";
			}
		});
	});
</script>
<section id="tabs">
	<div>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#readertype" data-toggle="tab">读者流通类型</a></li>
<!-- 			<li><a href="#libtype" data-toggle="tab">馆际流通类型</a></li> -->
			<li><a href="#libcode" data-toggle="tab">分馆管理</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		    <div class="tab-pane active" id="readertype">
		    	<%@include file="/jsp_tiles/admin/system/reader/readertype/readerTypeIndex.jsp" %>
		    </div>
<!-- 		    <div class="tab-pane fade" id="libtype"> -->
<%-- 		    	<%@include file="/jsp_tiles/admin/system/reader/libtype/libTypeIndex.jsp" %> --%>
<!-- 		    </div> -->
		    <div class="tab-pane fade" id="libcode">
		    	<%@include file="/jsp_tiles/admin/system/reader/libcode/libCodeIndex.jsp" %>
		    </div>

		</div>
	</div>
</section>