public class oblig1{
	public static void main(String[] args) {
		Regneklynge abel = new Regneklynge(12);

		int leggTilNoder = 650;
		for (int i = 0; i < leggTilNoder; i++){
			abel.settInnNode(new Node(64, 8, 2.6E9, 8, 2.6E9));
		}

		leggTilNoder = 16;
		for (int i = 0; i < leggTilNoder; i++){
			abel.settInnNode(new Node(1024, 8, 2.3E9, 8, 2.3E9));
		}

		System.out.println("Samlet FLOPS: " + abel.flops());
		System.out.println("Noder med minst 32GB: " + abel.noderMedNokMinne(32));
		System.out.println("Noder med minst 64GB: " + abel.noderMedNokMinne(64));
		System.out.println("Noder med minst 128GB: " + abel.noderMedNokMinne(128));
		System.out.println("Antall Rack: " + abel.hentRack());
	}
}