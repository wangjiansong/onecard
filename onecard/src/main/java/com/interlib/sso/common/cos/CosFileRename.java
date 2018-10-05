package com.interlib.sso.common.cos;

import java.io.File;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class CosFileRename implements FileRenamePolicy {

	//主表的ID字段作为文件在服务器上的新名字
	private String nameId;
	private String newName;
	private String oldName;
	
	//构造器初始化新名字ID
	public CosFileRename(String nameId) {
		this.nameId = nameId;
	}
	
	/**
	 * 重命名
	 */
	@Override
	public File rename(File file) {
		oldName = file.getName();
        String postfix="";
        int pot = oldName.lastIndexOf(".");
        
        if(pot!=-1){
        	postfix = oldName.substring(pot);
        }else{
        	postfix = "";
        }
        
        newName=nameId+postfix;
        file=new File(file.getParent(), newName);
        
        return file;
	}
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

}