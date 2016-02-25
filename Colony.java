import java.util.Random;

public class Colony implements Runnable {
	static Ant [] ants = new Ant[2];
	Enemy enemy = new Enemy();
	
	public Colony(){
		initialiseAnts();
		ants[0].status = 1;
		ants[1].status = 0;
	}
	
	public static void initialiseAnts(){
		for(int i = 0; i < ants.length; i++){
			//Random rand1 = new Random();
		//	Random rand2 = new Random();
			//int xPos = rand1.nextInt(690) + 1;
			//int yPos = rand2.nextInt(590) + 1;
			ants[i] = new Ant(650,550,0);
		}
	}
	
	public void follow(Ant a){
		System.out.println("Status: " + a.status);
		if(a.status == 1){
			ants[1].status = 1;
			ants[1].findPathToAnt(a);
			ants[1].antBorder(a);
		}
	}
	
	/*public void updateColony(){
		double shortestDistance = 5000;
		double shortDistance = 5000;
		int indexOfShort = -1;
		for(int i = 0; i < ants.length; i++){
			for(int j = 0; j < ants.length; j++){
				System.out.println(ants[i].ant.x + " " + ants[i].ant.y + "   " + ants[j].ant.x + " " + ants[j].ant.y);
				shortDistance = Math.sqrt(Math.pow((ants[j].ant.x - ants[i].ant.x), 2) + 
						Math.pow((ants[j].ant.y - ants[i].ant.y), 2));
				System.out.println("short Distance: " + shortDistance);
				if( shortDistance != 0 && shortDistance <= shortestDistance){
					shortestDistance = shortDistance;
					indexOfShort = j;
					System.out.println("indexOfShort: " + indexOfShort);
					System.out.println("shortestDistance: " + shortestDistance);
				}
				
			}
			if((ants[i].status != 0 && ants[indexOfShort].status == 0) && shortestDistance < 300){
					ants[indexOfShort].status = ants[i].status;
					ants[indexOfShort].findPathToAnt(ants[i]);
				}
		}
	}*/
	
	public void run() {
		try{
			while(true){
					
				follow(ants[0]);
				
				System.out.println("ant: " + (0 + 1) + " status: " + ants[0].status);
				System.out.println("ant: " + (1 + 1) + " status: " + ants[1].status);
				//System.out.println("ant: " + (2 + 1) + " status: " + ants[2].status);
				Thread.sleep(4);
			}
		} catch(Exception e){ System.err.println(e.getMessage());}
	}
}
