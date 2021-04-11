package mvopAlgorithm;

import java.util.ArrayList;

import Population.mvPopulation;
import Solution.mvSolution;
import problem.testfunc;

public class temp {
	
	public mvPopulation discrPop;
	public mvSolution discBest;	
	public mvPopulation contiPop;
	public mvSolution contiBest;
	
	public mvSolution gBest;
	
	public double weight = 0.4;
	testfunc tf = new testfunc();
	public ArrayList<double[]> probOfOrdinalValue;  // 记录每个维度的ordinal变量所有可选值的被选中概率

	public temp(int N, int rDIM, int oDIM, int cDIM, int M, int Tmax) {
		contiPop = new mvPopulation(N, rDIM, 0, cDIM, M);
		discrPop = new mvPopulation(N, 0, oDIM, cDIM, M);
		
		discBest = new mvSolution(0, oDIM, cDIM, M);
		contiBest = new mvSolution(rDIM, 0, cDIM, M);
		for (int i = 0; i < N; i++) {
			contiPop.solutions[i].Rvelocity = new double[rDIM];
			contiPop.solutions[i].pBest = new mvSolution(rDIM, 0, cDIM, M);
			discrPop.solutions[i].pBest = new mvSolution(0, oDIM, cDIM, M);
		}
		gBest = new mvSolution(rDIM, oDIM, cDIM, M);
	}
	
	public void beginAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num) {
		probOfOrdinalValue = new ArrayList<>();
		probOfOrdinalValue = discrPop.initOrdinalValueProb(oDIM, M, num); // 初始化种类变量概率列表	
		
		contiPop.initPopulation(N, M, num, tf);
		discrPop.initPopulation(N, M, num, tf);
		gBest.initSolution(M, num, tf);
		mvSolution tempSolution = new mvSolution(rDIM, oDIM, cDIM, M);
		for (int i = 0; i < N; i++) {					
			gBest.copySolutionToB(tempSolution, M);
			updateContiFitness(contiPop.solutions[i], gBest, rDIM, tempSolution, M, num);
			contiPop.solutions[i].copySolutionToB(contiPop.solutions[i].pBest, M);
			
			gBest.copySolutionToB(tempSolution, M);
			updateDiscFitness(discrPop.solutions[i], gBest, oDIM, tempSolution, M, num);
			discrPop.solutions[i].copySolutionToB(discrPop.solutions[i].pBest, M);
		}
		discrPop.solutions[0].copySolutionToB(discBest, M);
		discrPop.updateBestSolution(N, M, discBest);
		contiPop.solutions[0].copySolutionToB(contiBest, M);
		contiPop.updateBestSolution(N, M, contiBest);
	}
	
	public void updateContiFitness(mvSolution contiSolution, mvSolution gBest, int rDIM, mvSolution tempSolution, int M, int num) {
		gBest.copySolutionToB(tempSolution, M);
		for(int j = 0; j < rDIM; j++) {
			tempSolution.rVector[j] = contiSolution.rVector[j];
		}
		tempSolution.fixSolution(num);
		tempSolution.updateObjectiveValue(M, num, tf);
		contiSolution.f[0] = tempSolution.f[0] - gBest.f[0];   // update objective		 
	}
	public void updateDiscFitness(mvSolution discSolution, mvSolution gBest, int oDIM, mvSolution tempSolution, int M, int num)  {
		gBest.copySolutionToB(tempSolution, M);
		for(int j = 0; j < oDIM; j++) {
			tempSolution.oVector[j] = discSolution.oVector[j];
		}
		tempSolution.fixSolution(num);
		tempSolution.updateObjectiveValue(M, num, tf);
		discSolution.f[0] = tempSolution.f[0] - gBest.f[0];   // update objective
	}
	
	public void iteratAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num) {
		updatePop(N, rDIM, oDIM, cDIM, M, num);
		discrPop.PBIL_updateProList_pBest(probOfOrdinalValue, N, oDIM, M, num, 0.99);
	}
	
	public void updatePop(int N, int rDIM, int oDIM, int cDIM, int M, int num) {
		mvSolution tempSolution = new mvSolution(rDIM, oDIM, cDIM, M);

		for(int i = 0; i < N; i++) {			
			for(int j = 0; j < rDIM; j++) {
				contiPop.solutions[i].Rvelocity[j] = weight * contiPop.solutions[i].Rvelocity[j] 
						+ 2.0* Math.random() * (contiPop.solutions[i].rVector[j] - gBest.rVector[j])
						+ 2.0* Math.random() * (contiPop.solutions[i].rVector[j] - contiPop.solutions[i].pBest.rVector[j]) ;
				contiPop.solutions[i].rVector[j] =  contiPop.solutions[i].rVector[j] +  contiPop.solutions[i].Rvelocity[j];
			}
			contiPop.solutions[i].fixSolution(num);
			updateContiFitness(contiPop.solutions[i],       gBest, rDIM, tempSolution, M, num);
			updateContiFitness(contiPop.solutions[i].pBest, gBest, rDIM, tempSolution, M, num);
			if (contiPop.solutions[i].f[0] < contiPop.solutions[i].pBest.f[0]) {
				contiPop.solutions[i].copySolutionToB(contiPop.solutions[i].pBest, M);
			}
			
			// ***************		
			discrPop.solutions[i].O_updateOrdinalVariablesWithProbList(probOfOrdinalValue, num);
			discrPop.solutions[i].fixSolution(num);
			updateDiscFitness(discrPop.solutions[i], gBest, oDIM, tempSolution, M, num);
			updateDiscFitness(discrPop.solutions[i].pBest, gBest, oDIM, tempSolution, M, num);
			if (discrPop.solutions[i].f[0] < discrPop.solutions[i].pBest.f[0]) {
				discrPop.solutions[i].copySolutionToB(discrPop.solutions[i].pBest, M);
			}
			
		}		
		updateDiscFitness(discBest, gBest, oDIM, tempSolution, M, num);
		updateContiFitness(contiBest, gBest, rDIM, tempSolution, M, num);		
		discrPop.updateBestSolution(N, M, discBest);
		contiPop.updateBestSolution(N, M, contiBest);
		
		gBest.copySolutionToB(tempSolution, M);
		for(int j = 0; j < oDIM; j++) {
			tempSolution.oVector[j] = discBest.oVector[j];
		}
		for(int j = 0; j < rDIM; j++) {
			tempSolution.rVector[j] = contiBest.rVector[j];
		}
		tempSolution.fixSolution(num);
		tempSolution.updateObjectiveValue(M, num, tf);
		if (tempSolution.f[0] < gBest.f[0]) {
			tempSolution.copySolutionToB(gBest, M);
		}
		
	}
	
	
	
	
	
}
