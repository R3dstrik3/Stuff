import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DFSM {
	protected class DeltaDomain {
		public int q;
		public char a;
		private int hash;

		DeltaDomain(int q, char a) {
			this.q = q;
			this.a = a;
			hash = Character.getNumericValue(a) * numStates + q;
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof DeltaDomain) {
				DeltaDomain rhs = (DeltaDomain) o;
				return (this.q == rhs.q) && (this.a == rhs.a);
			} else {
				return false;
			}
		}

		public String toString() {
			String s = "q" + q + ": -" + "(" + a + ")" + "->";
			return s;
		}
	}

	protected class DFSMException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DFSMException(String str) {
			super(str);
		}
	}

	// Zustaende werden von 0 bis numStates-1 durchnummeriert
	protected int numStates;

	// Das Alphabet Sigma
	protected HashSet<Character> alphabet;

	// Die Menge der akzeptierenden Zustaende
	protected HashSet<Integer> acceptingStates;

	// Die Transitionsfunktion delta
	protected HashMap<DeltaDomain, Integer> transitionsFunction;

	// Der Startzustand
	protected int startingState;

	// Initialisiert einen DEA mit 'numStates' Zustaenden, dessen
	// Eingabealphabet die Zeichen in 'aplhabet' enthaelt
	public DFSM(int numStates, char alphabet[]) {
		this.alphabet = new HashSet<Character>();
		acceptingStates = new HashSet<Integer>();
		transitionsFunction = new HashMap<DeltaDomain, Integer>();

		this.numStates = numStates;
		for (char c : alphabet) {
			this.alphabet.add(c);
		}
		startingState = -1;
	}

	public void setStartingState(int state) throws DFSMException {
		checkState(state);
		startingState = state;
	}

	// Fuegt 'state' zur Menge der akzeptierenden Zustaende hinzu
	public void setAcepting(int state) throws DFSMException {
		checkState(state);
		acceptingStates.add(state);
	}

	// Entfernt 'state' aus der Menge der akzeptierenden Zustaende
	public void setRejecting(int state) throws DFSMException {
		checkState(state);
		acceptingStates.remove(state);
	}

	// Erweitert die Transitionsfunktion 'from' und 'to' sind Zustaende, 'width'
	// ist die Beschriftung der Kante
	public void addTransition(int from, char width, int to)
			throws DFSMException {
		if (!alphabet.contains(width)) {
			throw new DFSMException("Letter " + width
					+ " ist not in the alphabet " + alphabet);
		}

		checkState(from);
		checkState(to);

		transitionsFunction.put(new DeltaDomain(from, width), to);
	}

	// Wendet den DEA auf die Eingabe 's' an und gibt true zurueck, falls 's'
	// akzeptiert wurde
	public boolean run(String s) throws DFSMException {
		if (startingState == -1) {
			throw new DFSMException("I don't know where i start.");
		}

		int state = startingState;

		// s gueltig
		for (char letter : s.toCharArray()) {
			if (!alphabet.contains(letter)) {
				throw new DFSMException(
						"Letters used, which are not in the alphabet");
			}

			for (DeltaDomain dd : transitionsFunction.keySet()) {
				if (dd.q == state && dd.a == letter) {
					state = transitionsFunction.get(dd);
					// System.out.println(dd+" "+state);
					break;
				}
			}
		}

		if (acceptingStates.contains(state)) {
			return true;
		}

		return false;
	}

	public void checkState(int i) throws DFSMException {
		if (i < 0 || i > numStates-1) {
			throw new DFSMException(i + " is not a known state!");
		}
	}

	public void runWithListener(String input, Listener listener) {
		if (startingState == -1) {
			throw new DFSMException("I don't know where i start.");
		}

		int state = startingState;
		int pos = 0;
		Set<DeltaDomain> transition = transitionsFunction.keySet();
		ArrayList<Knoten> knoten = new ArrayList<>();
		for (int i = 0; i < transition.size() / alphabet.size(); i++) {
			knoten.add(new Knoten());
		}
		for (DeltaDomain d : transition) {
			knoten.get(d.q).addDomain(
					new DeltaDomain(transitionsFunction.get(d), d.a));
		}

		// s gueltig
		for (char letter : input.toCharArray()) {
			if (!alphabet.contains(letter)) {
				throw new DFSMException(
						"Letters used, which are not in the alphabet");
			}

			state = knoten.get(state).nextState(letter);

			if (acceptingStates.contains(state)) {
				listener.accepted(pos);
			}
			pos++;
		}
	}

	public class Knoten {
		ArrayList<DeltaDomain> dd;

		public Knoten() {
			dd = new ArrayList<>();
		}

		public void addDomain(DeltaDomain d) {
			dd.add(d);
		}

		public int nextState(char c) {
			for (DeltaDomain d : dd) {
				if (d.a == c) {
					return d.q;
				}
			}
			return -1;
		}
	}

	public static String genString(int size, char[] alphabet) {
		StringBuilder word = new StringBuilder(size);
		int num_letters = alphabet.length;
		for (int i = 0; i < size; i++) {
			int random = (int) (Math.random() * num_letters);
			word.append(alphabet[random]);
		}
		return word.toString();
	}

	public static int bfsearch(String s, String muster) {
		int musterl = muster.length();
		int counter = 0;
		for (int i = 0; i <= s.length() - musterl; i++) {
			if (s.substring(i, i + musterl).equals(muster)) {
				counter++;
			}
		}
		return counter;
	}

	public static int dea_search001001(String input) {
		Listener listener = new Listener();
		char[] alphabet = new char[2];
		alphabet[0] = '0';
		alphabet[1] = '1';
		DFSM machine = new DFSM(7, alphabet);

		machine.setStartingState(0);

		machine.addTransition(0, '0', 1);
		machine.addTransition(0, '1', 0);

		machine.addTransition(1, '0', 2);
		machine.addTransition(1, '1', 0);

		machine.addTransition(2, '0', 2);
		machine.addTransition(2, '1', 3);

		machine.addTransition(3, '0', 4);
		machine.addTransition(3, '1', 0);

		machine.addTransition(4, '0', 5);
		machine.addTransition(4, '1', 0);

		machine.addTransition(5, '0', 2);
		machine.addTransition(5, '1', 6);

		machine.addTransition(6, '0', 4);
		machine.addTransition(6, '1', 0);

		machine.setAcepting(6);

		machine.runWithListener(input, listener);

		return listener.times;
	}
}
