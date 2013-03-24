package pouet;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

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
	    SolutionGenerator generator = new Generator2();
	    Neighbourhood neighbourhood = new Neighbourhood2();
	    
	    ChessQueensTS tabuSearch = new ChessQueensTS(
		    argList.get(0), argList.get(1));

	    tabuSearch.search(neighbourhood, generator, argList.get(2));
	}
    }

}
