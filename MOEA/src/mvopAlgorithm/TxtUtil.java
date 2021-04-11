package mvopAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TxtUtil {
	/**
	  * ����Ŀ¼
	 * @param destDirName
	 * @return
	 */
	public static boolean createDir(String destDirName) {  
		File dir = new File(destDirName);  
		if (dir.exists()) {  
			System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");  
			return false;  
		}  
		if (!destDirName.endsWith(File.separator)) {  
			destDirName = destDirName + File.separator;  
		}  
		//����Ŀ¼  
		if (dir.mkdirs()) {  
			System.out.println("����Ŀ¼" + destDirName + "�ɹ���");  
			return true;  
		} else {  
			System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�");  
			return false;  
		}  
	}  
	
	/**
	 * �����ļ�
	 * @param destFileName
	 * @return
	 */
	public static boolean createFile(String destFileName) {  
		File file = new File(destFileName);  
		if(file.exists()) {  
			/*System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ���ļ��Ѵ��ڣ�");  */
			return false;  
		}  
		if (destFileName.endsWith(File.separator)) {  
			/*System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ���ļ�����ΪĿ¼��");  */
			return false;  
		}  
		//�ж�Ŀ���ļ����ڵ�Ŀ¼�Ƿ����  
		if(!file.getParentFile().exists()) {  
			//���Ŀ���ļ����ڵ�Ŀ¼�����ڣ��򴴽���Ŀ¼  
			/*System.out.println("Ŀ���ļ�����Ŀ¼�����ڣ�׼����������");*/  
			if(!file.getParentFile().mkdirs()) {  
				/*System.out.println("����Ŀ���ļ�����Ŀ¼ʧ�ܣ�");*/  
				return false;  
			}  
		}  
		//����Ŀ���ļ�  
		try {  
			if (file.createNewFile()) {  
				/*System.out.println("���������ļ�" + destFileName + "�ɹ���");*/  
				return true;  
			} else {  
				/*System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�");  */
				return false;  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
			/*System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�" + e.getMessage()); */ 
			return false;  
		}  
	}  
	
	/**
	 * д���ı�txt
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





