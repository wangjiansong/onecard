package com.interlib.sso.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interlib.opac.webservice.Finance;
import com.interlib.opac.webservice.FinanceWebservice;
import com.interlib.opac.webservice.Loan;
import com.interlib.opac.webservice.LoanWebservice;
import com.interlib.sso.common.Constants;
import com.interlib.sso.common.EncryptDecryptData;
import com.interlib.sso.common.MD5Util;
import com.interlib.sso.dao.ReaderDAO;
import com.interlib.sso.domain.CardGroup;
import com.interlib.sso.domain.LibCode;
import com.interlib.sso.domain.Reader;
import com.interlib.sso.domain.ReaderCardInfo;
import com.interlib.sso.domain.Roles;
import com.interlib.sso.domain.UReader_Role;

@SuppressWarnings("unchecked")
@Repository
public class ReaderDAOImpl 
		extends AbstractMybatisBaseDAO<Reader, String> implements ReaderDAO {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public SqlSession sqlSession;
	

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Override
	public String getMybatisMapperNamespace() {
		return "ReaderMapper";
	}
	
	public List<Map<String,String>> getReaderType() {
		return sqlSession.selectList(getMybatisMapperNamespace()+".getReaderType");
	}
	
	public List<Map<String,String>> getGlobalReaderType() {
		return sqlSession.selectList(getMybatisMapperNamespace()+".getGlobalReaderType");
	}
	
	public List<Map<String,String>> getThisGlobalReaderType(String libCode) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("libCode", libCode);
		return sqlSession.selectList(getMybatisMapperNamespace()+".getThisGlobalReaderType", param);
	}
	
	public List<Map<String,String>> getLibCode() {
		return sqlSession.selectList(getMybatisMapperNamespace()+".getLibCode");
	}
	
	public List<Map<String,String>> getLibReaderType(String libCode) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("libCode", libCode);
		return sqlSession.selectList(getMybatisMapperNamespace()+".getLibReaderType", param);
	}
	
	public int getValDate(String rdType) {
		int days = Integer.valueOf(sqlSession.selectOne(getMybatisMapperNamespace()+".getValDate", rdType).toString());
		return days;
	}
	
	public String checkRdIdExist(String rdId) {
		return sqlSession.selectOne(getMybatisMapperNamespace()+".checkRdIdExist", rdId).toString();
	}
	
	public int checkRdCertifyExist(String rdCertify) {
		return (Integer)sqlSession.selectOne(getMybatisMapperNamespace() + ".existByRdCertify", rdCertify);
	}
	
	public Reader getAvatar(String rdId) {
		return (Reader) sqlSession.selectOne(getMybatisMapperNamespace()+".getAvatar", rdId);
	}
	
	public int addReader(Reader reader) {
		return sqlSession.insert(getMybatisMapperNamespace()+".addReader", reader);
	}
	
	public List<Reader> queryReaderList(Reader reader) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".queryReaderList", reader);
	}
	
	public List<Reader> exportReaderData(Reader reader) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".exportReaderData", reader);
	}
	
	public int deleteReader(String rdId) {
		return sqlSession.update(getMybatisMapperNamespace()+".deleteReader", rdId);
	}
	
	public Reader getReader(String rdId) {
		return (Reader) sqlSession.selectOne(getMybatisMapperNamespace()+".getReader", rdId);
	}
	
	public Reader getReaderByRdCertify(String rdId){
		return (Reader) sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderByRdCertify", rdId);
	}
	
	public Reader getReaderByRdLoginId(String rdId){
		return  (Reader) sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderByRdLoginId", rdId);
	}
	
	public List<Reader> getReaderListByRdLoginId(String rdLoginId){
		return sqlSession.selectList(getMybatisMapperNamespace()+".getReaderListByRdLoginId", rdLoginId);

	}

	
	public List<Reader> getReaderListByRdCertify(String rdCertify,String rdName) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("rdCertify", rdCertify);
		params.put("rdName", rdName);
		return sqlSession.selectList(getMybatisMapperNamespace()+".getReaderListByRdCertify", params);
	}
	
	public Reader getReaderByRdIdAndRdCertify(String rdId,String rdCertify){
		Map<String,String> params = new HashMap<String,String>();
		params.put("rdId", rdId);
		params.put("rdCertify", rdCertify);
		return (Reader) sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderByRdIdAndRdCertify", params);

	}

	
	@Override
	public Reader getByRdId(String rdId) {
		
		return (Reader)sqlSession.selectOne(getMybatisMapperNamespace() + ".getByRdId", rdId);
	}

	
	public int updateReaderAvatar(Map<String, Object> map) {
		return sqlSession.update(getMybatisMapperNamespace()+".updateReaderAvatar", map);
	}
	
	public String getRealPassword(String rdId) {
		Object obj = sqlSession.selectOne(getMybatisMapperNamespace()+".getRealPassword", rdId);
		if(obj == null){
			return null;
		}
		return obj.toString();
	}
	
	public int updatePassword(String rdId, String newPassword) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("rdId", rdId);
		params.put("newPassword", newPassword);
		return sqlSession.update(getMybatisMapperNamespace()+".updatePassword", params);
	}
	//更新上传国图标识
	public int updateIsAuthor(String rdId, Integer isAuthor) {
		Reader reader = new Reader();
		reader.setRdId(rdId);
		reader.setIsAuthor(isAuthor);
		return sqlSession.update(getMybatisMapperNamespace()+".updateIsAuthor", reader);
	}
	
	public int updateReader(Reader reader) {
		return sqlSession.update(getMybatisMapperNamespace()+".updateReader", reader);
	}
	
	public void updatePhotoName(Reader reader) {
		sqlSession.update(getMybatisMapperNamespace()+".updatePhotoName", reader);
	}
	
	public String getPhotoName(String rdId) {
		Object photoName = sqlSession.selectOne(getMybatisMapperNamespace()+".getPhotoName", rdId);
		if(photoName == null || "".equals(photoName)) {
			return "";
		}else{
			return photoName.toString();
		}
	}
	
	public List<Reader> searchReader(Map<String,String> params) {
		return sqlSession.selectList(getMybatisMapperNamespace()+".searchReader", params);
	}

	@Override
	public List<Roles> getReaderRolesByRdId(String rdId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getReaderRoles", rdId);
	}

	@Override
	public List<Roles> getOtherRolesByRdId(String rdId) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getOtherRoles", rdId);
	}

	@Override
	public void deleteReaderRolesByRdId(String rdId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteReaderRole", rdId);
	}

	@Override
	public void saveReaderRoles(UReader_Role readerRole) {
		sqlSession.delete(getMybatisMapperNamespace() + ".insertReaderRole", readerRole);
	}

	@Override
	public int updateLibUser(String rdId, Integer isLibUser) {
		Reader reader = new Reader();
		reader.setRdId(rdId);
		reader.setLibUser(isLibUser);
		return sqlSession.update(getMybatisMapperNamespace()+".updateLibUser", reader);
	}

	@Override
	public List<Reader> getAllLlbUser(Integer isLibUser) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getAllLibUser", isLibUser);
	}
	
	public void addDeposit(String rdId, double deposit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rdId", rdId);
		map.put("deposit", deposit);
		sqlSession.insert(getMybatisMapperNamespace() + ".addDeposit", map);
	}
	
	public void updateDeposit(String rdId, double deposit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rdId", rdId);
		map.put("deposit", deposit);
		sqlSession.insert(getMybatisMapperNamespace() + ".updateDeposit", map);
	}

	@Override
	public void addReaderAsOperator(Reader reader) {
		sqlSession.update(getMybatisMapperNamespace() + ".addReaderAsOperator", reader);
	}

	@Override
	public void updateReaderAsOperator(Map<String, Reader> map) {
		sqlSession.update(getMybatisMapperNamespace() + ".updateReaderAsOperator", map);
	}
	
	public List<CardGroup> getCardGroups() {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".getCardGroups");
	}
	
	public void setReaderGroup(String rdId, int groupId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("rdId", rdId);
		params.put("groupId", groupId);
		sqlSession.insert(getMybatisMapperNamespace() + ".setReaderGroup", params);
	}
	
	public int getReaderGroupId(String rdId) {
		Object obj = sqlSession.selectOne(getMybatisMapperNamespace() + ".getReaderGroupId", rdId);
		if (obj == null) {
			obj = 0;
		}
		return Integer.valueOf(obj.toString());
	}
	
	public void deleteReaderBelongGroup(String rdId) {
		sqlSession.delete(getMybatisMapperNamespace() + ".deleteReaderBelongGroup", rdId);
	}
	
	public CardGroup getReaderGroupBelong(String rdId) {
		return (CardGroup) sqlSession.selectOne(getMybatisMapperNamespace() + ".getReaderGroupBelong", rdId);
	}

	@Override
	public int updateOldPasswordAndSynStatus(String rdId, String newPasswd,
			int synStatus) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("rdId", rdId);
		params.put("newPasswd", newPasswd);
		params.put("synStatus", synStatus+"");
		return sqlSession.update(getMybatisMapperNamespace()+".updateOldPasswordAndSynStatus", params);
	}

	@Override
	public int updateSynStatus(String rdId, int synStatus) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("rdId", rdId);
		params.put("synStatus", synStatus+"");
		return sqlSession.update(getMybatisMapperNamespace()+".updateSynStatus", params);
	}

	@Override
	public List<Loan> getCurrentLoanList(Reader reader, LibCode lib, 
			boolean doPage, Integer pageSize, Integer toPage) {
		String webserviceUrl = lib.getWebserviceUrl();
		
		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			webserviceUrl = webserviceUrl + (webserviceUrl.endsWith("/") ? "" : "/") + "webservice/loanWebservice";
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(LoanWebservice.class);

			factory.setAddress(webserviceUrl);
			
			LoanWebservice client = (LoanWebservice)factory.create();
			
			//Finance对象 cost欠款 tranid 财经流水记录号
			String rdOldPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getOldRdpasswd());
			List<Loan> loanList = null;
			try {
				loanList = client.getCurrentLoanList(reader.getRdId(),MD5Util.MD5Encode(rdOldPasswd),doPage, toPage, pageSize);
			} catch(Exception e) {
				log.error("getCurrentLoanList list error", e);
			}
			return loanList;
			
		} else {
			return null;
		}
	}

	@Override
	public List<Reader> readerUnSynList(Reader reader) {
		
		return sqlSession.selectList(getMybatisMapperNamespace()+".readerUnSynList", reader);
	}
	
	@Override
	public List<Finance> getReaderFinance(Reader reader, LibCode lib, 
			String paysign,boolean doPage,int toPage,int pageSize) {
			String webserviceUrl = lib.getWebserviceUrl();
		if (!"".equals(webserviceUrl) && webserviceUrl != null) {
			webserviceUrl = webserviceUrl + (webserviceUrl.endsWith("/") ? "" : "/") + "webservice/financeWebservice";
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

			factory.setServiceClass(FinanceWebservice.class);

			factory.setAddress(webserviceUrl);
			
			FinanceWebservice client = (FinanceWebservice)factory.create();
			
			//Finance对象 cost欠款 tranid 财经流水记录号
			String rdOldPasswd = EncryptDecryptData.decryptWithCode(Constants.DES_STATIC_KEY, reader.getOldRdpasswd());
			List<Finance> financeList = null;
			try {
				financeList = client.getFinanceByPaysign(reader.getRdId(), MD5Util.MD5Encode(rdOldPasswd), 
						paysign, doPage, toPage, pageSize);
			} catch(Exception e) {
				log.error("getReaderFinance list error", e);
			}
			return financeList;
		
		} else {
			return null;
		}
		
	}
	
	@Override
	public int updateLibAssign(Reader r) {
		
		return sqlSession.update(getMybatisMapperNamespace()+".updateLibAssign", r);
	}
	
	/**
	 * 获取读者所属馆对应的webServiceURL字段内容
	 */
	@Override
	public String getReaderWebServiceUrl(String rdid) {
		
		Object obj = sqlSession.selectOne(getMybatisMapperNamespace()+".getReaderWebServiceUrl", rdid);
		return obj == null?"":obj.toString();
	}

	@Override
	public int checkReaderActivate() {
		// TODO Auto-generated method stub
		return (Integer)sqlSession.selectOne(getMybatisMapperNamespace() + ".checkReaderActivate");
	}
	
	
}
