package meuh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import JaCoP.constraints.Alldifferent;
import JaCoP.constraints.XplusCeqZ;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.search.DepthFirstSearch;
import JaCoP.search.IndomainMedian;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;
import JaCoP.search.SmallestDomain;

import util.Pair;


/**
 * Seconde version de recherche tabu pour le probleme des n reines.
 * 
 * <p>
 * La solution initiale est formée en placant les reines sur une diagonale.
 * Le mouvement de voisinage est le swap entre deux lignes.
 * La taille du voisinage est ainsi reduite de n^2 a n*(n-1)/2 mais le nombre
 * de mouvements pour atteindre une solution admissible est accru.
 * Pas de gain de temps obtenu :(
 * </p>
 */
public class ChessQueensV2 {
    /**
     * Container du probleme.
     * 
     * <p>
     * Contient l'ensemble des variables et contraintes du probleme.
     * </p>
     */
    private Store store;

    /**
     * Les variables du probleme.
     * 
     * <p>
     * Q[i] correspond a la colonne de la reine de la ligne i.
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
     * tabou le plus vieux est retire pour laisser place au nouveau.
     * </p>
     */
    private int tabuListSize = 0;
    
    /**
     * Construit un nouveau probleme des n reines.
     * 
     * @param n
     *            Le nombre de reines
     * @param t
     *            La taille de la liste tabu
     * @throws IOException 
     */
    public ChessQueensV2(int n, int t) {
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

    /**
     * Retourne le domaine des variables.
     * 
     * @return Un tableau contenant le domaine de chaque variable
     */
    private IntDomain[] getDomains() {
	IntDomain[] tab = new IntDomain[this.Q.length];
	for (int i = 0; i < this.Q.length; ++i) {
	    tab[i] = this.Q[i].domain;
	}
	return tab;
    }

    /**
     * Genere aleatoirement une solution.
     * 
     * @param domains
     *            Tableau des domaines des varaibles
     * 
     * @return Un tableau contenant la valeur des variables de la solution
     *         generee
     */
    private int[] generateSolution(IntDomain[] domains) {
	int[] solution = new int[domains.length];
	int[] values = new int[domains.length];
	
	for (int i = 0; i < domains.length; ++i) {
	    values[i] = i;
	}
	
	Random rand = new Random();
	
	for (int k = 0; k < 4*domains.length ; ++k) {
	    int i = rand.nextInt(domains.length);
	    int j = rand.nextInt(domains.length);
	    int aux = values[i];
	    
	    values[i] = values[j];
	    values[j] = aux;
	}
	
	for (int i = 0; i < domains.length; ++i) {
	    solution[i] = values[i];
	}

	return solution;
    }

    // Cost or fitness of an alldifferent constraint
    private int costAllDifferent(int[] sol) {
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
    private int fitness(int[] sol) {
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

    /**
     * Affiche une solution.
     * 
     * @param sol
     *            Un tableau contenant les valeur des variables d'une solution
     */
    private static void printSolution(int[] sol) {
	System.out.print("{");
	for (int i = 0; i < sol.length; ++i) {
	    if (i != 0)
		System.out.print(", ");
	    System.out.print(sol[i]);
	}
	System.out.print("}");
    }

    /**
     * Teste si une position (ligne,colonne) est taboue.
     * 
     * @param row
     *            Un index de ligne (index de variable)
     * @param column
     *            Un index de colonne (valeur de la variable)
     * 
     * @return <ul>
     *         <li>true si la position est taboue</li>
     *         <li>false sinon</li>
     *         </ul>
     */
    private Boolean isTabu(Integer row, Integer column) {
	return (this.tabuList.contains(new Pair<Integer, Integer>(row, column)) == false);
    }

    /**
     * Parcourt le voisinnage d'une solution.
     * 
     * @param sol
     *            La solution courante
     * 
     * @return Une paire contenant le mouvement pour atteindre le meilleur
     *         voisin de la solution (ligne,ligne) et son cout
     */
    private Pair<Pair<Integer, Integer>, Integer> findBestNeighbour(int[] sol) {
	int bestCost = Integer.MAX_VALUE;
	List<Pair<Integer, Integer>> bestMoves =
		new ArrayList<Pair<Integer, Integer>>();

	int[] currentNeighbour = new int[sol.length];
	Pair<Integer, Integer> currentMove = null;
	int currentCost = 0;
	
	// Initialisation de currentNeighbour
	for (int i = 0; i < sol.length; i++) {
	    currentNeighbour[i] = sol[i];
	}
	
	// Parcours du voisinnage :
	// pour chaque variable, on teste l'ensemble des valeurs possibles
	for (int row1 = 0; row1 < sol.length-1 ; row1++) {
	    for (int row2 = row1 + 1 ; row2 < sol.length; row2++) {
		currentMove = new Pair<Integer, Integer>(row1, row2);

		// La position de la variable modifiee ne doit
		// pas appartenir a la liste tabu
		if (this.isTabu(row1, row2)) {
		    Integer aux = currentNeighbour[row1];
		    currentNeighbour[row1] = currentNeighbour[row2];
		    currentNeighbour[row2] = aux;
		    
		    currentCost = fitness(currentNeighbour);
		    
		    if (currentCost < bestCost) {
			bestMoves.clear();
			bestMoves.add(new Pair<Integer, Integer>(currentMove));
			
			bestCost = currentCost;
		    }
		    
		    else if(currentCost == bestCost) {
			bestMoves.add(new Pair<Integer, Integer>(currentMove));
		    }
		    
		    currentNeighbour[row2] = currentNeighbour[row1];
		    currentNeighbour[row1] = aux;
		}
	    }
	}
	    
	Random rand = new Random();
	Integer randIndex = rand.nextInt(bestMoves.size());
	
	return new Pair<Pair<Integer, Integer>, Integer>(
		bestMoves.get(randIndex), bestCost);
    }

    /**
     * Effectue une recherche taboue.
     * 
     * @param nRuns
     *            Nombre maximal de runs a effectuer
     * 
     * @return <ul>
     *         <li>true si une solution est trouvee,</li>
     *         <li>false sinon</li>
     *         </ul>
     * @throws IOException 
     */
    public boolean tabuSearch(Integer nRuns) throws IOException {
	int[] bestSoFarSol = new int[this.Q.length];
	int bestSoFarCost = Integer.MAX_VALUE;
	Boolean stop = false;
	
	IntDomain[] domains = getDomains();

	for (int run = 0; run < nRuns && stop == false; run++) {
	    long startTime = System.currentTimeMillis();
	    int mvtsNoImprovement = 0;
	    
	    System.out.println("Run " + (run + 1));
	    tabuList.clear();

	    // Generation de la solution initiale du run
	    int[] currentSol = generateSolution(domains);
	    int currentCost = fitness(currentSol);
	    
//	    System.out.print("Solution initiale aleatoire : ");
//	    printSolution(currentSol);
	    System.out.println("\nCout initial : " + currentCost);

	    Boolean goOn = true;

	    // On continue la recherche tant que l'on trouve
	    // un voisin au moins aussi bon que la solution courante
	    // et que celle-ci n'est pas admissible
	    while (goOn && currentCost > 0) {
		goOn = false;

		Pair<Pair<Integer, Integer>, Integer> p =
			findBestNeighbour(currentSol);

		if (p != null) {
		   int bestNeighbourCost = p.getSecond();

		    // Le voisin est meilleur que la solution courante
		    if (bestNeighbourCost <= currentCost) {
			
			if(bestNeighbourCost == currentCost) {
			    ++mvtsNoImprovement;
			    System.out.println(mvtsNoImprovement
				    + " mouvement(s) sans amelioration");
			}
			
			else {
			    mvtsNoImprovement = 0;
			}
			
			int row1 = p.getFirst().getFirst();
			int row2 = p.getFirst().getSecond();
			
			Integer aux = currentSol[row1];
			currentSol[row1] = currentSol[row2];
			currentSol[row2] = aux;
			
			currentCost = bestNeighbourCost;
			
			if ((this.tabuList.size() == tabuListSize)
			        && (tabuListSize > 0)) {
			    tabuList.remove(0);
			}

			tabuList.add(p.getFirst());

			goOn = true;

			if (bestNeighbourCost < bestSoFarCost) {
			    bestSoFarCost = bestNeighbourCost;

			    for (int i = 0; i < bestSoFarSol.length; i++) {
				bestSoFarSol[i] = currentSol[i];
			    }

			    System.out.println("Nouveau meilleur cout : "
				    + bestSoFarCost);
			}
		    }
		}
	    }
	    
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Temps d'execution : " + (endTime - startTime)
		        + "ms");
	    
	    if (currentCost == 0) {
		System.out.print("Solution admissible trouvee : ");
		printSolution(currentSol);
		System.out.println("\n");

		stop = true;
	    }
	    
	    else {
		System.out.println("Pas de solution admissible trouvee.");
		System.out.println();
	    }
	}
	
	return (bestSoFarCost == 0);
    }

    /**
     * Effectue une recherche totale.
     * 
     * @return <ul>
     *         <li>true si une solution est trouvee,</li>
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

    /**
     * Parse les arguments de la ligne de commande.
     * 
     * @param args
     *            Un tableau des arguments de la ligne de commande
     *            sous forme de chaines de caracteres
     * 
     * @return La liste des arguments passes en ligne de commande
     * 
     * @throws Exception
     *             Si un argument est manquant ou invalide
     */
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
			    + " les arguments doivent etre des entiers.");
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

    /**
     * Demande a l'utilisateur de saisir les parametres du programme.
     * 
     * @return La liste des parametres saisis par l'utilisateur
     */
    private static ArrayList<Integer> askArguments() {
	ArrayList<Integer> argList = new ArrayList<Integer>();
	Integer nQueens = 0, tabuListSize = 0, nRuns = 0;
	String saisie = null;

	Scanner scanner = new Scanner(System.in);

	// Nombre de reines
	// Superieur a 3 (pas de solution sinon)
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

	// Taille de la liste tabu
	// Positive non nulle
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

	// Nombre de runs
	// Positif non nul
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
	    
	    try {
		ChessQueensV2 model = new ChessQueensV2(argList.get(0), argList.get(1));
		model.tabuSearch(argList.get(2));
		// boolean res2 = model.completeSearch();
	    }
	    
	    catch(IOException e) {
		System.err.println(e.getMessage());
	    }
	}
    }

}
