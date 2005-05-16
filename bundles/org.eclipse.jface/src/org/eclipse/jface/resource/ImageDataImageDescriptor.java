/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.resource;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * @since 3.1
 */
class ImageDataImageDescriptor extends ImageDescriptor {

    private ImageData data;
    private Image originalImage = null;
    private Device originalDevice = null;
    
    /**
     * Creates an image descriptor, given an image and the device it was created on.
     * 
     * @param originalImage
     * @param originalDevice
     */
    ImageDataImageDescriptor(Image originalImage, Device originalDevice) {
        this(originalImage.getImageData());
        this.originalImage = originalImage;
        this.originalDevice = originalDevice;
    }
    
    /**
     * Creates an image descriptor, given an image.
     * 
     * @param originalImage
     */
    ImageDataImageDescriptor(Image originalImage) {
        this(originalImage.getImageData());
        this.originalImage = originalImage;
    }
    
    /**
     * Creates an image descriptor, given some image data.
     * 
     * @param data describing the image
     */

    ImageDataImageDescriptor(ImageData data) {
        this.data = data;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.resource.DeviceResourceDescriptor#create(org.eclipse.swt.graphics.Device)
     */
    public Object createResource(Device device) throws DeviceResourceException {

        // If this descriptor is an existing font, then we can return the original font
        // if this is the same device.
        if (originalImage != null) {

            // If we don't know what device the original was allocated on, we can't tell if we
            // can reuse the original until we try to create a new one.
            if (originalDevice == null) {
                Image result = createImage(false, device);
                if (result == null) {
                    throw new DeviceResourceException(this);
                }
                       
                // If this new font was equal to the original, then it must have been allocated
                // on the same device. 
                if (result.equals(originalImage)) {
                    // We now know the original device. We can reuse the original image,
                    // discard the temporary Image, and remember the device for the next time
                    // this descriptor is used.
                    result.dispose();
                    originalDevice = device;
                    return originalImage;
                }
                // The newly created font ended up being different from the original, so
                // it may have been allocated on a different device. Return the new version.
                return result;
            }
         
            // If we're allocating on the same device as the original font, return the original.
            if (originalDevice == device) {
                return originalImage;
            }            
        }
        
        return super.createResource(device);
    }
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.resource.DeviceResourceDescriptor#destroy(java.lang.Object)
     */
    public void destroyResource(Object previouslyCreatedObject) {
        if (previouslyCreatedObject == originalImage) {
            return;
        }
        
        super.destroyResource(previouslyCreatedObject);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
     */
    public ImageData getImageData() {
        return data;
    }
    
    /* (non-Javadoc)
     * @see Object#hashCode
     */
    public int hashCode() {
        return data.hashCode();
    }

    /* (non-Javadoc)
     * @see Object#equals
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ImageDataImageDescriptor))
            return false; 
        
        ImageDataImageDescriptor imgWrap = (ImageDataImageDescriptor) obj;
       
        if (originalImage != null) {
            return imgWrap.originalImage == originalImage;
        }
        
        return (imgWrap.originalImage == null && data.equals(imgWrap.data));
    }
    
}
