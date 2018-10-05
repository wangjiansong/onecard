package com.interlib.sso.page;

import java.io.Serializable;
import java.util.List;

public class Pager implements Serializable {
	
	private static final long serialVersionUID = 802577606543527677L;

	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	private Integer pageNumber = 1;// 当前页码
	private Integer pageSize = 20;// 每页记录数
	private Integer totalCount = 0;// 总记录数
	private Integer pageCount = 0;// 总页数
	
	public Pager() {}
	
	public Pager(int pageNumber, int pageSize) {
		this();
		setPageNumber(pageNumber);
		setPageSize(pageSize);
	}
	
	public Integer getPrevPage() {
		return (pageNumber - 1) < 1 ? 1: (pageNumber - 1);
	}
	public Integer getNextPage() {
		return (pageNumber + 1) > getPageCount() ? 
				getPageCount() : (pageNumber + 1);
	}

	/**
	 * 返回一个数组,分页的页码数组
	 * @param page 当前页
	 * @param pages 总页数
	 * @return
	 */
	public Integer[] getNavPages() {
		int page = pageNumber;
		int pages = getPageCount();
		/**
		 * 如果page=[1..7]
		 * 显示: 1 2 3 4 5 6 7 8 9 10
		 * 如果page=[8..]
		 * 显示: 3 4 5 6 7 8 9 10 11 12
		 * 
		 * 1 2 3 4 [5] 6 7 8 9 10, 11 12 13 14 15 16 17 18 19 20
		 * 
		 */
		int minPage = page - 5;
		minPage = minPage < 1 ? 1 : minPage;
//		System.out.println("minPage: " + minPage);
		int maxPage = page + 5;
		//如果当前页是在前5页范围内,后面的页码应该补足5位
		if(page <= 5) {
			maxPage = maxPage + (5 - page);
		}
		maxPage = maxPage > pages ? pages : maxPage;
		if((maxPage - page) <= 5) {
			minPage = minPage - (5 - (maxPage - page));
		}
		minPage = minPage < 1 ? 1 : minPage;
		
//		System.out.println("maxPage: " + maxPage);
		Integer[] pageArr = new Integer[maxPage - minPage + 1];
		for(int i=0; minPage <= maxPage; i++, minPage++) {
			pageArr[i] = minPage;
		}
//		for(int i : pageArr) {
//			if(page == i) {
//				System.out.print("[" + i + "] ");
//			} else {
//				System.out.print(i + " ");
//			}
//		}
//		System.out.println();
		return pageArr;
	}
	

	@SuppressWarnings("rawtypes")
	private List list;// 数据List

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		}
		/**
		else if(pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		*/
		this.pageSize = pageSize;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount ++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

}
