/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klwork.explorer.ui.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.klwork.explorer.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.thebuzzmedia.imgscalr.Scalr;
//import com.thebuzzmedia.imgscalr.Scalr.Mode;


/**
 * @author Joram Barrez
 */
public class ImageUtil {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class.getName());

  /**
   * Resizes the given image (passed as {@link InputStream}.
   * If the image is smaller then the given maximum width or height, the image
   * will be proportionally resized.
   */
  public static InputStream resizeImage(InputStream imageInputStream, String mimeType, int maxWidth, int maxHeight) {
    /*try {
      BufferedImage image = ImageIO.read(imageInputStream);
      
      int width = Math.min(image.getWidth(), maxWidth);
      int height = Math.min(image.getHeight(), maxHeight);
      
      Mode mode = Mode.AUTOMATIC;
      if (image.getHeight() > maxHeight) {
        mode = Mode.FIT_TO_HEIGHT;
      }
      
      if (width != image.getWidth() || height != image.getHeight()) {
        image = Scalr.resize(image, mode, width, height);
      }
      
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ImageIO.write(image, Constants.MIMETYPE_EXTENSION_MAPPING.get(mimeType), bos);
      return new ByteArrayInputStream(bos.toByteArray());
    } catch (IOException e) {
      LOGGER.error("Exception while resizing image", e);
      return null;
    }*/
	  return null;
  }
  	
   /**
   * 查询一个图片的原始大小
   * @param url
   * @return
   */
	public static String queryURLImageSize(String url) {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
			Iterator<ImageReader> readers = ImageIO
					.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			System.out.println("width:" + reader.getWidth(0));
			System.out.println("height:" + reader.getHeight(0));
			return reader.getWidth(0) + "," +  reader.getHeight(0);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.info("出错的图片url:" + url + " " + e.getMessage());
			//e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
