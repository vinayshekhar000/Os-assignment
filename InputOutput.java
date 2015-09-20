import java.util.LinkedList;
import java.util.Queue;


public class InputOutput {
	int number;//This indicates which IO device is in action
	int timeRem;//Time for the Io to finish
	int timeRequested;
	public int startTime;
	public int timeUsed;
	Queue<ProcessTable> inputqueue;
	ProcessTable usingProcess;
	InputOutput(int n,int max){
		this.number=n;
		timeRem=max;
		inputqueue=new LinkedList<ProcessTable>();
	}
	InputOutput(int n){
		this.number=n;
		this.timeRem=0;
		inputqueue=new LinkedList<ProcessTable>();
	}
	public InputOutput() {
		// TODO Auto-generated constructor stub
	}
	public void decrement(int i){
		timeRem=timeRem-i;
	}
	
}
