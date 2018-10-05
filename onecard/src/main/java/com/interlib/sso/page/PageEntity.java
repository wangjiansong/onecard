package com.interlib.sso.page;

public class PageEntity {
	private int showCount = 10; //每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	//private String currentPageS;	//字符串当前页,防止字符串
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;
	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	//新加
	//排序类型，升降序 asc, desc;
	private String sortType;
	//排序字段;
	private String sortCol;
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public int getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
		deal();//先处理属性，计算出当前页及总页数
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
//	public void setCurrentPage(int currentPage) {
//		this.currentPage = currentPage;
//	}
	
	public void setCurrentPageS(String currentPageS) {
		try{
			this.currentPage = Integer.parseInt(currentPageS);
		}catch(Exception e){
			this.currentPage = 0;
		}
		//System.out.println("setCurrentPageS:"+currentPage);
		//this.currentPageS = currentPageS;
	}
	
	//处理属性，计算出当前页及总页数等
	private void deal(){
		//处理总页数
		if(totalResult%showCount==0)
			totalPage = totalResult/showCount;
		else
			totalPage = totalResult/showCount+1;
		
		//处理当前页
		if(currentPage<=0)
			currentPage = 1;
		if(currentPage>totalPage)
			currentPage = totalPage;
		
		//当前开始记录数
		currentResult = (currentPage-1)*showCount;
		if(currentResult<0)
			currentResult = 0;
	}
	
	public String getPageStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("	<input type=\"hidden\" id=\"sortType\" name=\""+
				(entityOrField?"sortType":"page.sortType")+"\" value=\""+
				(sortType==null?"":sortType)+"\"></input>\n");
		sb.append("	<input type=\"hidden\" id=\"sortCol\" name=\""+
				(entityOrField?"sortCol":"page.sortCol")+"\" value=\""+
				(sortCol==null?"":sortCol)+"\"></input>\n");
		if(totalResult>0){ 
			sb.append("	<ul>\n");
			if(currentPage==1){
				sb.append("	<li class=\"pageinfo\">首页</li>\n");
				sb.append("	<li class=\"pageinfo\">上页</li>\n");
			}else{	
				sb.append("	<li class=\"pageinfo\"><a href=\"javascript:nextPage(1)\">首页</a></li>\n");
				sb.append("	<li class=\"pageinfo\"><a href=\"javascript:nextPage("+
						(currentPage-1)+")\">上页</a></li>\n");
			}
			int showTag = 5;	//分页标签显示数量
			int startTag = 1;
			if(currentPage>showTag){
				startTag = currentPage-1;
			}
			int endTag = startTag+showTag-1;
			for(int i=startTag; i<=totalPage && i<=endTag; i++){
				if(currentPage==i)
					sb.append("<li class=\"current\">"+i+"</li>\n");
				else
					sb.append("<li class=\"pageinfo\"><a href=\"javascript:nextPage("+
							i+")\">"+i+"</a></li>\n");
			}
			if(currentPage==totalPage){
				sb.append("	<li class=\"pageinfo\">下页</li>\n");
				sb.append("	<li class=\"pageinfo\">尾页</li>\n");
			}else{
				sb.append("	<li class=\"pageinfo\"><a href=\"javascript:nextPage("+(currentPage+1)+")\">下页</a></li>\n");
				sb.append("	<li class=\"pageinfo\"><a href=\"javascript:nextPage("+totalPage+")\">尾页</a></li>\n");
			}
			sb.append("	<li><input type=\"text\" style=\"width:25px;\" value=\""+currentPage+"\"/>");
			sb.append("<input type=\"button\" style=\"width:30px;\" value=\"GO\" ");
			sb.append("		onclick=\"nextPage(this.previousSibling.value)\"/></li>\n");
			//sb.append("	<li class=\"pageinfo\">第"+currentPage+"页</li>\n");
			//sb.append("	<li class=\"pageinfo\">共"+totalPage+"页</li>\n");
			sb.append("	<li class=\"pageinfo\">"+currentPage+"/"+totalPage+"页</li>\n");
			sb.append("</ul>\n");
			sb.append("<script type=\"text/javascript\">\n");
			sb.append("function nextPage(page){");
			sb.append("	if(true && document.forms[0]){\n");
			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("		if(url.indexOf('?')>-1){url += \"&"+
					(entityOrField?"currentPageS":"page.currentPageS")+"=\";}\n");
			sb.append("		else{url += \"?"+(entityOrField?"currentPageS":"page.currentPageS")+"=\";}\n");
			sb.append("		document.forms[0].action = url+page;\n");
			sb.append("		document.forms[0].submit();\n");
			sb.append("	}else{\n");
			sb.append("		var url = document.location+'';\n");
			sb.append("		if(url.indexOf('?')>-1){\n");
			sb.append("			if(url.indexOf('currentPageS')>-1){\n");
			sb.append("				var reg = /(page.)?currentPageS=\\d*/g;\n");
			sb.append("				url = url.replace(reg,'$1currentPageS=');\n");
			sb.append("			}else{\n");
			sb.append("				url += \"&"+(entityOrField?"currentPageS":"page.currentPageS")+"=\";\n");
			sb.append("			}\n");
			sb.append("		}else{url += \"?"+(entityOrField?"currentPageS":"page.currentPageS")+"=\";}\n");
			sb.append("		document.location = url + page;\n");
			sb.append("	}\n");
			sb.append("}\n");
			sb.append("</script>\n");
		}else{ 
			sb.append("<span>查询无结果</span>");
		} 
		pageStr = sb.toString();
		return pageStr;
	}
	
//	public void setPageStr(String pageStr) {
//		this.pageStr = pageStr;
//	}
	
	public int getShowCount() {
		return showCount;
	}
	
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	
	public int getCurrentResult() {
		return currentResult;
	}
	
//	public void setCurrentResult(int currentResult) {
//		this.currentResult = currentResult;
//	}
	
	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortCol() {
		return sortCol;
	}

	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}
	
	
}

