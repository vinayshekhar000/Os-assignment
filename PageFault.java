import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class PageFault {
	public static void main(String args[]){
		Random r = new Random();
		final int size=8;
		int Low = 0;
		int High = 10;
		int i;
		Queue<Integer> queue=new LinkedList<Integer>();
		for(int j=0;j<5;j++){
			int n=20;
			int a[]=new int[n];
			for(i=0;i<20;i++){
				a[i]=r.nextInt(High-Low) + Low;
			}
			for(i=0;i<20;i++){}
				//System.out.print(a[i]);
			//System.out.println("*****************");
			int pageFault=0;
			for(i=0;i<20;i++){
				if(queue.size()<size){
					if(queue.contains(a[i])){
						queue.remove(a[i]);
						queue.add(a[i]);
					}
					else{
						queue.add(a[i]);
						pageFault++;
					}
				}
				else{
					if(!queue.contains(a[i])){
						pageFault++;
						queue.remove();
						queue.add(a[i]);
					}
					else{
						queue.remove(a[i]);
						queue.add(a[i]);
					}
				}
				//printTheQueue(queue);
			}
			System.out.println(pageFault);
		}
	}

	private static void printTheQueue(Queue<Integer> queue) {
		// TODO Auto-generated method stub
		Iterator<Integer> iter=	queue.iterator();
		while(iter.hasNext()){
			Integer a=iter.next();
			System.out.print(a.intValue()+"    ");
		}
		System.out.println();
	}
}
