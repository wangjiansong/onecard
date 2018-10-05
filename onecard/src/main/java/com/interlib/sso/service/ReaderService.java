package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.opac.webservice.Finance;
import com.interlib.opac.webservice.Loan;
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.UReader_Role;

public interface ReaderService extends BaseService<Reader, String>{
	
	public List<Map<String,String>> getReaderType();
	
	public List<Map<String,String>> getGlobalReaderType();
	
	public List<Map<String,String>> getThisGlobalReaderType(String libCode);
	
	public List<Map<String,String>> getLibCode();
	
	public List<Map<String,String>> getLibReaderType(String libCode);
	
	public int getValDate(String rdType);
	
	public String checkRdIdExist(String rdId);
	
	public int checkRdCertifyExist(String rdCertify);
	/**
	 * 读者激活数
	 * @return
	 */
	public int checkReaderActivate();

	
	public Reader getAvatar(String rdId);
	
	public int addReader(Reader reader);
	
	public Reader readerLogin(Reader reader, boolean isPasswordMd5);
	
	public List<Reader> queryReaderList(Reader reader);
	
	public List<Reader> exportReaderData(Reader reader);
	
	public int deleteReader(String rdId);
	
	public Reader getReader(String rdId, byte type);
	
	public Reader getReaderByRdCertify(String rdId);
	
	public Reader getReaderByRdLoginId(String rdId);
	
	public List<Reader> getReaderListByRdLoginId(String rdLoginId);
	
	public List<Reader> getReaderListByRdCertify(String rdCertify,String rdName);
	
	public Reader getReaderByRdIdAndRdCertify(String rdId,String rdCertify);
	
	public Reader getByRdId(String rdId);
	
	public int updateReaderAvatar(Map<String, Object> map);
	
	public String getRealPassword(String rdId);
	
	public int updatePassword(String rdId, String newPassword);
	
	public int updateIsAuthor(String rdId, Integer isAuthor);
	
	public int updateLibUser(String rdId, Integer isLibUser);
	
	public int updateReader(Reader reader);
	
	public void updatePhotoName(Reader reader);
	
	public String getPhotoName(String rdId);
	
	public List<Roles> getReaderRolesByRdId(String rdId);
	
	public List<Roles> getOtherRolesByRdId(String rdId);
	
	public void deleteReaderRolesByRdId(String rdId);
	
	public void saveReaderRoles(UReader_Role readerRole);
	
	public List<Reader> searchReader(Map<String,String> params);
	
	public List<Reader> getAllLlbUser(Integer isLibUser);
	
	public void addReaderAsOperator(Reader reader);
	
	public void updateReaderAsOperator(Map<String, Reader>map);
	
	public void addDeposit(String rdId, double deposit);
	
	public void updateDeposit(String rdId, double deposit);
	
	public List<CardGroup> getCardGroups();
	
	public void setReaderGroup(String rdId, int groupId);
	
	public int getReaderGroupId(String rdId);
	
	public void deleteReaderBelongGroup(String rdId);
	
	public CardGroup getReaderGroupBelong(String rdId);

	public int updateOldPasswordAndSynStatus(String rdId, String newPasswd,int synStatus);

	public int updateSynStatus(String rdId, int synStatus);

	public List<Reader> readerUnSynList(Reader reader);//ADD 2014-05-26
	
	/**
	 * 读者当前借阅列表
	 * @param reader
	 * @param lib
	 * @param doPage
	 * @param pageSize
	 * @param toPage
	 * @return
	 */
	public List<Loan> getCurrentLoanList(Reader reader, LibCode lib, 
			boolean doPage, Integer pageSize, Integer toPage);
	
	/**
	 * 财经列表
	 * @param reader
	 * @param lib
	 * @param paysign
	 * @param doPage
	 * @param toPage
	 * @param pageSize
	 * @return
	 */
	public List<Finance> getReaderFinance(Reader reader, LibCode lib, 
			String paysign,boolean doPage,int toPage,int pageSize);
			
	public int updateLibAssign(Reader r);//ADD 2014-06-19

	public String getReaderWebServiceUrl(String rdid);
}
