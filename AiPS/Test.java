import java.util.Arrays;
public class Test {
	public static void main(String[] args){
		int[] a = {1,3,3,2};
		System.out.printf("A, %d\n",a[1]);
		sort2(a);
		System.out.printf("B, %d",a[1]);
	}
	public static int[] sort2(int[] array){ //namesto static lahko naredimo Myclass obj = new Myclass();
		Arrays.sort(array);
		System.exit(0);
		return(array);
	}
}