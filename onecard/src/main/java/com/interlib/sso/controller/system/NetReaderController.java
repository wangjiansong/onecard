package com.interlib.sso.controller.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.ResponseUtil;
import com.interlib.sso.common.ServletUtils;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.common.json.Jackson;
import com.interlib.sso.common.servlet.ServletUtil;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.service.NetReaderService;
import com.interlib.sso.service.RdAccountService;
import com.interlib.sso.service.ReaderService;

@Controller
@RequestMapping("admin/netreader")
public class NetReaderController {
	
	@Autowired
	private ReaderService readerService;
	@Autowired
	private NetReaderService netreaderService;
	@Autowired
	private RdAccountService rdAccountService;
	
	private static final Logger logger = Logger.getLogger(NetReaderController.class);
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**********************网络注册**********************/
	/**
	 * 注册页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/regist")
	public ModelAndView regist(Model model) {
		model.addAttribute("readertypes", Jackson.getBaseJsonData(readerService.getReaderType()));
		model.addAttribute("libcodes", Jackson.getBaseJsonData(readerService.getLibCode()));
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "regist_index");
		return new ModelAndView("admin/netreader/regist");
	}
	
	/**
	 * 检查读者证号是否已存在或已经被注册
	 * @param response
	 * @param readerId
	 */
	@RequestMapping(value = "/checkReaderIdIsExist")
	public void checkReaderIdIsExist(HttpServletResponse response, String readerId) {
		ServletUtil.responseOut("GBK", String.valueOf(netreaderService.checkReaderIdIsExist(readerId)), response);
	}
	
	/**
	 * 注册读者
	 * @param response
	 * @param netreader
	 */
	@RequestMapping(value = "/registReader")
	public void registReader(HttpServletResponse response, NetReader netreader) {
		ServletUtil.responseOut("GBK", String.valueOf(netreaderService.insertNetReader(netreader)), response);
	}
	
	
	/**********************注册审批**********************/
	/**
	 * 读者审批列表
	 * @param model
	 * @param netreader
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/approve")
	public ModelAndView approve(Model model, NetReader netreader) {
		List<NetReader> list = netreaderService.queryNetReaderList(netreader);
		model.addAttribute("list", list);
		model.addAttribute("obj", netreader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_approve");
		return new ModelAndView("admin/netreader/approve");
	}
	/**
	 * 获取最新一次上传的读者信息
	 * @param model
	 * @param response
	 * @param netReader
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/showImportReaderList")
	public ModelAndView showImportReaderList(Model model, HttpServletResponse response, NetReader netReader) {
		List<NetReader> list = netreaderService.queryNetReaderList(netReader);
		model.addAttribute("list", list);
		model.addAttribute("obj", netReader);
		model.addAttribute(Constants.ACTIVE_MENU_KEY, "reader_approve");
		return new ModelAndView("admin/netreader/approve");
	}
	/**
	 * 审批不通过
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/approveReject")
	public void approveReject(HttpServletResponse response, String ids) {
		String[] idsArr = ids.split(",");
		List<String> idList = new ArrayList<String>();
		int size = idsArr.length;
		for(int i=0; i<size; i++){
			idList.add(idsArr[i]);
		}
		ServletUtil.responseOut("GBK", String.valueOf(netreaderService.approveReject(idList)), response);
	}
	
	/**
	 * 审批通过
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/approvePass")
	public void approvePass(HttpServletResponse response, String ids) {
		String[] idsArr = ids.split(",");
		List<String> idList = new ArrayList<String>();
		int size = idsArr.length;
		for(int i=0; i<size; i++){
			idList.add(idsArr[i]);
		}
		ServletUtil.responseOut("GBK", String.valueOf(netreaderService.approvePass(idList)), response);
	}
	
	/**
	 * 删除记录
	 * @param response
	 * @param ids
	 */
	@RequiresRoles("admin")
	@RequestMapping(value="/deleteNetReader")
	public void deleteNetReader(HttpServletResponse response, String ids) {
		String[] idsArr = ids.split(",");
		List<String> idList = new ArrayList<String>();
		int size = idsArr.length;
		for(int i=0; i<size; i++){
			idList.add(idsArr[i]);
		}		
		ServletUtil.responseOut("UTF-8", String.valueOf(netreaderService.deleteNetReader(idList)), response);
	}
	
	/**
	 * 查看网络读者注册详情
	 * @param model
	 * @param netreaderId
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/detailNetReader/{netreaderId}")
	public ModelAndView detailNetReader(Model model, @PathVariable String netreaderId) {
		NetReader netreader = netreaderService.getNetReader(netreaderId);
		model.addAttribute("netreader", netreader);
		return new ModelAndView("admin/netreader/netreaderDetail");
	}
	@RequiresRoles("admin")
	@RequestMapping("/uploadFile")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
		final long MAX_SIZE = 20 * 1024 * 1024;// 可以上传的文件最大限度
		final String[] allowedExt = { "xls" };// 这里之所以用数组,是因为,可以提供更多的机会,你可以直接不用这里,直接判断是否是这个格式.
		// 设置字符编码,根据需要设置,
		// responseponse.setCharacterEncoding("");
		// 这一句必须要,否则就会出现问题,requestuest得不到数据.一直是null.
		response.setContentType("text/html");
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(32 * 1024);// 设置上传文件时用于临时存放文件的内存大小,这里是512K.多于的部分将临时存在硬盘
		//先判断该路径有没有对应临时的文件夹，没有则创建对应的目录 2014-11-12
		File tempFile = new File(request.getRealPath("/") + "UploadTemp");  
		if (!tempFile.exists()) {  
			tempFile.mkdirs();// 目录不存在的情况下，创建目录。  
		}  
		factory.setRepository(tempFile);//(new File(request.getRealPath("/") + "UploadTemp"));// 设置存放临时文件的目录,web根目录下的UploadTemp目录
		
		// 用以上工厂实例化上传组件
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(MAX_SIZE);// 设置最大上传尺寸
		String[] json = {"{fail:false,info:'读者信息导入失败'}","{success:true,info:'读者信息导入成功'}"};
		try {
			System.out.println("从request得到 所有 上传域的列表 ");
			// System.out.println("System.out.println(sfu.parseRequest(request)):"+sfu.parseRequest(request));
			List fileList = upload.parseRequest(request);
			System.out.println(fileList);
			if (fileList == null || fileList.size() == 0) {
				ResponseUtil.sendOneJsonRs(response, false, "导入文件不符!请检查");
			} else {
				Iterator fileIterator = fileList.iterator();// 得到所有上传的文件
				while (fileIterator.hasNext()) {// 循环处理所有文件
					FileItem fileItem = (FileItem) fileIterator.next();// 得到文件.
					if (fileItem == null || fileItem.isFormField()) {
						continue;// 忽略简单form字段而不是上传域的文件域(<input
									// type="text" />等)其实这里不可能,
					}
					String path = fileItem.getName();// 得到完整的上传路劲
					System.out.println(path);
					long size = fileItem.getSize();// 得到文件的大小.
					if (path.equals("") || size == 0) {
						ResponseUtil.sendOneJsonRs(response, false, "导入文件内容不符!请检查");
					} else {
						String t_name = path.substring(path
								.lastIndexOf("\\") + 1);// 得到去除路径的文件名
						System.out.println("t_name:" + t_name);
						String t_ext = t_name.substring(t_name
								.lastIndexOf(".") + 1);// 得到文件的扩展名(无扩展名时将得到全名)
						System.out.println("t_ext:" + t_ext);
						// 拒绝接受规定文件格式之外的文件类型
						boolean isFileOk = false;
						for (int allowFlag = 0; allowFlag < allowedExt.length; allowFlag++) {
							if (allowedExt[allowFlag].equals(t_ext)) {
								isFileOk = true;
								break;
							}
						}
						if(isFileOk) {
							// 根据系统时间生成上传后保存的文件名
							String prefix = String.valueOf(System
									.currentTimeMillis());
							// 保存的最终文件完整路径,保存在工程项目根目录下的Uploaded目录下
							String u_name = request.getRealPath("/") + "Uploaded/"
									+ prefix + "." + t_ext;
							System.out.println("u_name:" + u_name);
							// 开始保存文件了.
							File uploadFile = new File(u_name);
							System.out.println("uploadFile1:" + uploadFile);
							// uploadFile.createNewFile();
							fileItem.write(uploadFile);
							// fileItem.write(uploadFile);
							System.out.println("uploadFile2:" + uploadFile);
							//保存到临时读者表
							Map<?, ?> mAp = saveReaderFromWorkbook(uploadFile,request,response);
							System.out.println("mAp："+mAp);
						} else {
							ResponseUtil.sendOneJsonRs(response, false, "导入文件格式不符!请检查");
						}
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			ResponseUtil.sendOneJsonRs(response, false, "导入读者表失败");
		}
		ServletUtils.printHTML(response, json [1]);
	}
	
	/**
	 * 下载模板
	 * @param request
	 * @param response
	 * @param reader
	 */
	@RequiresRoles("admin")
	@RequestMapping(value = "/exportDemoExcel")
	public synchronized void exportReaderExcel(HttpServletRequest request, HttpServletResponse response, Reader reader) {
		
		String inFilePath = request.getSession().getServletContext().getRealPath("/demo/READER.xls");
		String filename = "READER.xls";
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel; charset=GBK");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO8859-1"));
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(inFilePath);
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("【读者管理】未读取到模板文件！", e);
		} catch (IOException e) {
			logger.error("【读者管理】读取模板文件时出现IO流异常！", e);
		} finally {
			if(inputStream != null || outputStream != null){
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					logger.error("【读者管理】导出模板文件关闭输出流时异常！", e);
				}
			}
		}
	}
	
	/**
	 * xls格式
	 * 读者证号 读者密码（MD5）读者姓名 住址 身份证号 出生日期 读者类型 手机号码 所属分管 读者证开始日期 读者证结束日期 办证日期 读者证状态（有效，暂停）性别（男，女）
	 * @param uploadFile
	 * @return
	 */
	public Map<?, ?> saveReaderFromWorkbook(File uploadFile,HttpServletRequest request, HttpServletResponse response) {
		
		NetReader reader = null;
		List<NetReader> list = new ArrayList();
		Map<String, NetReader>  map = new HashMap<String, NetReader>();
		Map<String, String>  mp = new HashMap<String, String>();
		String[] json = {"{fail:false,info:'读者信息导入失败'}","{success:true,info:'读者信息导入成功'}"};
		jxl.Workbook wb = null;		
		try {// 构建Workbook对象, 只读Workbook对象
				// //直接从本地文件创建Workbook
			// 从文件创建Workbook
			wb = jxl.Workbook.getWorkbook(uploadFile);// jxl只支持97到2003版的excel
		
			System.out.println("uploadFile3:" + uploadFile);
			
			Sheet sheet = wb.getSheet(0);// 这里就不用数组了,因为只是第一页写数据就够了.
			int rowNum = sheet.getRows();
			System.out.println(rowNum);
			Date now = new Date();
			for (int iRow = 1; iRow < rowNum; iRow++) {
				Cell[] cells = sheet.getRow(iRow);
				//2014-11-12写入审批模板之前，先过滤一遍，该账号的读者是否已经存在reader表，存在中午跳过此循环
				String rdId = cells[0].getContents();
				Reader r = readerService.getReader(rdId, (byte) 2);
				if(r != null) continue;//跳出本次循环
				if (cells != null || cells.length > 0) {
					/** 这里注意,自己写在Excel里面的数据不要出错,因为id是主键,当然,你可以不设置主键,字符长度设为"无限",要不然就自己写判断语句.冲突的进行处理. */
					reader=new NetReader();
					reader.setReaderId(cells[0].getContents());
					String rdpasswd = cells[1].getContents();
					//密码要处理成加密的
					if(!rdpasswd.startsWith("^") && !rdpasswd.endsWith("^")) {
						rdpasswd = EncryptDecryptData.encryptWithCode(Constants.DES_STATIC_KEY, rdpasswd);
					}
					reader.setReaderPassword(rdpasswd);
					reader.setReaderName(cells[2].getContents());
					reader.setReaderAddress(cells[3].getContents());
					reader.setReaderCertify(cells[4].getContents());// 读者的 身份证
					if(cells[5].getType() == CellType.DATE){// 出生日期 2014-11-12
						DateCell datec11 = (DateCell) cells[5];
						Date date = datec11.getDate();
						reader.setReaderBornDate(date);
					}else {
						String date = cells[5].getContents();
						if(!StringUtils.trimToEmpty(date).equals("")) {
							reader.setReaderBornDate(TimeUtils.stringToDate(date,
									"yyyy-MM-dd"));
						}
					}
					reader.setReaderType(cells[6].getContents());// 读者类型
					reader.setReaderMobile(cells[7].getContents());// 手机号码
					reader.setReaderLib(cells[8].getContents());// 所属的分馆
					if (cells[9].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[9];
						Date date = datec11.getDate();
						reader.setReaderStartDate(date);
					} else {
						String date = cells[9].getContents();
						reader.setReaderStartDate(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					if (cells[10].getType() == CellType.DATE) {
						DateCell datec11 = (DateCell) cells[10];
						Date date = datec11.getDate();
						reader.setReaderEndDate(date);
					} else {
						String date = cells[10].getContents();
						reader.setReaderEndDate(TimeUtils.stringToDate(date,
								"yyyy-MM-dd"));
					}
					
					reader.setReaderHandleDate(now);
					
					String status = cells[12].getContents();
					if (status.equals("有效")) {
						reader.setReaderCardState((byte) 1);
					} else if (status.equals("暂停")) {
						reader.setReaderCardState((byte) 4);

					} else if (status.equals("注销")) {
						reader.setReaderCardState((byte) 5);

					} else if (status.equals("验证")) {
						reader.setReaderCardState((byte) 2);

					} else if (status.equals("挂失")) {
						reader.setReaderCardState((byte) 3);
					}
					int sex = 1;
					if ("女".equals(cells[13].getContents())) {
						sex = 0;
					}
					reader.setReaderGender((byte) sex);
					reader.setReaderUnit(cells[14].getContents());
					reader.setReaderSort5(cells[15].getContents());
					reader.setReaderSort2(cells[16].getContents());

					netreaderService.insertNetReader(reader);
					list.add(reader);
				}
			}
			for(NetReader netReader : list) {
				map.put("netReader", netReader);
			}
			return map;
		} catch (Exception e) {
			logger.error("saveReaderFromWorkbook出现异常！", e);
			System.out.println("我捕获到了异常："+e.getMessage());
			ServletUtils.printHTML(response, json [0]);
			mp.put("e", e.getMessage());
			return mp;
		}
	}
}
