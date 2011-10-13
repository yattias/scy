package lpv;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;

 class LPVCallback implements Callback {
	LPV lpv;
	 public LPVCallback(LPV l) {
		 super();
		 lpv = l;
	}
	 
    public void call(Command c, int seq, Tuple t, Tuple bT) {
    	String[] l = new String[7];
    	l[0] = ((Long) t.getField(2).getValue()).toString();
    	if(((String)t.getField(3).getValue()).equals("elo_saved") || ((String)t.getField(3).getValue()).equals("elo_save")){
    	}
    	for(int i = 3; i<7;i++){
    		l[i-2] = (String) t.getField(i).getValue();
        	if(l[i-2] == "") l[i-2] = null;
        }
    	l[5] = (String) t.getField(8).getValue();
    	int j = 9;
    	boolean end = t.numberOfFields()<10;
    	while(!end){
    		String[] k = ((String) t.getField(j).getValue()).split("=");
    		if(k.length>1){
    			if(k[0].equals("elo_uri")){
    				l[6] = k[1];
    			}
    		}
    		j++;
    		if(j>=t.numberOfFields()) end = true;
    	}
    	lpv.addOnTheFly(l);
    }
}