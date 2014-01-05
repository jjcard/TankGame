package a4.view;

public class ScreenPanelDimensions {

	private double winWidth, winHeight, winLeft, winBottom;
	private double panelWidth, panelHeight;
	
	private boolean windowChanged;
	private boolean panelChanged;
	public double getWinWidth() {
		return winWidth;
	}
	
	public ScreenPanelDimensions(double winWidth, double winHeight, double winLeft, 
			double winBottom, double panelWidth, double panelHeight){
		setWinBottom(winBottom);
		setWinHeight(winHeight);
		setWinLeft(winLeft);
		setWinWidth(winWidth);
		setPanelHeight(panelHeight);
		setPanelWidth(panelWidth);
	}
	/**
	 * Sets only the Window Dimensions based on the passed in
	 * Dimensions.
	 * @param from
	 */
	public void setWindowDimensions(ScreenPanelDimensions from){
		setWinBottom(from.winBottom);
		setWinHeight(from.winHeight);
		setWinLeft(from.winLeft);
		setWinWidth(from.winWidth);
	}
	public void setWinWidth(double winWidth) {
		setWindowChanged(winWidth, this.winWidth);
		this.winWidth = winWidth;
	}
	public double getWinHeight() {
		return winHeight;
	}
	public void setWinHeight(double winHeight) {
		setWindowChanged(winHeight, this.winHeight);
		this.winHeight = winHeight;
		
	}
	public double getWinLeft() {
		return winLeft;
	}
	public void setWinLeft(double winLeft) {
		setWindowChanged(winLeft, this.winLeft);
		this.winLeft = winLeft;
		
	}
	public double getWinBottom() {
		return winBottom;
	}
	public void setWinBottom(double winBottom) {
		setWindowChanged(winBottom, this.winBottom);
		this.winBottom = winBottom;
	}
	public double getPanelWidth() {
		return panelWidth;
	}
	public void setPanelWidth(double panelWidth) {
		setPanelChanged(panelWidth, this.panelWidth);
		this.panelWidth = panelWidth;
		
	}
	public double getPanelHeight() {
		return panelHeight;
	}
	public void setPanelHeight(double panelHeight) {
		setPanelChanged(panelHeight, this.panelHeight);
		this.panelHeight = panelHeight;
		
	}
	public boolean getPanelChanged(){
		return panelChanged;
	}
	public boolean getWindowChanged(){
		return windowChanged;
	}
	private void setPanelChanged(double newValue, double oldValue){
		panelChanged = panelChanged || newValue != oldValue;
	}
	private void setWindowChanged(double newValue, double oldValue){
		windowChanged = windowChanged || newValue != oldValue;
	}
	public String toString(){
		//for debug purposes only
		return "winWidth=" + winWidth + " winHeight=" + winHeight + " winLeft=" + winLeft + " winBottom="
		 + winBottom + " panelWidth=" + panelWidth + " panelHeight=" + panelHeight + " windowChanged=" + windowChanged
		 + " panelChanged=" + panelChanged;
	}
	public void clearChangeFlags(){
		panelChanged = false;
		windowChanged = false;
	}

	
}
