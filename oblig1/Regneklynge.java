import java.util.ArrayList;

public class Regneklynge{
	private ArrayList<Rack> rackene;
	private int noderIRack;

/**
* Konstruktor:
* 	- Init ArrayList<Rack> rackene
* 	- lagrer @param noderIRack i objektet
* 	- Kaller metoden nyttRack()
*	
* @param noderIRack Maks antall noder som kan lagres i et rack
*/
	public Regneklynge(int noderIRack){
		rackene = new ArrayList<Rack>();
		this.noderIRack = noderIRack;
		nyttRack();		
	}

/**
* Metoden far inn et objekt av klassen Node. Dette skal settes inn paa en ledig plass i et objekt
* av klassen Rack. Dersom det ikke er ledig plass i det nyeste racket, opprettes et nytt rack,
* som noden legges inn i. 
*
* @param node Et objekt av klassen Node, som skal settes inn i ArrayList<Rack> rackene.
*/
	public void settInnNode(Node node){
		//Oppretter nytt rack dersom det ikke er plass i det gamle

		if (rackene.get(rackene.size()-1).erFullt()){
			nyttRack();
		}

		rackene.get(rackene.size()-1).leggTilNode(node);

	}

/**
* Regner ut regnekapasiteten til hele regneklyngen (FLOPS), og returnerer det
* som variabelet utregnetFlops.
*
* @return utregnetFlops	Regnekapasiteten til hele regneklyngen lagt sammen.
*/
	public double flops(){
		double utregnetFlops = 0.0;

		for (Rack itRack : rackene){ //itRack -> Iterer Rack
			for (Node itNode : itRack.hentNoder()){	//itNode -> Iterer Node
				if (itNode != null){
					for (Prosessor itPros : itNode.hentProsessorer()){ //itPros -> Iterer Prosessor
						
						utregnetFlops += (double)(itPros.hentKjerner() * itPros.hentKlok() * 8);
					
					}
				}
			}
		}
		return utregnetFlops;
	}

/**
*	Sjekker hvor mange noder som har minne, minst det samme som et gitt variabel (paakrevd minne)
*
*	@param 	paakrevdMinne	variabelet vi sjekker opp mot
*	@return	antNoder		antallet noder som har mer minne enn paakrevd
*/
	public int noderMedNokMinne(int paakrevdMinne){
		int antNoder = 0;

		for (Rack itRack : rackene){ //itRack -> Iterer Rack
			for (Node itNode : itRack.hentNoder()){	//itNode -> Iterer Node
				if (itNode != null && itNode.hentMinne() >= paakrevdMinne){
					antNoder++;
				}
			}
		}
		return antNoder;
	}

/**
* Gir antallet objekter av klassen Rack som er lagret i ArrayList<Rack> rackene.
*
* @return antall rack lagret i ArrayList<Rack> rackene
*/
	public int hentRack(){
		return rackene.size();
	}

/**
* Lager et nytt objekt av klassen Rack, og setter det inn i ArrayList<Rack> rackene.
*
*/
	private void nyttRack(){
		rackene.add(new Rack(noderIRack));
	}
}