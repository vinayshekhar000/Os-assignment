import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Memory {
	int totalMem;
	int freeMem;
	Queue<ProcessTable> memQueue;
	Memory(int n){
		totalMem=n;
		freeMem=n;
		memQueue=new LinkedList<>();
	}
	public void addToMemQueue(ProcessTable process){
		memQueue.add(process);
	}
	public ProcessTable chooseNextProcessInMemQueue(){
		Iterator<ProcessTable> iter=memQueue.iterator();	
		while(iter.hasNext()){
			ProcessTable process=iter.next();
			if(process.memory<freeMem){
				freeMem=freeMem-process.memory;
				memQueue.remove(process);
				return process;
			}
		}
		return null;
	}
}
