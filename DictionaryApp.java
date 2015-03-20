package treemap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * I added a method at the end of DictionaryApp to save statistics to a csv file
 * so that I open it in Excel. Nothing fancy but it works really well.
 */
/**
 * Program to read in strings from a file and insert them into a dictionary.
 * 
 * @author Donald Chinn, Monika Sobolewska
 * @version February 10, 2015
 * 
 *          Flags:
 *          -b binary search tree
 *          -v AVL tree
 *          -s Splay tree
 *          -r red/blacktree
 *          -j Java built-in class TreeMap
 *          -h Heap Priority Queue
 *          -a Hash Set
 *          -x do nothing
 */
public class DictionaryApp {

	// static variables used to identify the
	// data structure/algorithm to use
	// (based on the command line argument)
	private static final int NOALG = 0;
	private static final int useBST = 1;
	private static final int useAVL = 2;
	private static final int useSplay = 3;
	private static final int useRedBlack = 4;
	private static final int useJavaTreeMap = 5;
	private static final int useNothing = 7;
	private static final int useHPQ = 8;
	private static final int useHS = 9;

	/**
	 * Return the next word in a file.
	 * 
	 * @param file
	 *            the file to read from
	 * @return the next word in the file of length > 3, or null if there is no
	 *         next word A word is defined here as a consecutive sequence of
	 *         alphanumeric characters.
	 */
	private static String getWord(Scanner file) {
		String word = null;
		while (file.hasNext()) {
			word = file.next();
			word = word.replaceAll("\\W", "").toLowerCase();
			if (word.length() > 3)
				break;
		}
		return word;

	}

	/**
	 * The driver method for the word counting application.
	 */
	public static void main(String[] args) {
		boolean error = false;

		// timer variables
		long totalTime = 0;
		long printTime = 0;
		long insertionTime = 0;
		long startTime = 0;
		long finishTime = 0;
		int numInsertions = 0;

		int whichAlgorithm = NOALG;

		Scanner infile = null;

		MyTreeMap<String, Integer> bst = new BinarySearchTree<String, Integer>();
		MyTreeMap<String, Integer> avlTree = new AvlTree<String, Integer>();
		MyTreeMap<String, Integer> splayTree = new SplayTree<String, Integer>();
		MyTreeMap<String, Integer> rbTree = new RedBlackTree<String, Integer>();
		MyTreeMap<String, Integer> hpq = new HeapPriorityQueue<String, Integer>();
		MyTreeMap<String, Integer> hs = new HashSet<String, Integer>();
		TreeMap<String, Integer> javaTreeMap = new TreeMap<String, Integer>();

		// Handle command line arguments.
		// Usage: -[bvsrjx] input_filename
		// Options:
		// -b use a standard binary search tree
		// -v use the recursive implementation of an AVL tree
		// -s splay tree
		// -r red black tree
		// -j Java built-in class TreeMap

		// -x no data structure (just read in the file)

		if ((args.length < 2) || (args.length > 2)) {
			System.out.println("Argument usage: -[bvsrjx] infile");
			error = true;
		} else {
			// figure out which option was chosen
			if (args[0].charAt(0) == '-') {
				switch (args[0].charAt(1)) {
				case 'b':
					whichAlgorithm = useBST;
					break;

				case 'v':
					whichAlgorithm = useAVL;
					break;

				case 's':
					whichAlgorithm = useSplay;
					break;

				case 'r':
					whichAlgorithm = useRedBlack;
					break;

				case 'h':
					whichAlgorithm = useHPQ;
					break;

				case 'a':
					whichAlgorithm = useHS;
					break;

				case 'j':
					whichAlgorithm = useJavaTreeMap;
					break;

				case 'x':
					whichAlgorithm = useNothing;
					break;

				default:
					System.out.print("Usage: ");
					System.out.println("-" + args[0].charAt(1)
							+ " is not a valid option.");
					error = true;
					break;
				}

				// Get the input filename
				try {
					infile = new Scanner(Paths.get(args[1]));
				} catch (IOException ioexception) {
					System.out
							.println("Error: Could not open " + args[1] + ".");
					error = true;
				}
			} else {
				System.out.println("Argument usage: -[bvrjx] filename");
				error = true;
			}
		}

		if (!error) {

			String currentWord;
			Integer numTimes; // the number of times a key has been encountered
								// so far

			// start the timer
			Date startDate = new Date();
			startTime = startDate.getTime();

			while ((currentWord = DictionaryApp.getWord(infile)) != null) {
				switch (whichAlgorithm) {
				case useBST:
					numTimes = bst.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					bst.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useAVL:
					numTimes = avlTree.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					avlTree.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useSplay:
					numTimes = splayTree.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					splayTree.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useRedBlack:
					numTimes = rbTree.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					rbTree.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useHPQ:
					numTimes = hpq.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					hpq.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useHS:
					numTimes = hs.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					hs.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useJavaTreeMap:
					numTimes = javaTreeMap.get(currentWord);
					if (numTimes == null)
						numTimes = 0;
					numTimes++;

					javaTreeMap.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useNothing:
					// do nothing
					numInsertions++;
					break;
				}
			}

			// stop the insertion timer
			Date finishDate = new Date();
			finishTime = finishDate.getTime();

			insertionTime += (finishTime - startTime);

			startTime = finishDate.getTime();

			System.out
					.println("** Results for " + args[0] + " option on file ");
			System.out.println("    " + args[1]);
			System.out.println("Time to do insertions: ");
			System.out.println(insertionTime + " ms.");
			System.out.println("Number of insertions: " + numInsertions);

			switch (whichAlgorithm) {
			case useBST:
				System.out.println(bst);
				System.out.println();
				break;
			case useAVL:
				System.out.println(avlTree);
				System.out.println();
				break;
			case useSplay:
				System.out.println(splayTree);
				System.out.println();
				break;
			case useRedBlack:
				System.out.println(rbTree);
				System.out.println();
				break;
			case useHPQ:
				System.out.println(hpq);
				System.out.println();
				break;

			case useHS:
				System.out.println(hs);
				System.out.println();
				break;
			case useJavaTreeMap:
				System.out.println(javaTreeMap);
				break;
			case useNothing:
				// do nothing
				break;
			}

			// stop the timer
			Date finishWrite = new Date();
			finishTime = finishWrite.getTime();
			printTime += (finishTime - startTime);

			totalTime += insertionTime + printTime;

			switch (whichAlgorithm) {
			case useBST:
				System.out.println("Words with max number: ");
				bst.PrintMostFrequent(10);
				System.out.println();
				break;
			case useAVL:
				System.out.println("Words with max number: ");
				avlTree.PrintMostFrequent(5);
				System.out.println();
				break;
			case useSplay:
				System.out.println("Words with max number: ");
				splayTree.PrintMostFrequent(5);
				System.out.println();
				break;
			case useRedBlack:
				System.out.println("Words with max number: ");
				rbTree.PrintMostFrequent(5);
				System.out.println();
				break;
			case useHPQ:
				System.out.println("Words with max number: ");
				hpq.PrintMostFrequent(5);
				System.out.println();
				break;
			case useHS:
				System.out.println("Words with max number: ");
				hs.PrintMostFrequent(5);
				System.out.println();
				break;
			case useJavaTreeMap:
				//do nothing
				break;
			case useNothing:
				// do nothing
				break;
			}

			System.out.println("Time to do insertions and print the tree: ");
			System.out.println(totalTime + " ms.");

			//Writes the run statistics to a file.
			writeStats(whichAlgorithm, args[1], insertionTime, printTime, numInsertions);
		}

		infile.close();

		System.out.println("DictionaryApp is done. **");
	}

	/**
	 * Writes time and insertion data to a log file.
	 * 
	 * @param whichAlgorithm
	 *            is the data structure used.
	 * @param fileName
	 * 			  is the name of the file that was processed.
	 * @param insertionTime
	 *            is the time for insertion.
	 * @param printTime
	 * 			  is the time for traversal.
	 * @param numInsertions
	 *            is the total number of insertions.
	 */
	public static void writeStats(int whichAlgorithm, String fileName, long insertionTime,
			long printTime, int numInsertions) {
		String s = "NOTHING";
		switch (whichAlgorithm) {
		case useBST:
			s = "BST";
			break;
		case useAVL:
			s = "AVL";
			break;
		case useSplay:
			s = "SPLAY";
			break;
		case useRedBlack:
			s = "RB";
			break;
		case useJavaTreeMap:
			s = "JAVA";
			break;
		case useHPQ:
			s = "HPQ";
			break;
		case useHS:
			s = "HS";
			break;
		case useNothing:
			// do nothing
			break;
		}
		try {
			File file = new File("./myfiles/" + LocalDate.now() + ".csv");

			if (!(file.exists())) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
				BufferedWriter outputFile = new BufferedWriter(fw);

				outputFile.write("Type,File,Insertions,Insertion Time,Print Time");
				outputFile.newLine();
				outputFile.close();
			}

			FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
			BufferedWriter outputFile = new BufferedWriter(fw);

			outputFile.append(s + "," + fileName + ", " + numInsertions + "," + insertionTime
					+ "," + printTime);
			outputFile.newLine();
			outputFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
