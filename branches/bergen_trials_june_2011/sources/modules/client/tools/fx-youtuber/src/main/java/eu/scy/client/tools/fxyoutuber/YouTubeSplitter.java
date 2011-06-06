package eu.scy.client.tools.fxyoutuber;

public class YouTubeSplitter {
	/*
	public static void main(String[] args) {
		split("http://www.youtube.com/watch?v=spn-84Qe9i8");
		split("http://www.youtube.com/watch?v=ABnyHNgE2Q8&feature=related");
	}
	*/
	static String split(String input) {
            int pos = -1;
            if((pos = input.indexOf("v=")) != -1) {
                    input = input.substring(pos+2);
                    if((pos = input.indexOf("&")) != -1) {
                            input = input.substring(0, pos);
                    }
            }
            return(input);
	}
}