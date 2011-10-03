package village;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product>{
	@Override
	public int compare(Product arg0, Product arg1) {
		// TODO Auto-generated method stub
		if(arg0.getTime()<arg1.getTime()) return -1;
		if(arg0.getTime()>arg1.getTime()) return 1;
		return 0;
	}
}
