package org.wltea.analyzer.cfg;

import java.io.File;
import java.io.FileFilter;

public class DictionaryFilter implements FileFilter {

	public final static String SUFFIX = "dic";
	
	@Override
	public boolean accept(File file) {
		return file.getName().matches("^.*\\."+SUFFIX);
	}

}
