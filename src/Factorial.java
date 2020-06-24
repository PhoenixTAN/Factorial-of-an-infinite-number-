/*
Author: Ziqi Tan
*/

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Factorial {
	
	public int[] calculateFactorial(String bigInteger) {
		HugeInteger hugeInteger = new HugeInteger(bigInteger);
		HugeInteger inc = new HugeInteger("1");
		HugeInteger factor = new HugeInteger("1");
		HugeInteger factorial = new HugeInteger("1");
		while ( hugeInteger.compareTo(factor) >= 0 ) {
			factorial = factorial.multiply(factor);
			factor = factor.add(inc);
		}
		return factorial.getNumberArray();
	}

	public static void main(String[] args) {
		Factorial f = new Factorial();
		int[] result = f.calculateFactorial("99");
		System.out.println(result.length);
		System.out.println(Arrays.toString(result).replaceAll(", ", ""));
	}

}
