/**
 * GPhoto2 
 * Copyright 2012 Andrew Bythell, abythell@ieee.org
 *
 * This file is part of libgphoto2-jna.
 *
 * libgphoto2-jna is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * libgphoto2-jna is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * libphoto2-jna. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.angryelectron.gphoto2;

import com.angryelectron.libgphoto2.Camera;
import com.angryelectron.libgphoto2.CameraFilePath;
import com.angryelectron.libgphoto2.Gphoto2Library;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraCaptureType;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraEventType;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraFile;
import com.angryelectron.libgphoto2.Gphoto2Library.CameraFileType;
import com.angryelectron.libgphoto2.Gphoto2Library.GPContext;
import com.angryelectron.libgphoto2.Gphoto2Library.GPContextErrorFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.GPContextMessageFunc;
import com.angryelectron.libgphoto2.Gphoto2Library.va_list;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Simple API for controlling a camera using libgphoto2.
 */
public class GPhoto2 {
            
    Gphoto2Library gphoto2;    
    GPContext context;
    Camera camera;    
    private String error;
    private String message;
    
    /**
     * This will be called when libgphoto2 has an message to share.
     */
    private GPContextErrorFunc errorFunc = new GPContextErrorFunc() {
        @Override
        public void apply(GPContext context, Pointer format, va_list args, Pointer data) {            
            //TODO: parse these errors better            
            error = format.getString(0);            
            Logger.getLogger(GPhoto2.class.getName()).log(Level.DEBUG, "Error: " + error);
        }        
    };
    
    /**
     * This will be called when libgphoto2 has a message to share.
     */
    private GPContextMessageFunc messageFunc = new GPContextMessageFunc() {

        @Override
        public void apply(GPContext context, Pointer format, va_list args, Pointer data) {
            //TODO: parse these errors better
            message = format.getString(0);
            Logger.getLogger(GPhoto2.class.getName()).log(Level.DEBUG, "Message: " + message);
        }
        
    };
        
    /**
     * Constructor.  Loads the native libgphoto2 library and initializes it.
     */
    public GPhoto2() {
        
        PointerByReference ref = new PointerByReference();        
        gphoto2 = (Gphoto2Library) Native.loadLibrary("libgphoto2.so", Gphoto2Library.class);        
        gphoto2.gp_camera_new(ref);
        camera = new Camera(ref.getValue());
                
        context = gphoto2.gp_context_new();
        gphoto2.gp_context_set_error_func(context, errorFunc, null);
        gphoto2.gp_context_set_message_func(context, messageFunc, null);
    }    
    
    /**
     * Open camera connection.  Assumes only 1 camera is connected.  If multiple
     * cameras are connected only the first detected camera is used. 
     * @throws IOException If camera cannot be opened.  Includes ligphoto2 error code.
     */
    public void open() throws IOException {                
        int result = gphoto2.gp_camera_init(camera, context);
        if (result != Gphoto2Library.GP_OK) {
            gphoto2.gp_camera_unref(camera);
            camera = null;
            throw new IOException(error + "(" + result + ")");
        }            
    }
    
    /**
     * Close camera connection.
     */
    public void close() {                
        gphoto2.gp_context_unref(context);                                
        //gphoto2.gp_camera_unref(camera);                
    }
    
    /**
     * Wait (block) until the specified event is received or a timeout occurs.
     * @param timeout Timeout value, in milliseconds
     * @param event expected CameraEventType
     * @throws IOException if Timeout occurs or camera is unreachable.
     */
    private void waitForEvent(int timeout, int event) throws IOException {   
        IntByReference i = new IntByReference();
        PointerByReference data = new PointerByReference();        
        int rc;

        /*
         * need to loop, othewise GP_EVENT_UNKNOWN is almost always returned
         */
        while (true) {
            rc = gphoto2.gp_camera_wait_for_event(camera, timeout, i, data, context);
            if (rc != Gphoto2Library.GP_OK) {                
                throw new IOException("Wait for Event failed with code " + rc);
            }
            if (i.getValue() == event) {
                return;
            } else if (i.getValue() == CameraEventType.GP_EVENT_TIMEOUT) {
                throw new IOException("Timeout occured waiting for event " + event);
            }
        }                                        
    }
    
    /**
     * Capture and Image and create a CameraFilePath that points to the new
     * image on the camera.
     * @return CameraFilePath
     * @throws IOException If image cannot be captured.
     */
    private CameraFilePath captureImage() throws IOException {        
        CameraFilePath cameraFilePath = new CameraFilePath();        
        int result = gphoto2.gp_camera_capture(camera, CameraCaptureType.GP_CAPTURE_IMAGE, cameraFilePath, context);
        if (result != Gphoto2Library.GP_OK) {
            throw new IOException(error + "(" + result + ")");
        }                
        waitForEvent(5000, CameraEventType.GP_EVENT_CAPTURE_COMPLETE);
        return cameraFilePath;
    }
    
    /**
     * Save image to disk in current directory.  TODO:  allow path and
     * filename to be specified.
     * @param path CameraFilePath object returned by captureImage()
     * @return a File which points to the new image.     
     * @throws IOException If the image cannot be saved.
     */
    private File saveImage(CameraFilePath path) throws IOException {        
        String folder = new String(path.folder);
        String name = new String(path.name);  
        int rc;
        
        /* initialize a CameraFile object */
        PointerByReference ref = new PointerByReference();
        rc = gphoto2.gp_file_new(ref);
        if (rc != Gphoto2Library.GP_OK) {
            throw new IOException("gp_file_new failed with code " + rc);
        }
        CameraFile cameraFile = new CameraFile(ref.getValue());
        
        /* point the CameraFile object at the CameraFilePath */
        rc = gphoto2.gp_camera_file_get(camera, folder, name, CameraFileType.GP_FILE_TYPE_NORMAL, cameraFile, context);        
        if (rc != Gphoto2Library.GP_OK) {
            throw new IOException("gp_camera_file_get failed with code " + rc);
        }        
        
        /* save CameraFile to disk */
        rc = gphoto2.gp_file_save(cameraFile, name);
        gphoto2.gp_file_free(cameraFile);
        if (rc != Gphoto2Library.GP_OK) {
            throw new IOException("gp_file_save failed with code " + rc);
        }
        return new File(name);        
    }
    
    /**
     * Take a picture and save it to disk.  Currently images can only
     * be saved into the current working directory.
     * @return a File object which points to the saved image.  
     * @throws IOException If image cannot be captured or saved.
     */
    public File captureAndDownload() throws IOException {
        CameraFilePath path = captureImage();
        return saveImage(path);
    }
    
    /**
     * Take a picture.
     * @return Name of image file on the camera.
     * @throws IOException If the picture cannot be taken.
     */
    public String capture() throws IOException {
        CameraFilePath path = captureImage();        
        return path.name.toString();
    }            
    
    /**
     * Change a single camera setting.  If updating
     * several parameters at once, it may be more efficient to use the {@link
     * com.angryelectron.gphoto2.GPhoto2Config GPhoto2Config} class.
     * class.
     * @param param
     * @param value
     * @throws IOException 
     */
    public void setConfig(String param, String value) throws IOException {
        GPhoto2Config config = new GPhoto2Config(this);
        config.readConfig();
        config.setParameter(param, value);
        config.writeConfig();
    }
    
    /**
     * Read a single camera setting.  If reading several parameters at once it
     * may be more efficient to use the {@link com.angryelectron.gphoto2.GPhoto2Config 
     * GPhoto2Config} class.
     * @param param Parameter to be read
     * @return Value of the parameter
     * @throws IOException if the parameter cannot be read.
     */
    public String getConfig(String param) throws IOException {
        GPhoto2Config config = new GPhoto2Config(this);
        config.readConfig();
        String value = config.getParameter(param);        
        return value;
    }
                
}
