<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  --%>
<%@include file="/jsp_tiles/include/taglibs.jsp"%>

<%@ page isELIgnored="false"%><style type="text/css">
.error_div {
  color: #666;
  text-align: center;
  font-family: Helvetica, 'microsoft yahei', Arial, sans-serif;
  margin:0;
  width: 800px;
  margin: auto;
  font-size: 14px;
}
.error_div h1 {
  font-size: 56px;
  line-height: 100px;
  font-weight: normal;
  color: #456;
}
.error_div h2 { font-size: 24px; color: #666; line-height: 1.5em; }

.error_div h3 {
  color: #456;
  font-size: 20px;
  font-weight: normal;
  line-height: 28px;
}

.error_div hr {
  margin: 18px 0;
  border: 0;
  border-top: 1px solid #EEE;
  border-bottom: 1px solid white;
}

.error_div a{
    color: #17bc9b;
    text-decoration: none;
}

.error_div em{
    text-align: left;
}
</style>
<div style="margin:100px;" class="error_div">
  	<h3>非常抱歉，服务器出现了一个错误，我们正在修复中 (500)</h3>
  	<hr>
  	<p><a href="/onecard">返回首页</a></p>
  	<em>${model["ex"]}</em>
</div>