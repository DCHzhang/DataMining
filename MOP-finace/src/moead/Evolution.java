package moead;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.print.DocFlavor.INPUT_STREAM;

import moead.finaceData;
import moead.finaceData1;
import moead.finaceData2;
import moead.TxtUtil;

//ԭʼ����Ⱥ�Ż��㷨����
public class Evolution {

	final static int numOfBound = 31;  //��Ʊ����������
	final static int []NUM = {1};
	
	final static int N=500;  //�����������solution����
	final static int T=5;   //��������ھӸ���
	final static double upperLimit = 1.00;
	final static double lowerLimit = 0.00;
	final static double Penalty = 100; // penalty coefficient
	final static double crossoverRate = 0.8;
	final static double muteRate = 1/numOfBound;
	final static double stepSize = 0.5;
	
	public Population[] Solution = new Population[N];  //XΪN��������Ľ�
	public double [] Z =  new double[3];  //reference point 
	
	public ArrayList<Population> exPopulation = new ArrayList<Population>();  //�����ⲿ��Ⱥ
	
	//������Ϣ
/*	public double coefficient[][] = new double[numOfBound][numOfBound];	
	public double meanSD[] = new double[numOfBound];
	public double meanProfitRate[] = new double[numOfBound];
	public double refPoint[] = new double[2];
	public double [][]COV = new double[numOfBound][numOfBound];
	*/
	public double coefficient[][] = finaceData.coefficient1;
	public double meanSD[] = finaceData.meanSD1;
	public double meanProfitRate[] = finaceData.meanProfitRate1;
	public double refPoint[] = finaceData.refPoint1;
	public double [][]COV = new double[numOfBound][numOfBound];
	
	
	
/*	public void DataReset(int DIM , int num) throws IOException{ 
        try {
            FileReader fr = new FileReader("C:/Users/xjr.DESKTOP-MIOLUFM/Desktop/5.txt");
            BufferedReader br = new BufferedReader(fr);
            
            String str;
            while ((str = br.readLine()) != null) {
            	System.out.println(str);
            }
            
            br.close();
            
        	} catch (IOException e) {
        		e.printStackTrace();
        }

		for(int i = 0; i < DIM; i++){
			meanSD[i] = finaceData5.meanSD5[i];
			meanProfitRate[i] = finaceData5.meanProfitRate5[i];
			for(int j = 0; j < DIM; j++){
				coefficient[i][j] = finaceData5.coefficient5[i][j];		
			}
			
		}
		refPoint[0] = finaceData5.refPoint5[0];
		refPoint[1] = finaceData5.refPoint5[1];
	}
	
	*/
	public void calculateCOV(int DIM){
		for(int i=0; i < DIM; i++){
			for(int j = i; j < DIM; j++){
				COV[i][j] = coefficient[i][j] * meanSD[i] * meanSD[j] ;
			}
			for (int j = 0; j < i; j++) {
				COV[i][j] = COV[j][i];
				/*System.out.print(COV[i][j]+",");*/
			}
			/*System.out.println(" ");*/
		}
	}
	
	/**
	 * ��ʼ������Ⱥ�����������ʼ��
	 */
	public void Init(int DIM, int num)throws Exception {
		
		
		for(int i =0; i < N; i++){
			Solution[i] = new Population(DIM, T);			
		}
		
		for(int i =0; i < N; i++){
			double count = 0;
			
			for(int j = 0; j < DIM;j++){
				Solution[i].vector[j] = Math.random();
				count  += Solution[i].vector[j];
			}
			for(int j = 0;j < DIM; j++){
				Solution[i].vector[j] = Solution[i].vector[j] / count;
				/*System.out.println(Solution[i].vector[j]);*/
			}
			
			//����f1��ֵ
			calculateF1(Solution[i], DIM);			
			//����f2��ֵ
			calculateF2(Solution[i], DIM);  
		} // end for i=N  Ϊÿ�����ʼ������Ͷ�����Ȩ��
		
		// ���²ο���
		/*Z[1] = Solution[0].f1;
		Z[2] = Solution[0].f2;*/
		
		Z[1] = refPoint[0];
		Z[2] = refPoint[1];
		for(int i = 1; i < DIM; i++ ){
			if (Solution[i].f1 > Z[1]) {
				Z[1] = Solution[i].f1;
			}
			if (Solution[i].f2 < Z[2]) {
				Z[2] = Solution[i].f2;
			}
		}
		
		//����ÿ��solution��Ӧ�ֽ����ɵ��������ֵFV
		for(int i = 0; i < N; i++){
			Solution[i].FV = CalFitness(Solution[i], i);
		}
		
		//����ÿ������ھӽ���±�
		for(int i = 0 ; i < N; i++){
			if (i < T) {
				for(int t = 0; t < T+1; t++){
					Solution[i].neighborIndex[t] = t;
				}
			}
			else if (i > N -T -1){
				for(int t = 0; t < T+1; t++){
					Solution[i].neighborIndex[t] = N - T - 1 + t;
				}
			}
			
			else {
				for(int t = 0; t < T+1; t++){
					Solution[i].neighborIndex[t] = i - T/2 + t;
				}
			}			
		}
		
		
	}
	
	/**
	 * ������Ⱥ�еĽ�
	 */
	public void Update(int DIM)throws Exception {		
		Random random = new Random();
		
		//�����ⲿ��Ⱥ
		for(int i = 0 ; i < N ; i++){
			updateExPopulation(Solution[i]);
		}
		
		//ʹ�ò�ֽ������ӽ��б���
		for(int i = 0; i < N; i++){
			Population tempSolution = new Population(DIM, T);
			
			tempSolution.f1 = Solution[i].f1;
			tempSolution.f2 = Solution[i].f2;
			tempSolution.FV = Solution[i].FV;

			for(int j = 0; j <DIM ; j++){
				int m = random.nextInt(T+1);
				int n = random.nextInt(T+1);
				
				tempSolution.vector[j] = Solution[i].vector[j]; //��ʱ���� �Ա�����ѡ��
				
				// crossover 
				if (random.nextDouble() < crossoverRate) {
					Solution[i].vector[j] = Solution[i].vector[j] +
							stepSize * (Solution[Solution[i].neighborIndex[m]].vector[j] - Solution[Solution[i].neighborIndex[n]].vector[j]) ;
				}
				
				//mutation
				if (random.nextDouble() < muteRate) {
					Solution[i].vector[j] = Solution[i].vector[j]  + random.nextGaussian();
				}				
			 } // end for J to DIM
			
				repairVector(Solution[i],DIM); //�޲��� ʹ�ý����޶��ռ�			
				//����f1��ֵ
				calculateF1(Solution[i], DIM);			
				//����f2��ֵ
				calculateF2(Solution[i], DIM);			
				//����solution��Ӧ��FV
				Solution[i].FV = CalFitness(Solution[i], i);				
				//û�������ԭ
				if (tempSolution.FV < Solution[i].FV){
					Solution[i].FV = tempSolution.FV;
					Solution[i].f1 = tempSolution.f1;
					Solution[i].f2 = tempSolution.f2;
					for(int j = 0; j < DIM; j++){
						Solution[i].vector[j] = tempSolution.vector[j];
					}
				}
				//�����ھӽ�
				for(int j = 0 ; j < T+1; j++){
					double tempNeigborFV = 0.0;
					tempNeigborFV = CalFitness(Solution[i], Solution[i].neighborIndex[j]);
					if(Solution[Solution[i].neighborIndex[j]].FV > tempNeigborFV){
						Solution[Solution[i].neighborIndex[j]].FV = tempNeigborFV;
						Solution[Solution[i].neighborIndex[j]].f1 = Solution[i].f1;
						Solution[Solution[i].neighborIndex[j]].f2 = Solution[i].f2;
						for(int m = 0 ;m < DIM; m++){
							Solution[Solution[i].neighborIndex[j]].vector[m] = Solution[i].vector[m];
						}
					}
				}
			
		}//end for N
		
		
		//���²ο��� Z
		for(int i = 0; i < N; i++ ){
			if (Solution[i].f1 > Z[1]) {
				Z[1] = Solution[i].f1;
			}
			if (Solution[i].f2 < Z[2]) {
				Z[2] = Solution[i].f2;
			}
		}
			
				
	}
	//�����ⲿ��Ⱥ
	public void updateExPopulation(Population solution){
		int size = exPopulation.size();
		
		if (size == 0) {
			exPopulation.add(solution);
		}
		else {
			for(int i = 0 ; i < size; i++){
				if (exPopulation.get(i).f1 <= solution.f1 && exPopulation.get(i).f2 >= solution.f2) {
					exPopulation.remove(i);
					size =  size - 1;
				}
			} // �Ƴ����б�solution֧��Ľ�
			int flag = 1;
			for(int i = 0 ; i < size; i ++ ){
				if (exPopulation.get(i).f1 > solution.f1 && exPopulation.get(i).f2 < solution.f2) {
					flag = 0;
				}
			}
			if (flag == 1) {
				exPopulation.add(solution);
			}	// ���ʣ�µĽ���û��֧��solution�� ��solution��ӽ�exPpuolation
		}
	}
	
	public void repairVector(Population Solution,int DIM){
		double count = 0.0;
		
		for(int j = 0 ; j < DIM; j++){
			if (Solution.vector[j] < 0 ) {
				Solution.vector[j] = 0;
			}
			count += Solution.vector[j];
		}
		for(int j = 0 ; j < DIM; j++){
			Solution.vector[j] = Solution.vector[j] / count;
		}
}
	
	
	
	/**
	 * @param particle ĳ������
	 * @return
	 * ����ֵ
	 */
	public double CalFitness(Population Solution,int index) throws Exception{
		double FV = 0.0;
		double delta1,delta2;  //��άȨ������
		double A,B,C;  			// ֱ�� Ax+By+C=0
		double d1,d2;
		
		A = Math.tan(Math.PI * index / 2 / N);
		delta1 = - 1 /(1+A) ;
		delta2 = A / (1+A);

		// PBI approach or weighted sum approach
/*		A = -A;
		B = 1;
		C = delta2 / delta1 * Z[1] - Z[2];
		double delta1e2 = Math.pow(delta1, 2);
		double delta2e2 = Math.pow(delta2, 2);
		double under1 = Math.sqrt(delta1e2+delta2e2);
		double under2 = Math.sqrt(A*A+B*B);
		d1 = ( ( Solution.f1 - Z[1]) * delta1 + (Solution.f2 - Z[2]) * delta2 ) / Math.sqrt(Math.pow(delta1, 2)+Math.pow(delta2, 2));  //����ֱ���ϵ�ͶӰ����
		d2 = Math.sqrt(    Math.pow(Solution.f1-Z[1]-d1 * delta1 / under1 ,2  ) + Math.pow(Solution.f2-Z[2]-d1 * delta2 /under1 , 2) );  //�㵽ֱ�߾���
		FV = d1 + Penalty * d2;*/
		
		// �б�ѩ�򷽷�
		if (delta1 * (Solution.f1 - Z[1]) > delta2 * (Solution.f2 - Z[2])) {
			FV = delta1 * (Solution.f1 - Z[1]);
		}
		else {
			FV = delta2 * (Solution.f2 - Z[2]);
		}
		return FV;
	}
	public void calculateF1(Population Solution,int DIM){
		double f1 = 0.0;
		
		for(int j = 0; j < DIM; j++){
			f1 += Solution.vector[j] * meanProfitRate[j];
		}
		Solution.f1 = f1;
	}
	public void calculateF2(Population Solution, int DIM){
	/*	double f2 = 0.0;
		double tempMatrix[] = new double[DIM];
		
		for(int m = 0; m < DIM; m++){
			for(int n = 0 ; n < DIM; n++){
			 tempMatrix[m] = tempMatrix[m] + COV[m][n] * Solution.vector[n];
			}
		}
		for(int j = 0 ; j <DIM; j++){
			f2 += Solution.vector[j] * tempMatrix[j];
		}*/
		double tempI = 0,tempJ;
		
		for(int i = 0 ; i < DIM; i++){
			tempJ = 0;
			for(int j = 0 ; j < DIM; j++){
				tempJ = tempJ + Solution.vector[i] * Solution.vector[j] * COV[i][j];
			}
			tempI = tempI + tempJ;
		}	
		Solution.f2 = tempI;
	}
	
	
	/**
	 * �޲ι��캯��
	 */
	public Evolution() {
	}
	

	
	
	
	
	public static void main(String[] args) throws Exception {
		String dirName = "E:/EMO/EAM";
		TxtUtil.createDir(dirName);		
		int DIM = numOfBound;
		int TMax = 500;  //��������
		
		for(int k = 0; k < 1;k++){
		for(int num : NUM) {
			String fileName1 = dirName + "/"+num+"_value.txt";  
			String fileName2 = dirName + "/"+num+"_time.txt";  
			String fileName3 = dirName + "/"+num+"_exPopulation.txt";  
			TxtUtil.createFile(fileName1);
			TxtUtil.createFile(fileName2);
			TxtUtil.createFile(fileName3);
			
				for(int m = 0; m < 1; m++) {
					Evolution moead = new Evolution();
					long startTime = System.currentTimeMillis();
/*					moead.DataReset(DIM,num);
*/					moead.calculateCOV(DIM);
					moead.Init(DIM,num);
					for(int t = 1; t <= TMax; t++) {
						moead.Update(DIM);
						
					}
					for(int i = 0; i < N;i++ ){
						String content1 = /*"��"+i +"����ϵ�����/����:"+*/  moead.Solution[i].f1+" "+ moead.Solution[i].f2 + "\r\n";
						/*for(int j = 0 ; j < DIM; j++){
							System.out.print(moead.Solution[i].vector[j]+",");
						}*/
						TxtUtil.writeTxtFile(content1, fileName1);
					}
					for(int i = 0 ; i <  moead.exPopulation.size(); i++){
						String content = "" + moead.exPopulation.get(i).f1 +"  "+ moead.exPopulation.get(i).f2 + "\r\n";
						TxtUtil.writeTxtFile(content, fileName3);
					}
					
					long endTime = System.currentTimeMillis();
					String content3 = "��������ʱ�䣺"+(endTime - startTime)+"\r\n";
					TxtUtil.writeTxtFile(content3, fileName2);
					
					String content5 = "\r\n";
					TxtUtil.writeTxtFile(content5, fileName1);
					TxtUtil.writeTxtFile(content5, fileName2);
					TxtUtil.writeTxtFile(content5, fileName3);

				}
			}
			
		}
	}

}
