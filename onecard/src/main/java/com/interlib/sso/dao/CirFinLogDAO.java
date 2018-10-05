package com.interlib.sso.dao;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinSettlementResult;

public interface CirFinLogDAO extends BaseDAO<CirFinLog, String> {

	public List<CirFinLog> queryCirFinLogList(CirFinLog cirFinLog);
	
	public List<Map<String, String>> statistics(CirFinLog cirFinLog);
	
	public void updateRdid(Map<String, String> map);
	
	public List<Map<String, String>> statisticsGroupByReglib(CirFinLog cirFinLog);
	
	public List<Map<String, String>> statisticsGroupByRdtype(CirFinLog cirFinLog);
	
	public List<CirFinLog> monetaryList(CirFinLog cirFinLog);

	public CirFinLog monetaryStatistics(CirFinLog cirFinLog);
	
	public List<FinSettlementResult> finSettlement(CirFinLog cirFinLog);
	
	public FinSettlementResult finSettlementStatistics(CirFinLog cirFinLog);
	
	public Long slotCountByRules(Map<String, String> params);

	public FinSettlementResult finSettlementRepairCost(CirFinLog cirFinLog);

	public int getCirFinLogCount(CirFinLog cirFinLog);
	
	//ADD 2014-05-20
	public List<CirFinLog> autoStatistics(CirFinLog cirFinLog);

	public int autoStatisticsCount(CirFinLog cirFinLog);

	public int updateIsReturnByID(Map<String, String> map);//20140707

	public List<Map<String, String>> moreLibFinSettle(Map<String, String> params);//20140710 多馆财经结算

	public List<Map<String, String>> moreLibFinSettleDetail(Map<String, String> params);//20140712

	public List<CirFinLog> queryPageCirFinLogSet(Map params);
	
	public List<Map<String, Object>> cirFinLogStatistics(CirFinLog cirFinLog);//流通财经统计
	
	public Map<String, Object> cirFinLogOtherStatistics(CirFinLog cirFinLog);
	
	public Map<String, Object> cirFinLogTotalStatistics(CirFinLog cirFinLog);
}
