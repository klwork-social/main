package com.klwork.business.domain.service;

/**
 * 
 */

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {

	/**
	 * 源图片路径名称如:c:/1.jpg
	 */
	private String srcpath = "f:/temp/a.jpg";

	public ImageUtil() {

	}

	public static void main(String[] args) throws Exception {
		ImageUtil util = new ImageUtil();
		//util.getImageSizeByImageReader(util.getSrcpath());
		util.getImageSizeByBufferedImage(util.getSrcpath());
		System.out.println(queryURLImageSize("http://app.qpic.cn/mblogpic/b926ed928429f551b360/2000"));
	}

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
			e.printStackTrace();
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

	/**
	 * 使用ImageReader获取图片尺寸
	 * 
	 * @param src
	 *            源图片路径
	 */
	public void getImageSizeByImageReader(String src) {
		long beginTime = new Date().getTime();
		File file = new File(src);
		try {
			Iterator<ImageReader> readers = ImageIO
					.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			reader.setInput(iis, true);
			System.out.println("width:" + reader.getWidth(0));
			System.out.println("height:" + reader.getHeight(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = new Date().getTime();
		System.out.println("使用[ImageReader]获取图片尺寸耗时：[" + (endTime - beginTime)
				+ "]ms");
	}

	/**
	 * 使用BufferedImage获取图片尺寸
	 * 
	 * @param src
	 *            源图片路径
	 */
	public void getImageSizeByBufferedImage(String src) {
		long beginTime = new Date().getTime();
		File file = new File(src);
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		BufferedImage sourceImg = null;
		try {
			sourceImg = javax.imageio.ImageIO.read(is);
			System.out.println("width:" + sourceImg.getWidth());
			System.out.println("height:" + sourceImg.getHeight());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		long endTime = new Date().getTime();
		System.out.println("使用[BufferedImage]获取图片尺寸耗时：["
				+ (endTime - beginTime) + "]ms");
	}

	public String getSrcpath() {
		return srcpath;
	}

	public void setSrcpath(String srcpath) {
		this.srcpath = srcpath;
	}

}