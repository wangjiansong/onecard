package com.interlib.sso.common;

import java.util.UUID;

public class UUIDGenerator {
	public UUIDGenerator() {
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(UUIDGenerator.getUUID());
	}
}
