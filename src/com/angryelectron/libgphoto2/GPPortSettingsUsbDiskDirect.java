package com.angryelectron.libgphoto2;
import com.sun.jna.Structure;
/**
 * <i>native declaration : /usr/include/gphoto2/gphoto2-port.h:97</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class GPPortSettingsUsbDiskDirect extends Structure {
	/**
	 * < /brief The ports device node path (/dev/sdX)<br>
	 * C type : char[128]
	 */
	public byte[] path = new byte[128];
	public GPPortSettingsUsbDiskDirect() {
		super();
		initFieldOrder();
	}
	protected void initFieldOrder() {
		setFieldOrder(new String[]{"path"});
	}
	/**
	 * @param path < /brief The ports device node path (/dev/sdX)<br>
	 * C type : char[128]
	 */
	public GPPortSettingsUsbDiskDirect(byte path[]) {
		super();
		if (path.length != this.path.length) 
			throw new IllegalArgumentException("Wrong array size !");
		this.path = path;
		initFieldOrder();
	}
	public static class ByReference extends GPPortSettingsUsbDiskDirect implements Structure.ByReference {
		
	};
	public static class ByValue extends GPPortSettingsUsbDiskDirect implements Structure.ByValue {
		
	};
}
