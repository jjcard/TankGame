package a4.view;

public interface IObservable {
	
	public void addObserver(IObserver o);
	public void notifyObservers();

}
