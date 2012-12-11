package com.angryelectron.libgphoto2;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraAboutFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraCaptureFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraCapturePreviewFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraExitFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraGetConfigFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraManualFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraPrePostFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraSetConfigFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraSummaryFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraWaitForEvent;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
/**
 * <i>native declaration : /usr/include/gphoto2/gphoto2-camera.h:1219</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CameraFunctions extends Structure {
	/**
	 * < \brief Function called before each camera operation.<br>
	 * C type : CameraPrePostFunc
	 */
	public CameraPrePostFunc pre_func;
	/**
	 * < \brief Function called after each camera operation.<br>
	 * C type : CameraPrePostFunc
	 */
	public CameraPrePostFunc post_func;
	/**
	 * < \brief Function called on closing the camera.<br>
	 * C type : CameraExitFunc
	 */
	public CameraExitFunc exit;
	/**
	 * Configuration<br>
	 * < \brief Called for requesting the configuration widgets.<br>
	 * C type : CameraGetConfigFunc
	 */
	public CameraGetConfigFunc get_config;
	/**
	 * < \brief Called after a configuration was changed<br>
	 * C type : CameraSetConfigFunc
	 */
	public CameraSetConfigFunc set_config;
	/**
	 * Capturing<br>
	 * < \brief Remote control the camera to capture<br>
	 * C type : CameraCaptureFunc
	 */
	public CameraCaptureFunc capture;
	/**
	 * < \brief Preview viewfinder content.<br>
	 * C type : CameraCapturePreviewFunc
	 */
	public CameraCapturePreviewFunc capture_preview;
	/**
	 * Textual information<br>
	 * < \brief Give a summary about the current camera status, translated.<br>
	 * C type : CameraSummaryFunc
	 */
	public CameraSummaryFunc summary;
	/**
	 * < \brief Give a brief manual about any specific items a user has to know, translated.<br>
	 * C type : CameraManualFunc
	 */
	public CameraManualFunc manual;
	/**
	 * < \brief A little About text, including authors and credits.<br>
	 * C type : CameraAboutFunc
	 */
	public CameraAboutFunc about;
	/**
	 * Event Interface<br>
	 * < \brief Wait for a specific event from the camera<br>
	 * C type : CameraWaitForEvent
	 */
	public CameraWaitForEvent wait_for_event;
	/**
	 * Reserved space to use in the future without changing the struct size<br>
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved1;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved2;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved3;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved4;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved5;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved6;
	/**
	 * < \brief reserved for future use<br>
	 * C type : void*
	 */
	public Pointer reserved7;
	public CameraFunctions() {
		super();
		initFieldOrder();
	}
	protected void initFieldOrder() {
		setFieldOrder(new String[]{"pre_func", "post_func", "exit", "get_config", "set_config", "capture", "capture_preview", "summary", "manual", "about", "wait_for_event", "reserved1", "reserved2", "reserved3", "reserved4", "reserved5", "reserved6", "reserved7"});
	}
	public static class ByReference extends CameraFunctions implements Structure.ByReference {
		
	};
	public static class ByValue extends CameraFunctions implements Structure.ByValue {
		
	};
}
