package village;

public abstract  class StateObject {
	
	int state = 0;

	
	public int getState(){
		return state;
	}
	
	public void setState(int s){
		state = s;
	}
	
	public abstract long getCenterX();

	public abstract long getCenterY();

}
