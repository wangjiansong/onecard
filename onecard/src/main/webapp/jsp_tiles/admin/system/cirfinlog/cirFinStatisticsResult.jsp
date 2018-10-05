<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="BASE_URL">
	<c:url value="/admin/sys/cirfinlog" />
</c:set>
<script type="text/javascript">
	function pressEnter(e, method) {
		if (e == null) {//ie6..
			e = event;
		}
		if ((e.which == 13 || e.keyCode == 13) && (!(e.ctrlKey || e.metaKey))
				&& !e.shiftKey && !e.altKey) {
			method();
		}
	}
	function exportExcel() {
		var form1 = document.getElementById("form1");
		form1.action = "<c:url value='/admin/sys/cirfinlog/exportCifFinStatisticsExcel' />";
		form1.submit();
	}
</script>

<div class="well form-actions_1" >
	<center>
	<h4>流通财经统计报表</h4><br/>
	【  起始日期：${cirFinLog.startTime} 终止日期：${cirFinLog.endTime} 】<br/>
	<c:if test="${cirFinLog.startRdid ne ''}">
	【  数据范围：起始证号：${cirFinLog.startRdid} 终止证号：${cirFinLog.endRdid} 】<br/>
	</c:if>
	<c:if test="${cirFinLog.rdname ne ''}">
	【  读者姓名：${cirFinLog.rdname} 】<br/>
	</c:if>
	<c:if test="${cirFinLog.startRdid eq '' && cirFinLog.rdname eq ''}">
	【  数据范围：全部读者  】<br/>
	</c:if>
	【 参与财经系统：
	<c:forEach items="${authList}" var="item">
		${item.appName},
	</c:forEach>
	】
	<button class="btn btn-info" type="button" onclick="exportExcel();">导出Excel</button>
	</center>
</div>
<div id="resultDiv">
	<form name="form1" action="exportCifFinStatisticsExcel" id="form1">
		<input id="regLib" name="regLib" type="hidden" value="${cirFinLog.regLib}"/>
		<input id="feeAppCode" name="feeAppCode" type="hidden" value="${cirFinLog.feeAppCode}"/>
		<input id="startTime" name="startTime" type="hidden" value="${cirFinLog.startTime}"/>
		<input id="endTime" name="endTime" type="hidden" value="${cirFinLog.endTime}"/>
		<input id="regman" name="regman" type="hidden" value="${cirFinLog.regman}"/>
		<input id="feetype" name="feetype" type="hidden" value="${cirFinLog.feetype}"/>
		<input id="startRdid" name="startRdid" type="hidden" value="${cirFinLog.startRdid}"/>
		<input id="endRdid" name="endRdid" type="hidden" value="${cirFinLog.endRdid}"/>
		<input id="rdname" name="rdname" type="hidden" value="${cirFinLog.rdname}"/>
	</form>
	<hr style="border: 2 dotted black; width: 100%">
	<center>
	<table border="0" id="resultTable">
		<tbody>
			<tr>
				<td valign="top"><div id="statisResultTable" valign="top">
						<table id="resultContentTable" cellpadding="0" cellspacing="0"
							border="1" bordercolor="darkslateblue"
							style="border-collapse: collapse; word-break: keep-all; text-align: center;"
							width="100%">
							<tbody>
								<tr>
									<td><div id="statisColRowName">
											<b>(<a href="javascript:doSort('row',0)">经手人员</a>\<a
												href="javascript:doSort('col',0)">财经类型</a>)
											</b>
										</div></td>
									<c:forEach items="${finTypeList}" var="array">
										<td><b>${array.describe}</b><br/>(${array.appName})</td>
									</c:forEach>
									<td><b>合计</b></td>
								</tr>
								<c:forEach items="${results}" var="map">
									<tr>
									<c:forEach items="${map}" var="item">
										<c:if test="${item.key eq 'REGMAN'}">
											<c:if test="${item.value eq null}">
												<td><b>合计</b></td>
											</c:if>
											<c:if test="${item.value ne null}">
											<td><b>${item.value}</b></td>
											</c:if>
					 					</c:if>
					 					<c:if test="${item.key ne 'REGMAN'}">
						 					<td><div name="val1">${item.value}</div></td>
					 					</c:if>
									</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div></td>
				<!--统计结果表格-->
			</tr>
		</tbody>
	</table>
	</center>
	<hr style="border: 2 dotted black; width: 100%">
	<script type="text/javascript">
		$(function() {
			var today = new Date().Format("yyyy-MM-dd");
			$("#today").html(today);
		});
	</script>
	<div class="well form-actions_1">
		<center>
			统计人：${READER_SESSION.reader.rdName}  统计日期：${today}
		</center>
	</div>
</div>