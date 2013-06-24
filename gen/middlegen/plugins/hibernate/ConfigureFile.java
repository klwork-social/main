package middlegen.plugins.hibernate;

import java.io.*;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ConfigureFile {

	private final static Logger log = Logger.getLogger(ConfigureFile.class);
	
	private static Properties properties = null;
	private static String config = "middlegen.properties";
	private static ConfigureFile configurefile = null;

	public static ConfigureFile getInstance() {
		if(configurefile!=null)
			return configurefile;
		else
		{
			configurefile = new ConfigureFile();
			properties = new Properties();
			init();
			return configurefile;
		}
	}
	
	public static void init() {
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(config));
			if(file!=null) {
				properties.load(file);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			log.debug("Exception throwed by the init method",e);
		}finally{
			if(file != null){
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static void main(String[] args) {
		ConfigureFile  configureFile = ConfigureFile.getInstance();
		System.out.println("====daoImpl.dir=============="+configureFile.getProperty("daoImpl.dir"));
	}
	
}
