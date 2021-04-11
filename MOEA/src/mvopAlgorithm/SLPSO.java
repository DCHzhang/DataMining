package mvopAlgorithm;
import java.util.Random;
import Population.mvPopulation;
import Solution.mvSolution;
import problem.testfunc;

// A social learning particle swarm optimization algorithm for scalable optimization 陈然 金耀初  2015
public class SLPSO {
	public mvPopulation particles;
	public mvSolution gBest;
	
	public double alpha = 0.5;
	public double beta = 0.01;
	public double epsilon;  // eplison  is denoted as the social influence factor
	public double learningPro[];
	
	public testfunc tf = new testfunc();
	
	public SLPSO(int N, int rDIM, int oDIM, int cDIM, int M){
		particles = new mvPopulation(N, rDIM, oDIM, cDIM, M);
		gBest = new mvSolution(rDIM, oDIM, cDIM, M);
		epsilon = beta * ( (double)(rDIM +oDIM) / (double) N);
		learningPro = new double[N];
	}
	
	public void beginAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		particles.initPopulation(N, M, num, tf);
		for(int i = 0; i < N; i++){
			// initialize velocity
			particles.solutions[i].Rvelocity = new double[rDIM];
			particles.solutions[i].Ovelocity = new double[oDIM];
			// initialize  P_i^L			
			learningPro[i] = Math.pow(1- ((double)i-1)/(double)N, alpha * Math.log((double)(rDIM + oDIM)  / (double)N));
		}
		particles.solutions[0].copySolutionToB(gBest, M);
		particles.updateBestSolution(N, M, gBest);		
	}
	
	
	public void iteraAlgorithm(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		updateParticle(N, rDIM, oDIM, cDIM, M, num);
	}
	
	public void updateParticle(int N, int rDIM, int oDIM, int cDIM, int M, int num){
		Random random = new Random();
		double r1, r2, r3;
		particles.swarmSorting(); // 按照fitness 递减排序
		for(int i = 0; i < N-1; i++){
			double pro_i = Math.random();
			// social learning
			if (pro_i <= learningPro[i]) {
				// continuous variables
				for(int j = 0; j < rDIM; j++){
					r1 = Math.random();  r2 = Math.random(); r3 = Math.random();	
					double meanCollectiveBehavior = 0.0;
					int K = random.nextInt(N-i-1) + i+1;
					for(int c = 0; c < N; c++){
						meanCollectiveBehavior += particles.solutions[c].rVector[j];
					}
					meanCollectiveBehavior = meanCollectiveBehavior / (double) N;			
					particles.solutions[i].Rvelocity[j] = r1 * particles.solutions[i].Rvelocity[j]
									      +   r2 * (particles.solutions[K].rVector[j] - particles.solutions[i].rVector[j])
										  +    r3 * epsilon * (meanCollectiveBehavior - particles.solutions[i].rVector[j]);
					double lower = particles.solutions[i].setContinuousLowerBorder(num, j);
					double upper = particles.solutions[i].setContinuousUpperBorder(num, j);
					// fix velocity  less than 0.5*(upper - lower)
					if (  Math.abs(particles.solutions[i].Rvelocity[j] )  > 0.2 *(upper - lower) ) {
						particles.solutions[i].Rvelocity[j] = particles.solutions[i].Rvelocity[j] < 0 ? 
								 -0.2 * (upper - lower) : 0.2 * (upper - lower);
					}
					particles.solutions[i].rVector[j] += particles.solutions[i].Rvelocity[j];
				}
				// discrete variables
				for(int j = 0; j < oDIM; j++){
					r1 = Math.random();  r2 = Math.random(); r3 = Math.random();	
					double meanCollectiveBehavior = 0.0;
					int K = random.nextInt(N-i-1) + i+1;	
					for(int c = 0; c < N; c++){
						meanCollectiveBehavior += particles.solutions[c].oVector[j];
					}
					meanCollectiveBehavior = meanCollectiveBehavior / (double) N;
					
					particles.solutions[i].Ovelocity[j] = r1 * particles.solutions[i].Ovelocity[j]
									      +   r2 * (particles.solutions[K].oVector[j] - particles.solutions[i].oVector[j])
										  + r3 * epsilon * (meanCollectiveBehavior - particles.solutions[i].oVector[j]);
					double lower = particles.solutions[i].setOrdinalLowerBorder(num, j);
					double upper = particles.solutions[i].setOrdinalUpperBorder(num, j);
					// fix velocity  less than 0.5*(upper - lower)
					if (  Math.abs(particles.solutions[i].Ovelocity[j] )  > 0.2 *(upper - lower) ) {
						particles.solutions[i].Ovelocity[j] = particles.solutions[i].Ovelocity[j] < 0 ? 
								 -0.2 * (upper - lower) : 0.2 * (upper - lower);
					}
					particles.solutions[i].oVector[j] += particles.solutions[i].Ovelocity[j];
				}	
			}
			particles.solutions[i].fixSolution(num);
			particles.solutions[i].updateObjectiveValue(M, num, tf);
			// neighborBest 同步更新  (包括 localBest  globalBest)
			if (particles.solutions[i].f[0] < gBest.f[0]) {
				particles.solutions[i].copySolutionToB(gBest, M);
			}
		}
		particles.updateBestSolution(N, M, gBest);	
	}
	
	
}
