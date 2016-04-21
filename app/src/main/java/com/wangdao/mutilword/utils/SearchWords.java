package com.wangdao.mutilword.utils;

import com.wangdao.mutilword.bean.interpretBean.Dict;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/**
 *  for interpret part.
 */
public class SearchWords {

	private final static String URL="http://dict-co.iciba.com/api/dictionary.php?&key=A11838A53DFCDEA01EFAD1A205C7FE17&w=";
	
	public static Dict transWord(String word){
		System.out.println("---------------------------"+URL+word);
		InputStream in=DataAcess.getStreamByUrl(URL+word);
		
		
		//Dict dict=PullXmlHelper.pullParseXml(in);
		Dict dict=null;
		 SAXParser parser = null;  
	        try {  
	            //SAXParser
	            parser = SAXParserFactory.newInstance().newSAXParser();  
	            //  DefaultHandler
	            SaxParseXml parseXml=new SaxParseXml();  
	            //parse()
	            parser.parse(in, parseXml);
	            
	            dict=parseXml.getDict();
	            
	        }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}
		
		return dict;
		
	}
	
}
