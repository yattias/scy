




public class MyContentBubble extends MyInfoBubble {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -620111665991297216L;
	private String header,author,time,description;
	private village.StateObject owner;
	
	public MyContentBubble(int foX, int foY, int w, int h) {
		super(foX, foY, w, h);
		header="";
		author="";
		time="";
		description="";
		init();
	}

	public MyContentBubble(int foX, int foY, int ll, int w, int h) {
		super(foX, foY, ll, w, h);
		header="";
		author="";
		time="";
		description="";
		init();
	}

	public MyContentBubble(int foX, int foY, int w, int h, String s) {
		super(foX, foY, w, h, s);
		header="";
		author="";
		time="";
		description="";
		init();
	}

	public MyContentBubble(int foX, int foY, int ll, int w, int h, String s) {
		super(foX, foY, ll, w, h, s);
		header="";
		author="";
		time="";
		description="";
		init();
	}

	private void init(){
	
	}
	
	
	public void setOwner(village.StateObject o){
		owner = o;
	}
	
	public village.StateObject getOwner(){
		return owner;
	}
	
	public void setHeader(String h){
		header=h;
	}


	public void setAuthor(String a){
		author = a;
	}


	public void setTime(String t){
		time = t;
	}
	
	public void setDescription(String d){
		description = d;
	}
	
	public void makeContent(){
		setText("<html><font face=\"Verdana,Arial,sans-serif\" size=5><b>"+header+
				"</b><br><br></font>" +
				"<font face=\"Verdana,Arial,sans-serif\" size=3>"+author+
				"<br> Creation time: "+time+"<br>"
				+description+"</font></html>");
	}


		
	

	
	


	

}
