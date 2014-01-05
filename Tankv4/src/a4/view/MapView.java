package a4.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Iterator;

import javax.swing.JPanel;

import a4.Game;
import a4.gameObjects.GameObject;

public class MapView extends JPanel implements IObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6807570642599946767L;
	//private JTextArea textMap;
	//private JLabel message;
	private IGameWorld gameWorld;
	
	private AffineTransform worldToND;
	private AffineTransform ndToScreen;
	private AffineTransform theVTM;
	private AffineTransform inverseVTM;
	private ScreenPanelDimensions dimensions;
	private Game game;
	
	public MapView(ScreenPanelDimensions dimensions, Game game){
		
		
		setBackground(Color.BLUE);
		this.dimensions = dimensions;
		updateVTM();
		if (Game.debug){
			System.out.println("Map View: " + dimensions);	
		}
		SelectedMouseListener sml = new SelectedMouseListener();
		this.addMouseListener(sml);
		this.addMouseWheelListener(new zoomInWheelListern());
		this.addMouseMotionListener(sml);
		this.game = game;
		
		//super(new BorderLayout());
		
		//setBorder(new TitledBorder("Map"));
		//textMap = new JTextArea("");
		//textMap.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");
		//textMap.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");
		//textMap.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "none");
		//textMap.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "none");
		//textMap.setEditable(false);
		
		//add(textMap, BorderLayout.CENTER);
		
		//message = new JLabel(messageBegin+"Welcome to Tank!");
		//message.setAlignmentX(LEFT_ALIGNMENT);
		//message.setFont(new Font(message.getFont().getName(), Font.BOLD, 18));
		//message.setPreferredSize(new Dimension(100, 100));
		
		//add(message, BorderLayout.SOUTH);
		
		
	}

	@Override
	public void update(IObservable o, Object... arg) {
		if (o instanceof IGameWorld){
			
			IGameWorld gw = (IGameWorld) o;
			
			if (arg.length > 1 && arg[1] instanceof ScreenPanelDimensions){
				dimensions.setWindowDimensions((ScreenPanelDimensions) arg[1]);
				updateVTM();
			}
//			StringBuilder mapBuilder = new StringBuilder();
//			iter = gw.iterator();
			
//			while (iter.hasNext()){
//				mapBuilder.append(iter.next().toString());
//				mapBuilder.append('\n');
//			}
			
			
			//textMap.setText(mapBuilder.toString());
			//System.out.println(mapBuilder.toString());
			
			
			
			//message.setText(messageBegin+gw.getLatestMessage());
			//System.out.println(messageBegin+gw.getLatestMessage());
			
			gameWorld = gw;
			repaint();
		}

	}
	/**
	 * updates the worldToND, ndToScreen, and the VTM if applicable.
	 */
	private void updateVTM(){
		boolean changed = false;
		if (dimensions.getWindowChanged()){
			worldToND = buildWorldToNDXform(dimensions.getWinWidth(), dimensions.getWinHeight(), 
					dimensions.getWinLeft(), dimensions.getWinBottom());
			changed = true;
		}
		if (dimensions.getPanelChanged()){
			ndToScreen = buildNDToScreenXform(dimensions.getPanelWidth(), 
					dimensions.getPanelHeight());
			changed = true;
		}
		if (changed){
			theVTM = (AffineTransform) ndToScreen.clone();
			theVTM.concatenate(worldToND);
			
			try {
				inverseVTM = theVTM.createInverse();
			} catch (NoninvertibleTransformException e) {
				if (Game.debug){
					e.printStackTrace();	
				}
				
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (dimensions != null){
			dimensions.setPanelHeight(this.getHeight());
			dimensions.setPanelWidth(this.getWidth());
			updateVTM();			
			//System.out.println("Map View paint" + dimensions);
		}

		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();
		
		if (theVTM != null){
			g2d.transform(theVTM);	
//			if (Game.debug){
//				if (dimensions != null){
//					System.out.println("vtm dimensions " +dimensions);
//				}
//				System.out.println("The VTM is " + theVTM);
//			}
		} else {
			if (Game.debug){
				System.out.println("No VTM yet");
			}
		}
		
		//set the bounds for if the size gets changed
		//GameObject.setBounds(this.getWidth(), this.getHeight());
		//System.out.println("Bounds:"+this.getWidth()+", "+ this.getHeight());

		if (gameWorld != null){
			Iterator<GameObject> iter = gameWorld.iterator();
			GameObject go;
			while (iter.hasNext()){
				go = iter.next();
				if (go != null){
					go.draw(g);
				} 
			}						
		}

		
		
		g2d.setTransform(saveAT);

		
	}
	private AffineTransform buildWorldToNDXform(double winWidth, double winHeight, double winLeft, double winBottom){
		AffineTransform worldToND = new AffineTransform();
		worldToND.scale(1/winWidth, 1/winHeight);
		worldToND.translate(-winLeft, -winBottom);
		
		return worldToND;
	}
	private AffineTransform buildNDToScreenXform(double panelWidth, double panelHeight){
		AffineTransform NDtoScreen = new AffineTransform();
		NDtoScreen.translate(0,panelHeight);
		NDtoScreen.scale(panelWidth,-panelHeight);
		
		return NDtoScreen;
	}
	private class SelectedMouseListener implements MouseListener,MouseMotionListener{

		private int panX;
		private int panY;
		public SelectedMouseListener(){
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {

			Point p = arg0.getPoint();
			if (inverseVTM != null){
				inverseVTM.transform(p, p);
			}
			game.objectSelected(p, arg0.isControlDown());
			panX = arg0.getX();
			panY = arg0.getY();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			int xDif = panX-arg0.getX();
			int yDif = arg0.getY()-panY;
			
			//if (xDif > 3 || yDif > 3){
				panY = arg0.getY();
				panX = arg0.getX();
				game.pan(xDif, yDif);				
			//}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			int xDif = panX-e.getX();
			int yDif = e.getY()-panY;
			
			
			panY = e.getY();
			panX = e.getX();
			game.pan(xDif, yDif);
			if (Game.debug){
				System.out.println("mouse drageed" + xDif + "," + yDif);
			}
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class zoomInWheelListern implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			int rotations = arg0.getWheelRotation();
			game.zoom(rotations);
			
			
		}
	}


}
