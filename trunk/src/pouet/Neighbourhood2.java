//package pouet;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import util.Pair;
//
//
//public class Neighbourhood2 implements Neighbourhood {
//
//    @Override
//    public Neighbour findBestNeighbour(Solution sol,
//	    List<Pair<Integer, Integer>> tabuList) {
//	
//	int bestCost = sol.cost();
//	List<Pair<Integer, Integer>> bestMoves =
//		new ArrayList<Pair<Integer, Integer>>();
//	
//	Pair<Integer, Integer> currentMove = null;
//	
//	long nbNeighbours = sol.size() * (sol.size()-1) / 2;
//	long cntNeighbours = 0;
//	int k = 1;
//	
//	for(Integer row1 = 0 ; row1 < sol.size()-1 ; ++row1) {
//	    for(Integer row2 = row1+1 ; row2 < sol.size() ; ++row2) {
//		
//		if(cntNeighbours / (double) (nbNeighbours) > k*0.1) {
//		    System.out.println("Neighbourhood exploration : " + (k*10) + "%");
//		    ++k;
//		}
//		
//		currentMove = new Pair<Integer, Integer>(row1, row2);
//		
//		if(tabuList.contains(currentMove) == false) {
//		    sol.swap(row1, row2);
//		    
//		    if(sol.cost() <= bestCost) {
//			if(sol.cost() < bestCost) {
//			    bestMoves.clear();
//			}
//
//			bestMoves.add(new Pair<Integer, Integer>(currentMove));
//			bestCost = sol.cost();
//		    }
//		    
//		    sol.swap(row1, row2);
//		}
//		
//		++cntNeighbours;
//	    }
//	}
//	
//	Random rand = new Random();
//	Integer randIndex = rand.nextInt(bestMoves.size());
//	Pair<Integer, Integer> move = bestMoves.get(randIndex);
//
//	
//	Solution neighbour = (Solution) sol.clone();
//	neighbour.swap(move.getFirst(), move.getSecond());
//	
//	return new Neighbour(neighbour, move);
//    }
//}
