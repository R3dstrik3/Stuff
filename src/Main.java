
public class Main {
	public static void main(String [] agrs){
		//beispiel aufgabe 1 a)
		char[] alphabet = new char[2];
		alphabet[0] = '0';
		alphabet[1] = '1';
		
		DFSM machine = new DFSM(5, alphabet);
		machine.setStartingState(0);
		
		machine.addTransition(0, '0', 4);
		machine.addTransition(0, '1', 1);
		
		machine.addTransition(1, '0', 2);
		machine.addTransition(1, '1', 1);
		
		machine.addTransition(2, '0', 3);
		machine.addTransition(2, '1', 1);
		
		machine.addTransition(3, '0', 3);
		machine.addTransition(3, '1', 1);
		
		machine.addTransition(4, '0', 4);
		machine.addTransition(4, '1', 4);
		
		machine.setAcepting(3);
		
		//ausgaben:
		String word;
		
		word = "100000110101000";
		ausgabe(word,machine);
		
		word = "000000";
		ausgabe(word,machine);
		
		word = "10000010";
		ausgabe(word,machine);
		
		word = "10000001";
		ausgabe(word,machine);
		
		word = "";
		ausgabe(word, machine);
	}
	
	public static void ausgabe(String s, DFSM machine){
		System.out.println("machine contains '"+s+"'?: "+machine.run(s));
	}
}
