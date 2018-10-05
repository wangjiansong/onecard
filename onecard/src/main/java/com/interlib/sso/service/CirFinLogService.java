package com.interlib.sso.service;

import java.util.List;
import java.util.Map;

import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinSettlementResult;

public interface CirFinLogService extends BaseService<CirFinLog, String> {
	
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
	//ADD 2014-04-29 根据条件查询出需要的结果的数量
	public int getCirFinLogCount(CirFinLog cirFinLog);
	//ADD 2014-05-20
	public List<CirFinLog> autoStatistics(CirFinLog cirFinLog);

	public int autoStatisticsCount(CirFinLog cirFinLog);//获取自动统计总数
	
	public int updateIsReturnByID(Map<String,String> map);//20140707

	public List<Map<String, String>> moreLibFinSettle(Map<String, String> params);//20140710 多馆财经结算

	public List<Map<String, String>> moreLibFinSettleDetail(Map<String, String> params);//20140712

	public List<CirFinLog> queryPageCirFinLogSet(Map params);//日志接口使用的 2015-1-19
	
	public List<Map<String, Object>> cirFinLogStatistics(CirFinLog cirFinLog);
	
	public Map<String, Object> cirFinLogOtherStatistics(CirFinLog cirFinLog);
	
	public Map<String, Object> cirFinLogTotalStatistics(CirFinLog cirFinLog);
}
