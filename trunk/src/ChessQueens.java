import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import JaCoP.constraints.Alldifferent;
import JaCoP.constraints.XplusCeqZ;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.ValueEnumeration;
import JaCoP.search.DepthFirstSearch;
import JaCoP.search.IndomainMedian;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;
import JaCoP.search.SmallestDomain;

public class ChessQueens {
    /**
     * Container du probleme.
     * 
     * <p>
     * Contient l'ensemble des variables et contraintes du problème.
     * </p>
     */
    private Store store;

    /**
     * Les variables du problème.
     * 
     * <p>
     * Q[i] correspond à la colonne de la reine de la ligne i.
     * </p>
     */
    private IntVar[] Q;

    /**
     * La liste tabu.
     * 
     * <p>
     * File de taille maximale fixe qui contient les mouvements tabous.
     * </p>
     * 
     * @see #tabuListSize
     */
    private ArrayList<Pair<Integer, Integer>> tabuList;

    /**
     * La taille maximale de la liste tabu.
     * 
     * <p>
     * Lorsque la taille maximale de la liste tabu est atteinte, le mouvement
     * tabou le plus vieux est retiré pour laisser place au nouveau.
     * </p>
     */
    private int tabuListSize = 0;

    /**
     * Construit un nouveau problème des n reines.
     * 
     * @param n
     *            Le nombre de reines
     * @param t
     *            La taille de la liste tabu
     */
    public ChessQueens(int n, int t) {
	this.store = new Store();
	this.Q = new IntVar[n];
	this.tabuList = new ArrayList<Pair<Integer, Integer>>();
	this.tabuListSize = t;

	IntVar[] y = new IntVar[n];
	IntVar[] z = new IntVar[n];

	for (int i = 0; i < n; ++i) {
	    this.Q[i] = new IntVar(this.store, "Q" + i, 0, n - 1);
	    y[i] = new IntVar(this.store, "y" + i, -i, n - 1 - i);
	    z[i] = new IntVar(this.store, "z" + i, i, n - 1 + i);

	    this.store.impose(new XplusCeqZ(this.Q[i], i, z[i]));
	    this.store.impose(new XplusCeqZ(y[i], i, this.Q[i]));
	}

	// all different: no attack on columns
	this.store.impose(new Alldifferent(this.Q));
	this.store.impose(new Alldifferent(y));
	this.store.impose(new Alldifferent(z));
    }

    // Get the domains of the main variables
    public IntDomain[] getDomains() {
	IntDomain[] tab = new IntDomain[this.Q.length];
	for (int i = 0; i < this.Q.length; ++i) {
	    tab[i] = this.Q[i].domain;
	}
	return tab;
    }

    // Generate randomly a solution within the domains
    public int[] generateSolution(IntDomain[] domains) {
	Random rand = new Random();
	int[] solution = new int[domains.length];

	for (int i = 0; i < domains.length; ++i) {
	    ValueEnumeration values = domains[i].valueEnumeration();
	    int r = rand.nextInt(domains[i].getSize()); // 0 .. getSize()-1

	    for (int j = 0; j <= r; ++j) {
		solution[i] = values.nextElement(); // only the r-th is relevant
	    }
	}

	return solution;
    }

    // Cost or fitness of an alldifferent constraint
    public int costAllDifferent(int[] sol) {
	int n = 0;
	for (int i = 0; i < sol.length; ++i) {
	    for (int j = i + 1; j < sol.length; ++j) {
		if (sol[i] == sol[j])
		    ++n;
	    }
	}
	return n;
    }

    // Fitness of a solution for the n-queens problem
    public int fitness(int[] sol) {
	int n = 0;

	// allDifferent on Q
	n += costAllDifferent(sol);

	// allDifferent on y
	int[] aux = new int[sol.length];
	for (int i = 0; i < sol.length; ++i) {
	    aux[i] = sol[i] + i;
	}
	n += costAllDifferent(aux);

	// allDifferent on z
	for (int i = 0; i < sol.length; ++i) {
	    aux[i] = sol[i] - i;
	}
	n += costAllDifferent(aux);

	return n;
    }

    // Display a solution
    public static void printSolution(int[] sol) {
	System.out.print("{");
	for (int i = 0; i < sol.length; ++i) {
	    if (i != 0)
		System.out.print(", ");
	    System.out.print(sol[i]);
	}
	System.out.print("}");
    }

    private Boolean isTabu(Integer row, Integer column) {
	return (this.tabuList.contains(new Pair<Integer, Integer>(row, column)) == false);
    }

    /**
     * Parcourt le voisinnage d'une solution.
     * 
     * @param sol
     * 		La solution courante
     * 
     * @return Le mouvement à effectuer pour atteindre le meilleur voisin
     * de la solution courante
     */
    private Pair<Integer, Integer> findBestNeighbour(int[] sol) {
	int[] bestNeighbour = new int[sol.length];
	Pair<Integer, Integer> bestMove = null;
	int bestCost = Integer.MAX_VALUE;

	int[] currentNeighbour = new int[sol.length];
	Pair<Integer, Integer> currentMove = null;
	int currentCost = 0;

	// Initialisation de currentNeighbour
	for (int i = 0; i < sol.length; i++) {
	    currentNeighbour[i] = sol[i];
	}

	// Parcours du voisinnage :
	// pour chaque variable, on teste l'ensemble des valeurs possibles
	for (int row = 0; row < sol.length; row++) {
	    for (int column = 0; column < sol.length; column++) {
		currentMove = new Pair<Integer, Integer>(row, column);

		// La position de la variable modifiée ne doit
		// pas appartenir à la liste tabu
		if (this.isTabu(row, column)) {
		    currentNeighbour[row] = column;
		    currentCost = fitness(currentNeighbour);

		    if (currentCost < bestCost) {
			for (int i = 0; i < bestNeighbour.length; i++) {
			    bestNeighbour[i] = currentNeighbour[i];
			}

			bestMove = currentMove;
			bestCost = currentCost;
		    }
		}
	    }

	    currentNeighbour[row] = sol[row];
	}

	return bestMove;
    }

    /**
     * Effectue une recherche taboue.
     * 
     * @param nRuns Nombre maximal de runs à effectuer
     * 
     * @return <ul>
     *         <li>true si une solution est trouvée,</li>
     *         <li>false sinon</li>
     *         </ul>
     */
    public boolean tabuSearch(Integer nRuns) {
	int[] bestSoFarSol = new int[this.Q.length];
	int bestSoFarCost = Integer.MAX_VALUE;
	Boolean stop = false;

	IntDomain[] domains = getDomains();

	long startTime = System.currentTimeMillis();

	for (int run = 0; run < nRuns && stop == false; run++) {
	    System.out.println("Run " + (run + 1));
	    tabuList.clear();

	    // Génération de la solution initiale du run
	    int[] currentSol = generateSolution(domains);
	    int currentCost = fitness(currentSol);

	    // System.out.print("Solution initiale aléatoire : ");
	    // printSolution(currentSol);
	    System.out.println("Cout initial : " + currentCost);

	    Boolean goOn = true;
	    
	    // On continue la recherche tant que l'on trouve
	    // un voisin au moins aussi bon que la solution courante
	    // et que celle-ci n'est pas admissible
	    while (goOn && currentCost > 0) {
		goOn = false;
		
		Pair<Integer, Integer> moveToNeighbour = findBestNeighbour(currentSol);

		if (moveToNeighbour != null) {
		    int[] bestNeighbour = new int[currentSol.length];

		    for (int i = 0; i < bestNeighbour.length; i++) {
			if (i == moveToNeighbour.getFirst()) {
			    bestNeighbour[i] = moveToNeighbour.getSecond();
			} else {
			    bestNeighbour[i] = currentSol[i];
			}
		    }

		    int bestNeighbourCost = fitness(bestNeighbour);

		    if (bestNeighbourCost <= currentCost) {
			for (int i = 0; i < currentSol.length; i++) {
			    currentSol[i] = bestNeighbour[i];
			}

			currentCost = bestNeighbourCost;

			if ((this.tabuList.size() == tabuListSize)
			        && (tabuListSize > 0)) {
			    tabuList.remove(0);
			}

			tabuList.add(moveToNeighbour);

			// Affichage de la liste tabu (DBG)
			// System.out.print("Liste tabu : ");
			// for(int i = 0 ; i < tabuList.size() ; i++) {
			// System.out.print(tabuList.get(i) + " ");
			// }
			// System.out.println();

			// System.out.println("Nouveau meilleur cout"
			// + " pour le run : " + bestNeighbourCost);

			goOn = true;

			if (bestNeighbourCost < bestSoFarCost) {
			    bestSoFarCost = bestNeighbourCost;

			    for (int i = 0; i < bestSoFarSol.length; i++) {
				bestSoFarSol[i] = bestNeighbour[i];
			    }

			    System.out.println("Nouveau meilleur cout : "
				    + bestSoFarCost);
			}
		    }
		}
	    }

	    if (currentCost == 0) {
		System.out.print("Solution admissible trouvée : ");
		printSolution(currentSol);
		System.out.println("\n");

		stop = true;
	    } else {
		System.out.println("Pas de solution admissible trouvée.");
		System.out.println();
	    }
	}

	long endTime = System.currentTimeMillis();

	System.out.println("Temps d'execution : " + (endTime - startTime) + "ms");

	return (bestSoFarCost == 0);
    }

    /**
     * Effectue une recherche complete.
     * 
     * @return <ul>
     *         <li>true si une solution est trouvée,</li>
     *         <li>false sinon</li>
     *         </ul>
     */
    public boolean completeSearch() {
	DepthFirstSearch<IntVar> search = new DepthFirstSearch<IntVar>();

	search.getSolutionListener().searchAll(true);
	search.getSolutionListener().recordSolutions(true);

	SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(this.Q,
	        new SmallestDomain<IntVar>(), new IndomainMedian<IntVar>());

	boolean result = search.labeling(store, select);

	for (int i = 1; i <= search.getSolutionListener().solutionsNo(); i++) {
	    System.out.print("Solution " + i + ": [");
	    for (int j = 0; j < search.getSolution(i).length; j++) {
		if (j != 0)
		    System.out.print(", ");
		System.out.print(search.getSolution(i)[j]);
	    }
	    System.out.println("]");
	}

	return result;
    }

    private static ArrayList<Integer> parseCmdLine(String[] args)
	    throws Exception {

	ArrayList<Integer> argList = new ArrayList<Integer>();

	if (args.length > 0) {
	    if (args.length >= 2) {
		try {
		    String arg = args[0];
		    argList.add(Integer.valueOf(arg));

		    arg = args[1];
		    argList.add(Integer.valueOf(arg));

		    if (args.length >= 3) {
			arg = args[2];
			argList.add(Integer.valueOf(arg));
		    }
		}

		catch (NumberFormatException e) {
		    System.out.println("Erreur :"
			    + " les arguments doivent être des entiers.");
		    System.out.println("Usage : java -jar ChessQueens"
			    + " [<nQueens> <tabuListSize> <nRuns>]");

		    throw e;
		}

	    }

	    else {
		System.out.println("Erreur : nombre d'arguments incorrect.");
		System.out.println("Usage : java -jar ChessQueens"
		        + " [<nQueens> <tabuListSize> <nRuns>]");

		throw new Exception();
	    }
	}

	return argList;
    }

    private static ArrayList<Integer> askArguments() {
	ArrayList<Integer> argList = new ArrayList<Integer>();
	Integer nQueens = 0, tabuListSize = 0, nRuns = 0;
	String saisie = null;

	Scanner scanner = new Scanner(System.in);

	while (nQueens < 4) {
	    System.out.print("Nombre de reines (4 minimum) : ");
	    saisie = scanner.nextLine();

	    try {
		nQueens = Integer.valueOf(saisie);
	    }

	    catch (NumberFormatException e) {
		;
	    }

	    if (nQueens > 3) {
		argList.add(nQueens);
	    }

	    else {
		System.out.println("Saisie incorrecte.");
	    }
	}

	while (tabuListSize < 1) {
	    System.out.print("Taille de la liste tabu : ");
	    saisie = scanner.nextLine();

	    try {
		tabuListSize = Integer.valueOf(saisie);
	    }

	    catch (NumberFormatException e) {
		;
	    }

	    if (tabuListSize > 0) {
		argList.add(tabuListSize);
	    }

	    else {
		System.out.println("Saisie incorrecte.");
	    }
	}

	while (nRuns < 1) {
	    System.out.print("Nombre de runs : ");
	    saisie = scanner.nextLine();

	    try {
		nRuns = Integer.valueOf(saisie);
	    }

	    catch (NumberFormatException e) {
		;
	    }

	    if (tabuListSize > 0) {
		argList.add(nRuns);
	    }

	    else {
		System.out.println("Saisie incorrecte.");
	    }
	}

	scanner.close();

	return argList;
    }

    public static void main(String[] args) {
	ArrayList<Integer> argList = null;

	try {
	    argList = parseCmdLine(args);

	    if (argList.isEmpty()) {
		argList = askArguments();
	    }
	}

	catch (Exception e) {
	    return;
	}

	if ((argList != null) && (argList.isEmpty() == false)) {
	    ChessQueens model = new ChessQueens(argList.get(0), argList.get(1));
	    model.tabuSearch(argList.get(2));
	    // boolean res2 = model.completeSearch();
	}
    }

}
