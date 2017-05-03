package orbits;

//import Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class OrbitIdentification {

	private static List<OrbitRepresentative> orbits;
	public static List<Integer> graphletsPerSize;
	public static List<Integer> totalPerSize;
	private static Map<Set<Edge>,Integer> newOrbitNumbers;

	/**
	 * Reads all orbits in from file and stores them by number for
	 * quick reference.
	 */
	public static void readGraphlets(String filename, int maxOrder) {
		orbits = new ArrayList<OrbitRepresentative>();
		graphletsPerSize = new ArrayList<Integer>();
		totalPerSize = new ArrayList<Integer>();
//		orbitNumbers = new HashMap<OrbitRepresentative, Integer>();
		newOrbitNumbers=new HashMap<Set<Edge>,Integer>();
		File file = new File(filename);// this file contains the graphlets
											// in order
		try {
			Scanner scanner = new Scanner(file);
			int counter = 0;
			int size = 2;
			int orbitNumber = 0;
			boolean first = true;
			while (scanner.hasNextLine()&&size<=maxOrder) {
				String s = scanner.nextLine();
				Set<Edge> set = new HashSet<Edge>();
				int max = 0;
				for (int i = 0; i < s.length() - 2; i += 4) {
					Edge e = new Edge(s.charAt(i) - '0', s.charAt(i + 2) - '0');
					set.add(e);
					if (s.charAt(i) - '0' > max) {
						max = s.charAt(i) - '0';
					}
					if (s.charAt(i + 2) - '0' > max) {
						max = s.charAt(i + 2) - '0';
					}
				}
				if (max + 1 > size) {
					size = max + 1;
					graphletsPerSize.add(counter);
					if(first){
						first = false;
						totalPerSize.add(counter);
						
					}
					counter = 0;
				}
				counter++;
				OrbitRepresentative og = new OrbitRepresentative(set, max + 1,orbitNumber);
				orbits.add(og);
//				orbitNumbers.put(og, orbitNumber);
//				System.out.println(calculateSymmetry(og));
				for(Set<Edge> e:calculateSymmetry(og)){
					newOrbitNumbers.put(e, orbitNumber);
				}
				orbitNumber++;
			}
			graphletsPerSize.add(counter);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Ongeldige bestandsnaam");
		}
	}

	/**
	 * Returns the number of a given orbit.
	 * 
	 * @param g
	 *            OrbitGraphlet representing the orbit.
	 * @return The number of the orbit.
	 */
	public static int identifyOrbit(OrbitRepresentative g) {
//		if (!ready)
//			readGraphlets();
//		if (orbits == null) {
//			readGraphlets();
//		}
		Integer i = newOrbitNumbers.get(g.getEdges());
		if (i == null)
			return -1;
		return i;
	}

	/**
	 * Returns the number of orbits of the given order
	 * 
	 * @param order
	 *            The order of which the number of graphlets is asked
	 * @return The number of orbits of the given order
	 */
	public static int getNOrbitsForOrder(int order) {
//		if (!ready)
//			readGraphlets();
		return graphletsPerSize.get(order - 2);
	}

	/**
	 * Returns the total number of orbits of the given order or lower
	 * 
	 * @param order
	 *            The maximal order of the counted orbits
	 * @return The number of orbits in graphlets of no higher than the given
	 *         order
	 */
	public static int getNOrbitsTotal(int order) {
//		if (!ready)
//			readGraphlets();
		int result = 0;
		for (int i = 1; i < order; i++) {
			result += graphletsPerSize.get(i - 1);
		}
		return result;
	}
	
	public static OrbitRepresentative getOrbit(int n){
		return orbits.get(n);
	}
	
	private static Set<Set<Edge>> calculateSymmetry(OrbitRepresentative or) {
		Set<Set<Edge>>permutedEdges = new HashSet<Set<Edge>>();
		List<List<Integer>> permutations = OrbitRepresentative.generatePermutations(or.order - 1);
		for (List<Integer> l : permutations) {
			for (int j = 0; j < l.size(); j++) {
				l.set(j, l.get(j) + 1);
			}
			l.add(0, 0);
		}
//		List<SortedSet<Integer>> orbitsTemporary = new ArrayList<SortedSet<Integer>>();
		for (List<Integer> permutation : permutations) {
			Set<Edge> s = OrbitRepresentative.permute(or.edges, permutation);
//			if (or.edges.equals(s)) {
//				for (int i = 0; i < permutation.size(); i++) {
//					SortedSet<Integer> permuted = new TreeSet<Integer>();
//					permuted.add(i);
//					permuted.add(permutation.get(i));
//					orbitsTemporary.add(permuted);
//				}
//			}
			permutedEdges.add(s);
		}
		return permutedEdges;
	}

	
}