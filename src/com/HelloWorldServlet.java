package com;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet("/HelloWorld")
public class HelloWorldServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ScheduledExecutorService service;
	private String urlPath, source, destination, temp_path;
	private ReentrantLock lock;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HelloWorldServlet() {
		super();
		
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
		lock = new ReentrantLock();
		try {
			
			InputStream resourceStream = loader
					.getResourceAsStream(resourceName);
			
			if (resourceStream != null) {
				props.load(resourceStream);
				//System.out.println(props.getProperty("NUM_THREADS"));
				num_threads = Integer
						.parseInt(props.getProperty("NUM_THREADS"));
				delay = Integer.parseInt(props.getProperty("delay"));
				next = Integer.parseInt(props.getProperty("next"));
				urlPath = props.getProperty("url");
				source = props.getProperty("source_path");
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

		service = Executors.newScheduledThreadPool(num_threads);
		AsyncWebService srvc = new AsyncWebService();
		service.scheduleAtFixedRate(srvc, next, delay, TimeUnit.MILLISECONDS);

	}

	public class AsyncWebService implements Runnable {

		public AsyncWebService() {

		}

		public void run() {

			long threadId = Thread.currentThread().getId();

			System.out.print("Thread # " + threadId + " is doing this task");

			extract();
			
			String dest = temp_path;

			try {

				Thread.sleep(50);
				// lock.lock() ;
				FileUtils.deleteDirectory(new File(dest));
				// lock.unlock();
				System.out.println("temp dir deleted"
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
			
			URL url = new URL(urlPath);
			
			File destination_path = new File(source);
			System.out.println(source);

			
			Thread.sleep(50);
			FileUtils.copyURLToFile(url, destination_path);
			// lock.unlock();
			System.out.println("Download completed");
			
			System.out.println("Extraction started");

			Thread.sleep(50);

			unzip(source, destination);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		System.out.println("return from extract");
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
					//System.out.println("Done extracting: "
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

		System.out.println("return from extraction");
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
