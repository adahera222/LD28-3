package com.intelligentbeans.dare;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.intelligentbeans.boilerplate.*;
public class DareGameScreen extends GameScreen {
	Player player;
	private List<Platform> platformsl;
	private List<PhysicalImage> obstacles;
	private ImageButton resetbutton;
	int platformCount =0;
	boolean followPlayer = true;
	Game game;
	SpriteImage intro;
	boolean started = false;
	public DareGameScreen(String level, Game game) {
		super(level, game);
		this.game = game;
		
		if(Gdx.app.getType() == ApplicationType.Android) {
			intro = new SpriteImage(new Vector2((Gdx.graphics.getWidth()/2)-200, Gdx.graphics.getHeight() - 600),"intro-mobile");
			intro.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
					start();
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,int pointer, int button) {
					
				}

			});
		}else{
			intro = new SpriteImage(new Vector2((Gdx.graphics.getWidth()/2)-200, Gdx.graphics.getHeight() - 600),"intro");
		}
		intro.setX((Gdx.graphics.getWidth()/2)- (intro.getWidth()/2));
		intro.setY((Gdx.graphics.getHeight()/2) - (intro.getHeight()/2));
		staticStage.addActor(intro);
		
		
		SoundManager.getInstance().loadSong("data/sounds/test.mp3");
	}
	
	@Override
	protected void itemCreationLoop(JSONGameItem item){
		
		
		if (item.getItemType().equals("Player")) {
			player = new Player(new Vector2(item.getX(),item.getY()), world,stage, this);
			stage.addActor(player);
			player.addEgg();
		}else if(item.getItemType().equals("Platform")){
			Platform platform = new Platform(new Vector2(item.getX(),item.getY()), world);
			
			if(this.platformsl == null){
				this.platformsl = new LinkedList<Platform>();
			}
			stage.addActor(platform);
			platformsl.add(platform);
			//Gdx.app.log("test","" + platforms.size);
		}else if(item.getItemType().equals("Block")){
			Block block = new Block(new Vector2(item.getX(),item.getY()), world);
			
			if(this.obstacles == null){
				this.obstacles = new LinkedList<PhysicalImage>();
			}
			stage.addActor(block);
			obstacles.add(block);
			//Gdx.app.log("test","" + platforms.size);
		}else if(item.getItemType().equals("Spikes")){
			Spikes spikes = new Spikes(new Vector2(item.getX(),item.getY()), world);
			
			if(this.obstacles == null){
				this.obstacles = new LinkedList<PhysicalImage>();
			}
			stage.addActor(spikes);
			obstacles.add(spikes);
			//Gdx.app.log("test","" + platforms.size);
		}

		
		
		
		
	}
	
	
	/*************************************************************************************
	 * This handles when the window is resized
	 *************************************************************************************/
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		resetbutton.setBounds(Gdx.graphics.getWidth() - 177 - 30, Gdx.graphics.getHeight() - 60 - 30, 177, 60);
		intro.setX((Gdx.graphics.getWidth()/2)- (intro.getWidth()/2));
		intro.setY((Gdx.graphics.getHeight()/2) - (intro.getHeight()/2));
		

	}
	
	
	/*************************************************************************************
	 * This adds the user interface needed based on the type of device
	 *************************************************************************************/
	@Override
	public void addInterface() {


		// Create a table that fills the screen. Everything else will go inside
		// this table.
		Table table = new Table();
		if(Gdx.app.getType() == ApplicationType.Android) {
			table.setFillParent(true);
		}else{
			table.setFillParent(false);
		}

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/z/dare-textures"));
	

		TextureRegionDrawable jumpup = new TextureRegionDrawable(atlas.findRegion("jumpButton"));
		TextureRegionDrawable jumpdown = new TextureRegionDrawable(atlas.findRegion("jumpButtonDown"));

		TextureRegionDrawable leftup = new TextureRegionDrawable(atlas.findRegion("leftButton"));
		TextureRegionDrawable leftdown = new TextureRegionDrawable(atlas.findRegion("leftButtonDown"));

		TextureRegionDrawable rightup = new TextureRegionDrawable(atlas.findRegion("rightButton"));
		TextureRegionDrawable rightdown = new TextureRegionDrawable(atlas.findRegion("rightButtonDown"));
		
		TextureRegionDrawable resetup = new TextureRegionDrawable(atlas.findRegion("resetButton"));
		TextureRegionDrawable resetdown = new TextureRegionDrawable(atlas.findRegion("resetButtonDown"));

		// Create a button with the "default" TextButtonStyle. A 3rd parameter
		// can be used to specify a name other than "default".
		final ImageButton button = new ImageButton(jumpup, jumpdown);
		final ImageButton leftbutton = new ImageButton(leftup, leftdown);
		final ImageButton rightbutton = new ImageButton(rightup, rightdown);
		resetbutton = new ImageButton(resetup, resetdown);

		button.setBounds(Gdx.graphics.getWidth() - 128 - 20, 15, 128, 128);
		leftbutton.setBounds(20, 15, 226, 226);
		rightbutton.setBounds(30, 5, 128, 128);
		resetbutton.setBounds(Gdx.graphics.getWidth() - 177 - 30, Gdx.graphics.getHeight() - 60 - 30, 177, 60);
		// table.add(button);

		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				player.jumpbuttonpressed  = true;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				player.jumpbuttonpressed = false;
			}

		});

		leftbutton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				player.leftbuttonpressed = true;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				player.leftbuttonpressed = false;
			}

		});

		rightbutton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				player.rightDown = true;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				player.rightDown = false;
			}

		});
		
		
		
		resetbutton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
				//////////////RESET HERE
				reset();
				
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,int pointer, int button) {
				
			}

		});

		

		// this ads on screen controls for mobile devices
		if (Gdx.app.getType() == ApplicationType.Android) {
			// android specific code
			staticStage.addActor(leftbutton);
			staticStage.addActor(button);
	
		}
		
		staticStage.addActor(resetbutton);
		resetbutton.setVisible(false);

	}
	
	public void reset(){
		SoundManager.getInstance().stopSong();
		game.setScreen(new DareGameScreen("data/levels/level.json",game));
	}
	public void start(){
		intro.setVisible(false);
		player.rightDown = true;
		started = true;
		
		float delay = .5f; // seconds

		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	player.started = true;
		    	resetbutton.setVisible(true);
		    }
		}, delay);
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		
		
		if(!started && Gdx.input.isKeyPressed(Keys.SPACE)){
			start();
			
		}
		
		if(player.getX() > obstacles.get(0).getX() + Gdx.graphics.getWidth() + (200 * obstacles.size())){
			
			PhysicalImage firstObstacle = obstacles.remove(0);
			firstObstacle.body.setTransform((float) ((player.getX() +  Math.random()*2000 + 1000) * GameScreen.WORLD_TO_BOX), firstObstacle.body.getPosition().y, firstObstacle.body.getAngle());
			obstacles.add(firstObstacle);
			
			if(firstObstacle instanceof Block){
				Random random = new Random();
				((Block) firstObstacle).setMoving(random.nextBoolean());
			}
		}
		
		
		if(platformsl.size() > 4){
			//right trigger
			if(player.getX() > platformsl.get(4).getX()){
				Platform first = platformsl.remove(0);
				first.body.setTransform(platformsl.get(platformsl.size()-1).body.getPosition().x + (422 * GameScreen.WORLD_TO_BOX), first.body.getPosition().y, first.body.getAngle());
				platformsl.add(first);
				
			
			}
		}
		
		
		if(platformsl.size() > 4){
			
			if(player.getX() < platformsl.get(3).getX()){
				Platform last= platformsl.remove(platformsl.size()-1);
				last.body.setTransform(platformsl.get(0).body.getPosition().x - (422 * GameScreen.WORLD_TO_BOX), last.body.getPosition().y, last.body.getAngle());
				platformsl.add(0,last);
			}
		}
		Camera camera = stage.getCamera();
		// pan the camera to the player
		if (player != null && followPlayer) {
			camera.position.y += (player.getY() - camera.position.y + 2f) * .08f   + 10;
			camera.position.x += (player.getX() - camera.position.x) * .08f;


		}
		camera.update();
	}
	
	
	
	
	
	/*************************************************************************************
	 * This handles collisions detected by Box2d
	 *************************************************************************************/
	@Override
	public void beginContact(Contact contact) {

		// this tells us what 2 objects collided
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		// this is where we take action to address the collision
		if ((a.getUserData() instanceof Spikes && b.getUserData() instanceof Player)
				|| (a.getUserData() instanceof Player && b.getUserData() instanceof Spikes)) {

			final Spikes obstacle;

			if (a.getUserData() instanceof Spikes) {
				obstacle = (Spikes) a.getUserData();
			} else {
				obstacle = (Spikes) b.getUserData();
			}

			
				// if the player has hit an obstacle they must die :(
				player.die();
				followPlayer = false;
			
		} 
	}


	
	

}
