package com.interlib.sso.page;

/**
 * TODO 写好使用说明,注释
 * @author zhoulonghuan@gmail.com
 */
public class Paginator {

	/**
	 * 返回一个数组,分页的页码数组
	 * 如果page=[1..7]
	 * 显示: 1 2 3 4 5 6 7 8 9 10
	 * 如果page=[8..]
	 * 显示: 3 4 5 6 7 8 9 10 11 12
	 * 
	 * 1 2 3 4 [5] 6 7 8 9 10, 11 12 13 14 15 16 17 18 19 20
	 * @param page 当前页
	 * @param pages 总页数
	 * @return
	 */
	public static int[] getNavPages(int page, int pages) {
		//minPage代表分页页面数组中最小的页码，maxPage即最大的页面
		int minPage = page - 5;
		minPage = minPage < 1 ? 1 : minPage;
		int maxPage = page + 5;
		//如果当前页是在前5页范围内,后面的页码应该补足5位:page+5+(5-page)=page-page+5+5=5+5=10
		if(page <= 5) {
			maxPage = maxPage + (5 - page);
		}
		maxPage = maxPage > pages ? pages : maxPage;
		if((maxPage - page) <= 5) {
			minPage = minPage - (5 - (maxPage - page));
		}
		minPage = minPage < 1 ? 1 : minPage;
		
		int[] pageArr = new int[maxPage - minPage + 1];
		for(int i=0; minPage <= maxPage; i++, minPage++) {
			pageArr[i] = minPage;
		}
		return pageArr;
	}
	
	public Paginator() {
	}
	
	public Paginator(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}
	
	public void init(int totalRows) {
		this.totalRows = totalRows;
		pages = totalRows / rows + (totalRows % rows == 0 ? 0 : 1);
		
		if(page > pages) {
			page = pages;
		}
		if(page < 1)
		{
			page=1;
		}
		startIndex = (page - 1) * rows;
		endIndex = page * rows;
		
		hasNextPage = page < pages ? true : false;
		hasPrevPage = page > 1 ? true : false;
		hasOtherPage = hasNextPage || hasPrevPage;
		nextPage = hasNextPage ? page + 1 : page;
		prevPage = hasPrevPage ? page - 1 : page;
		currentPages = getNavPages(page, pages);
	}
	
	public String toString() {
		String info = "page:[" + page + "], totalRows:[" + totalRows + "], " +
				"pages:[" + pages + "], startIndex:[" + startIndex + "], " +
				"endIndex:[" + endIndex + "]";
		
		return info;
	}
	
	/**
	 * 当前页
	 */
	private int page=1;
	
	/**
	 * 每页显示的行数
	 */
	private int rows=10;
	
	/**
	 * 总共多少页
	 */
	private int pages;
	
	/**
	 * 总记录数
	 */
	private int totalRows;
	
	/**
	 * 是否还有下一页
	 */
	private boolean hasNextPage;
	
	/**
	 * 是否有上一页
	 */
	private boolean hasPrevPage;
	
	/**
	 * 是否有上一页或者下一页
	 */
	private boolean hasOtherPage;
	
	/**
	 * 记录起始值
	 */
	private int startIndex;
	
	/**
	 * 记录结束值
	 */
	private int endIndex;
	
	/**
	 * 下一页
	 */
	private int nextPage;
	
	/**
	 * 上一页
	 */
	private int prevPage;
	
	/**
	 * 当前页显示的页列表
	 */
	private int[] currentPages;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPrevPage() {
		return hasPrevPage;
	}

	public void setHasPrevPage(boolean hasPrevPage) {
		this.hasPrevPage = hasPrevPage;
	}

	public boolean isHasOtherPage() {
		return hasOtherPage;
	}

	public void setHasOtherPage(boolean hasOtherPage) {
		this.hasOtherPage = hasOtherPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int[] getCurrentPages() {
		return currentPages;
	}

	public void setCurrentPages(int[] currentPages) {
		this.currentPages = currentPages;
	}
	
}
