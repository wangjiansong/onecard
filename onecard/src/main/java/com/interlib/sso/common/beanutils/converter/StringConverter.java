package com.interlib.sso.common.beanutils.converter;

import org.apache.commons.beanutils.Converter;

public final class StringConverter implements Converter {

	public StringConverter() {
	}

	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		if (value == null || "".equals(value.toString())) {
			return (String) null;
		} else {
			return value.toString();
		}
	}
}

