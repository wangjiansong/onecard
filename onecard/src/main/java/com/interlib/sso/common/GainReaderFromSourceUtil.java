package com.interlib.sso.common;

import java.io.File;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;

import com.interlib.sso.domain.Reader;

public class GainReaderFromSourceUtil {
	
	/**
	 * xls格式
	 * 读者证号 读者密码（MD5）读者姓名 住址 身份证号 出生日期（yyyy-MM-dd）读者类型 手机号码 所属分管 读者证开始日期 读者证结束日期 办证日期 读者证状态（有效，暂停）性别（男，女）
	 * @param uploadFile
	 * @return
	 */
	public static void gainReaderFromWorkbook(File uploadFile) {
		Reader reader = new Reader();
		
		jxl.Workbook wb = null;
		try {// 构建Workbook对象, 只读Workbook对象
				// //直接从本地文件创建Workbook
			// 从文件创建Workbook
			wb = jxl.Workbook.getWorkbook(uploadFile);// jxl只支持97到2003版的excel
		
		System.out.println("uploadFile3:" + uploadFile);
		
		Sheet sheet = wb.getSheet(0);// 这里就不用数组了,因为只是第一页写数据就够了.
		int rowNum = sheet.getRows();
		System.out.println(rowNum);
		for (int iRow = 0; iRow < rowNum; iRow++) {
			Cell[] cells = sheet.getRow(iRow);
			if (cells != null || cells.length > 0) {
				/** 这里注意,自己写在Excel里面的数据不要出错,因为id是主键,当然,你可以不设置主键,字符长度设为"无限",要不然就自己写判断语句.冲突的进行处理. */
				reader=new Reader();
				reader.setRdId(cells[0].getContents());
				reader.setRdPasswd(cells[1].getContents());
				reader.setRdName(cells[2].getContents());
				reader.setRdAddress(cells[3].getContents());
					reader.setRdCertify(cells[4].getContents());// 读者的 身份证
					if (cells[5].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[5];
						Date date = datec11.getDate();

						reader.setRdBornDate(date);// 时间需要转换
					} else {
						String date = cells[5].getContents();
						reader.setRdBornDate(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					reader.setRdType(cells[6].getContents());// 读者类型
					reader.setRdPhone(cells[7].getContents());// 手机号码
					reader.setRdLib(cells[7].getContents());// 所属的分馆
					if (cells[8].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[8];
						Date date = datec11.getDate();
						reader.setRdStartDate(date);
					} else {
						String date = cells[8].getContents();
						reader.setRdStartDate(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					if (cells[9].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[9];
						Date date = datec11.getDate();
						reader.setRdEndDate(date);
					} else {
						String date = cells[9].getContents();
						reader.setRdEndDate(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					if (cells[10].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[10];
						Date date = datec11.getDate();
						reader.setRdInTime(date);
					} else {
						String date = cells[10].getContents();
						reader.setRdInTime(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					String status = cells[11].getContents();
					if (status.equals("有效")) {
						reader.setRdCFState((byte) 1);
					} else if (status.equals("暂停")) {
						reader.setRdCFState((byte) 4);

					} else if (status.equals("注销")) {
						reader.setRdCFState((byte) 5);

					} else if (status.equals("验证")) {
						reader.setRdCFState((byte) 2);

					} else if (status.equals("挂失")) {
						reader.setRdCFState((byte) 3);

					}
					int sex = 1;
					if ("女".equals(cells[12].getContents())) {
						sex = 0;
					}
					reader.setRdSex((byte) sex);

					if (iRow == rowNum - 1) {
						return;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
