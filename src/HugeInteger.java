import java.util.Arrays;

/*
Author: Ziqi Tan
*/
public class HugeInteger implements Comparable<HugeInteger> {
	
	private int[] number;
	
	/*
	 * Constructor
	 * */
	public HugeInteger(String bigInteger) {
		if (bigInteger.length() == 0) {
			throw new NumberFormatException("Zero length BigInteger");
		}
		this.number = new int[bigInteger.length()];
		
		// trim 
		for ( int i = 0; i < bigInteger.length(); i++ ) {
			Character ch = bigInteger.charAt(i);

			number[i] = Character.getNumericValue(ch);
		}
	}
	
	/*
	 * Constructor
	 * */
	public HugeInteger(int[] bigInteger) {
		// trim
		int firstIndex = -1;
		for( int i = 0; i < bigInteger.length; i++ ) {
			if ( bigInteger[i] != 0 ) {
				firstIndex = i;
				break;
			}
		}
		if ( firstIndex == -1 ) {
			this.number = new int[] {0};
		}
		else {
			int[] temp = new int[bigInteger.length - firstIndex];
			for ( int i = firstIndex, j = 0; i < bigInteger.length; i++, j++ ) {
				temp[j] = bigInteger[i];
			}
			this.number = temp;
		}
	}
	
	@Override
	public String toString() {
		String array = Arrays.toString(number);
		array = array.replaceAll(" ", "");
		array = array.replaceAll(",", "");
		array = array.substring(1, array.length()-1);
		return array;
	}
	
	/* getter */
	public int[] getNumberArray() {
		return this.number;
	}
	
	public HugeInteger add(HugeInteger bigInteger) {
		int[] number1 = this.number;
		int[] number2 = bigInteger.getNumberArray();
		
		int M = number1.length;
		int N = number2.length;
		// System.out.println("Adding... " + Arrays.toString(number1) + "+" + Arrays.toString(number2));
		int[] sum = new int[Math.max(M, N)+1];
		int i = M - 1; 
		int j = N - 1;
		int k = sum.length - 1;
		for ( ; i >= 0 && j >= 0; i--, j--, k-- ) {
			int[] bitAdditionResult = additionHelper(number1[i], number2[j]);
			sum[k] += bitAdditionResult[1];
			sum[k-1] = bitAdditionResult[0];
		}
		
		while( i >= 0 ) {
			sum[k] += number1[i];
			i--;
			k--;
		}
		
		while( j >= 0 ) {
			sum[k] += number2[j];
			j--;
			k--;
		}
		// System.out.println("Two sum:" + Arrays.toString(sum));
		return new HugeInteger(sum);
	}
	
	public HugeInteger leftShift(int bits) {
		int[] res = new int[this.number.length + bits];
		for( int i = 0; i < this.number.length; i++ ) {
			res[i] = this.number[i];
		}
		this.number = res;
		return this;
	}
	
	public HugeInteger multiply(HugeInteger bigInteger) {
		
		int[] number1 = this.number;
		int[] number2 = bigInteger.getNumberArray();

		// make sure number1 is greater than or equal to number2
		if ( number1.length < number2.length ) {
			int[] tmp = number1;
			number1 = number2;
			number2 = tmp;
		}
		
		int M = number1.length;
		int N = number2.length;
		// System.out.println(Arrays.toString(number1));
		// System.out.println(Arrays.toString(number2));
		int[] product = new int[Math.max(M, N)+1];
		int[] temp = new int[Math.max(M, N)+1];
		int len = product.length;
		// System.out.println("len: " + len);
		for ( int i = N - 1; i >= 0; i-- ) {
			for ( int j = M - 1; j >= 0; j-- ) {
				// java.lang.ArrayIndexOutOfBoundsException
				// System.out.println("number1[j]: " + number1[j]);
				// System.out.println("number2[i]:" + number2[i]);
				int[] bitMultiplyResult = multiplyHelper(number1[j], number2[i]);
				// System.out.println("temp length:" + temp.length);
				// System.out.println("j+1="+(j+1));
				// System.out.println("j="+j);
				
				temp[j+1] += bitMultiplyResult[1];	// res
				if ( temp[j+1] >= 10 ) {
					temp[j] = temp[j+1] / 10 + bitMultiplyResult[0];
					temp[j+1] %= 10; 
				}
				else {
					temp[j] += bitMultiplyResult[0];	// carry
				}
				// System.out.println("temp: " + Arrays.toString(temp));
			}
			
			// product += temp;
			temp = new HugeInteger(temp).leftShift(N-i-1).getNumberArray();
			product = new HugeInteger(product).add(new HugeInteger(temp)).getNumberArray();
			// System.out.println("product: " + Arrays.toString(product));
			temp = new int[Math.max(M, N)+1];
		}

		// System.out.println(Arrays.toString(product));
		
		return new HugeInteger(product);
	}
	
	private int[] additionHelper(int num1, int num2) {
		int sum = num1 + num2;
		int carry = sum / 10;
		int res = sum % 10;
		return new int[] {carry, res};
	}
	
	private int[] multiplyHelper(int num1, int num2) {
		int product = num1 * num2;
		int carry = product / 10;
		int res = product % 10;
		return new int[] {carry, res};
	}

	@Override
	public int compareTo(HugeInteger bigInteger) {
		int[] number1 = this.number;
		int[] number2 = bigInteger.getNumberArray();
		if ( number1.length > number2.length ) {
			return 1;
		}
		if ( number1.length < number2.length ) {
			return -1;
		}
		
		for ( int i = 0; i < number1.length; i++ ) {
			if ( number1[i] > number2[i] ) {
				return 1;
			}
			if ( number1[i] < number2[i] ) {
				return -1;
			}
		}
		return 0;
	}
}
