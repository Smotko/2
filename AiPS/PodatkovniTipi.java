import java.util.Arrays;
import java.util.HashSet;
import java.util.Set; //množica
import java.util.ArrayDeque;
import java.util.Deque; //sklad
public class PodatkovniTipi {
	public static void main(String[] args){
		Set<String> set = new HashSet<>();
		set.add("A");
		set.add("A");
		set.add("b");
		set.add("a");
		set.remove("A");
		System.out.printf("ELEMENTS: %s,%b,%b\n",set,set.contains("a"),set.contains("A"));
		//java ima avtomatsko dereferenciranje
		Deque<Integer> stack = new ArrayDeque<>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
	}
}