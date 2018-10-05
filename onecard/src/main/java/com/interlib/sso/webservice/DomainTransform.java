package com.interlib.sso.webservice;

import com.interlib.opac.webservice.Reader;
import com.interlib.sso.common.ConvertUtils;

public class DomainTransform {
	
	public static Reader transToWebserviceReader(com.interlib.sso.domain.Reader reader) {
		
		Reader r = new Reader();
		r.setRdid(reader.getRdId());
		r.setRdLoginId(reader.getRdLoginId());
		r.setRdPasswd(reader.getRdPasswd());//传递这个参数会在修改读者的信息时候，把interlib原来的密码修改了
		r.setRdName(reader.getRdName());
		r.setRdCertify(reader.getRdCertify());
		if(reader.getRdBornDate() != null) {
			r.setRdBornDate(ConvertUtils.getXmlDatetime(reader.getRdBornDate(), "yyyy-MM-dd"));
		}
		if(reader.getRdSex()==2){
			r.setRdSex(0);
		}else{
			r.setRdSex(new Integer(reader.getRdSex()));
		}
		r.setRdcfstate(new Integer(reader.getRdCFState()));
		r.setRdType(reader.getRdType());
		System.out.println(reader.getRdType());
		r.setGlobalType(reader.getRdLibType());
		r.setGlobal(reader.getRdGlobal()==1? true : false);
		r.setRdLib(reader.getRdLib());
		r.setStartDate(ConvertUtils.getXmlDatetime(reader.getRdStartDate(), "yyyy-MM-dd"));
		r.setEndDate(ConvertUtils.getXmlDatetime(reader.getRdEndDate(), "yyyy-MM-dd"));
		r.setRdAddress(reader.getRdAddress());
		r.setRdPostCode(reader.getRdPostCode());
		r.setRdEmail(reader.getRdEmail());
		r.setRdPhone(reader.getRdPhone());
		r.setRdUnit(reader.getRdUnit());
		r.setRdRemark(reader.getRdRemark());
		r.setRdInterest(reader.getRdInterest());
		r.setRdSpecialty(reader.getRdSpecialty());
		r.setRdSort1(reader.getRdSort1());
		r.setRdSort2(reader.getRdSort2());
		r.setRdSort3(reader.getRdSort3());
		r.setRdSort4(reader.getRdSort4());
		r.setRdSort5(reader.getRdSort5());
		r.setRdNation(reader.getRdNation());
		r.setRdNative(reader.getRdNative());
		r.setRdRegisterName(reader.getRdRegisterName());
		r.setWorkCardNo(reader.getWorkCardNo());
		r.setRdDeposit(reader.getRdDeposit());
		if(reader.getPhotobytes() != null) {
			r.setRdphoto(reader.getPhotobytes());
		}
		if(reader.getRdPhoto() != null) {
			r.setRdphoto(reader.getRdPhoto());
		}
		r.setRdGlobal((int) reader.getRdGlobal());//添加是否是馆际读者
		r.setRegman(reader.getRegman());//添加操作员 20140322
		r.setPaytype(reader.getPaytype());//添加押金类型20140322
		return r;
	}
}
