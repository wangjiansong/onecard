package com.interlib.sso.spring.httpclient;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * httpClient工厂方法，配置为spring的bean作为单例，调用getHttpClient会获得一个
 * 支持多线程调用的httpclient对象
 * @author zhoulonghuan
 *
 */
public class HttpClientFactory {

	private final HttpClient httpClient;

	public HttpClientFactory() {
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

		// prevent retries (note: this didn't work when set on mgr.. needed to
		// be set on client)
		DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler(
				0, false);
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				retryhandler);
		//设置默认参数
		httpClient.getHttpConnectionManager().getParams()
				.setDefaultMaxConnectionsPerHost(32);
		httpClient.getHttpConnectionManager().getParams()
				.setMaxTotalConnections(128);
	}
	
	public HttpClient getHttpClient() {
		return this.httpClient;
	}
	
	//通过spring注入的参数设置
	public void setConnectionTimeout(int timeout) {
		getHttpClient().getParams().setConnectionManagerTimeout(timeout);
	}

	public void setDefaultMaxConnectionsPerHost(int connections) {
		getHttpClient().getHttpConnectionManager().getParams()
			.setDefaultMaxConnectionsPerHost(connections);
	}
	
	public void setMaxTotalConnections(int connections) {
		getHttpClient().getHttpConnectionManager().getParams()
			.setMaxTotalConnections(connections);
	}
}
