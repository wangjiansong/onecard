package com.interlib.sso.common.file;

import java.io.File;
import java.util.Properties;

import com.interlib.sso.common.servlet.ConfigInit;

/**
 * 系统附件上传路径
 * @author `still
 *
 */
public class FileConfig {
	
	private String uploadPath;//附件根目录

	private int maxFileSize;//附件大小控制
	
	/**
	 * 构造器初始化根目录
	 * @param pageContext
	 */
	public FileConfig(String filePath) {
		init(filePath);
	}
	
	/**
	 * 初始化服务器上传路径及文件大小控制
	 */
	private void init(String filePath) {
		Properties props = ConfigInit.prop;
		
		uploadPath = props.getProperty("uploadPath")+filePath;
		maxFileSize = Integer.valueOf(props.getProperty("maxFileSize"));
		
		File file = new File(uploadPath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public int getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
}
