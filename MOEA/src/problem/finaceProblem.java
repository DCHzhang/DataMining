package problem;

import Solution.mvSolution;
import fianceData.finaceData;
import fianceData.finaceData1;
import fianceData.finaceData2;
import fianceData.finaceData5;
public class finaceProblem {
	public static double calRiskLoss(mvSolution solution, double[][] COV, int rDIM) {
		//计算梯度向量 * weight向量
		double gradW[] = new double[rDIM];
		for(int i=0; i <rDIM; i++) {
			double grad_w = 0.0;
			for (int j = 0; j < rDIM; j++) {
				grad_w += COV[i][j] * solution.rVector[j];
			}
			gradW[i] = grad_w * solution.rVector[i];
		}
		//计算风险均衡loss
		double loss = 0.0;
		for (int i = 0; i < rDIM; i++) {
			double loss_ij = 0.0;
			for (int j = 0; j < rDIM; j++) {
				loss_ij += (gradW[i] - gradW[j]) * (gradW[i] - gradW[j]);
			}
			loss += loss_ij;
		}
//		for (int i = 0; i < gradW.length; i++) {
//			System.out.print(gradW[i] + ",");
//		}
//		System.out.println();
		return loss;
	}
	public static void fianceProblem_0(mvSolution solution, int rDIM) {
		double coefficient[][] = finaceData.coefficient1;
		double meanSD[] = finaceData.meanSD1;
		double meanProfitRate[] = finaceData.meanProfitRate1;
		double refPoint[] = finaceData.refPoint1;
		double [][]COV = new double[rDIM][rDIM];
//		计算协方差矩阵
		for(int i=0; i < rDIM; i++){
			for(int j = i; j < rDIM; j++){
				COV[i][j] = coefficient[i][j] * meanSD[i] * meanSD[j] ;
			}
			for (int j = 0; j < i; j++) {
				COV[i][j] = COV[j][i];
			}
		}
		double loss = calRiskLoss(solution, COV, rDIM);
		solution.f[0] = loss;
	}
	
	public static void fianceProblem_1(mvSolution solution, int rDIM) {
		double coefficient[][] = finaceData.coefficient2;
		double meanSD[] = finaceData.meanSD2;
		double meanProfitRate[] = finaceData.meanProfitRate2;
		double refPoint[] = finaceData.refPoint2;
		double [][]COV = new double[rDIM][rDIM];
//		计算协方差矩阵
		for(int i=0; i < rDIM; i++){
			for(int j = i; j < rDIM; j++){
				COV[i][j] = coefficient[i][j] * meanSD[i] * meanSD[j] ;
			}
			for (int j = 0; j < i; j++) {
				COV[i][j] = COV[j][i];
			}
		}
		double loss = calRiskLoss(solution, COV, rDIM);
		solution.f[0] = loss;
	}
	
	public static void fianceProblem_2(mvSolution solution, int rDIM) {
		double coefficient[][] = finaceData1.coefficient3;
		double meanSD[] = finaceData1.meanSD3;
		double meanProfitRate[] = finaceData1.meanProfitRate3;
		double refPoint[] = finaceData1.refPoint3;
		double [][]COV = new double[rDIM][rDIM];
//		计算协方差矩阵
		for(int i=0; i < rDIM; i++){
			for(int j = i; j < rDIM; j++){
				COV[i][j] = coefficient[i][j] * meanSD[i] * meanSD[j] ;
			}
			for (int j = 0; j < i; j++) {
				COV[i][j] = COV[j][i];
			}
		}
		double loss = calRiskLoss(solution, COV, rDIM);
		solution.f[0] = loss;
	}
	
	public static void fianceProblem_3(mvSolution solution, int rDIM) {
		double coefficient[][] = finaceData2.coefficient4;
		double meanSD[] = finaceData2.meanSD4;
		double meanProfitRate[] = finaceData2.meanProfitRate4;
		double refPoint[] = finaceData2.refPoint4;
		double [][]COV = new double[rDIM][rDIM];
//		计算协方差矩阵
		for(int i=0; i < rDIM; i++){
			for(int j = i; j < rDIM; j++){
				COV[i][j] = coefficient[i][j] * meanSD[i] * meanSD[j] ;
			}
			for (int j = 0; j < i; j++) {
				COV[i][j] = COV[j][i];
			}
		}
		double loss = calRiskLoss(solution, COV, rDIM);
		solution.f[0] = loss;
	}
}
