package com.interlib.sso.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.interlib.sso.common.TimeUtils;
import com.interlib.sso.service.ScanNetReaderTaskService;


/**
 * 扫描读者注册定时任务
 * @author Administrator
 *
 */
@Controller
public class ScanNetReaderTask {
//	extends QuartzJobBean {
	
		private static final Logger logger = Logger.getLogger(ScanNetReaderTask.class);
		@Autowired
		private ScanNetReaderTaskService scanNetReaderTaskService;
		
	    protected void execute()  {  
			// TODO Auto-generated method stub
			Date currentDate = new Date();
			logger.info("[-------------虚拟读者扫描激活定时任务开始执行-------------]" + TimeUtils.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss SSS"));
			
//			scanNetReaderTaskService.queryCurrentScans();
			
			scanNetReaderTaskService.doScan();
			scanNetReaderTaskService.doScanForNetReader();
			
			logger.info(TimeUtils.dateToString(currentDate, "yyyy年MM月dd日：") + "扫描更新【读者年数大于等于1】成功！");
			logger.info("[-------------虚拟读者扫描激活定时任务执行完毕-------------]" + TimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
	    	
		}
	    /**
	     * 回收批量注册读者证号
	     */
	    protected void batchDeleteNetReaders()  {  
			// TODO Auto-generated method stub
			Date currentDate = new Date();
			logger.info("[-------------虚拟读者回收定时任务开始执行-------------]" + TimeUtils.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss SSS"));
			
			
			scanNetReaderTaskService.batchDeleteNetReaders();
			
			logger.info(TimeUtils.dateToString(currentDate, "yyyy年MM月dd日：") + "虚拟读者回收成功！");
			logger.info("[-------------虚拟读者回收定时任务执行完毕-------------]" + TimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
	    	
		}
	    
}  
