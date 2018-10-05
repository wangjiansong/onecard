package com.interlib.sso.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.dao.NetReaderDao;
import com.interlib.sso.domain.NetReader;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderType;
import com.interlib.sso.service.NetReaderService;

@Service
public class NetReaderServiceImpl implements NetReaderService {
	
	@Autowired
	private NetReaderDao netreaderDao;
	
	private static final String[] CHECKSTATEARRAY = {"","待审批","审批通过","审批未通过"};
	
	public int checkReaderIdIsExist(String readerId) {
		int total = netreaderDao.getReaderNum(readerId) + netreaderDao.getNetReaderNum(readerId);
		return total;
	}
	
	public int insertNetReader(NetReader netreader) {
		return netreaderDao.insertNetReader(netreader);
	}
	
	public List<NetReader> queryNetReaderList(NetReader netreader) {
		List<NetReader> list = netreaderDao.queryNetReaderList(netreader);
		for(NetReader item : list){
			item.setReaderCardStateStr(ReaderServiceImpl.RDCFSTATEARRAY[item.getReaderCardState()]);
			item.setCheckStateStr(CHECKSTATEARRAY[item.getCheckState()]);
		}
		return list;
	}
	public List<NetReader> queryBatchNetReaderList(NetReader netreader) {
		List<NetReader> list = netreaderDao.queryBatchNetReaderList(netreader);
		for(NetReader item : list){
			item.setReaderCardStateStr(ReaderServiceImpl.RDCFSTATEARRAY[item.getReaderCardState()]);
			item.setCheckStateStr(CHECKSTATEARRAY[item.getCheckState()]);
		}
		return list;
	}
	
	public List<NetReader> queryBatchNetReaderListByReaderType(String rdtype,
			String readerHandleStartDate,String readerHandleEndDate){
		
		List<NetReader> list = netreaderDao.queryBatchNetReaderListByReaderType(rdtype,
				 readerHandleStartDate, readerHandleEndDate);
		return list;
	}
	
	
	public int approveReject(List<String> idList) {
		return netreaderDao.approveReject(idList);
	}
	
	public int approvePass(List<String> idList) {
		List<NetReader> netreaders = netreaderDao.getNetReaders(idList);
		int result = 0;
		List<String> netreaderIds = new ArrayList<String>();
		for(NetReader netreader : netreaders){
			if(netreader.getReaderMobile() == null){
				netreader.setReaderMobile("");
			}
			if(netreader.getReaderNative() == null){
				netreader.setReaderNative("");
			}
			if(netreader.getReaderAddress() == null){
				netreader.setReaderAddress("");
			}
			if(netreader.getReaderEmail() == null){
				netreader.setReaderEmail("");
			}
			if(netreader.getRemark() == null){
				netreader.setRemark("");
			}
			
			result = netreaderDao.approvePass(netreader);//插入读者表
			if(result > 0){
				netreaderIds.add(netreader.getReaderId());
			}
			netreaderDao.createAccount(netreader);//创建账户
		}
		result = netreaderDao.updateCheckPassed(netreaderIds);
		if(result == netreaderIds.size()) {
			result = deleteNetReader(netreaderIds);//审批通过就删掉
		}
		return result;
	}
	
	public NetReader getNetReader(String netreaderId) {
		NetReader netreader = netreaderDao.getNetReader(netreaderId);
		netreader.setCheckStateStr(CHECKSTATEARRAY[netreader.getCheckState()]);
		netreader.setReaderCardStateStr(ReaderServiceImpl.RDCFSTATEARRAY[netreader.getReaderCardState()]);
		netreader.setReaderHandleDateStr(TimeUtils.dateToString(netreader.getReaderHandleDate(), TimeUtils.YYYYMMDD));
		netreader.setReaderStartDateStr(TimeUtils.dateToString(netreader.getReaderStartDate(), TimeUtils.YYYYMMDD));
		netreader.setReaderEndDateStr(TimeUtils.dateToString(netreader.getReaderEndDate(), TimeUtils.YYYYMMDD));
		return netreader;
	}
	
	public NetReader getNetReaderByRdId(String netreaderId) {
		return netreaderDao.getNetReaderByRdId(netreaderId);
	}
	public int getBatchNetReader() {
		return netreaderDao.getBatchNetReader();
	}
	@Override
	public int deleteNetReader(List<String> idList) {
		return netreaderDao.deleteNetReader(idList);
	}
	
	/**
	 * 默认密码是des加密的
	 */
	@Override
	public NetReader netReaderLogin(NetReader netReader, boolean isPasswordMd5) {
		NetReader netReaderInDb = getNetReaderByRdId(netReader.getReaderId());
		if(netReaderInDb != null) {
			if(isPasswordMd5) {//如果reader传的密码是md5的
				//明文密码，把读者真实密码进行md5加密再比较
				String realPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, netReaderInDb.getReaderPassword());
				String rdpasswdMd5 = MD5Util.MD5Encode(realPassword, "");
				netReaderInDb.setReaderPassword(rdpasswdMd5);
			}
			
			if(netReader.getReaderPassword().equals(netReaderInDb.getReaderPassword())) {
				return netReaderInDb;
			}
		} 
		return null;
	}
	
}
