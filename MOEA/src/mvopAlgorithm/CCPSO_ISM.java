package mvopAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Population.mvPopulation;
import Population.mvPopulation.swarmComparator;
import Solution.mvSolution;
import problem.testfunc;

// Paper :  Õ²Ö¾»Ô  2015
// Competitive and cooperative particle swarm optimization with information sharing mechanism for global optimization problems

public class CCPSO_ISM {
	public mvPopulation particles;
	public mvPopulation ccBest;
	public mvSolution gBest;
	public double G = 5;
	public double[] notImproved;
	public double Pro = 0.05;
	public double weight = 0.6;
	public double c = 2.0;
	
	public double iteration;	
	public double Tmax;
	testfunc tf = new testfunc();
	
	public CCPSO_ISM(int N, int rDIM, int oDIM, int cDIM, int M, int Tmax){
		particles = new mvPopulation(N, rDIM, oDIM, cDIM, M);
		ccBest = new mvPopulation(N, rDIM, oDIM, cDIM, M);
		gBest = new mvSolution(rDIM, oDIM, cDIM, M);
		notImproved = new double[N];
		this.Tmax = Tmax;
		iteration = 0.0;
	}
	
	public void beginAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		particles.initPopulation(N, M, num, tf);
		for(int i = 0; i < N; i++){
			// initialize velocity and pBest
			particles.solutions[i].Rvelocity = new double[rDIM];
			particles.solutions[i].Ovelocity = new double[oDIM];			
			particles.solutions[i].pBest = new mvSolution(rDIM, oDIM, cDIM, M);
			
			particles.solutions[i].copySolutionToB(ccBest.solutions[i], M);
			particles.solutions[i].copySolutionToB(particles.solutions[i].pBest, M);
		}
		particles.solutions[0].copySolutionToB(gBest, M);
		particles.updateBestSolution(N, M, gBest);
	}
	
	public void iterAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		iteration ++;
		updateParticle(N, rDIM, oDIM, cDIM, M, num);
	}
	
	public void updateParticle(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		double K = Math.ceil(iteration / Tmax * N);
		Random random = new Random();
		for(int i = 0; i < N; i++){
			if (notImproved[i] >= G) {
				// continuous
				for(int j = 0; j <rDIM; j++){
					if (Math.random() < Pro) {
						ArrayList<Integer> indexList = new ArrayList<>();
						for(int index = 0; index < N; index++){
							indexList.add(index);
						}					
						ArrayList<mvSolution> blackboard = new ArrayList<>();
						for(int b = 0; b < K; b++){
							int rand = random.nextInt(indexList.size());
							int index = indexList.get(rand);
							indexList.remove(rand);
							blackboard.add(particles.solutions[index].pBest);
						}
						Collections.sort(blackboard,new swarmComparator());
						ccBest.solutions[i].rVector[j] = blackboard.get(blackboard.size() - 1).rVector[j];
						blackboard.clear();
					}
					else{
						ccBest.solutions[i].rVector[j] = particles.solutions[i].pBest.rVector[j];
					}
				}
				// discrete 
				for(int j = 0; j <oDIM; j++){
					if (Math.random() < Pro) {
						ArrayList<Integer> indexList = new ArrayList<>();
						for(int index = 0; index < N; index++){
							indexList.add(index);
						}					
						ArrayList<mvSolution> blackboard = new ArrayList<>();
						for(int b = 0; b < K; b++){
							int rand = random.nextInt(indexList.size());
							int index = indexList.get(rand);
							indexList.remove(rand);
							blackboard.add(particles.solutions[index].pBest);
						}
						Collections.sort(blackboard,new swarmComparator());
						ccBest.solutions[i].oVector[j] = blackboard.get(blackboard.size() - 1).oVector[j];
						blackboard.clear();
					}
					else{
						ccBest.solutions[i].oVector[j] = particles.solutions[i].pBest.oVector[j];
					}
				}
			}
			// update continuous
			for (int j = 0; j < rDIM; j++) {
				particles.solutions[i].Rvelocity[j] = weight * particles.solutions[i].Rvelocity[j]
						+ c * Math.random() * (ccBest.solutions[i].rVector[j] - particles.solutions[i].rVector[j]);
				double lower = particles.solutions[i].setContinuousLowerBorder(num, j);
				double upper = particles.solutions[i].setContinuousUpperBorder(num, j);
				// fix velocity less than 0.5*(upper - lower)
				if (Math.abs(particles.solutions[i].Rvelocity[j]) > 0.2 * (upper - lower)) {
					particles.solutions[i].Rvelocity[j] = particles.solutions[i].Rvelocity[j] < 0
							? -0.2 * (upper - lower) : 0.2 * (upper - lower);
				}
				particles.solutions[i].rVector[j] += particles.solutions[i].Rvelocity[j];
			}
			
			// update discrete
			for (int j = 0; j < oDIM; j++) {
				particles.solutions[i].Ovelocity[j] = weight * particles.solutions[i].Ovelocity[j]
						+ c * Math.random() * (ccBest.solutions[i].oVector[j] - particles.solutions[i].oVector[j]);
				double lower = particles.solutions[i].setContinuousLowerBorder(num, j);
				double upper = particles.solutions[i].setContinuousUpperBorder(num, j);
				// fix velocity less than 0.5*(upper - lower)
				if (Math.abs(particles.solutions[i].Ovelocity[j]) > 0.2 * (upper - lower)) {
					particles.solutions[i].Ovelocity[j] = particles.solutions[i].Ovelocity[j] < 0
							? -0.2 * (upper - lower) : 0.2 * (upper - lower);
				}
				particles.solutions[i].oVector[j] += particles.solutions[i].Ovelocity[j];
			}
			
			particles.solutions[i].fixSolution(num);
			particles.solutions[i].updateObjectiveValue(M, num, tf);			
			if (particles.solutions[i].f[0] < particles.solutions[i].pBest.f[0]) {
				notImproved[i] = 0;
				particles.solutions[i].copySolutionToB(particles.solutions[i].pBest, M);
			}
			else{
				notImproved[i] ++;
			}
			if (particles.solutions[i].f[0] < gBest.f[0]) {
				particles.solutions[i].copySolutionToB(gBest, M);
			}
		}
				
	}
	
	
}
