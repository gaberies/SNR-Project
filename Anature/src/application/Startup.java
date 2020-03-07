package application;

import java.io.IOException;

import application.controllers.BattleController;
import application.controllers.LoggerController;
import application.controllers.overworld_cells.PathOneController;
import application.controllers.overworld_cells.StarterTownController;
import application.enums.LoggingTypes;
import application.enums.SceneType;
import application.enums.Species;
import application.enums.WarpPoints;
import application.enums.items.ItemIds;
import application.items.ItemPool;
import application.models.StarterTownModel;
import application.trainers.Trainer;
import application.views.overworld_cells.PathOneCell;
import application.views.overworld_cells.StarterTownCell;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Startup extends Application
{
	private static LoggerStartUp mLogger;
	private static Stage mStage, mLoggerStage;
	private static Player mPlayer;
	private static EventHandler<KeyEvent> mKeyListener;
	private static SceneType mLastSceneType, mCurrSceneType;

	private static StarterTownModel mStarterTownModel;
	private static StarterTownCell mStarterTownView;
	private static StarterTownController mStarterTownController;

//	private static PathOneModel mPathOneModel;
	private static PathOneCell mPathOneView;
	private static PathOneController mPathOneController;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		mPlayer = new Player(null); // TODO Remove Null
		mKeyListener = new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getText().compareTo("`") == 0)
				{
					mLogger.toggleWindow();
				}
			}
		};

		mStage = primaryStage;
		mStage.getIcons().add(new Image(Startup.class.getResourceAsStream("/resources/images/Icon.png")));
		mStage.setTitle("Anature");

		mStage.setMinWidth(640);
		mStage.setMinHeight(360);

		mStage.setWidth(1280);
		mStage.setHeight(720);

		mLogger = new LoggerStartUp(mKeyListener);
		mLogger.init();
		mLoggerStage = new Stage();
		mLogger.start(mLoggerStage);
		mStage.setOnCloseRequest(event -> System.exit(-3000));
		LoggerController.logEvent(LoggingTypes.Error, "The logger has started.");

		mLastSceneType = SceneType.Intro;
		mCurrSceneType = SceneType.Intro;
		changeScene(SceneType.Intro, null);
		mStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	public static void changeScene(SceneType type, WarpPoints warpPoint)
	{
		double width = mStage.getWidth();
		double height = mStage.getHeight();
		
		if(type == null)
		{
			type = mLastSceneType;
		}

		try
		{
			switch(type)
			{
				case Intro:
					FXMLLoader introLoader = new FXMLLoader(Startup.class.getResource("/application/views/IntroView.fxml"));
					Parent introRoot = introLoader.load();
					Scene intro = new Scene(introRoot);
					intro.setOnKeyReleased(mKeyListener);
					
					LoggerController.logEvent(LoggingTypes.Misc, "Changing Scene to Intro");
					mStage.setScene(intro);
					break;

				case Starter_Town:
					if(mStarterTownModel == null)
					{
						mStarterTownModel = new StarterTownModel();
					}
					
					if(mStarterTownView == null)
					{
						mStarterTownView = new StarterTownCell(mLogger);
					}
					
					if(mStarterTownController == null)
					{
						mStarterTownController = new StarterTownController(mLogger, mStarterTownView, mStarterTownModel);
					}
					
					Scene townScene = mStarterTownView.getScene();
					mStarterTownController.movePlayer(warpPoint);

					LoggerController.logEvent(LoggingTypes.Misc, "Changing Scene to Starter Town");
					mStage.setScene(townScene);
					break;

				case Path_1:
//					if(mPathOneModel == null)
//					{
//						mPathOneModel = new StarterTownModel();
//					}
					
					if(mPathOneView == null)
					{
						mPathOneView = new PathOneCell(mLogger);
					}
					
					if(mPathOneController == null)
					{
						mPathOneController = new PathOneController(mLogger, mPathOneView);
					}

					Scene pathOneScene = mPathOneView.getScene();
					mPathOneController.movePlayer(warpPoint);
					
					LoggerController.logEvent(LoggingTypes.Misc, "Changing Scene to Path 1");
					mStage.setScene(pathOneScene);
					break;
					
				default:
					break;
			}

			mLastSceneType = mCurrSceneType;
			mCurrSceneType = type;
		}

		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}

		mStage.setWidth(width);
		mStage.setHeight(height);
	}
	
	public static void startBattle(Trainer toBattle)
	{
		try
		{
			double width = mStage.getWidth();
			double height = mStage.getHeight();
			
			FXMLLoader loader = new FXMLLoader(Startup.class.getResource("/application/views/BattleView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.setOnKeyReleased(mKeyListener);

			BattleController controller = (BattleController) loader.getController();
			controller.setUpBindingsAndElements(scene);
			controller.updateElements(mPlayer, toBattle);
			
			mStage.setWidth(width);
			mStage.setHeight(height);
			
			mLastSceneType = mCurrSceneType;
			
			LoggerController.logEvent(LoggingTypes.Misc, "Changing Scene to Battle");
			mStage.setScene(scene);
		}
		
		catch(IOException e)
		{
			LoggerController.logEvent(LoggingTypes.Error, "Exception when starting battle. \n" + e.getMessage());
		}
	}

	public static void createDemo()
	{
		mPlayer.addAnatures(AnatureBuilder.createAnature(Species.Null, 15));

		Anature second = AnatureBuilder.createAnature(Species.Null, 12);
		second.setName("Other Null");
		mPlayer.addAnatures(second);

		mPlayer.getBackpack().addItem(ItemPool.getHealthPotion(ItemIds.Potion));
		mPlayer.getBackpack().addItem(ItemPool.getHealthPotion(ItemIds.Great_Potion));
		mPlayer.getBackpack().addItem(ItemPool.getHealthPotion(ItemIds.Ultra_Potion));
		mPlayer.getBackpack().addItem(ItemPool.getHealthPotion(ItemIds.Master_Potion));
		
		LoggerController.logEvent(LoggingTypes.Misc, "Generated Demo Player");

		changeScene(SceneType.Starter_Town, WarpPoints.Starter_Town_House_1);
	}

	public static String getPlayerName()
	{
		return mPlayer.getName();
	}
}
