package com.rest.utils;

import org.apache.commons.io.FileUtils;
import java.io.File;

public class CustomFileReader {

    public String readFile(String location) throws Exception {
      ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource(location).getFile());
      String data = FileUtils.readFileToString(file,"UTF-8");
      return data;
    }
}
