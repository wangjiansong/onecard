package com.interlib.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interlib.sso.dao.CirFinLogDAO;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinSettlementResult;
import com.interlib.sso.service.CirFinLogService;

@Service
public class CirFinLogServiceImpl 
		extends AbstractBaseServiceImpl<CirFinLog, String> implements CirFinLogService {

	@Autowired
	public CirFinLogDAO cirFinLogDAO;
	
	@Autowired
	public void setBaseDAO(CirFinLogDAO cirFinLogDAO) {
		super.setBaseDAO(cirFinLogDAO);
	}
	
	
	@Override
	public List<CirFinLog> queryCirFinLogList(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.queryCirFinLogList(cirFinLog);
	}

	@Override
	public List<Map<String, String>> statistics(CirFinLog cirFinLog) {
		return cirFinLogDAO.statistics(cirFinLog);
	}
	
	@Override
	public void updateRdid(Map<String, String> map) {
		cirFinLogDAO.updateRdid(map);
	}


	@Override
	public List<Map<String, String>> statisticsGroupByReglib(CirFinLog cirFinLog) {
		return cirFinLogDAO.statisticsGroupByReglib(cirFinLog);
	}


	@Override
	public List<Map<String, String>> statisticsGroupByRdtype(CirFinLog cirFinLog) {
		return cirFinLogDAO.statisticsGroupByRdtype(cirFinLog);
	}


	@Override
	public List<CirFinLog> monetaryList(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.monetaryList(cirFinLog);
	}


	@Override
	public CirFinLog monetaryStatistics(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.monetaryStatistics(cirFinLog);
	}


	@Override
	public List<FinSettlementResult> finSettlement(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.finSettlement(cirFinLog);
	}


	@Override
	public FinSettlementResult finSettlementStatistics(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.finSettlementStatistics(cirFinLog);
	}


	@Override
	public Long slotCountByRules(Map<String, String> params) {
		return cirFinLogDAO.slotCountByRules(params);
	}


	@Override
	public FinSettlementResult finSettlementRepairCost(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.finSettlementRepairCost(cirFinLog);
	}


	@Override
	public int getCirFinLogCount(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.getCirFinLogCount(cirFinLog);
	}


	@Override
	public List<CirFinLog> autoStatistics(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.autoStatistics(cirFinLog);
	}


	@Override
	public int autoStatisticsCount(CirFinLog cirFinLog) {
		
		return cirFinLogDAO.autoStatisticsCount(cirFinLog);
	}


	@Override
	public int updateIsReturnByID(Map<String, String> map) {
		
		return cirFinLogDAO.updateIsReturnByID(map);
	}


	@Override
	public List<Map<String, String>> moreLibFinSettle(Map<String, String> params) {
		
		return cirFinLogDAO.moreLibFinSettle(params);
	}


	@Override
	public List<Map<String, String>> moreLibFinSettleDetail(
			Map<String, String> params) {
		
		return cirFinLogDAO.moreLibFinSettleDetail(params);
	}

	@Override
	public List<CirFinLog> queryPageCirFinLogSet(Map params) {
		
		return cirFinLogDAO.queryPageCirFinLogSet(params);
		
	}

	@Override
	public List<Map<String, Object>> cirFinLogStatistics(CirFinLog cirFinLog) {
		return cirFinLogDAO.cirFinLogStatistics(cirFinLog);
	}
	
	
	@Override
	public Map<String, Object> cirFinLogOtherStatistics(CirFinLog cirFinLog) {
		return cirFinLogDAO.cirFinLogOtherStatistics(cirFinLog);
	}
	
	public Map<String, Object> cirFinLogTotalStatistics(CirFinLog cirFinLog) {
		return cirFinLogDAO.cirFinLogTotalStatistics(cirFinLog);
	}
	

}
