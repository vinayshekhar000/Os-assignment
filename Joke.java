import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
public class Joke {
	public static void main(String args[]) throws FileNotFoundException{
		Queue<String> queue=new LinkedList<String>();
		queue.add("Process");
		queue.add("Job");
		queue.add("Sujay");
		queue.add("Job");
		queue.remove("Job");
		Iterator<String > iter=queue.iterator();
		while(iter.hasNext()){
			String it=iter.next();
			System.out.println(it);
		}
		
		//String res=queue.remove();
		//System.out.println(res);
		/*Scanner in=new Scanner(new File("C:\\Users\\Kumar BN\\Desktop\\Processor\\InputProcessor.txt"));
		String x=in.nextLine();
		//String y=in.nextLine();
		System.out.println(x);
		in.close();*/
		/*int ch=1;
		boolean flag=true;
		switch(ch){
			case 1:
				
		}*/
	}
}
