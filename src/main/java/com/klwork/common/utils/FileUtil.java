package com.klwork.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FileUtil {
	private final static int BUFFER_SIZE = 16 * 1024;
	
	private static void copy(InputStream src, File dst) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = src;
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}
	
	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}
	
	/**
	 * 把freemark的模板转换成StringBuffer
	 * @param clazz
	 * @param fName
	 * @param pathPrefix
	 * @param root
	 * @return
	 */
	public static StringBuffer getContetByFreemarker(Class clazz,String fName, String pathPrefix,
			Map root) {
		Template t;
		Configuration cfg;
		try {
			cfg = new Configuration();
			cfg.setLocalizedLookup(false);
			cfg.setClassForTemplateLoading(clazz, pathPrefix);
			t = cfg.getTemplate(fName, "UTF-8");
			StringWriter stringWriter = new StringWriter();
			BufferedWriter writer = new BufferedWriter(stringWriter);
			// t.setEncoding("UTF-8");
			t.process(root, writer);
			// StringReader reader = new StringReader(stringWriter.toString());
			StringBuffer b = stringWriter.getBuffer();
			writer.flush();
			writer.close();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	 /**
     * Write the content to a file.
     * */
    public static boolean writeFile(String content, String filePath){
        //File file = new File(filePath);
        try {
            //BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
        	OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
            bw.write(content);
            bw.write(" ");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
