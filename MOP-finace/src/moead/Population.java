package moead;

public class Population {
	
	public double[] vector;  
	public double FV;	// stand for the FV's value
	public double f1;  //f1 problem value ƽ������
	public double f2;  //f2 problem value ���淽��
	
	public int [] neighborIndex;
	
	public Population(int DIM,int T) {
		vector = new double[DIM];
		neighborIndex = new int[T+1];		
	}
}
