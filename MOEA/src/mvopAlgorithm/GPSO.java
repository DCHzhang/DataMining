package mvopAlgorithm;

import Population.mvPopulation;
import Solution.mvSolution;
import problem.testfunc;

public class GPSO {
	public mvPopulation particles;
	public mvPopulation pBest;
	public mvSolution gBest;
	
	public double w = 0.4;
	public double c1 = 2.0, c2 = 2.0;
	
	testfunc tf = new testfunc();
 	public GPSO(int N, int rDIM, int oDIM, int cDIM, int M){
		particles = new mvPopulation(N, rDIM, oDIM, cDIM, M);
		pBest = new mvPopulation(N, rDIM, oDIM, cDIM, M);
		gBest = new mvSolution(rDIM, oDIM, cDIM, M);
	}
	
	public void beginAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		particles.initPopulation(N, M, num, tf);
		for(int i = 0; i < N; i++){
			particles.solutions[i].copySolutionToB(pBest.solutions[i], M);
			// initialize velocity
			particles.solutions[i].Rvelocity = new double[rDIM];
			particles.solutions[i].Ovelocity = new double[oDIM];
		}
		particles.solutions[0].copySolutionToB(gBest, M);
		particles.updateBestSolution(N, M, gBest);	
	}
	
	public void iterAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){		
		updatePopulation(N, rDIM, oDIM, cDIM, M, num);
	}
	
	public void updatePopulation(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		for(int i = 0; i < N; i++){
			particles.solutions[i].R_GPSOmethod(pBest.solutions[i], gBest, w, num);			
			particles.solutions[i].O_GPSOmethod(pBest.solutions[i], gBest, w, num);
			particles.solutions[i].fixSolution(num);
			particles.solutions[i].updateObjectiveValue(M, num,tf);
			if (particles.solutions[i].f[0] < pBest.solutions[i].f[0]) {
				particles.solutions[i].copySolutionToB(pBest.solutions[i], M);
			}
			// neighborBest 同步更新  (包括 localBest  globalBest)
			if (particles.solutions[i].f[0] < gBest.f[0]) {
				particles.solutions[i].copySolutionToB(gBest, M);
			}
		}
		particles.updateBestSolution(N, M, gBest);
	}
	
	
	
}
