
public class ProcessDetail {
	int flag;//1 for cpu and 0 for IO
	int memory;
	int timeReq;
	int deviceNumber;
	public ProcessDetail(int f,int m,int t) {//m for memory and t for time
		flag=f;
		if(flag==1){
			memory=m;
			timeReq=t;
			deviceNumber=-1;
		}
		else{
			deviceNumber=m;
			timeReq=t;
		}
	}
}
