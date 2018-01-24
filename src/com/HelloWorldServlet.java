package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet("/HelloWorld")
public class HelloWorldServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(HelloWorldServlet.class);
	private ScheduledExecutorService service;
	private String urlPath, source, destination, temp_path;
	private ReentrantLock lock;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HelloWorldServlet() {
		super();
		System.setProperty("app.root", "/tmp/tomcat/logs");
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("resources/log4j.properties"));
		
	}

	public void init() throws ServletException {
		
		threadFunc();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("HelloWorld Testing Code");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	public void destroy() {
		super.destroy();

		try {

			
			service.shutdown();

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void threadFunc() {
		
		String resourceName = "resources/thread.properties"; // could also be a
																// constant
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		int num_threads = 0, next = 0, delay = 0;
		boolean download= true,randomThread=false;
		lock = new ReentrantLock();
		try {
			
			InputStream resourceStream = loader
					.getResourceAsStream(resourceName);
			
			if (resourceStream != null) {
				props.load(resourceStream);
				
				num_threads = Integer.parseInt(System.getProperty("threads","3"));
				
				delay = Integer.parseInt(System.getProperty("delay","8"));
				next = Integer.parseInt(System.getProperty("next","4"));
				download = Boolean.parseBoolean(System.getProperty("download","true"));
				randomThread = Boolean.parseBoolean(System.getProperty("random","true"));
				logger.info("Properties values: thread :"+ num_threads+":: delay "+delay+" ::download:"+download+"::randomThread :"+randomThread);
				
				
				if ( download == true )
				{
				    urlPath = props.getProperty("url");
				    source = props.getProperty("source_path");
				}
				else
				{
					ClassLoader classLoader = getClass().getClassLoader();
				    String path  = classLoader.getResource("resources/Python35_x64.zip").getPath();
					source=path;
				}
				
				destination = props.getProperty("dest_path");
				temp_path = props.getProperty("temp_path");
			} else {
				num_threads = 3;
				delay = 5;
				next = 10;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ( randomThread== true )
		{
		final Random random = new Random();
        final long maxSleepTime=600L;
        for (int i = 0; i < 10; i++) {
            int randomSleepTime = random.nextInt((int) maxSleepTime);
            service = Executors.newScheduledThreadPool(num_threads);
			AsyncWebService srvc = new AsyncWebService();
			service.scheduleAtFixedRate(srvc,randomSleepTime,delay,TimeUnit.SECONDS);
        }
		}else
		{
		

			if (num_threads > 0) {
				service = Executors.newScheduledThreadPool(num_threads);
				AsyncWebService srvc = new AsyncWebService();
				service.scheduleAtFixedRate(srvc, next, delay, TimeUnit.SECONDS);
		}
		}	

	}

	public class AsyncWebService implements Runnable {

		public AsyncWebService() {

		}

		public void run() {

			long threadId = Thread.currentThread().getId();

			logger.info("Thread # " + threadId + " is doing this task");

			extract();
			boolean download = Boolean.parseBoolean(System.getProperty("download","true"));
			String dest="";
			if (download==true)
			   dest = temp_path;
			else
			{
				dest=destination+"/Python35_x64";
				logger.info("Destination directory"+dest);
			}
			

			try {

				//Thread.sleep(50);
				// lock.lock() ;
				FileUtils.deleteDirectory(new File(dest));
				// lock.unlock();
				logger.info("temp dir deleted"
						+ Thread.currentThread().getId());

			} catch (IOException e) {

				//e.printStackTrace();
			}

			catch (Exception e) {
				//e.printStackTrace();
			}
		
		}

	}

	private void extract() {
		try {

			boolean download = Boolean.parseBoolean(System.getProperty("download","true"));
			if( download== true)
			{
			
				URL url = new URL(urlPath);
			
				File destination_path = new File(source);
				logger.info(source);

			
				Thread.sleep(50);
				FileUtils.copyURLToFile(url, destination_path);
			// lock.unlock();
				logger.info("Download completed");
			}
			
			logger.info("Extraction started");

			Thread.sleep(50);

			unzip(source, destination);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		logger.info("return from extract");
	}

	private void unzip(String sourceFile, String outputFolder) {
		
		ZipInputStream is = null;
		OutputStream os = null;
		int BUFF_SIZE = 4096;

		

		try {
			ZipFile zipFile = new ZipFile(sourceFile);

			// Get a list of FileHeader. FileHeader is the header information
			// for all the files in the ZipFile
			List fileHeaderList = zipFile.getFileHeaders();
			// Loop through all the fileHeaders
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
				if (fileHeader != null) {
					// Build the output file
					String outFilePath = outputFolder
							+ System.getProperty("file.separator")
							+ fileHeader.getFileName();
					File outFile = new File(outFilePath);
					// Checks if the file is a directory
					if (fileHeader.isDirectory()) {
						
						// For now  create the directory
						outFile.mkdirs();
						continue;
					}
					// Check if the directories(including parent directories)
					// in the output file path exists
					File parentDir = outFile.getParentFile();
					if (!parentDir.exists()) {
						parentDir.mkdirs();
					}
					// Get the InputStream from the ZipFile
					is = zipFile.getInputStream(fileHeader);
					// Initialize the output stream
					os = new FileOutputStream(outFile);
					int readLen = -1;
					byte[] buff = new byte[BUFF_SIZE];
					// Loop until End of File and write the contents to the
					// output stream
					while ((readLen = is.read(buff)) != -1) {
						os.write(buff, 0, readLen);
					}
					// Please have a look into this method for some important
					// comments
					closeFileHandlers(is, os);
					// To restore File attributes (ex: last modified file time,
					// read only flag, etc) of the extracted file, a utility
					// class can be used as shown below
					UnzipUtil.applyFileAttributes(fileHeader, outFile);
					//logger.info("Done extracting: "
					//		+ fileHeader.getFileName());
				} else {
					System.err.println("fileheader is null. Shouldn't be here");
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeFileHandlers(is, os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		logger.info("return from extraction");
		// create output directory is not exists

	}

	void closeFileHandlers(ZipInputStream is, OutputStream os)
			throws IOException {
		// Close output stream
		if (os != null) {
			os.close();
			os = null;
		}
		// Closing inputstream also checks for CRC of the the just extracted
		// file. If CRC check has to be skipped (for ex: to cancel the unzip
		// operation, etc) use method is.close(boolean skipCRCCheck) and set the
		// flag, skipCRCCheck to false
		// NOTE: It is recommended to close outputStream first because Zip4j
		// throws an exception if CRC check fails
		if (is != null) {
			is.close();
			is = null;
		}
	}

}
