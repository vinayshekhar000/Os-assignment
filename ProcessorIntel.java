import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;


public class ProcessorIntel{
	static int numberOfProcess;
	public static void main(String args[]) throws FileNotFoundException{
		try {
			/*System.setIn(new FileInputStream(
					"C:\\Users\\Kumar BN\\Desktop\\Processor\\InputProcessor.txt"));*/
			System.setOut(new PrintStream(
					"C:\\Users\\Kumar BN\\Desktop\\Processor\\out3.txt"));
		} catch (Exception e) {
			System.out.print("CATCH BLOCK");
		}
		/*First Read from  some configuration file which gives information about the processor*/
		Scanner in=new Scanner(new File("C:\\Users\\Kumar BN\\Desktop\\Processor\\sample1\\simulator_config"));
		
		HashMap<Integer,LinkedList<ProcessTable>> mapProcess=new HashMap<>();
		HashMap<Integer,String> timeList=new HashMap<Integer,String>();
		HashMap<String,LinkedList<ProcessDetail>> detailProcess=new HashMap<>();
		LinkedList<ProcessTable> seenProcess=new LinkedList<>();
		int id=0;
		int max=in.nextInt();
		int timeGran=in.nextInt();
		int numberOfIO=in.nextInt();
		int memory=in.nextInt();
		InputOutput[] arrayInput=new InputOutput[numberOfIO+1];
		for(int i=0;i<arrayInput.length;i++){
			arrayInput[i]=new InputOutput(i);
		}
		System.out.println("Time Gran is "+timeGran);
		int totalTimeInFile=0;
		int start=0;
		int nofinishedProcess=0;
		Processor processor=new Processor(max,timeGran);
		System.out.println("Done with config");
		in=new Scanner(new File("C:\\Users\\Kumar BN\\Desktop\\Processor\\sample1\\test1.job"));
		while(in.hasNext()){
			String action=in.next();
			if(action.equalsIgnoreCase("SUBMIT_JOB")){
				String name=in.next();
				Scanner tempFile=new Scanner(new File("C:\\Users\\Kumar BN\\Desktop\\Processor\\sample1\\"+name+".model"));
				int totalTime=0;
				int startTime=in.nextInt();
				while(tempFile.hasNext()){
					String line=tempFile.nextLine();
					if(line.contains("cpu")){
						String ex[]=line.split(" ");
						totalTime+=Integer.parseInt(ex[1]);
					}
				}
				totalTimeInFile=startTime+totalTime;
				LinkedList<ProcessTable> list=mapProcess.get(startTime);
				if(list==null){
					LinkedList<ProcessTable> temp=new LinkedList<>();
					temp.add(new ProcessTable(id,name,startTime,totalTime,0,totalTime));
					id=id+1;
					mapProcess.put(startTime,temp);
				}
				else{
					LinkedList<ProcessTable> temp=mapProcess.get(startTime);
					temp.add(new ProcessTable(id,name,startTime,totalTime,0,totalTime));
					id=id+1;
					mapProcess.put(startTime,temp);
				}
			}
			else if(action.equalsIgnoreCase("PRINT")){
				String unwanted=in.next();//Contains 'Process table'
				int time=in.nextInt();
				totalTimeInFile=time;
				timeList.put(time,"Print");
			}
		}
		System.out.println("Printing mapProcess="+mapProcess);
		System.out.println(timeList);
		/*You have finished reading from Job file*/
		Set<Integer> iter1=mapProcess.keySet();
		for(int item :iter1){
			LinkedList<ProcessTable> list=mapProcess.get(item);
			Iterator<ProcessTable> iter2=list.iterator();
			while(iter2.hasNext()){
				ProcessTable process=iter2.next();
				Scanner inner=new Scanner(new File("C:\\Users\\Kumar BN\\Desktop\\Processor\\sample1\\"+process.name+".model"));
				while(inner.hasNext()){
					String line=inner.nextLine();
					//System.out.println(line);
					ProcessDetail det;
					if(line.contains("cpu")){
						String[] ex = line.split(" ");
						det=new ProcessDetail(1,Integer.parseInt(ex[3]),Integer.parseInt(ex[1]));
					}
					else{
						String[] ex = line.split(" ");
						det=new ProcessDetail(0,Integer.parseInt(ex[1]),Integer.parseInt(ex[2]));
					}
					LinkedList<ProcessDetail> list1=detailProcess.get(process.name);
					if(list1==null){
						list1=new LinkedList<>();
						list1.add(det);
						detailProcess.put(process.name,list1);
					}
					else{
						list1.add(det);
						detailProcess.put(process.name,list1);
					}
				}
			}
		}
		//printProcessDetails(detailProcess,mapProcess);
		System.out.println(detailProcess);
		Memory systemMemory=new Memory(memory);
		int i=1;
		int totalNumberOfProcess=detailProcess.size();
		while((!(processor.queue.isEmpty())||i<totalTimeInFile|| processor.state==1 || getCount(arrayInput)>0)&& processor.numberOfProcessFinished<totalNumberOfProcess){
			//processor.printProcessTable(i);
			if(i%timeGran==0){
				processor.chooseNextProcess(i,systemMemory,arrayInput,detailProcess,numberOfProcess);
				try{
					if(processor.state==1){
					//System.out.println("Running process is "+processor.runningProcess.name);
					}
				}
				catch(Exception e){}
			}
				
				//System.out.println("Here");
				if(mapProcess.containsKey(i)){
					seenProcess.addAll(mapProcess.get(i));
					if(start==0)
						start=i;//start is the time when the first process comes in
					System.out.println("Got a process at "+i+" ");
					LinkedList<ProcessTable> toStartList=mapProcess.get(i);//You'll have to take the first porcess and enqueue the remaining in the queue if in case more than one process comes at a time
					Iterator<ProcessTable> listiter=toStartList.iterator();
					ProcessTable toStart=listiter.next();
					if(processor.queue.isEmpty() && processor.state==0){
						//System.out.println("Putting the process inside the core directly");
						toStart.status=1;
						processor.state=1;
						processor.runningProcess=toStart;
						ProcessDetail det=detailProcess.get(toStart.name).getFirst();//This is getting the head of the list
						if(processor.runningProcess.currentInstructiontime==0 && det.flag==1){
							processor.runningProcess.currentInstructiontime=det.timeReq;
						}
						if(det.flag==1)
							adjustSystemMemory(toStart,systemMemory,processor,det);
						else
							adjustIo(toStart,det,arrayInput,toStart,i,processor);
					}
					else{
						//System.out.println("Enquing other process");
						if(detailProcess.get(toStart.name).size()>0){
							ProcessDetail det=detailProcess.get(toStart.name).getFirst();
							adjustSystemMemory(toStart, systemMemory, processor, det);
						}
					}
					while(listiter.hasNext()){
						ProcessTable other=listiter.next();
						ProcessDetail det=detailProcess.get(other.name).getFirst();
						adjustSystemMemory(other, systemMemory, processor, det);
					}
				}
				if(timeList.containsKey(i)){
					//System.out.println("Printing table");
					processor.printProcessTable(i);
				}
			//processor.mopUp();
			//someList = mapProcess.values();
			Iterator<ProcessTable> nextInstructionCheckList=seenProcess.iterator();
			//System.out.println(seenProcess.size());
			while(nextInstructionCheckList.hasNext()){
				//System.out.println("Random");
				ProcessTable toCheckProcess=nextInstructionCheckList.next();
				System.out.println("The process to be checked is :"+toCheckProcess.name);
				//System.out.println("Running Process="+processor.runningProcess.name+"and to check is "+toCheckProcess.name);
				if(detailProcess.containsKey(toCheckProcess.name) && processor.runningProcess!=toCheckProcess ){
					if(detailProcess.get(toCheckProcess.name).size()>0){
						ProcessDetail det=detailProcess.get(toCheckProcess.name).getFirst();
						System.out.println("While sendding to adjust"+det.flag);
						if(det.flag==1)
							adjustSystemMemory(toCheckProcess,systemMemory,processor,det);
						else
							adjustIo(toCheckProcess,det,arrayInput,toCheckProcess,i,processor);
					}
				}
			}
			if(processor.state==0){
				System.out.println("Choosing next");
				processor.chooseNextProcess(i,systemMemory,arrayInput,detailProcess,processor.numberOfProcessFinished);
			}
			System.out.println("Number of process:"+numberOfProcess);
			i=i+1;
		}
		System.out.println("Time taken is"+(i-start));
	}

	private static int getCount(InputOutput[] arrayInput) {
		for (int i=0;i<arrayInput.length;i++){
			if(arrayInput[i].usingProcess!=null || arrayInput[i].inputqueue.size()>0){
				System.out.println("Returning 1");
				return 1;
			}
		}
		// TODO Auto-generated method stub
		return 0;
	}

	private static void printProcessDetails(
			HashMap<String, LinkedList<ProcessDetail>> detailProcess, HashMap<Integer, LinkedList<ProcessTable>> mapProcess) {
		// TODO Auto-generated method stub
	Set<String> keys = detailProcess.keySet();
	Iterator<String> iter=keys.iterator();
	while(iter.hasNext()){
		String temp=iter.next();
		System.out.print(temp+" ");
		LinkedList<ProcessDetail> list = detailProcess.get(temp);
		Iterator<ProcessDetail> list1=list.iterator();
		while(list1.hasNext()){
			ProcessDetail p=list1.next();
			System.out.print(p.flag+" ");
		}
		System.out.println();
	}
		
	}

	private static void adjustIo(ProcessTable toStart, ProcessDetail det,InputOutput[] arrayIo,ProcessTable process,int i,Processor processor) {
		int deviceNum=det.deviceNumber;
		if(processor.queue.contains(process)){
			processor.queue.remove(process);//Removing from ready queue
		}
		System.out.println(process.name+"Has requested for Io device Number"+deviceNum);
		if(arrayIo[deviceNum].inputqueue.isEmpty()){
			System.out.println("Empty queue");
			arrayIo[deviceNum].usingProcess=process;
			arrayIo[deviceNum].usingProcess.status=2;
			arrayIo[deviceNum].startTime=i;
			System.out.println("The time requested is "+arrayIo[deviceNum].timeRequested);
			if(arrayIo[deviceNum].timeRequested==0){
				System.out.println("making an Io call and hence blocked");
				arrayIo[deviceNum].timeRequested=det.timeReq;
			}
			if(arrayIo[deviceNum].timeRem==0)
				arrayIo[deviceNum].timeRem=det.timeReq;
			else{
				System.out.println("Time remaining for process "+arrayIo[deviceNum].usingProcess.name+" is "+arrayIo[deviceNum].timeRem);
				arrayIo[deviceNum].timeRem=arrayIo[deviceNum].timeRem-1;
			}
		}
		else{
			process.status=2;
			arrayIo[deviceNum].inputqueue.add(process);
		}
	}

	private static void adjustSystemMemory(ProcessTable process,Memory systemMemory,Processor processor,ProcessDetail det) {
		if(systemMemory.freeMem-det.memory<=0){
			process.status=2;
			processor.memoryBlockedQueue.add(process);
		}
		else{
			processor.queue.add(process);
			process.status=0;
			if(process.currentInstructiontime==0)
				process.currentInstructiontime=det.timeReq;
			systemMemory.freeMem-=det.memory;
		}
	}
}
