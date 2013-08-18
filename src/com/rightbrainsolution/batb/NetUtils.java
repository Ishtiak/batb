package com.rightbrainsolution.batb;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetUtils {
	private static String m_domain = "http://emicrodevsite.com/batb_test_api/api.php";
	
	public static String getDomain()
	{
		return m_domain;
	}
	public static String formPostRequestAndGetData(String url, 
			List<NameValuePair> parameters) {
        HttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(m_domain + url);
        HttpResponse response;
        InputStream is;
        
        try {  
            httppost.setEntity(new UrlEncodedFormEntity(parameters));
            response = httpclient.execute(httppost);              
			is = response.getEntity().getContent();
        } catch (ClientProtocolException e) {  
            return null; 
        } catch (IOException e) {  
            return null;  
        }  
		
        StringBuilder sb = new StringBuilder();
        String line;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        	while ((line = reader.readLine()) != null) {
        		sb.append(line).append("\n");
        	}
        	is.close();
        	System.err.print(sb.toString());
			return sb.toString();
        } catch (UnsupportedEncodingException e) {
        	try {
				is.close();
			} catch (IOException e1) {
			}
			return null;
		} catch (IOException e) {
			try {
				is.close();
			} catch (IOException e1) {
			}
			return null;
		} 
	}

	public static JSONObject formGetRequestAndGetData(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        //httpclient.
        HttpGet httpget = new HttpGet(m_domain + url);
        System.out.println( m_domain + url);
        HttpResponse response;
        InputStream is;
        
        try {  
            response = httpclient.execute(httpget);              
			is = response.getEntity().getContent();
        } catch (ClientProtocolException e) {  
            return null; 
        } catch (IOException e) {  
            return null;  
        }  
		
        StringBuilder sb = new StringBuilder();
        String line;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        	while ((line = reader.readLine()) != null) {
        		sb.append(line).append("\n");
        	}
        	is.close();
        	System.err.print(sb.toString());
			return new JSONObject(sb.toString());
        } catch (UnsupportedEncodingException e) {
        	try {
				is.close();
			} catch (IOException e1) {
			}
			return null;
		} catch (IOException e) {
			try {
				is.close();
			} catch (IOException e1) {
			}
			return null;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static String MultiPartFormDataPost(Hashtable<String,String>parameters,Hashtable<String,String>files,String methodeName)
	{
		// TODO Auto-generated method stub
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String response = "";
		
		// Is this the place are you doing something wrong.

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;

		byte[] buffer;

		int maxBufferSize = 1 * 1024 * 1024;

		// String responseFromServer = "";

		String urlString = NetUtils.getDomain()+methodeName;
		StringBuilder requestBuilder = new StringBuilder();

		try {
			// open a URL connection to the Servlet
			
			URL url = new URL(urlString);
			
			
			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) url.openConnection();

			//set herader token
			//conn.getHeaderFields().put("token", )
			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			

			dos = new DataOutputStream(conn.getOutputStream());
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			requestBuilder.append(twoHyphens + boundary + lineEnd);
			
		 if( parameters != null )
		 { Enumeration<String>keys = parameters.keys();
			//Keys.
			// ------------------ CLIENT REQUEST
           while( keys.hasMoreElements() )
           {
				
				//String exsistingFileName = Environment.getExternalStorageDirectory()+"/"+params[i];				

				//
        	   String key = keys.nextElement();
				if( !key.equalsIgnoreCase("image"))
				{	
					//dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\";"+lineEnd+lineEnd
						       +parameters.get(key)+lineEnd);
					requestBuilder.append("Content-Disposition: form-data; name=\""+key+"\";"+lineEnd+lineEnd
						       +parameters.get(key)+lineEnd);
					requestBuilder.append(twoHyphens + boundary + lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
				}


           }
		 }
           
           if( files != null)
           {
           
	           Enumeration<String> filesKey = files.keys();
	           while(filesKey.hasMoreElements())
	           {
	        	   String key = filesKey.nextElement();
	
						requestBuilder.append("Content-Disposition: form-data; name=\""+key+"\";filename=\""
								+ "temImage.jpg" + "\"" + lineEnd+lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\";filename=\""
								+ "temImage.jpg" + "\"" + lineEnd);
				               dos.writeBytes(lineEnd);
				               
						FileInputStream fileInputStream = new FileInputStream(new File(files.get(key)));
						// create a buffer of maximum size
	
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
	
						// read file and write it into form...
	
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							requestBuilder.append(buffer);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math.min(bytesAvailable, maxBufferSize);
							bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						}
						dos.writeBytes(lineEnd);
						requestBuilder.append(lineEnd);
						requestBuilder.append(twoHyphens + boundary + lineEnd);
						dos.writeBytes(twoHyphens + boundary + lineEnd);
	
						// send multipart form data necesssary after file data...
	
						
						fileInputStream.close();
				
	           }
           }
			// close streams
			Log.e("MediaPlayer", "File is written");
			//Log.e("Request:",requestBuilder.toString());
			System.out.println(requestBuilder.toString());
			//dos.writeBytes(requestBuilder.toString());
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
		}

		// ------------------ read the SERVER RESPONSE

		try 
		{
			inStream = new DataInputStream(conn.getInputStream());
			StringBuilder strBuilder = new StringBuilder();
			

			while ((response = inStream.readLine()) != null) {
				
				strBuilder.append( response );
			}
			
			response = strBuilder.toString();
			
			Log.e("Public Stuff:", "Server Response" + response);

		} catch (IOException ioex) {
			Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
		}
		
		return response;
	}
	
	
}

