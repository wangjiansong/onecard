package com.interlib.sso.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.opac.webservice.Finance;
import com.interlib.opac.webservice.Loan;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.dao.ReaderCardInfoDAO;
import com.interlib.sso.dao.ReaderDAO;
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.UReader_Role;
import com.interlib.sso.service.ReaderService;


@Service
public class ReaderServiceImpl extends AbstractBaseServiceImpl<Reader, String> 
		implements ReaderService {
	@Autowired
	public ReaderDAO readerDao;
	
	@Autowired
	public ReaderCardInfoDAO readerCardInfoDAO;

	public void setReaderDao(ReaderDAO readerDao) {
		this.readerDao = readerDao;
	}

//	private static final String[] RDSORT1ARRAY   = {"","其他","物理","化学","中文","数学","历史","经济","管理","计算机"};
//	private static final String[] RDSORT2ARRAY   = {"","其他","公务员","科研人员","教师","文化工作者","工人","军人","义务工作者","公司职员","学生","个体劳动者"};
//	private static final String[] RDSORT3ARRAY   = {"","其他","科级","处级","局级","厅级"};
//	private static final String[] RDSORT4ARRAY   = {"","无","初级","中级","副高","正高"};
//	private static final String[] RDSORT5ARRAY   = {"","其他","高中","中专","大专","本科","研究生","博士生"};

	static final String[] RDCFSTATEARRAY = {"","有效","验证","丢失","暂停","注销","","","","","信用有效"};//读者证状态
	static final Map<String,String> RDLIBTYPEMAP = new HashMap<String,String>();//开户管
	static final Map<String,String> RDGLOBALLIBTYPEMAP = new HashMap<String,String>();//馆际类型
	
//	static final String[] RDSORT1ARRAY = {"","其他","物理","化学","中文","数学","历史","经济","管理","计算机"};//专业
//	static final String[] RDSORT2ARRAY = {"","其他","公务员","科研人员","教师","文化工作者","工人","军人","义务工作者","公司职员","学生","个体劳动者"};//职业
//	static final String[] RDSORT3ARRAY = {"","其他","科级","处级","局级","厅级"};//职务
//	static final String[] RDSORT4ARRAY = {"","无","初级","中级","副高","正高"};//职称
//	static final String[] RDSORT5ARRAY = {"","其他","高中","中专","大专","本科","研究生","博士生"};//文化
	
	public List<Map<String,String>> getReaderType() {
		return readerDao.getReaderType();
	}
	
	public List<Map<String,String>> getGlobalReaderType() {
		return readerDao.getGlobalReaderType();
	}
	
	public List<Map<String,String>> getThisGlobalReaderType(String libCode) {
		return readerDao.getThisGlobalReaderType(libCode);
	}
	
	public List<Map<String,String>> getLibCode() {
		return readerDao.getLibCode();
	}
	
	public int getValDate(String rdType) {
		return readerDao.getValDate(rdType);
	}
	
	public List<Map<String,String>> getLibReaderType(String libCode) {
		return readerDao.getLibReaderType(libCode);
	}
	
	public String checkRdIdExist(String rdId) {
		return readerDao.checkRdIdExist(rdId);
	}
	
	public Reader getAvatar(String rdId) {
		return readerDao.getAvatar(rdId);
	}
	
	public int addReader(Reader reader) {
		return readerDao.addReader(reader);
	}
	
	public List<Reader> queryReaderList(Reader reader) {
		List<Reader> list = readerDao.queryReaderList(reader);
		
		for(Reader item : list){
			if(item.getRdCFState() < 0) {
				item.setRdCFState((byte)1);
			}
			item.setRdStateStr(RDCFSTATEARRAY[item.getRdCFState()]);
		}
		return list;
	}
	
	public List<Reader> getReaderListByRdCertify(String rdCertify,String rdName){
		
		return readerDao.getReaderListByRdCertify(rdCertify, rdName);
	}
	public Reader getReaderByRdIdAndRdCertify(String rdId,String rdCertify){
		return readerDao.getReaderByRdIdAndRdCertify(rdId, rdCertify);
	}

	public List<Reader> getReaderListByRdLoginId(String rdLoginId){
		List<Reader> list = readerDao.getReaderListByRdLoginId(rdLoginId);
		for(Reader item : list){
//			if(item.getRdId() == null) {
//				item.setRdId();
//			}
			item.setRdId(item.getRdId());
		}
		return list;
	}

	
	public List<Reader> exportReaderData(Reader reader) {
		List<Reader> list = readerDao.exportReaderData(reader);
		if(RDLIBTYPEMAP.isEmpty() || RDGLOBALLIBTYPEMAP.isEmpty()){
			RDLIBTYPEMAP.clear();
			RDGLOBALLIBTYPEMAP.clear();
			List<Map<String,String>> readerTypeList = readerDao.getReaderType();
			for(Map<String,String> map : readerTypeList){
				RDLIBTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
			
			List<Map<String,String>> globalTypeList = readerDao.getGlobalReaderType();
			for(Map<String,String> map : globalTypeList){
				RDGLOBALLIBTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
		}
		for(Reader item : list){
			item.setRdType(RDLIBTYPEMAP.get(item.getRdType()));
			item.setRdLibType(RDGLOBALLIBTYPEMAP.get(item.getRdLibType()));
			item.setRdInTimeStr(TimeUtils.dateToString(item.getRdInTime(), TimeUtils.YYYYMMDD));
			item.setRdStartDateStr(TimeUtils.dateToString(item.getRdStartDate(), TimeUtils.YYYYMMDD));
			item.setRdEndDateStr(TimeUtils.dateToString(item.getRdEndDate(), TimeUtils.YYYYMMDD));
			item.setRdBornDateStr(TimeUtils.dateToString(item.getRdBornDate(), TimeUtils.YYYYMMDD));
			item.setRdStateStr(RDCFSTATEARRAY[item.getRdCFState()]);
//			item.setRdSort1(RDSORT1ARRAY[Byte.valueOf(item.getRdSort1())]);
//			item.setRdSort2(RDSORT1ARRAY[Byte.valueOf(item.getRdSort2())]);
//			item.setRdSort3(RDSORT1ARRAY[Byte.valueOf(item.getRdSort3())]);
//			item.setRdSort4(RDSORT1ARRAY[Byte.valueOf(item.getRdSort4())]);
//			item.setRdSort5(RDSORT1ARRAY[Byte.valueOf(item.getRdSort5())]);
		}
		return list;
	}
	
	public int deleteReader(String rdId) {
		return readerDao.deleteReader(rdId);
	}
	public Reader getByRdId(String rdId){
		return readerDao.getByRdId(rdId);
	}
	public Reader getReader(String rdId, byte type) {//type=1 查看    type=2 修改
		Reader reader = readerDao.getReader(rdId);
		if(type == 1 && reader != null){
			List<Map<String,String>> readerTypeList = readerDao.getReaderType();
			for(Map<String,String> map : readerTypeList){
				RDLIBTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
			
			List<Map<String,String>> globalTypeList = readerDao.getGlobalReaderType();
			for(Map<String,String> map : globalTypeList){
				RDGLOBALLIBTYPEMAP.put(map.get("READERTYPE"), map.get("DESCRIPE"));
			}
			
			reader.setRdStateStr(RDCFSTATEARRAY[reader.getRdCFState()]);
			reader.setRdType(RDLIBTYPEMAP.get(reader.getRdType()));
			reader.setRdLibType(RDGLOBALLIBTYPEMAP.get(reader.getRdLibType()));
			reader.setRdInTimeStr(TimeUtils.dateToString(reader.getRdInTime(), TimeUtils.YYYYMMDD));
			reader.setRdStartDateStr(TimeUtils.dateToString(reader.getRdStartDate(), TimeUtils.YYYYMMDD));
			reader.setRdEndDateStr(TimeUtils.dateToString(reader.getRdEndDate(), TimeUtils.YYYYMMDD));
			reader.setRdBornDateStr(TimeUtils.dateToString(reader.getRdBornDate(), TimeUtils.YYYYMMDD));
//			reader.setRdSort1(RDSORT1ARRAY[Byte.valueOf(reader.getRdSort1())]);
//			reader.setRdSort2(RDSORT2ARRAY[Byte.valueOf(reader.getRdSort2())]);
//			reader.setRdSort3(RDSORT3ARRAY[Byte.valueOf(reader.getRdSort3())]);
//			reader.setRdSort4(RDSORT4ARRAY[Byte.valueOf(reader.getRdSort4())]);
//			reader.setRdSort5(RDSORT5ARRAY[Byte.valueOf(reader.getRdSort5())]);
		}else{
			if(reader != null){
				reader.setRdInTimeStr(TimeUtils.dateToString(reader.getRdInTime(), TimeUtils.YYYYMMDD));
				reader.setRdStartDateStr(TimeUtils.dateToString(reader.getRdStartDate(), TimeUtils.YYYYMMDD));
				reader.setRdEndDateStr(TimeUtils.dateToString(reader.getRdEndDate(), TimeUtils.YYYYMMDD));
				reader.setRdBornDateStr(TimeUtils.dateToString(reader.getRdBornDate(), TimeUtils.YYYYMMDD));
			}
		}
		return reader;
	}
	
	public Reader getReaderByRdCertify(String rdId){
		return readerDao.getReaderByRdCertify(rdId);
	}
	
	public Reader getReaderByRdLoginId(String rdId){
		return readerDao.getReaderByRdLoginId(rdId);
	}
	
	public int updateReaderAvatar(Map<String, Object> map) {
		return readerDao.updateReaderAvatar(map);
	}
	
	public String getRealPassword(String rdId) {
		return readerDao.getRealPassword(rdId);
	}
	
	public int updatePassword(String rdId, String newPassword) {
		return readerDao.updatePassword(rdId, newPassword);
	}
	
	public int updateIsAuthor(String rdId, Integer isAuthor){
		return readerDao.updateIsAuthor(rdId, isAuthor);	
	}
	public int updateReader(Reader reader) {
		//卡处理
		//下面的操作是为了给桂林旅专学院更新专门处理的，最新的版本在controller处理了，但因为版本更新问题，
		//先在下面处理，方便更新 20161011
		ReaderCardInfo readerCardInfo = readerCardInfoDAO.getByRdId(reader.getRdId());
		if(readerCardInfo != null) {
			readerCardInfo.setIsUsable(1);
			readerCardInfoDAO.update(readerCardInfo);
		}
		return readerDao.updateReader(reader);
	}
	
	public void updatePhotoName(Reader reader) {
		readerDao.updatePhotoName(reader);
	}
	
	public String getPhotoName(String rdId) {
		return readerDao.getPhotoName(rdId);
	}

	/**
	 * 默认密码是des加密的
	 */
	public Reader readerLogin(Reader reader, boolean isPasswordMd5) {
		Reader realReader = getReader(reader.getRdId(), (byte)2);
		if(realReader != null) {
			if(isPasswordMd5) {//如果reader传的密码是md5的
				//明文密码，把读者真实密码进行md5加密再比较
				String realPassword = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, realReader.getRdPasswd());
				String rdpasswdMd5 = MD5Util.MD5Encode(realPassword, "");
				realReader.setRdPasswd(rdpasswdMd5);
			}
			
			if(reader.getRdPasswd().equals(realReader.getRdPasswd())) {
				return realReader;
			}
		} 
		return null;
	}

	@Override
	public List<Roles> getReaderRolesByRdId(String rdId) {
		return readerDao.getReaderRolesByRdId(rdId);
	}

	@Override
	public List<Roles> getOtherRolesByRdId(String rdId) {
		return readerDao.getOtherRolesByRdId(rdId);
	}

	@Override
	public void deleteReaderRolesByRdId(String rdId) {
		readerDao.deleteReaderRolesByRdId(rdId);
	}

	@Override
	public void saveReaderRoles(UReader_Role readerRole) {
		readerDao.saveReaderRoles(readerRole);
	}
	
	public List<Reader> searchReader(Map<String,String> params) {
		return readerDao.searchReader(params);
	}

	@Override
	public int updateLibUser(String rdId, Integer isLibUser) {
		return readerDao.updateLibUser(rdId, isLibUser);
	}

	@Override
	public List<Reader> getAllLlbUser(Integer isLibUser) {
		return readerDao.getAllLlbUser(isLibUser);
	}

	@Override
	public void addReaderAsOperator(Reader reader) {
		readerDao.addReaderAsOperator(reader);
	}

	@Override
	public void updateReaderAsOperator(Map<String, Reader> map) {
		readerDao.updateReaderAsOperator(map);
	}
	
	public void addDeposit(String rdId, double deposit) {
		readerDao.addDeposit(rdId, deposit);
	}
	
	public void updateDeposit(String rdId, double deposit) {
		readerDao.updateDeposit(rdId, deposit);
	}
	
	public List<CardGroup> getCardGroups() {
		return readerDao.getCardGroups();
	}
	
	public void setReaderGroup(String rdId, int groupId) {
		readerDao.setReaderGroup(rdId, groupId);
	}
	
	public int getReaderGroupId(String rdId) {
		return readerDao.getReaderGroupId(rdId);
	}
	
	public void deleteReaderBelongGroup(String rdId) {
		readerDao.deleteReaderBelongGroup(rdId);
	}
	
	public CardGroup getReaderGroupBelong(String rdId) {
		return readerDao.getReaderGroupBelong(rdId);
	}

	@Override
	public int updateOldPasswordAndSynStatus(String rdId, String newPasswd,
			int synStatus) {
		return readerDao.updateOldPasswordAndSynStatus(rdId, newPasswd,synStatus);
	}

	@Override
	public int updateSynStatus(String rdId, int synStatus) {
		
		return readerDao.updateSynStatus(rdId, synStatus);
	}

	@Override
	public List<Loan> getCurrentLoanList(Reader reader, LibCode lib,
			boolean doPage, Integer pageSize, Integer toPage) {
		return readerDao.getCurrentLoanList(reader, lib, doPage, pageSize, toPage);
	}


	@Override
	public List<Reader> readerUnSynList(Reader reader) {
		List<Reader> list = readerDao.readerUnSynList(reader);
		for(Reader item : list){
			item.setRdStateStr(RDCFSTATEARRAY[item.getRdCFState()]);
		}
		return list;
	}
	
	@Override
	public List<Finance> getReaderFinance(Reader reader, LibCode lib, 
			String paysign,boolean doPage,int toPage,int pageSize) {
		return readerDao.getReaderFinance(reader, lib, paysign, doPage, toPage, pageSize);
	}
	
	@Override
	public int updateLibAssign(Reader r) {
		
		return readerDao.updateLibAssign(r);
	}

	@Override
	public String getReaderWebServiceUrl(String rdid) {
		
		return readerDao.getReaderWebServiceUrl(rdid);
	}
	
	@Override
	public int checkRdCertifyExist(String rdCertify) {
		return readerDao.checkRdCertifyExist(rdCertify);
	}

	@Override
	public int checkReaderActivate() {
		// TODO Auto-generated method stub
		return readerDao.checkReaderActivate();
	}
}
