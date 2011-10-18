package lpv;
import java.util.Comparator;


class AllDataComparator implements Comparator<String[]> {

	@Override
	public int compare(String[] arg0, String[] arg1) {
		if(Long.parseLong(arg0[0])<Long.parseLong(arg1[0])) return -1;
		if(Long.parseLong(arg0[0])>Long.parseLong(arg1[0])) return 1;
		return 0;
	}

}
