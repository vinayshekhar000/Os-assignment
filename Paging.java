import java.util.Scanner;
public class Paging {
	public static void main(String args[]){
		String inputAddress;
		final int n=64;
		int a[]=new int[n];
		Scanner in=new Scanner(System.in);
		inputAddress=in.nextLine();
		String pageOffset=inputAddress.substring(inputAddress.length()-3,inputAddress.length());
		String virtualPageFrame=inputAddress.substring(0,inputAddress.length()-3);
		String pageOffsetRes="0x"+pageOffset;
		System.out.println("Input Address:"+inputAddress);
		System.out.println("Page offset:"+pageOffsetRes);
		System.out.println("Virtual Page Frame Number: "+virtualPageFrame);
		for(int i=0;i<n;i++){
			a[i]=(i+1)%63;
		}
		int total=0,total1=0;
		char b=virtualPageFrame.charAt(2);
		char c=virtualPageFrame.charAt(3);
		if(Character.isDigit(b)){
			total=b-48;
			total=16*total;
		}
		if(Character.isDigit(c)){
			total1=c-48;
		}
		else{
			//System.out.println("******");
			total1=c-87;
		}
		int res=total+total1;
		String hex=Integer.toHexString(a[res]);
		String physicalAddress="0x"+hex;
		System.out.println("Physical Address: "+physicalAddress+pageOffset);
	}
}
