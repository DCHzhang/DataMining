package problem;

import java.util.ArrayList;
import java.util.Random;
import Solution.mvSolution;

public class mvProblem {
	public static double PI = Math.PI;
	public static double E = Math.E;
	
	public static void newsVendor(mvSolution solution, int M){
		Random random = new Random();
		int rDIM, oDIM, cDIM;
		double aa = 0.05; // 置信水平
		double deltaPP = 0.001;
		rDIM = solution.rDIM;
		oDIM = solution.oDIM;
		cDIM = solution.cDIM;
		/*double cost[] =    {90, 125,350,220,350,150,100,850, 550, 65,500,215,175, 65,120,550,320,750,60,55}; //商品进价
		double retail[] =  {100,140,390,270,450,160,130,1280,680,100,560,250,190,100,140,600,350,900,70,60};//商品售价	
		double risk[] =    {9,  12.5,35,22, 35, 15, 10, 85, 55, 6.5, 50, 21.5, 17.5, 6.5, 12, 55, 32, 75, 6, 5.5};	
*/		
		double cost[] =   {100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100};
		double retail[] = {110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,260,270,280,290,300};
		double realRisk[] =   /*{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};*/
			{19, 12, 7, 10, 14 ,18 ,3, 15, 20 ,2 ,11 ,16 ,5, 4 ,17, 8 ,0 ,9 ,6, 1};
		double demandX[] = {56,  93, 67, 19, 19, 98, 13, 20,  56, 50,7,92,77,5,23,64,37,51,77,49};  //商品需求均值
		double variance[]= {20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1}; //商品需求方差
		double kuExpend[]=   {8 ,9 ,3, 5, 3 ,8 ,7 ,3, 9, 5 ,5, 1, 5 ,1 ,2 ,1 ,3, 7, 9, 9}; //商品库存成本
		double queExpend[] = {3,3,5,9,1,3,2,3,5,4,2,3,8,2,6,10,2,9,10,1}; //商品缺货成本
		
		double budget = 100000;
		int realDemand[] = new int[oDIM];
		int realOrder[] = new int[oDIM];
		for(int i = 0; i < oDIM; i++){
			realOrder[i] = (int) (solution.oVector[i] + 0.5 );
		}
		for(int i = 0 ; i <oDIM; i++){
			realDemand[i] = (int) demandX[i];
		}
		//计算总利润
		double beneSum =0.0;
		for(int i = 0 ; i <oDIM ;i ++){
			beneSum  += realDemand[i] > realOrder[i] ? 
					realOrder[i] * (retail[i] - cost[i]) - (realDemand[i] - realOrder[i]) * queExpend[i] :
						realDemand[i] * (retail[i] - cost[i]) - kuExpend[i]*(realOrder[i] - realDemand[i]);
		}
		solution.f[0] =  -beneSum + 120000 ;
		double riskSum = 0.0;
		//订单偏差作为风险
		riskSum = 0.0;
		double risk[] = new double[oDIM];
		double var[] = new double[oDIM];
		for(int i = 0; i < oDIM; i++){
			risk[i] = (realOrder[i] - realDemand[i])/ variance[i] ;
			var[i] = realDemand[i] < realOrder[i] ?
					1 - calc(risk[i]) : calc(risk[i]);
			riskSum += var[i];
		}
		
		/*for(int i = 0; i < oDIM; i++){
			riskSum += realOrder[i] * realRisk[i];
		}*/
		
		solution.f[1] = riskSum ;
	}
	
	public static ArrayList<double[]> categoricalValNewsVendor(){
		ArrayList<double[]> tempList = new ArrayList<>();
		double tempVal_1[] = {1,2,3,4,5};tempList.add(tempVal_1);
		double tempVal_2[] = {11,12,13,14,15};tempList.add(tempVal_2);
		double tempVal_3[] = {21,22,23,24,25};tempList.add(tempVal_3);
		double tempVal_4[] = {31,32,33,34,35};tempList.add(tempVal_4);
		double tempVal_5[] = {41,42,43,44,45};tempList.add(tempVal_5);
		return tempList;
	}
	
	public static boolean singleNewsboy_1(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {29, 15, 41, 48, 40, 7, 34, 31, 34, 9, 43, 13, 26, 27, 42, 40, 35, 27, 9, 3};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j]  * 0.5) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {7.98, 9.39, 8.93, 7.0, 7.04, 7.95, 3.99, 1.91, 7.82, 0.8, 3.89, 0.74, 6.94, 9.31, 0.5, 4.04, 5.91, 3.35, 9.1, 9.74};
		double holdcost[] = {9.21, 0.16, 1.84, 0.83, 6.65, 9.05, 3.24, 9.97, 2.64, 9.46, 5.79, 6.74, 5.91, 4.77, 7.49, 0.45, 6.05, 7.05, 5.67, 4.63};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {8.06, 8.98, 0.71, 2.07, 2.08, 1.18, 1.28, 5.2, 7.39, 8.5, 1.78, 5.54, 3.32, 6.77, 7.79, 4.56, 0.53, 1.71, 9.58, 3.94};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_2(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {15, 20, 37, 28, 82, 76, 76, 52, 32, 62, 32, 92, 10, 8, 13, 35, 26, 84, 83, 41};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j]* 0.25) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {2.58, 9.99, 6.92, 4.31, 0.94, 3.45, 5.51, 2.09, 5.31, 7.99, 6.67, 4.58, 7.56, 9.77, 0.52, 0.15, 8.85, 1.4, 8.09, 8.65};
		double holdcost[] = {3.22, 0.39, 0.1, 7.42, 9.23, 9.11, 9.38, 3.02, 7.03, 0.84, 9.25, 1.15, 6.93, 7.84, 7.8, 3.36, 4.18, 4.7, 1.56, 2.08};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {3.62, 7.67, 8.16, 9.62, 1.8, 1.28, 9.88, 7.42, 6.51, 0.76, 2.68, 1.45, 6.42, 0.31, 9.86, 4.94, 7.04, 2.0, 2.1, 1.41};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
		
	public static boolean singleNewsboy_3(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {42, 48, 10, 11, 14, 3, 2, 6, 33, 24, 1, 1, 14, 49, 17, 11, 21, 7, 46, 39, 44, 15, 16, 12, 28, 31, 46, 33, 10, 32, 7, 34, 5, 42, 28, 30, 4, 6, 6, 30};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * 0.25) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {9.29, 8.63, 6.42, 1.97, 0.67, 5.24, 2.63, 6.7, 6.25, 6.54, 1.82, 8.64, 2.07, 8.65, 0.31, 8.83, 3.56, 7.02, 3.42, 4.27, 2.75, 4.93, 6.83, 5.22, 0.63, 3.05, 2.8, 1.09, 6.19, 7.12, 0.54, 3.7, 7.63, 5.83, 2.33, 6.03, 3.72, 6.7, 1.63, 9.24};
		double holdcost[] = {4.38, 8.15, 2.57, 1.69, 4.53, 6.33, 8.75, 2.69, 9.0, 7.37, 7.75, 2.73, 2.08, 3.41, 4.67, 3.04, 4.34, 6.27, 0.46, 7.07, 7.92, 4.9, 2.9, 4.73, 4.24, 4.26, 9.47, 4.0, 4.34, 9.05, 8.19, 1.58, 6.35, 3.03, 3.36, 4.04, 4.3, 9.19, 3.81, 3.53};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {9.99, 9.93, 9.67, 5.76, 5.91, 0.75, 9.21, 4.1, 1.6, 1.75, 1.02, 9.55, 8.38, 7.43, 4.03, 6.09, 6.0, 4.24, 8.13, 8.27, 0.52, 0.95, 9.94, 7.92, 8.35, 3.05, 6.6, 7.01, 0.04, 9.09, 7.01, 5.71, 8.95, 0.09, 7.76, 6.24, 7.58, 5.42, 1.24, 8.33};	
		double orderingBudget = 2500;
		double holding = 2500;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_4(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {68, 89, 39, 80, 82, 64, 79, 61, 39, 62, 65, 77, 75, 2, 54, 81, 12, 97, 67, 24, 32, 61, 25, 98, 8, 66, 42, 86, 93, 73, 99, 14, 27, 51, 37, 84, 18, 77, 26, 8};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * 0.5) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {6.85, 6.25, 0.46, 0.74, 9.32, 3.81, 7.89, 7.84, 5.9, 7.42, 2.14, 6.75, 7.96, 1.61, 9.04, 4.84, 4.27, 8.12, 3.29, 1.57, 0.78, 2.58, 9.87, 1.59, 7.09, 1.85, 9.35, 8.6, 4.23, 5.16, 0.86, 9.53, 9.15, 5.88, 3.38, 4.64, 7.21, 1.84, 3.44, 8.39};
		double holdcost[] = {4.3, 7.04, 7.91, 8.25, 6.75, 4.27, 8.6, 5.95, 0.05, 3.58, 5.29, 3.12, 3.0, 9.79, 8.03, 3.72, 8.38, 5.36, 8.35, 1.98, 7.26, 2.15, 8.78, 0.77, 9.77, 5.12, 0.33, 5.69, 7.04, 6.5, 4.11, 7.17, 7.6, 5.43, 3.62, 8.14, 6.59, 4.54, 7.8, 5.49};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {4.94, 0.02, 8.0, 2.62, 0.18, 1.74, 4.6, 6.97, 7.93, 2.58, 8.39, 1.9, 8.04, 8.08, 7.87, 6.25, 4.47, 2.66, 5.84, 9.33, 3.87, 1.82, 2.14, 5.94, 3.29, 3.82, 5.13, 2.27, 3.32, 4.55, 0.06, 2.86, 8.05, 1.58, 1.86, 0.73, 9.23, 8.04, 4.43, 3.18};	
		double orderingBudget = 2000;
		double holding = 2000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;	
	}
	
	public static boolean singleNewsboy_5(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {27, 44, 23, 11, 35, 31, 12, 48, 21, 38, 15, 3, 39, 9, 24, 1, 13, 23, 26, 43, 29, 16, 47, 39, 48, 10, 32, 6, 39, 18, 43, 40, 22, 24, 12, 30, 2, 29, 25, 5, 19, 48, 3, 18, 22, 8, 5, 19, 0, 5, 4, 24, 12, 12, 45, 21, 38, 42, 14, 36, 24, 13, 36, 33, 31, 24, 10, 3, 13, 13, 15, 10, 1, 37, 17, 28, 21, 4, 34, 38};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j]  * 0.25) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {3.47, 9.69, 8.6, 0.73, 6.33, 7.64, 4.37, 4.38, 5.96, 8.68, 0.64, 7.75, 4.11, 9.72, 6.84, 5.96, 6.34, 0.5, 0.52, 2.13, 5.88, 2.2, 6.45, 4.76, 4.29, 8.29, 6.63, 5.57, 6.94, 5.88, 7.73, 5.46, 1.91, 6.22, 1.89, 0.83, 2.08, 5.03, 1.82, 5.78, 1.32, 9.47, 1.53, 5.8, 7.61, 6.28, 9.18, 7.61, 7.63, 7.13, 9.95, 5.56, 9.75, 0.87, 2.53, 9.36, 4.98, 9.41, 4.61, 9.7, 8.27, 2.99, 9.46, 8.09, 5.65, 3.56, 7.82, 4.56, 6.64, 9.71, 6.67, 2.46, 4.63, 4.74, 9.49, 2.62, 2.47, 0.26, 0.14, 9.83};
		double holdcost[] = {2.94, 0.73, 0.47, 7.01, 7.03, 8.06, 3.69, 8.66, 3.3, 8.51, 7.81, 0.96, 4.55, 7.89, 0.22, 4.2, 6.23, 6.61, 8.52, 6.85, 5.55, 4.52, 2.71, 3.25, 7.61, 4.44, 5.63, 6.08, 8.07, 2.3, 7.08, 1.19, 3.06, 7.66, 7.67, 2.76, 8.54, 6.66, 3.74, 5.44, 3.79, 0.32, 5.63, 6.21, 5.16, 5.29, 7.91, 2.87, 9.78, 0.79, 1.42, 7.96, 0.9, 4.86, 3.96, 6.43, 0.43, 5.68, 8.56, 3.2, 7.4, 8.66, 5.64, 1.81, 5.08, 8.12, 8.16, 8.62, 3.49, 2.99, 6.82, 7.84, 8.66, 9.83, 5.99, 1.7, 4.69, 2.63, 9.38, 6.32};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {1.62, 2.21, 0.1, 3.2, 1.35, 2.1, 0.11, 8.23, 0.58, 7.93, 9.69, 7.26, 5.68, 2.64, 2.89, 9.67, 1.3, 6.48, 3.41, 2.83, 7.92, 8.86, 1.05, 9.2, 9.82, 2.86, 5.95, 6.46, 2.73, 5.93, 6.05, 9.53, 4.8, 2.96, 7.17, 6.12, 9.74, 3.81, 4.5, 9.32, 7.82, 7.51, 1.14, 1.93, 9.34, 9.8, 2.46, 6.97, 5.12, 8.9, 4.5, 8.27, 0.09, 6.85, 0.97, 2.89, 9.71, 0.02, 6.3, 4.65, 3.3, 3.34, 1.22, 4.01, 5.21, 6.83, 3.73, 5.35, 2.22, 2.11, 0.2, 6.59, 5.5, 9.04, 1.01, 0.08, 6.18, 9.41, 0.14, 5.93};	
		double orderingBudget = 4000;
		double holding = 4000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;	
	}
	
	public static boolean singleNewsboy_6(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {63, 31, 17, 73, 66, 81, 5, 28, 83, 88, 9, 61, 66, 65, 36, 75, 73, 32, 49, 91, 17, 39, 56, 32, 28, 86, 90, 78, 89, 80, 35, 35, 58, 12, 11, 61, 18, 32, 25, 25, 31, 54, 77, 18, 8, 13, 67, 37, 67, 72, 84, 42, 44, 86, 80, 98, 11, 94, 94, 55, 14, 21, 18, 58, 16, 31, 87, 44, 23, 92, 68, 52, 74, 72, 38, 6, 11, 54, 28, 46};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * 0.5) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {2.73, 3.23, 3.44, 4.14, 4.44, 1.43, 0.87, 9.06, 1.84, 4.03, 0.21, 3.34, 6.9, 5.62, 0.52, 7.58, 3.39, 4.05, 8.12, 7.26, 2.98, 1.6, 9.86, 2.25, 8.27, 1.03, 1.43, 9.45, 3.23, 6.66, 5.54, 7.27, 4.94, 8.25, 3.91, 5.25, 8.09, 9.27, 5.76, 8.22, 5.81, 2.22, 3.0, 1.89, 5.89, 0.11, 8.84, 6.07, 1.66, 7.69, 2.26, 2.52, 0.28, 5.67, 5.54, 6.59, 9.93, 8.43, 5.87, 5.67, 8.7, 8.93, 4.79, 3.26, 4.63, 4.55, 1.32, 3.93, 6.38, 4.16, 5.44, 3.51, 8.39, 9.7, 8.84, 8.56, 2.35, 5.43, 8.7, 1.26};
		double holdcost[] = {9.55, 4.52, 7.65, 7.67, 8.11, 4.05, 0.62, 8.66, 5.6, 8.7, 7.83, 3.36, 6.85, 6.77, 2.04, 8.47, 2.33, 3.61, 6.76, 2.68, 5.36, 0.7, 4.9, 7.98, 4.48, 4.43, 1.59, 4.18, 4.03, 5.82, 2.12, 0.43, 3.33, 6.68, 7.8, 1.91, 3.65, 5.17, 0.04, 2.49, 0.19, 3.94, 4.66, 4.64, 5.45, 6.89, 6.99, 5.72, 9.32, 6.48, 0.78, 5.89, 0.87, 2.07, 2.54, 6.66, 6.4, 1.19, 6.67, 0.65, 8.53, 9.96, 5.61, 2.4, 1.49, 5.66, 3.59, 7.45, 8.43, 4.75, 2.19, 1.78, 7.66, 1.75, 0.84, 6.6, 6.02, 0.41, 5.69, 4.08};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {2.86, 6.39, 7.06, 1.87, 8.66, 8.58, 5.56, 8.1, 9.44, 9.89, 2.09, 2.33, 7.98, 8.11, 9.37, 6.08, 0.5, 3.79, 0.97, 9.09, 3.92, 6.31, 2.32, 3.66, 6.99, 9.49, 0.1, 3.22, 9.08, 5.39, 2.61, 4.4, 4.85, 3.05, 7.94, 0.06, 9.22, 0.43, 7.31, 8.61, 4.61, 3.95, 8.05, 1.16, 1.62, 9.55, 4.41, 4.21, 4.98, 3.33, 4.77, 9.81, 9.2, 8.01, 0.59, 9.42, 1.78, 3.21, 5.84, 0.09, 7.8, 3.85, 5.63, 3.81, 6.22, 1.27, 9.34, 1.88, 7.09, 5.78, 6.59, 5.93, 9.0, 7.68, 4.14, 6.62, 7.53, 6.36, 7.75, 9.05};	
		double orderingBudget = 8000;
		double holding = 8000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;	
	}
	
	public static boolean singleNewsboy_7(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {11, 49, 33, 23, 39, 6, 46, 16, 48, 27, 4, 7, 2, 7, 38, 11, 18, 1, 12, 20, 17, 30, 30, 8, 0, 1, 23, 42, 21, 47, 46, 29, 27, 4, 16, 42, 8, 14, 16, 0, 9, 20, 38, 34, 26, 24, 32, 5, 37, 36, 46, 3, 5, 1, 0, 14, 5, 0, 1, 6, 4, 48, 0, 30, 34, 41, 41, 10, 39, 39, 16, 3, 39, 7, 22, 42, 49, 8, 6, 46, 48, 14, 49, 30, 37, 8, 48, 43, 37, 38, 11, 42, 12, 7, 47, 7, 12, 33, 40, 29};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * 0.5) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {7.3, 2.22, 8.83, 1.42, 2.46, 0.52, 9.26, 1.67, 3.3, 9.48, 7.22, 0.55, 2.81, 3.47, 9.95, 1.26, 3.13, 1.59, 9.18, 7.37, 2.22, 6.84, 2.82, 0.09, 3.15, 1.33, 9.94, 8.37, 2.15, 3.77, 2.86, 3.3, 9.25, 6.58, 4.43, 1.86, 9.05, 8.49, 3.51, 1.5, 7.53, 5.32, 6.56, 6.1, 5.16, 5.92, 8.92, 6.14, 5.43, 2.33, 4.77, 0.6, 2.98, 3.38, 1.0, 5.83, 5.21, 8.81, 6.5, 7.05, 6.4, 6.38, 5.0, 1.62, 4.27, 8.18, 6.53, 8.99, 4.34, 3.41, 0.01, 1.55, 9.07, 4.1, 6.77, 1.93, 4.91, 6.92, 3.06, 7.27, 2.28, 9.28, 7.52, 7.85, 0.91, 0.09, 8.21, 8.15, 4.84, 5.92, 2.79, 0.86, 9.7, 1.83, 4.28, 0.69, 7.54, 1.32, 4.62, 2.95};
		double holdcost[] = {3.49, 7.54, 9.87, 1.43, 7.36, 6.92, 8.36, 9.49, 0.67, 0.87, 6.54, 4.48, 2.15, 4.47, 1.56, 7.32, 3.92, 5.65, 1.31, 8.59, 0.52, 6.33, 8.2, 6.95, 7.54, 7.14, 3.66, 9.93, 5.01, 9.93, 0.96, 1.18, 2.07, 3.53, 4.1, 9.11, 9.88, 1.31, 3.64, 9.19, 8.97, 3.49, 8.41, 6.64, 5.99, 3.23, 3.61, 6.16, 2.0, 3.16, 1.62, 3.23, 1.43, 8.21, 1.95, 4.7, 6.29, 1.4, 0.53, 6.93, 5.0, 1.4, 6.48, 6.45, 8.9, 4.81, 6.91, 1.77, 2.76, 1.23, 6.02, 4.49, 6.84, 6.52, 5.37, 4.92, 9.8, 7.92, 6.15, 0.08, 1.26, 5.07, 2.78, 1.89, 2.13, 2.48, 9.58, 9.43, 9.01, 2.39, 8.49, 1.97, 6.19, 1.57, 6.67, 8.87, 5.02, 2.36, 7.07, 8.33};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {7.74, 9.03, 1.94, 5.42, 9.57, 9.37, 4.6, 8.97, 4.96, 4.12, 4.38, 5.21, 8.12, 5.81, 6.83, 8.45, 5.1, 7.52, 7.98, 4.83, 2.87, 4.11, 8.19, 4.69, 5.28, 3.61, 9.46, 6.8, 6.7, 5.35, 1.41, 3.71, 8.76, 2.53, 2.86, 6.77, 6.28, 7.37, 8.55, 3.18, 6.09, 6.51, 5.39, 5.21, 6.38, 2.53, 8.53, 3.34, 4.88, 5.34, 4.97, 0.94, 1.4, 3.6, 2.09, 8.09, 3.72, 2.03, 8.7, 6.0, 6.39, 5.23, 9.06, 1.54, 8.78, 2.47, 3.56, 3.49, 1.89, 0.73, 5.96, 8.2, 3.83, 5.07, 3.31, 2.7, 9.92, 6.73, 3.87, 7.27, 6.98, 9.04, 8.06, 8.27, 0.48, 0.47, 9.39, 3.93, 0.0, 2.53, 5.43, 2.61, 0.75, 7.09, 3.87, 3.38, 8.54, 4.19, 4.79, 3.41};	
		double orderingBudget = 5000;
		double holding = 5000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;	
	}
	
	public static boolean singleNewsboy_8(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {19, 36, 85, 63, 78, 91, 94, 64, 52, 37, 69, 65, 55, 54, 67, 42, 19, 17, 37, 98, 2, 92, 2, 52, 81, 14, 97, 38, 71, 14, 86, 25, 15, 67, 91, 44, 66, 31, 64, 97, 15, 94, 81, 56, 76, 77, 57, 44, 5, 82, 66, 26, 61, 40, 9, 6, 70, 85, 73, 67, 48, 93, 64, 45, 34, 38, 75, 76, 33, 40, 28, 12, 10, 31, 80, 16, 73, 57, 80, 85, 73, 71, 13, 58, 37, 21, 26, 50, 83, 28, 34, 87, 16, 90, 64, 23, 68, 49, 1, 19};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * 0.25) );
//			realDemand[j] = oDIMdemand[j];
//			solution.rVector[j] = 20;
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {6.2, 4.79, 7.81, 6.3, 1.14, 3.1, 4.26, 2.57, 7.16, 5.67, 4.73, 5.11, 0.42, 0.02, 7.43, 8.37, 1.82, 9.49, 1.53, 8.33, 4.79, 4.06, 2.5, 4.2, 0.45, 8.2, 5.17, 3.97, 0.87, 7.6, 6.85, 0.52, 2.79, 2.95, 2.84, 5.3, 5.79, 6.42, 2.43, 5.24, 0.73, 8.27, 4.12, 4.84, 6.58, 4.44, 5.31, 9.21, 6.6, 3.06, 2.75, 6.08, 7.26, 7.1, 7.39, 4.1, 6.98, 2.81, 8.78, 2.66, 4.98, 8.9, 2.23, 3.41, 4.53, 9.25, 4.14, 7.15, 8.9, 2.63, 9.33, 0.29, 3.01, 6.89, 7.41, 4.36, 8.19, 0.96, 3.89, 8.05, 2.96, 7.83, 5.88, 6.18, 7.84, 3.08, 1.34, 1.81, 8.01, 9.03, 5.57, 7.84, 3.71, 1.72, 8.64, 2.4, 4.51, 8.63, 2.41, 7.7};
		double holdcost[] = {5.42, 0.39, 0.08, 2.68, 5.03, 1.7, 5.7, 2.98, 5.1, 5.4, 2.65, 7.59, 1.29, 5.11, 1.16, 4.95, 3.82, 5.57, 8.75, 4.79, 9.32, 4.86, 8.38, 5.6, 3.28, 7.8, 4.5, 2.97, 2.06, 1.78, 6.54, 5.09, 9.42, 4.71, 6.4, 7.48, 2.29, 9.03, 0.46, 3.93, 1.88, 4.71, 9.17, 6.64, 1.84, 9.21, 7.05, 4.47, 2.06, 1.82, 3.86, 4.44, 7.94, 5.56, 3.41, 8.55, 7.56, 9.82, 9.69, 4.62, 5.87, 3.3, 1.48, 7.44, 3.42, 7.63, 5.68, 3.75, 9.43, 8.82, 6.23, 2.95, 2.9, 5.19, 0.49, 3.15, 3.44, 7.86, 7.32, 0.99, 3.27, 2.05, 1.39, 2.51, 4.81, 5.45, 6.38, 1.32, 4.44, 2.68, 5.95, 9.94, 1.19, 3.47, 4.56, 9.88, 0.69, 5.8, 2.4, 6.16};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {2.52, 5.67, 7.64, 5.84, 2.58, 9.85, 9.44, 2.46, 1.73, 4.14, 2.77, 9.67, 3.73, 7.18, 5.43, 4.32, 4.99, 2.08, 2.59, 4.02, 7.56, 2.1, 1.06, 0.67, 2.72, 9.02, 5.3, 2.89, 7.9, 6.13, 9.76, 1.08, 0.93, 7.11, 9.77, 0.44, 2.12, 8.51, 1.04, 2.07, 6.27, 8.82, 5.93, 9.34, 4.25, 9.08, 3.44, 7.14, 5.59, 0.66, 8.53, 4.47, 2.02, 3.76, 5.43, 6.5, 4.27, 6.41, 9.42, 1.22, 3.27, 7.08, 0.84, 6.76, 0.31, 5.97, 5.72, 9.94, 9.96, 9.32, 7.35, 3.68, 3.5, 8.1, 4.06, 0.61, 0.59, 7.74, 9.55, 2.97, 1.71, 8.17, 3.29, 4.4, 3.94, 4.17, 0.19, 7.34, 8.64, 4.39, 0.89, 1.2, 9.98, 3.83, 5.98, 9.91, 9.89, 8.42, 0.4, 4.67};	
		double orderingBudget = 10000;
		double holding = 10000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;	
	}
	
		
	public static boolean singleNewsboy_1001(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {66, 57, 73, 87, 69, 80, 86, 73, 91, 60, 55, 72, 83, 98, 54, 51, 67, 86, 64, 79};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * j * 0.25) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
//			System.out.print(realDemand[j] + ",");
		}
//		System.out.println(",");
		//商品存储成本单价
		double orderingcost[] = {6, 9, 8, 2, 3, 6, 1, 9, 9, 9, 1, 8, 7, 7, 7, 1, 5, 2, 2, 10};
		double holdcost[] = {1, 8, 5, 8, 7, 5, 9, 8, 2, 6, 5, 7, 1, 8, 10, 2, 9, 9, 8, 5};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {4, 6, 10, 2, 6, 7, 2, 9, 10, 1, 10, 5, 3, 4, 8, 3, 1, 3, 10, 2};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
//			System.out.println("yes");
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
//			System.out.println(solution.f[0]);
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1002(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {83, 81, 71, 58, 87, 82, 92, 51, 56, 93, 53, 71, 56, 73, 73, 50, 73, 51, 52, 91};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * j * 0.25) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {3, 4, 1, 2, 6, 7, 7, 7, 8, 10, 1, 8, 2, 7, 3, 4, 10, 2, 2, 4};
		double holdcost[] = {1, 2, 8, 3, 6, 2, 9, 10, 1, 6, 4, 7, 6, 8, 6, 2, 1, 9, 10, 6};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {1, 7, 9, 2, 9, 8, 9, 9, 7, 2, 4, 4, 3, 6, 7, 1, 1, 5, 6, 2};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1003(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {73, 98, 85, 67, 95, 95, 52, 70, 76, 55, 71, 97, 96, 72, 96, 57, 69, 98, 61, 87};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.25) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {7, 5, 4, 8, 10, 5, 4, 5, 5, 10, 1, 3, 6, 8, 3, 1, 10, 2, 6, 8};
		double holdcost[] = {6, 1, 6, 2, 4, 5, 1, 7, 4, 5, 8, 5, 4, 4, 2, 9, 3, 7, 3, 2};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {5.3, 2.58, 1.25, 3.8, 7.17, 1.22, 2.69, 1.05, 7.33, 7.45, 9.22, 5.18, 9.68, 3.11, 7.28, 8.66, 2.88, 4.28, 3.26, 1.33};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1004(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {81, 95, 61, 59, 54, 95, 75, 95, 84, 55, 59, 90, 87, 50, 84, 59, 82, 89, 94, 68};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.05) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {5.16, 3.25, 1.72, 5.14, 5.75, 4.06, 6.69, 3.93, 4.95, 9.58, 4.93, 2.44, 9.38, 2.98, 8.03, 0.56, 8.99, 9.53, 6.61, 9.2};
		double holdcost[] = {9.52, 4.21, 8.75, 2.36, 6.1, 9.13, 0.81, 6.88, 0.46, 9.29, 9.46, 6.52, 9.09, 7.34, 0.11, 2.83, 6.35, 4.76, 2.12, 9.21};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {6, 5, 7, 5, 5, 10, 8, 9, 4, 7, 1, 4, 6, 1, 4, 1, 6, 7, 2, 7};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1005(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {79, 85, 77, 76, 70, 65, 75, 67, 76, 66, 83, 88, 93, 53, 98, 73, 71, 94, 84, 75};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (2*rDIM - j) * 0.05) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {5.4, 3.88, 1.61, 9.13, 8.54, 1.38, 9.18, 6.58, 9.87, 2.17, 6.97, 4.34, 6.34, 4.18, 1.9, 3.08, 5.18, 2.19, 2.86, 1.37};
		double holdcost[] = {9, 5, 10, 3, 1, 3, 1, 3, 5, 7, 5, 6, 7, 8, 4, 5, 4, 5, 10, 7};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {8.58, 4.86, 4.21, 7.31, 9.79, 6.17, 6.79, 5.71, 2.08, 6.78, 5.53, 2.93, 2.08, 9.01, 4.55, 0.79, 3.18, 6.44, 6.13, 5.69};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1006(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {76, 93, 94, 95, 93, 64, 89, 54, 92, 53, 86, 57, 62, 73, 81, 95, 71, 82, 78, 89};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {1.71, 8.59, 6.71, 0.77, 4.66, 7.0, 3.35, 5.35, 4.89, 6.31, 2.27, 3.79, 6.26, 3.33, 5.62, 4.57, 5.95, 2.72, 1.78, 9.56};
		double holdcost[] = {9, 8, 3, 9, 2, 4, 6, 2, 5, 8, 4, 3, 2, 2, 4, 2, 4, 7, 6, 1};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {2.35, 8.01, 8.15, 6.42, 8.53, 5.79, 7.47, 5.49, 5.23, 3.53, 2.31, 5.05, 5.39, 2.31, 2.84, 6.04, 4.34, 3.81, 4.59, 3.28};	
		double orderingBudget = 1000;
		double holding = 1000;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1007(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {74, 84, 57, 66, 56, 82, 88, 78, 71, 62, 81, 95, 87, 79, 83, 66, 88, 66, 92, 53};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {3.06, 2.78, 3.81, 8.23, 2.21, 6.43, 3.32, 4.01, 8.19, 8.03, 7.61, 7.45, 3.61, 0.47, 6.42, 4.12, 8.19, 3.64, 9.31, 3.26};
		double holdcost[] = {1.93, 3.03, 4.93, 7.0, 9.36, 9.87, 6.62, 7.88, 2.58, 8.09, 9.49, 3.54, 3.68, 3.03, 7.34, 3.72, 1.17, 5.9, 9.43, 8.31};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {9.07, 7.8, 9.79, 1.74, 8.21, 2.76, 6.19, 2.32, 4.66, 3.25, 2.47, 6.22, 2.54, 7.34, 3.1, 7.91, 7.35, 4.21, 9.24, 3.26};	
		double orderingBudget = 800;
		double holding = 800;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1008(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {81, 82, 64, 87, 68, 74, 71, 82, 52, 77, 95, 79, 96, 64, 70, 81, 55, 72, 82, 89};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {4.27, 7.68, 7.88, 4.81, 6.01, 2.99, 5.18, 5.94, 1.8, 9.19, 2.93, 3.16, 3.94, 2.16, 1.48, 5.07, 1.36, 3.25, 7.58, 8.39};
		double holdcost[] = {3.89, 3.91, 8.56, 3.32, 1.71, 7.8, 7.18, 7.29, 8.26, 2.87, 5.79, 8.12, 2.35, 7.21, 9.17, 9.24, 2.48, 1.86, 3.82, 4.34};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {9.44, 0.99, 4.88, 7.9, 5.57, 0.78, 9.85, 8.21, 2.99, 5.8, 6.52, 3.27, 7.8, 0.83, 7.08, 4.52, 9.11, 6.12, 1.98, 4.13};	
		double orderingBudget = 800;
		double holding = 800;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_1009(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {52, 60, 79, 81, 61, 82, 91, 80, 57, 81, 50, 88, 73, 58, 59, 53, 76, 51, 58, 63};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {1.01, 2.3, 7.19, 0.9, 5.95, 5.92, 1.3, 2.04, 0.32, 3.53, 2.92, 6.79, 3.37, 9.2, 6.82, 7.08, 4.14, 3.86, 1.13, 2.89};
		double holdcost[] = {3.5, 2.64, 2.55, 5.37, 3.35, 7.8, 2.67, 2.93, 7.02, 0.33, 5.83, 7.24, 4.68, 7.22, 9.96, 5.09, 5.26, 9.37, 3.63, 0.59};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {2.63, 2.83, 9.32, 9.38, 4.15, 1.77, 0.94, 4.91, 9.65, 0.64, 6.79, 9.45, 2.88, 1.19, 6.54, 0.13, 4.72, 3.97, 7.31, 1.43};	
		double orderingBudget = 800;
		double holding = 800;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_10010(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {86, 61, 71, 88, 58, 96, 87, 53, 64, 96, 69, 65, 55, 84, 72, 90, 53, 64, 89, 88, 72, 89, 88, 71, 94, 81, 72, 70, 85, 89, 60, 56, 80, 79, 64, 74, 93, 86, 94, 71};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {8.33, 4.89, 0.94, 1.84, 1.32, 2.74, 5.26, 6.75, 6.13, 2.44, 7.06, 7.7, 4.25, 9.52, 8.18, 0.86, 4.71, 9.71, 4.25, 6.34, 3.42, 7.36, 0.54, 5.05, 2.8, 5.86, 1.49, 4.76, 8.15, 0.52, 6.34, 7.91, 2.45, 4.9, 5.87, 8.79, 3.74, 3.4, 0.79, 7.71};
		double holdcost[] = {5.99, 0.39, 3.03, 8.61, 8.05, 5.98, 1.88, 9.94, 1.71, 5.78, 5.59, 5.89, 0.96, 2.54, 5.79, 1.28, 9.08, 2.56, 9.03, 4.22, 5.2, 2.59, 4.13, 8.93, 5.08, 3.32, 1.86, 7.23, 3.35, 8.8, 7.13, 1.24, 7.95, 3.39, 4.21, 4.19, 6.99, 6.29, 5.51, 2.96};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {9.63, 8.48, 1.67, 6.85, 6.41, 1.64, 1.11, 5.23, 0.8, 3.19, 1.98, 4.89, 5.86, 4.09, 8.15, 7.25, 7.34, 2.34, 7.18, 3.99, 3.93, 5.96, 8.11, 0.21, 6.46, 7.16, 5.76, 9.92, 8.54, 2.02, 6.57, 3.36, 1.81, 8.64, 3.89, 3.1, 6.91, 6.29, 6.54, 9.12};	
		double orderingBudget = 1600;
		double holding = 1600;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_10011(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {65, 98, 86, 99, 68, 50, 83, 98, 83, 67, 73, 50, 99, 59, 57, 83, 85, 50, 89, 93, 76, 72, 54, 84, 79, 88, 92, 74, 53, 94, 62, 89, 61, 88, 62, 66, 54, 54, 56, 72};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
//			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.05) );
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {6.21, 7.66, 5.28, 3.01, 7.07, 3.41, 3.44, 6.36, 6.06, 9.76, 9.35, 6.85, 4.3, 9.53, 7.03, 5.61, 5.29, 5.39, 1.14, 4.99, 6.7, 3.36, 0.88, 8.93, 9.67, 1.26, 6.12, 5.16, 4.3, 5.69, 6.07, 4.72, 9.75, 5.14, 4.81, 9.78, 8.97, 6.02, 0.41, 6.38};
		double holdcost[] = {6.07, 3.57, 1.21, 8.19, 9.99, 8.67, 3.17, 7.58, 5.9, 8.8, 8.03, 6.72, 9.03, 8.51, 3.49, 2.21, 4.09, 1.79, 8.98, 9.78, 7.88, 5.44, 8.85, 3.44, 4.68, 6.05, 5.3, 0.48, 9.0, 9.64, 0.09, 5.96, 3.34, 6.55, 1.41, 9.49, 2.92, 1.96, 6.6, 5.59};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {5.8, 8.59, 2.75, 4.53, 0.33, 4.33, 1.57, 3.17, 0.64, 2.15, 0.66, 9.86, 8.32, 5.77, 5.7, 6.45, 3.27, 7.87, 1.53, 4.69, 3.58, 7.74, 6.07, 3.71, 7.9, 2.68, 5.76, 1.26, 2.6, 5.67, 2.11, 6.36, 4.05, 2.72, 4.2, 4.56, 9.55, 2.29, 5.95, 3.54};	
		double orderingBudget = 1600;
		double holding = 1600;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}
	
	public static boolean singleNewsboy_10012(mvSolution solution, int M){
		boolean flag = false;
		int rDIM = solution.rDIM, oDIM = solution.oDIM, cDIM = solution.cDIM;
		double sumProfit = 0.0,sumHoldCost=0.0, sumOrderingCost = 0.0;
		//商品需求
		double oDIMdemand[] = {94, 58, 83, 81, 94, 73, 87, 77, 60, 98, 93, 76, 64, 60, 68, 59, 89, 94, 52, 68, 53, 79, 64, 65, 73, 83, 57, 99, 96, 52, 78, 56, 68, 91, 75, 70, 65, 96, 80, 75};	
		double realDemand[] = new double[oDIM];
		//根据商品单价 更新需求
		for(int j = 0; j <rDIM; j++){
			realDemand[j] = (int) ( oDIMdemand[j] -  (solution.rVector[j] * (rDIM - j) * 0.1) );
//			realDemand[j] = oDIMdemand[j];
			if (realDemand[j] <= 0) {
				realDemand[j] = 0;
			}
		}
		//商品存储成本单价
		double orderingcost[] = {9.52, 1.52, 6.39, 6.76, 1.33, 0.04, 5.12, 2.71, 3.3, 8.4, 7.19, 1.09, 8.55, 6.76, 8.67, 3.84, 2.82, 7.4, 1.72, 5.84, 9.28, 2.78, 2.98, 9.9, 6.54, 1.27, 4.69, 2.11, 4.26, 2.35, 1.48, 3.44, 2.14, 6.04, 2.08, 1.61, 1.49, 7.33, 8.11, 4.67,};
		double holdcost[] = {5.78, 4.78, 1.81, 3.81, 4.49, 6.33, 2.89, 7.66, 7.8, 3.9, 2.63, 2.43, 1.54, 4.13, 6.16, 7.81, 6.41, 0.94, 8.29, 5.35, 2.1, 7.58, 3.98, 7.52, 5.63, 6.26, 4.23, 2.02, 4.49, 7.02, 6.27, 6.22, 6.49, 1.06, 8.73, 6.43, 2.45, 2.94, 1.68, 6.97};
		//商品单位利润
		double profit[] =  new double[rDIM];
		for(int j = 0; j < rDIM; j++){
			profit[j] = solution.rVector[j] - orderingcost[j];
		}
		//缺货损失
		double lostExpand[] = {5.52, 4.97, 9.28, 0.83, 9.77, 5.96, 8.4, 3.27, 6.79, 9.42, 5.85, 7.46, 4.58, 3.89, 3.38, 4.59, 1.53, 8.91, 7.17, 0.84, 4.94, 1.37, 1.81, 2.82, 0.35, 8.36, 8.72, 4.73, 3.25, 0.35, 8.55, 9.99, 7.14, 3.93, 8.62, 6.26, 5.3, 2.06, 4.45, 7.57};	
		double orderingBudget = 1600;
		double holding = 1600;
		// constraint evaluation
		for (int j = 0; j < rDIM; j++) {
			sumHoldCost += holdcost[j] * (int)(solution.oVector[j] + 0.5);
			sumOrderingCost += orderingcost[j] * (int)(solution.oVector[j] + 0.5);
			sumProfit += (int)(solution.oVector[j]+0.5) > realDemand[j] ?
					realDemand[j] * profit[j] + ((int)(solution.oVector[j] + 0.5) - realDemand[j]) * holdcost[j] :
						(int)(solution.oVector[j] + 0.5) * profit[j] - (realDemand[j] - (int)(solution.oVector[j] + 0.5)) * lostExpand[j];
		}
		if (sumOrderingCost > orderingBudget || sumHoldCost > holding) {
			// 罚函数
			solution.f[1] = Math.max(0, sumOrderingCost - orderingBudget);
			solution.f[2] = Math.max(0, sumHoldCost - holding);
			solution.f[0] = sumProfit - 1000 *solution.f[1] - 1000*solution.f[2];
			flag = false;
		} else {
			solution.f[0] = sumProfit - sumOrderingCost - sumHoldCost;
			flag = true;
		}
		return flag;
	}

	
	public static double calc(double u) {
		double y = Math.abs(u);
		double y2 = y * y;
		double z = Math.exp(-0.5 * y2) * 0.398942280401432678;
		double p = 0;
		int k = 28;
		double s = -1;
		double fj = k;

		if (y > 3) {
			// 当y>3时
			for (int i = 1; i <= k; i++) {
				p = fj / (y + p);
				fj = fj - 1.0;
			}
			p = z / (y + p);
		} else {
			// 当y<3时
			for (int i = 1; i <= k; i++) {
				p = fj * y2 / (2.0 * fj + 1.0 + s * p);
				s = -s;
				fj = fj - 1.0;
			}
			p = 0.5 - z * y / (1 - p);
		}
		if (u > 0)
			p = 1.0 - p;
		return p;
	}

}
