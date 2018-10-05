/*
 * RSAUtil 1.0.6 2005-11-30
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * 创建日期 2005-11-30
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.interlib.sso.license;

import javax.crypto.Cipher;

import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.io.*;
import java.math.BigInteger;

/**
 * @author toofu
 * @reviewer 	toofu
 * @version     1.0, 2003-9-12
 * @env		    JDK1.4.1	
 * @modified	toofu,	2005-11-30
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
/**
 * 非对称加密 RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * 加密可以用公钥，解密用私钥；或者加密用私钥。通常非对称加密是非常消耗资源的
 * ，因此可以对大数据用对称加密如：des（具体代码可以看我以前发的贴子），而对其对称密钥进行非对称加密，这样既保证了数据的安全，还能保证效率。
 */
public class RSAUtil {

	/**
	 * 生成密钥对
	 * 
	 * @return KeyPair PublicKey pubkey = keys.getPublic(); PrivateKey prikey =
	 *         keys.getPrivate();
	 * @throws EncryptException
	 */
	public static KeyPair generateKeyPair(String passwd) {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		// 一般2048够健壮了.呵呵.不过这里可以多生成一下也无所谓.
		final int KEY_SIZE = 2048;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
		keyPairGen.initialize(KEY_SIZE, new SecureRandom(passwd.getBytes()));
		KeyPair keyPair = keyPairGen.genKeyPair();
		return keyPair;
	}

	/**
	 * 生成公钥
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @return RSAPublicKey
	 * @throws EncryptException
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
			byte[] publicExponent) {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
		}
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
				modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成私钥
	 * 
	 * @param modulus
	 * @param privateExponent
	 * @return RSAPrivateKey
	 * @throws EncryptException
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
			byte[] privateExponent) {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
		}

		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
				modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            加密的密钥
	 * @param data
	 *            待加密的明文数据
	 * @return 加密后的数据
	 * @throws EncryptException
	 */
	public static byte[] encrypt(Key key, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
													// 加密块大小为127
													// byte,加密后为128个byte;因此共有2个加密块，第一个127
													// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。

				i++;
			}
			return raw;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            解密的密钥
	 * @param raw
	 *            已经加密的数据
	 * @return 解密后的明文
	 * @throws EncryptException
	 */
	public static byte[] decrypt(Key key, byte[] raw) {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, key);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			byte [] retbyte = bout.toByteArray();
			bout.close();
			return retbyte;
		} catch (Exception e) {
		}
		return null;
	}

	private static void saveKey2File(RSAPrivateKey priKey, RSAPublicKey pubKey) {
		java.io.ObjectOutputStream out;
		try {
			String url = RSAUtil.class.getResource("/").getPath().replaceAll("%20", " ");
			String privateLicense = url + "interlibsso_private.dat";
			// out = new java.io.ObjectOutputStream(new
			// java.io.FileOutputStream("/interlibsso_private.dat"));
			out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(privateLicense));
			out.writeObject(priKey);
			out.close();
			// out=new java.io.ObjectOutputStream(new
			// java.io.FileOutputStream("/WEB-INF/interlibsso_public.dat"));
			String publicLicense = url + "interlibsso_public.dat";
			out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(publicLicense));
			out.writeObject(pubKey);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

	private static void buildKey(String passwd) {
		// 生成密匙对
		KeyPair keyPair = RSAUtil.generateKeyPair(passwd);
		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();// 获取公匙
		RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();// 获取私匙

		byte[] pubModBytes = pubKey.getModulus().toByteArray();
		byte[] pubPubExpBytes = pubKey.getPublicExponent().toByteArray();

		byte[] priModBytes = priKey.getModulus().toByteArray();
		byte[] priPriExpBytes = priKey.getPrivateExponent().toByteArray();

		// System.out.println("priModBytes="+DESUtil.byte2hex(priModBytes));
		// System.out.println("priPriExpBytes="+DESUtil.byte2hex(priPriExpBytes));

		// 生成公匙
		RSAPublicKey recoveryPubKey = RSAUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
		// 生成私匙
		RSAPrivateKey recoveryPriKey = RSAUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
		saveKey2File(priKey, recoveryPubKey);
	}

	protected static RSAPublicKey readPublicKey(String fileName) {
		RSAPublicKey priKey = null;
		java.io.ObjectInputStream in = null;
		try {// RSAUtil.class.getClass().getResourceAsStream("/interlibsso_public.dat"));//
			in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileName));
			priKey = (RSAPublicKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		return priKey;
	}

	private static RSAPrivateKey readPrivateKey(String fileName) {
		RSAPrivateKey priKey = null;
		java.io.ObjectInputStream in = null;
		try {
			in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileName));
			priKey = (RSAPrivateKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		return priKey;
	}

	protected static String loadLicenseConfig() {
		// String licensefile
		// =RSAUtil.class.getResource("/interlibssoLicense.dat").toString();//.substring(5);
		String url = RSAUtil.class.getResource("/").getPath();
		String licensefile = url + "onecardlicense.dat";
		licensefile = licensefile.replaceAll("%20", " ");// 使路径支持空格
		if (licensefile.toLowerCase().indexOf("file:") == 0) {
			licensefile = licensefile.substring(5);
		}
		// String licensepubfile
		// =RSAUtil.class.getResource("/interlibsso_public.dat").toString();//.substring(5);
		String licensepubfile = url + "interlibsso_public.dat";
		licensepubfile = licensepubfile.replaceAll("%20", " ");// 使路径支持空格
		if (licensepubfile.toLowerCase().indexOf("file:") == 0) {
			licensepubfile = licensepubfile.substring(5);
		}

		// System.out.println(licensefile+"\n"+licensepubfile);
		// 读取内容并解密
		java.io.ObjectInputStream in = null;
		try {// RSAUtil.class.getClass().getResourceAsStream("/interlibssoLicense.dat"));//
			in = new java.io.ObjectInputStream(new FileInputStream(licensefile));
			byte[] raw = (byte[]) in.readObject();// 读文件到对象
			// 解密
			// 通过公匙进行解密D:/Tcsoft Interlib/webapps/interlibSSO/classes/
			RSAPublicKey recoveryPubKey = readPublicKey(licensepubfile);
			byte[] data = RSAUtil.decrypt(recoveryPubKey, raw);
			return new String(data, "UTF-8");
			// System.out.println("公钥解密后："+new String(data));
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException:no license!" + e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:no license class!" + e);
		} catch (IOException e) {
			System.out.println("IOException:no license! " + e);
		} catch (Exception e) {
			System.out.println("Exception:no license! " + e);
		} finally {
			try {
				if(in != null) in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		// 这里进行注释，防止类文件中出现产生公钥和私钥的信息。
		// 密钥生成器，生成私钥和对应的公钥，一般生成一次即可
		// String passwd="#$<!123interlib321!>$#";//sipII所使用的密钥
		// String passwd="#$<!12345interlib54321!>$#";//interlibSSO所使用的密钥
		// buildKey(passwd);
		String url = RSAUtil.class.getResource("/interlibsso_public.dat").getPath().replaceAll("%20", " ");
		System.out.println(url);
		String privateLicense = url + "interlibsso_public.dat";
		System.out.println(privateLicense);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(privateLicense));
		System.out.println(in.readObject());
	}

}
