import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
public class Processor {
	int maxProcess;
	//PriorityQueue<ProcessTable> queue;
	Queue<ProcessTable> queue;
	Queue<ProcessTable> blockedQueue;
	ProcessTable runningProcess;
	int numberOfProcessFinished;
	int timeGran;
	int state;
	Queue<ProcessTable> memoryBlockedQueue;
	Processor(int n,int time){
		maxProcess=n;
		/*queue= new PriorityQueue<ProcessTable>(
		        new Comparator<ProcessTable>( ) {
		            // override the compare method
		        	public int compare(ProcessTable i,ProcessTable j) {
		        		  return (i.startTime+i.timeReq) -(j.startTime+j.timeReq);
		        		}});*/
		queue=new LinkedList<ProcessTable>();
		blockedQueue=new LinkedList<ProcessTable>();
		memoryBlockedQueue=new LinkedList<>();
		timeGran=time;
		state=0;
	}	        
	/*public void chooseNextProcess(int i,Memory memoryObj){
		removeBlocked(i);
		try{
			//System.out.println(i);
			runningProcess.timeUsed=(i-runningProcess.startTime);
			//System.out.println(runningProcess.timeRem);
			runningProcess.timeRem=runningProcess.timeRem-timeGran;
				InputOutput ioObj=runningProcess.ioRequired;
				if(ioObj.number!=-1){
					runningProcess.status=2;
					ProcessTable process=memoryObj.chooseNextProcessInMemQueue();
					if(process!=null){
						runningProcess.status=2;
						blockedQueue.add(runningProcess);
						runningProcess=process;
						runningProcess.status=1;
					}
				}
				else{
					System.out.println("No device used by this process");
					if(runningProcess.timeRem<=0){
						//System.out.println("Killing process"+runningProcess.name);
						if(!queue.isEmpty()){
							ProcessTable process=queue.remove();
							runningProcess=process;
							runningProcess.status=1;
						}
						else{
							state=0;
							runningProcess=null;
						}
					}
					else{
						runningProcess.status=0;
						queue.add(runningProcess);
						ProcessTable process=memoryObj.chooseNextProcessInMemQueue();
						runningProcess=process;
						runningProcess.status=1;
					}
				}
		}
		catch(Exception e){
			//System.out.println("No process there yet");
		}
		
	}*/
	public void chooseNextProcess(int i,Memory Obj,InputOutput[] arrayIo,HashMap<String,LinkedList<ProcessDetail>> detailsMap,int numberOfProcess){
		removeBlocked(i,arrayIo,detailsMap);
		try{
			//System.out.println(i);
			runningProcess.timeUsed=(i-runningProcess.startTime);
			System.out.println("TIme remaining for the current instruction of "+runningProcess.name+"is  :"+runningProcess.currentInstructiontime);
			runningProcess.timeRem=runningProcess.timeRem-timeGran;
			/*if(runningProcess.currentInstructiontime==0){
				LinkedList<ProcessDetail> det=detailsMap.get(runningProcess.name);
				if(runningProcess.currentInstructiontime==0){
					runningProcess.currentInstructiontime=det.getFirst().timeReq;
				}
			}*/
			runningProcess.currentInstructiontime=runningProcess.currentInstructiontime-timeGran;
			System.out.println("Running process:"+runningProcess.name);
			if(runningProcess.currentInstructiontime<0){
				System.out.println("Killing process Instruction"+runningProcess.name);
				LinkedList<ProcessDetail> det=detailsMap.get(runningProcess.name);
				System.out.println("Size before removing:"+det.size());
				det.remove(0);
				ProcessDetail something;
				try{
					something=det.getFirst();
				}
				catch(Exception e){
					numberOfProcessFinished+=1;
					queue.removeAll(Collections.singleton(runningProcess));
				}
				//System.out.println("Head of the list now is  "+something.flag+"and size is "+ det.size());
				if(det.size()>0){
					runningProcess.currentInstructiontime=0;
					detailsMap.put(runningProcess.name,det);
					ProcessDetail temp=detailsMap.get(runningProcess.name).getFirst();
					System.out.println("temp.flag="+temp.flag);
					if(temp.flag==1){
									queue.add(runningProcess);
									runningProcess.currentInstructiontime=temp.timeReq;
					}
					else{
						//System.out.println("Reaching here");
						try{
							queue.removeAll(Collections.singleton(runningProcess));
						}
						catch(Exception e){
							System.out.println("pRcess not in queue");
						}
						try{
						System.out.println("Head of the queue :"+arrayIo[temp.deviceNumber].inputqueue.peek().name);
						}catch(Exception e){}
						//System.out.println("Reaching here");
						if(arrayIo[temp.deviceNumber].inputqueue.isEmpty()){
							System.out.println("Using process is "+runningProcess.name);
							arrayIo[temp.deviceNumber].usingProcess=runningProcess;
							arrayIo[temp.deviceNumber].timeRem=temp.timeReq;
							arrayIo[temp.deviceNumber].timeRequested=temp.timeReq;
							runningProcess.status=2;
							queue.removeAll(Collections.singleton(runningProcess));
							runningProcess=null;
						}
						else{
							arrayIo[temp.deviceNumber].inputqueue.add(runningProcess);
							runningProcess.status=2;
						}
					}
					if(!queue.isEmpty()){
						System.out.println("Trying to get something from ready queue");
						//cleanReadyQueue();
						//if(detailsMap.get(process.name).getFirst)
						ProcessTable process=queue.remove();
						runningProcess=process;
						runningProcess.status=1;
						Iterator<ProcessTable> iter=memoryBlockedQueue.iterator();
						putIntoReadQueue(iter,detailsMap,Obj);
					}
					else{//Means Ready queue is empty
						if(!memoryBlockedQueue.isEmpty()){
							Iterator<ProcessTable> iter=memoryBlockedQueue.iterator();
							putIntoReadQueue(iter, detailsMap, Obj);
						}
						else{
							System.out.println("Nothing to execute");
							state=0;
							runningProcess=null;
						}
					}
				}
				else{
					System.out.println("I was right");
					System.out.println("The head of queue is"+queue.peek().name);
					runningProcess=queue.remove();
					runningProcess.status=1;
				}
			}//Copy till here
			else{//Nothing gets unblocked here as Nothing is released from the memory
				runningProcess.status=0;
				queue.add(runningProcess);
				ProcessTable process=queue.remove();
				System.out.println("Removing from ready queue"+process.name);
				runningProcess=process;
				runningProcess.status=1;
			}
			
		}
		catch(Exception e){
			//System.out.println("No process there yet");
		}
		System.out.println("My ready queue is "+printReadQueue());
		
		
	}
	private void cleanReadyQueue() {
		// TODO Auto-generated method stub
		Iterator<ProcessTable> iter=queue.iterator();
		while(iter.hasNext()){
			ProcessTable process=iter.next();
			if(process.status==2 || process.status==1){
				queue.remove(process);
			}
				
		}
	}
	private String printReadQueue() {
		// TODO Auto-generated method stub
		Iterator<ProcessTable> iter=queue.iterator();
		while(iter.hasNext()){
			ProcessTable process=iter.next();
			System.out.print(process.name+" ");
		}
		System.out.println();
		return null;
	}
	private void putIntoReadQueue(Iterator<ProcessTable> iter, HashMap<String, LinkedList<ProcessDetail>> detailsMap, Memory Obj) {
		while(iter.hasNext()){
			ProcessTable tempProcess=iter.next();
			//You have to check how much memory the temp process requires and accordingly choose the process
			LinkedList<ProcessDetail> temp=detailsMap.get(tempProcess.name);
			ProcessDetail headLine=temp.getFirst();
			if(Obj.freeMem-headLine.memory>=0){
				tempProcess.status=0;
				memoryBlockedQueue.remove(tempProcess);
				queue.add(tempProcess);
				if(state==0)
					state=1;
			}
		}
		
	}
	public void removeBlocked(int i1,InputOutput[] arrayIo, HashMap<String, LinkedList<ProcessDetail>> detailsMap) {
		//Iterator<ProcessTable> blockedGuys=blockedQueue.iterator();
		//while(blockedGuys.hasNext()){
			//ProcessTable process=blockedGuys.next();
			/*process.ioRequired.timeUsed=i-process.ioRequired.startTime;
			process.ioRequired.timeRem=process.ioRequired.timeRequested-process.ioRequired.timeUsed;
			if(process.ioRequired.timeRem<=0){
				process.status=0;
				queue.add(process);
				blockedQueue.remove(process);
			}*/
			for(int i=0;i<arrayIo.length;i++){
				//System.out.print("Inside removeBlocked"+arrayIo[i].usingProcess);
				if(arrayIo[i].usingProcess!=null){
					System.out.println("Checking for process"+arrayIo[i].usingProcess.name);
					arrayIo[i].timeRem-=timeGran;
					if(arrayIo[i].timeRem<0){
						System.out.println("Done with IO puttinng into ready queue:"+arrayIo[i].usingProcess.name);
						arrayIo[i].usingProcess.status=0;
						if(state==0)
							state=1;
						if(queue.isEmpty()){
							if(runningProcess!=null){
								queue.add(runningProcess);
								runningProcess.status=0;
							}
							runningProcess=arrayIo[i].usingProcess;
							runningProcess.status=1;
							runningProcess.currentInstructiontime=0;
						}
						else{
							queue.add(arrayIo[i].usingProcess);
						}
						LinkedList<ProcessDetail> somelist=detailsMap.get(arrayIo[i].usingProcess.name);
						if(somelist.size()>0)
							somelist.remove(0);
						System.out.println("Number of instructions left is:"+somelist.size());
						detailsMap.put(arrayIo[i].usingProcess.name, somelist);
						System.out.println(somelist.size());
						ProcessDetail det=somelist.getFirst();
						if(det.flag==1){
							runningProcess=arrayIo[i].usingProcess;
							System.out.println("This shit is saying:"+det.timeReq);
							runningProcess.currentInstructiontime=det.timeReq;
							runningProcess.status=1;
						}
						if(!arrayIo[i].inputqueue.isEmpty()){
							arrayIo[i].usingProcess=arrayIo[i].inputqueue.remove();
						}
						else{
							arrayIo[i].usingProcess=null;
						}
						arrayIo[i].timeRem=0;
						arrayIo[i].timeRequested=0;
					}
				}
			}
	}
	public void printProcessTable(int i) {
		// TODO Auto-generated method stub
		try{
			Iterator<ProcessTable> iter=queue.iterator();
			while(iter.hasNext()){
				ProcessTable entry=iter.next();
				entry.timeUsed=i-entry.startTime;
				entry.display();
			}
			runningProcess.timeUsed=i-runningProcess.startTime;
			runningProcess.display();
		}
		catch(Exception e){System.out.println("Nothing to print");}
	}
	/*public LinkedList<ProcessTable> mopUp() {
		LinkedList<ProcessTable> list=new LinkedList<ProcessTable>();
		Iterator<ProcessTable> iter=queue.iterator();
		while(iter.hasNext()){
			ProcessTable process=iter.next();
			if(process.timeRem<=0){
				list.add(process);
			}
		}
		cleanFromQueue(process);
	}*/
	
}
