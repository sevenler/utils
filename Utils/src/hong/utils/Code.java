package hong.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Code {
	
	
	public void parse(){
		//1. 字符串有整型的相互转换 
		String a = String.valueOf(2);
		@SuppressWarnings("unused")
		int i = Integer.parseInt(a); 
	}
	
	/**
	 * 向文件末尾添加内容 
	 * @throws IOException
	 */
	public void superaddition() throws IOException{
		//
		String file_name="";
		BufferedWriter out = null; 
		try { 
			out = new BufferedWriter(new FileWriter(file_name, true)); 
			out.write("add string...."); 
		} catch (IOException e) { 
			
		} finally { 
			if (out != null) { 
				out.close(); 
			}
		}
	}
	
	/**
	 * 得到当前方法的名字
	 * @return
	 */
	public String getCurrentMethedName(){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		return methodName;
	}
	
	/**
	 * 转字符串到日期
	 * @return
	 * @throws ParseException 
	 */
	public Date parse(String date_str) throws ParseException{
		Date date = java.text.DateFormat.getDateInstance().parse(date_str);
		SimpleDateFormat format = new SimpleDateFormat( "dd.MM.yyyy" );
		date = format.parse(date_str);
		return date;
	}
	
	/**
	 * 使用NIO进行快速的文件拷贝
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void fileCopy(File in,File out)throws IOException 
	{ 
		 FileChannel inChannel = new FileInputStream( in ).getChannel(); 
		 FileChannel outChannel = new FileOutputStream( out ).getChannel(); 
		 try 
		 { 
			 int maxCount = (64 * 1024 * 1024) - (32 * 1024); 
			 long size = inChannel.size(); 
			 long position = 0; 
			 while ( position < size ){ 
				 position += inChannel.transferTo( position, maxCount, outChannel ); 
			 } 
		 } 
		 finally 
		 { 
			 if ( inChannel != null ) { 
				 inChannel.close(); 
			 } 
			 if ( outChannel != null ) { 
				 outChannel.close(); 
			 } 
		 } 
	}
	
	
	public static void getDirectorysAndFiles(String directory){
		 File dir = new File(directory); 
		 String[] children = dir.list(); 
		 if (children == null) { 
		 } else { 
			 for (int i=0; i < children.length; i++) { 
				 String filename = children[i];
				 System.out.println("all of dirctory:"+filename);
			 } 
		 }
		 //过滤得到目录或子目录中中含有index的文件
		 FilenameFilter filter = new FilenameFilter() { 
			 public boolean accept(File dir, String name) { 
				 return name.indexOf("beta2")!=-1; 
			 } 
		 }; 
		 String[] files = dir.list(filter);
		 for(String file : files){
			 System.out.println("... file:"+file);
		 }
		 //过滤得到目录
		 FileFilter fileFilter = new FileFilter() { 
			 public boolean accept(File file) { 
				 return file.isDirectory(); 
			 } 
		 }; 
		 File[] dirs = dir.listFiles(fileFilter);
		 for(File dire : dirs){
			 System.out.println("// directory:"+dire.getName());
		 }
	}
	
	/**
	 * 创建压缩文件
	 * @param target_name 压缩目标文件名，文件名带路径
	 * @param dir_path 压缩来源文件路径
	 * @param from_file 压缩来源文件名数组
	 * @throws IOException
	 */
	 public static void createJarOrZip(String target_name,String dir_path,String[] from_file) throws IOException { 
		 File zipFile = new File(target_name); 
		 if (zipFile.exists()) { 
			 throw new IllegalArgumentException("Zip file already exists, please try another");
		 } 
		 FileOutputStream fos = new FileOutputStream(zipFile); 
		 ZipOutputStream zos = new ZipOutputStream(fos); 
		 int bytesRead; 
		 byte[] buffer = new byte[1024]; 
		 CRC32 crc = new CRC32(); 
		 for (int i=0, n=from_file.length; i < n; i++) { 
			 String name = from_file[i]; 
			 File file = new File(dir_path+"\\"+name); 
			 if (!file.exists()) { 
				 if(zos!=null){
					 zos.closeEntry();
				 }
				 System.out.println("----");
				 zipFile.delete();
				 throw new IllegalArgumentException("no such an directory or file: " + dir_path+"\\"+name); 
			 } 
			 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)); 
			 crc.reset(); 
			 while ((bytesRead = bis.read(buffer)) != -1) { 
				 crc.update(buffer, 0, bytesRead); 
			 } 
			 bis.close(); 
			 bis = new BufferedInputStream(new FileInputStream(file)); 
			 ZipEntry entry = new ZipEntry(name); 
			 entry.setMethod(ZipEntry.STORED); 
			 entry.setCompressedSize(file.length()); 
			 entry.setSize(file.length()); 
			 entry.setCrc(crc.getValue()); 
			 zos.putNextEntry(entry); 
			 while ((bytesRead = bis.read(buffer)) != -1) { 
				 zos.write(buffer, 0, bytesRead); 
			 } 
			 bis.close(); 
		 } 
		 zos.close(); 
	}
	 
	 public static void arrayToMap() { 
		 
		/* String[ ][ ] countries = { { "United States", "New York" }, { "United Kingdom", "London" }, 
		 { "Netherland", "Amsterdam" }, { "Japan", "Tokyo" }, { "France", "Paris" } };
		 */
		 //Map countryCapitals = ArrayUtils.toMap(countries);

		// System.out.println("Capital of Japan is " + countryCapitals.get("Japan")); 
		 //System.out.println("Capital of France is " + countryCapitals.get("France")); 
	} 
	 
	 public void postMail( String recipients[ ], String subject, String message , String from) 
			 /*throws MessagingException*/ 
	 { 
		  /*boolean debug = false;
		  //Set the host smtp address 
		  Properties props = new Properties(); 
		  props.put("mail.smtp.host", "smtp.example.com");
		  // create some properties and get the default Session 
		  Session session = Session.getDefaultInstance(props, null); 
		  session.setDebug(debug);
		  // create a message 
		  Message msg = new MimeMessage(session);
		  // set the from and to address 
		  InternetAddress addressFrom = new InternetAddress(from); 
		  msg.setFrom(addressFrom);
		  InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
		  for (int i = 0; i < recipients.length; i++) 
		  { 
			  addressTo[i] = new InternetAddress(recipients[i]); 
		  } 
		  msg.setRecipients(Message.RecipientType.TO, addressTo);
		  // Optional : You can also set your custom headers in the Email if you Want 
		  msg.addHeader("MyHeaderName", "myHeaderValue");
		  // Setting the Subject and Content Type 
		  msg.setSubject(subject); 
		  msg.setContent(message, "text/plain"); 
		  Transport.send(msg);*/ 
	 }
	 
	 public static Object resizeArray(Object oldArray,int newSize) { 
		 int oldSize = java.lang.reflect.Array.getLength(oldArray); 
		 @SuppressWarnings("rawtypes")
		 Class elementType = oldArray.getClass().getComponentType(); 
		 Object newArray = java.lang.reflect.Array.newInstance(elementType,newSize); 
		 int preserveLength = Math.min(oldSize,newSize); 
		 if (preserveLength > 0) 
			 System.arraycopy (oldArray,0,newArray,0,preserveLength); 
		 return newArray; 
	}
	
	public static void main(String [] args){
		//测试创建压缩文件函数
		String sys_dir=System.getProperty("user.dir");
		String [] from={"files\\students.xml"};
		try {
			createJarOrZip(sys_dir+"\\files\\students.zip",sys_dir,from);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
