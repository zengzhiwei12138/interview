package azj.zzw.interview;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InterviewApplicationTests {


	private final List<String> list = new LinkedList<String>();

	@Test
	public void contextLoads() {
	}


	@Test
	public void testCollection(){
		String[] array = {"1","2","zzw","zj"};
		Collections.addAll(list, array);
		System.out.println(list.toString());
	}

}
