package com.interlib.sso.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.SynchronizedSortedSet;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ctc.wstx.util.StringUtil;
import com.interlib.sso.common.DateUtils;
import com.interlib.sso.common.StringUtils;
import com.interlib.sso.dao.CirFinLogDAO;
import com.interlib.sso.domain.CirFinLog;
import com.interlib.sso.domain.FinSettlementResult;

@Repository
public class CirFinLogDAOImpl 
		extends AbstractMybatisBaseDAO<CirFinLog, String>implements CirFinLogDAO {

	@Autowired
	public SqlSession sqlSession;

	@Override
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@Override
	public String getMybatisMapperNamespace() {
		return "CirFinLog";
	}

	@Override
	public List<CirFinLog> queryCirFinLogList(CirFinLog cirFinLog) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageList", cirFinLog);
	}

	@Override
	public List<Map<String, String>> statistics(CirFinLog cirFinLog) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".statistics", cirFinLog);
	}

	@Override
	public void updateRdid(Map<String, String> map) {
		sqlSession.update(getMybatisMapperNamespace() + ".updateRdid", map);
	}

	@Override
	public List<Map<String, String>> statisticsGroupByReglib(CirFinLog cirFinLog) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".statisticsGroupByReglib", cirFinLog);
	}

	@Override
	public List<Map<String, String>> statisticsGroupByRdtype(CirFinLog cirFinLog) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".statisticsGroupByRdtype", cirFinLog);
	}

	@Override
	public List<CirFinLog> monetaryList(CirFinLog cirFinLog) {
		
		return sqlSession.selectList(getMybatisMapperNamespace()+".monetaryList",cirFinLog);
	}

	@Override
	public CirFinLog monetaryStatistics(CirFinLog cirFinLog) {
		
		return (CirFinLog) sqlSession.selectOne(getMybatisMapperNamespace()+".monetaryStatistics", cirFinLog);
	}

	@Override
	public List<FinSettlementResult> finSettlement(CirFinLog cirFinLog) {
		
		if(cirFinLog.getDateFormat().equals("YEAR")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".finSettlementByYearList", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("MONTH")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".finSettlementByMonthList", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("DAY")) {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".finSettlementByDayList", cirFinLog);
		} else {
			return sqlSession.selectList(getMybatisMapperNamespace() + ".finSettlementByDayList", cirFinLog);
		}
		
	}

	@Override
	public FinSettlementResult finSettlementStatistics(CirFinLog cirFinLog) {
		if(cirFinLog.getDateFormat().equals("YEAR")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementStatisticsByYear", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("MONTH")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementStatisticsByMonth", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("DAY")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementStatisticsByDay", cirFinLog);
		} else {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementStatisticsByDay", cirFinLog);
		}
		
	}

	@Override
	public Long slotCountByRules(Map<String, String> params) {
		return Long.parseLong(sqlSession.selectOne(getMybatisMapperNamespace() + ".slotCountByRules", params).toString());
	}

	@Override
	public FinSettlementResult finSettlementRepairCost(CirFinLog cirFinLog) {
		
		if(cirFinLog.getDateFormat().equals("YEAR")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementRepairCostByYear", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("MONTH")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementRepairCostByMonth", cirFinLog);
		} else if(cirFinLog.getDateFormat().equals("DAY")) {
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementRepairCostByDay", cirFinLog);
		} else {
			//默认是当月份
			//计算下当月的开始时间和最后一天的时间加到cirFinLog对象里面
			if(cirFinLog==null)
				cirFinLog=new CirFinLog();
			
			cirFinLog.setStartTime(DateUtils.getFirstDayOfMonth());
			cirFinLog.setEndTime(DateUtils.getLastDayOfMonth());
				
			return (FinSettlementResult)sqlSession.selectOne(getMybatisMapperNamespace() + ".finSettlementRepairCostByDay", cirFinLog);
		}
	}

	@Override
	public int getCirFinLogCount(CirFinLog cirFinLog) {
		
		return Integer.parseInt(sqlSession.selectOne(getMybatisMapperNamespace() + ".getCirFinLogCount", cirFinLog).toString());
	}

	@Override
	public List<CirFinLog> autoStatistics(CirFinLog cirFinLog) {
		return sqlSession.selectList(getMybatisMapperNamespace() + ".autoStatistics", cirFinLog);
	}

	@Override
	public int autoStatisticsCount(CirFinLog cirFinLog) {
		
		return Integer.parseInt(sqlSession.selectOne(getMybatisMapperNamespace() + ".autoStatisticsCount", cirFinLog).toString());
	}

	@Override
	public int updateIsReturnByID(Map<String, String> map) {
		
		return sqlSession.update(getMybatisMapperNamespace() + ".updateIsReturnByID", map);
	}

	@Override
	public List<Map<String, String>> moreLibFinSettle(Map<String, String> params) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".moreLibFinSettle", params);//20140710 多馆财经结算
	}

	@Override
	public List<Map<String, String>> moreLibFinSettleDetail(
			Map<String, String> params) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".moreLibFinSettleDetail", params);//20140712 多馆财经结算明细
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CirFinLog> queryPageCirFinLogSet(Map params) {
		
		return sqlSession.selectList(getMybatisMapperNamespace() + ".queryPageCirFinLogSet", params);//20150119
	}

	@Override
	public List<Map<String, Object>> cirFinLogStatistics(CirFinLog cirFinLog) {
		//feetyp:'101','102','103','104','105','106','107','108','109','110'
		//regman:'admin','huang'
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select (select r.rdname from reader r where c.REGMAN=r.rdid) as REGMAN, ");
		String [] feetypes = cirFinLog.getFeetype().split(",");
		StringBuffer feetypeSql = new StringBuffer();
		for(String feetype : feetypes) {
			sql.append("sum(case when c.FEETYPE='" + feetype + "' then fee else 0 end) as \"" + feetype + "\",");
			feetypeSql.append("'" + feetype + "',");
		}
		StringUtils.removeEndChar(feetypeSql, ",");
		sql.append("sum(case when c.FEETYPE in (" + feetypeSql + ") then fee else 0 end) as allSum ");
		sql.append(" from CIR_FIN_LOG c,READER r where r.RDID=c.RDID ");
		if(cirFinLog.getRegman() != null) {
			String [] regmans = cirFinLog.getRegman().split(",");
			if(regmans.length > 0 && !cirFinLog.getRegman().equals("")) {
				sql.append(" and c.REGMAN in (");
				for(String regman : regmans) {
					sql.append("'" + regman + "',");
				}
				StringUtils.removeEndChar(sql, ",");
				sql.append(")");
			}
		}
		if(!cirFinLog.getStartTime().equals("")) {
			sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') >= '"+ cirFinLog.getStartTime() +"' ");
		}
		if(!cirFinLog.getEndTime().equals("")) {
			sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') <= '"+ cirFinLog.getEndTime()+"' ");
		}
		String feeAppCodes [] = cirFinLog.getFeeAppCode().split(",");
		if(feeAppCodes.length > 0 && !cirFinLog.getFeeAppCode().equals("")) {
			sql.append(" AND c.FEEAPPCODE in (");
			for(String feeAppCode : feeAppCodes) {
				sql.append("'" + feeAppCode + "',");
			}
			StringUtils.removeEndChar(sql, ",");
			sql.append(")");
		}
		if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getRdname()).equals("")) {
			sql.append(" AND r.RDNAME='" + cirFinLog.getRdname() + "'");
		}
		if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getStartRdid()).equals("")) {
			sql.append(" AND length(r.RDID) = " + getWordCount(cirFinLog.getStartRdid()));
			sql.append(" AND r.RDID >= '" + cirFinLog.getStartRdid() + "'");
		}
		if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getEndRdid()).equals("")) {
			sql.append(" AND r.RDID <= '" + cirFinLog.getEndRdid() + "'");
		}
		sql.append(" group by rollup(c.REGMAN) ");
		sql.append(") where REGMAN is not null");
		System.out.println(sql.toString());
		List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql.toString());
		return results;
	}
	
	/**
	 * 统计其他管理员收费，但是只有管理员ID，没有管理员信息记录的数据
	 * @param cirFinLog
	 * @return
	 */
	public Map<String, Object> cirFinLogOtherStatistics(CirFinLog cirFinLog) {
		
		//feetyp:'101','102','103','104','105','106','107','108','109','110'
				//regman:'admin','huang'
				StringBuffer sql = new StringBuffer();
				sql.append("select * from (");
				sql.append("select min('其他') as REGMAN, ");
				String [] feetypes = cirFinLog.getFeetype().split(",");
				StringBuffer feetypeSql = new StringBuffer();
				for(String feetype : feetypes) {
					sql.append("sum(case when c.FEETYPE='" + feetype + "' then fee else 0 end) as \"" + feetype + "\",");
					feetypeSql.append("'" + feetype + "',");
				}
				StringUtils.removeEndChar(feetypeSql, ",");
				sql.append("sum(case when c.FEETYPE in (" + feetypeSql + ") then fee else 0 end) as allSum ");
				sql.append(" from CIR_FIN_LOG c, READER r where r.RDID=c.RDID");
				if(cirFinLog.getRegman() != null) {
					String [] regmans = cirFinLog.getRegman().split(",");
					if(regmans.length > 0 && !cirFinLog.getRegman().equals("")) {
						sql.append(" and c.REGMAN not in (");
						for(String regman : regmans) {
							sql.append("'" + regman + "',");
						}
						StringUtils.removeEndChar(sql, ",");
						sql.append(")");
					}
				}
				if(!cirFinLog.getStartTime().equals("")) {
					sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') >= '"+ cirFinLog.getStartTime() +"' ");
				}
				if(!cirFinLog.getEndTime().equals("")) {
					sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') <= '"+ cirFinLog.getEndTime()+"' ");
				}
				String feeAppCodes [] = cirFinLog.getFeeAppCode().split(",");
				if(feeAppCodes.length > 0 && !cirFinLog.getFeeAppCode().equals("")) {
					sql.append(" AND c.FEEAPPCODE in (");
					for(String feeAppCode : feeAppCodes) {
						sql.append("'" + feeAppCode + "',");
					}
					StringUtils.removeEndChar(sql, ",");
					sql.append(")");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getRdname()).equals("")) {
					sql.append(" AND r.RDNAME='" + cirFinLog.getRdname() + "'");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getStartRdid()).equals("")) {
					sql.append(" AND length(r.RDID) = " + getWordCount(cirFinLog.getStartRdid()));
					sql.append(" AND r.RDID >= '" + cirFinLog.getStartRdid() + "'");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getEndRdid()).equals("")) {
					sql.append(" AND r.RDID <= '" + cirFinLog.getEndRdid() + "'");
				}
				sql.append(") where REGMAN is not null");
				List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql.toString());
				if(results.size() > 0) {
					return results.get(0);
				} else {
					return null;
				}
	}
	
	/**
	 * 统计总数
	 * @param cirFinLog
	 * @return
	 */
	public Map<String, Object> cirFinLogTotalStatistics(CirFinLog cirFinLog) {
		//feetyp:'101','102','103','104','105','106','107','108','109','110'
				//regman:'admin','huang'
				StringBuffer sql = new StringBuffer();
				sql.append("select * from (");
				sql.append("select c.REGMAN, ");
				String [] feetypes = cirFinLog.getFeetype().split(",");
				StringBuffer feetypeSql = new StringBuffer();
				for(String feetype : feetypes) {
					sql.append("sum(case when c.FEETYPE='" + feetype + "' then fee else 0 end) as \"" + feetype + "\",");
					feetypeSql.append("'" + feetype + "',");
				}
				StringUtils.removeEndChar(feetypeSql, ",");
				sql.append("sum(case when c.FEETYPE in (" + feetypeSql + ") then fee else 0 end) as allSum ");
				sql.append(" from CIR_FIN_LOG c,READER r where r.RDID=c.RDID");
				
				if(!cirFinLog.getStartTime().equals("")) {
					sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') >= '"+ cirFinLog.getStartTime() +"' ");
				}
				if(!cirFinLog.getEndTime().equals("")) {
					sql.append(" AND to_char(c.REGTIME, 'YYYY-MM-DD') <= '"+ cirFinLog.getEndTime()+"' ");
				}
				String feeAppCodes [] = cirFinLog.getFeeAppCode().split(",");
				if(feeAppCodes.length > 0 && !cirFinLog.getFeeAppCode().equals("")) {
					sql.append(" AND c.FEEAPPCODE in (");
					for(String feeAppCode : feeAppCodes) {
						sql.append("'" + feeAppCode + "',");
					}
					StringUtils.removeEndChar(sql, ",");
					sql.append(")");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getRdname()).equals("")) {
					sql.append(" AND r.RDNAME='" + cirFinLog.getRdname() + "'");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getStartRdid()).equals("")) {
					sql.append(" AND length(r.RDID) = " + getWordCount(cirFinLog.getStartRdid()));
					sql.append(" AND r.RDID >= '" + cirFinLog.getStartRdid() + "'");
				}
				if(!org.apache.commons.lang.StringUtils.trimToEmpty(cirFinLog.getEndRdid()).equals("")) {
					sql.append(" AND r.RDID <= '" + cirFinLog.getEndRdid() + "'");
				}
				
				sql.append(" group by rollup(c.REGMAN) ");
				sql.append(") where REGMAN is null");
				List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql.toString());
				if(results == null || results.size() == 0) {
					return new HashMap();
				} else {
					return results.get(0);
				}
			}
	private int getWordCount(String s) {
		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}
	
}
