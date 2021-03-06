package mvopAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TxtUtil {
	/**
	  * 创建目录
	 * @param destDirName
	 * @return
	 */
	public static boolean createDir(String destDirName) {  
		File dir = new File(destDirName);  
		if (dir.exists()) {  
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");  
			return false;  
		}  
		if (!destDirName.endsWith(File.separator)) {  
			destDirName = destDirName + File.separator;  
		}  
		//创建目录  
		if (dir.mkdirs()) {  
			System.out.println("创建目录" + destDirName + "成功！");  
			return true;  
		} else {  
			System.out.println("创建目录" + destDirName + "失败！");  
			return false;  
		}  
	}  
	
	/**
	 * 创建文件
	 * @param destFileName
	 * @return
	 */
	public static boolean createFile(String destFileName) {  
		File file = new File(destFileName);  
		if(file.exists()) {  
			/*System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  */
			return false;  
		}  
		if (destFileName.endsWith(File.separator)) {  
			/*System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  */
			return false;  
		}  
		//判断目标文件所在的目录是否存在  
		if(!file.getParentFile().exists()) {  
			//如果目标文件所在的目录不存在，则创建父目录  
			/*System.out.println("目标文件所在目录不存在，准备创建它！");*/  
			if(!file.getParentFile().mkdirs()) {  
				/*System.out.println("创建目标文件所在目录失败！");*/  
				return false;  
			}  
		}  
		//创建目标文件  
		try {  
			if (file.createNewFile()) {  
				/*System.out.println("创建单个文件" + destFileName + "成功！");*/  
				return true;  
			} else {  
				/*System.out.println("创建单个文件" + destFileName + "失败！");  */
				return false;  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
			/*System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage()); */ 
			return false;  
		}  
	}  
	
	/**
	 * 写入文本txt
	 * @param content
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static boolean writeTxtFile(String content,String fileName)throws Exception{  
		RandomAccessFile mm=null;  
		boolean flag=false;  
		FileWriter o=null;  
		try {  
			o = new FileWriter(fileName, true);  
			o.write(content);  
			o.close();  
			flag=true;  
		} catch (Exception e) {  
			// TODO: handle exception  
			e.printStackTrace();  
		}finally{  
			if(mm!=null){  
				mm.close();  
			}  
		}  
		return flag;  
	}  
	
	
	
	
	
}





