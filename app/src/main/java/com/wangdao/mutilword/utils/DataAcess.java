package com.wangdao.mutilword.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *  for interpret part.
 */
public class DataAcess {

	public static InputStream getStreamByUrl(String _url)
	{
		
		InputStream in=null;
		try {
			System.out.println("-----------------"+_url);
			URL url = new URL(_url);			
		    HttpURLConnection conn=(HttpURLConnection) url.openConnection(); 
		    conn.connect();
		    in=conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}     
		
		/*HttpClient client=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(_url);
		try {
			HttpResponse response=client.execute(httpGet);
			in=response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return in;
	}
	
}
