public class Main2 {
	public static void main(String[] agrs) {
		char[] sigmabool = new char[2];
		sigmabool[0] = '0';
		sigmabool[1] = '1';
		String muster = "001001";
		
		System.out.print("Generating String ... ");
		String s = DFSM.genString(1000000, sigmabool);
		System.out.println("done.");
		
		dea(s);
		bf(s,muster);
	}
	
	public static void bf(String s, String muster){
		System.out.println("\nStarting bfsearch...");
		double start = (double) System.currentTimeMillis();
		int a = DFSM.bfsearch(s, muster);
		double end = (double) System.currentTimeMillis();
		System.out.println("bfsearch found the muster: "+a+" times");
		System.out.println("bfsearch time: "+(end-start)/1000+" sek");
	}
	
	public static void dea(String s){
		System.out.println("\nStarting dea_search...");
		double start = (double) System.currentTimeMillis();
		int b = DFSM.dea_search001001(s);
		double end = (double) System.currentTimeMillis();
		System.out.println("dea_search found the muster: "+b+" times");
		System.out.println("dea_search time: "+(end-start)/1000 + " sek");
	}
}
