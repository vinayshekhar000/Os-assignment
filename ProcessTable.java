import java.util.Comparator;


public class ProcessTable{
	int PID;
	String name;//Name of the process
	int startTime;//The time when the processor started processing  
	int timeReq;//The Time required by the process 
	int timeUsed;//The time it has spent on the processor
	int timeRem;//Time which is still to be spent on the processor
	int status;//1 represents that the process is running and 0 represents that the process is in ready state and 2 for wait
	int memory;//The total amount of memory required by the process
	int currentInstructiontime;
	InputOutput ioRequired;
	ProcessTable(int PID,String n,int st,int timeReq,int timeUsed,int timeRem){
		this.PID=PID;
		name=n;
		startTime=st;
		this.timeReq=timeReq;
		this.timeUsed=timeUsed;
		this.timeRem=timeRem;
		status=0;
		ioRequired=new InputOutput();
		currentInstructiontime=0;
	}
	public void display(){
		System.out.println("ID:"+PID+" Name:"+name+" StartTime:"+startTime+" Time Required"+timeReq+" TimeUSed"+timeUsed+" Time Rem:"+timeRem+" Status"+status);
	}


}
