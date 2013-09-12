package org.wltea.analyzer.cfg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


public class StandardConfig implements Configuration {
	static Logger logger = Logger.getLogger(StandardConfig.class.getName());
	/*
	 * 分词器默认字典路径 
	 */
	private static final String PATH_DIC_MAIN = "org/wltea/analyzer/dic/main2012.dic";
	private static final String PATH_DIC_QUANTIFIER = "org/wltea/analyzer/dic/quantifier.dic";

	/*
	 * 分词器配置文件路径
	 */	
	private static final String FILE_NAME = "IKAnalyzer.cfg.xml";
	//配置属性——扩展字典
	private static final String EXT_DICT = "ext";
	//配置属性——扩展停止词典
	private static final String EXT_STOP = "stopwords";
	
	private Properties props;
	
	private String defaultPath;
	
	/*
	 * 是否使用smart方式分词
	 */
	private boolean useSmart;
	
	public static Configuration getInstance(){
		return new StandardConfig();
	}
	
	private StandardConfig(){		
		props = new Properties();
		//logger.debug("读取配置文件:"+this.getClass().getClassLoader().getResource(FILE_NAME).getPath());
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		if(input != null){
			try {
				props.loadFromXML(input);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String path = URLDecoder.decode(this.getClass().getClassLoader().getResource(FILE_NAME).getPath(),"UTF-8");
			File file = new File(path);
			defaultPath = file.getParent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> getExtDictionarys() {
		List<String> extDictFiles = new ArrayList<String>(2);
		
		File file = new File(this.getExtDictionaryPath());
		if(file.exists() && file.isDirectory()){
			File [] dics = file.listFiles(new DictionaryFilter());
			for(int i=0;i<dics.length;i++){
				extDictFiles.add(dics[i].getPath());
			}
		}
		return extDictFiles;
	}

	@Override
	public List<String> getExtStopWordDictionarys() {
		List<String> extDictFiles = new ArrayList<String>(2);
		String path = this.getExtStopDictionaryPath();
		if(path == null){
			return extDictFiles;
		}
		File file = new File(path);
		System.out.println(file.getPath());
		if(file.exists() && file.isDirectory()){
			File [] dics = file.listFiles(new DictionaryFilter());
			for(int i=0;i<dics.length;i++){
				extDictFiles.add(dics[i].getPath());
			}
		}
		return extDictFiles;
	}

	@Override
	public String getMainDictionary() {
		return PATH_DIC_MAIN;
	}

	@Override
	public String getQuantifierDicionary() {
		return PATH_DIC_QUANTIFIER;
	}

	@Override
	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}

	@Override
	public boolean useSmart() {
		return useSmart;
	}
	
	public String getExtDictionaryPath(){
		String extDictCfg = props.getProperty(EXT_DICT);
		if(extDictCfg == null) return null;
		File absoluteDir = new File(extDictCfg);
		File relativeDir = new File(this.defaultPath+File.separator+extDictCfg);
		if(absoluteDir != null && absoluteDir.exists()){
			return absoluteDir.getPath();
		}
		
		if(relativeDir != null && relativeDir.exists()){
			return relativeDir.getPath();
		}else{
			throw new IllegalStateException(extDictCfg+"不存在");
		}		
	}
	
	public String getExtStopDictionaryPath(){
		String extDictCfg = props.getProperty(EXT_STOP);
		if(extDictCfg == null) return null;
		File absoluteDir = new File(extDictCfg);
		File relativeDir = new File(this.defaultPath+File.separator+extDictCfg);
		if(absoluteDir != null && absoluteDir.exists()){
			return absoluteDir.getPath();
		}
		
		if(relativeDir != null && relativeDir.exists()){
			return relativeDir.getPath();
		}else{
			throw new IllegalStateException(extDictCfg+"不存在");
		}	
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		Configuration config = StandardConfig.getInstance();
		List<String> list = config.getExtDictionarys();
		File main = new File(URLDecoder.decode(StandardConfig.class.getClassLoader().getResource(config.getMainDictionary()).getFile(),"UTF-8"));
		System.out.println(main.getPath()+"  "+main.exists());
		System.out.println("ExtPath: "+config.getExtDictionaryPath());
		for(String ff : list){
			System.out.println(ff);			
		}
	}

}
