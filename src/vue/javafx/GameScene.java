package vue.javafx;

import java.util.ArrayList;
import java.util.List;

import Enums.Colors;
import Enums.Values;
import controler.Action;
import controler.Controler;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GameScene extends Scene {
	
	private Stage fenetre;
	private BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
	private final BackgroundImage bi = new BackgroundImage(new Image("file:./ressources/Tarot_Background.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
	private final Background b = new Background(bi);
	
	private ArrayList<CardFX> cards = new ArrayList<CardFX>();
	private ArrayList<CardFX> playerHand = new ArrayList<CardFX>();
	private ArrayList<CardFX> dogMaw = new ArrayList<CardFX>();
	
	private final StackPane stackPane = new StackPane();
	private final SubScene subScene = new SubScene(stackPane, 1600, 900, false, SceneAntialiasing.BALANCED); 
    private final TarotButton distribButton = new TarotButton("Begin !");
    private TarotButton backMenuButton = new TarotButton("Back to menu");
    private final ToolBar toolBar = new ToolBar(); 
    private final static BorderPane root = new BorderPane(); 
    private SequentialTransition flips = new SequentialTransition();
    private SequentialTransition moves = new SequentialTransition();
    private SequentialTransition distrib = new SequentialTransition();
	
    
	
	public GameScene(Stage fenetre){
		super(root, 1600, 900);
		this.fenetre = fenetre;
		
		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
			
		
		/*for (Colors c : Colors.values()) {
			System.out.println(c.toString());
			if(c == Colors.TRUMPS){
				for (Values v : Values.getValues(false)) {
					System.out.println(v.toString());
					CardFX c1 = new CardFX (v,c, 500,400-x);
					cards.add(c1);
					x+=1;
				}
			}
			else {
				for(Values v : Values.getValues(true)) {
					System.out.println(v.toString());
					CardFX c1 = new CardFX (v,c, 500,400-x);
					cards.add(c1);
					x+=1;
				}
			}
		}*/
		double x = 670;
		double y = 250;
		for (model.Card cm : Controler.activeControler.getModel().getDeck().getCards()) {
			cards.add(new CardFX(cm.getValue(),cm.getColor(),x,y));
			x -= 0.05;
			y -= 0.3;
		}
		/** POSITION DECK JOUEUR 1436,600-x **/

        stackPane.getChildren().addAll(cards);
        //stackPane.setAlignment(Pos.BASELINE_CENTER);
        //stackPane.getChildren().addAll(ptGroup);
        stackPane.setBackground(null);
        //Toolbar
        toolBar.getItems().addAll(distribButton);
        toolBar.getItems().addAll(backMenuButton);
        
        //BorderPane
        root.setTop(toolBar); 
        root.setCenter(subScene);
        root.setBackground(b);
        
        subScene.setCamera(new PerspectiveCamera());
        //scene.getStylesheets().add("https://github.com/JFXtras/jfxtras-styles/blob/master/src/jmetro/JMetroDarkTheme.css");

        int k=0;
        for(CardFX c : cards){
        	moves.getChildren().add(c.getMoveLeft(k));
        	flips.getChildren().add(c.getRotateCard());
        	k++;
        }
        
        //animation.setCycleCount(SequentialTransition.INDEFINITE); 
        intializingEvents();
	}

	/**
	 * 
	 */
	private void intializingEvents() {
        distribButton.setOnAction(event ->{
        		Controler.activeControler.performAction(Action.START_DISTRUBUTION);
        		System.out.println("DISTRIBUTION COMMENCE");
        		//Commencer la distribution visuelle
        		toolBar.getItems().remove(distribButton);
        		initializeDistribution();
        		int u=0;
    			boolean b=true;
    	        for(CardFX c : cards) {
    	        	c.getShuffle(b,u).play();
    	        	if(cards.size()/2 < u+1 && b)
    	        		b=!b;
    	        	u++;
    	        }
        		distrib.play();
        });
        
        backMenuButton.setOnAction(event ->{
        	boolean fc = fenetre.isFullScreen();
        	fenetre.setScene(MainJavaFX.scenes.get(MainJavaFX.MAIN_MENU_INDEX));
        	if(fc)
        		fenetre.setFullScreen(true);
        });
	}
	
	private void initializeDistribution() {
		List<Integer> l = Controler.activeControler.getModel().getDistributionOrder();
		for(int i : l) {
			CardFX c = cards.get(cards.size()-1);
			switch (i) {
			case -1:
				dogMaw.add(c);
				break;
			case 0:
				playerHand.add(c);
				//c.toFront();
				break;
			case 1:
				//c.setRotate(10);
				break;
			case 2:
				//c.setRotate(10);
				break;
			case 3:
				
				break;
			}
			//c.toFront();
			cards.remove(c);
			distrib.getChildren().add(c.getAnimationDistrib(i));
		}
	}
	public void reset(){
		initialize();
	}

	public GameScene(Parent root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public GameScene(Parent root, Paint fill) {
		super(root, fill);
		// TODO Auto-generated constructor stub
	}

	public GameScene(Parent root, double width, double height) {
		super(root, width, height);
		// TODO Auto-generated constructor stub
	}

	public GameScene(Parent root, double width, double height, Paint fill) {
		super(root, width, height, fill);
		// TODO Auto-generated constructor stub
	}

	public GameScene(Parent root, double width, double height, boolean depthBuffer) {
		super(root, width, height, depthBuffer);
		// TODO Auto-generated constructor stub
	}

	public GameScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
		super(root, width, height, depthBuffer, antiAliasing);
		// TODO Auto-generated constructor stub
	}

}