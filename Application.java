import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.canvas.*; 
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer; 
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Application
{

    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int SPRITE_SIZE = 64;
    public static final int BEAM_SIZE = 32;

    static Set<KeyCode> keysPressed = new HashSet<>();

    public static void main(String args[])
    {       
        JFXPanel panel = new JFXPanel();        
        Platform.runLater(() -> start());               
    }

    private static void start() 
    {
        try
        {         
            System.out.println("Application Starting...");

            FrameRegulator fr = new FrameRegulator();

            Set<SpaceEntity> invaders = new HashSet<>();
            Player player = new Player(512, 700);
            Set<SpaceEntity> beams = new HashSet<>();

            Group root = new Group();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            Canvas canvas = new Canvas();

            stage.setTitle("JavaFX Space Invaders");
            stage.setResizable(false);
            stage.setScene(scene);                        
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Close button was clicked!");
                        Application.terminate();
                    }
                });
            stage.show(); 
            stage.setWidth(WINDOW_WIDTH);
            stage.setHeight(WINDOW_HEIGHT);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> keysPressed.add(event.getCode()));
            scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> keysPressed.remove(event.getCode()));

            canvas.setWidth(WINDOW_WIDTH);
            canvas.setHeight(WINDOW_HEIGHT);            
            root.getChildren().add(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.WHITE);
            gc.setFont(new Font("Arial", 14));    

            Image background = new Image("/background.jpg");

            Image[] sprites = new Image[9];
            sprites[0] = new Image("/0.png", SPRITE_SIZE, SPRITE_SIZE, true, true);
            sprites[1] = new Image("/1.png", SPRITE_SIZE, SPRITE_SIZE, true, true);
            sprites[2] = new Image("/2.png", SPRITE_SIZE, SPRITE_SIZE, true, true);
            sprites[3] = new Image("/3.png", SPRITE_SIZE, SPRITE_SIZE, true, true);
            sprites[4] = new Image("/4.png", SPRITE_SIZE, SPRITE_SIZE, true, true);
            sprites[5] = new Image("/A.png", BEAM_SIZE, BEAM_SIZE, true, true);
            sprites[6] = new Image("/B.png", BEAM_SIZE, BEAM_SIZE, true, true);
            sprites[7] = new Image("/C.png", BEAM_SIZE, BEAM_SIZE, true, true);
            sprites[8] = new Image("/D.png", BEAM_SIZE, BEAM_SIZE, true, true);

            Random rnd = new Random();

            for (int x = 0; x < 12; x++)
            {
                for (int y = 0; y < 6; y++)
                {
                    int sprite;
                    if (y < 2) sprite = 1;
                    else if (y < 4) sprite = 2;
                    else sprite = 3;

                    Invader i = new Invader(x * 70 + 40, y * 50 + 40, sprite);                                        

                    i.setVelocity(50, 0);
                    invaders.add(i);
                }
            }

            new AnimationTimer() {
                @Override
                public void handle(long now) {

                    for(KeyCode k : keysPressed)
                    {
                        if (k == KeyCode.ESCAPE) Application.terminate();

                        if (player.alive())
                        {
                            if (k == KeyCode.LEFT) 
                                player.setVelocity(400, 0);
                            else if (k == KeyCode.RIGHT)
                                player.setVelocity(-400, 0);

                            if (k == KeyCode.S)
                            {
                                if (player.reloaded())
                                {
                                    beams.add(new Beam(player.getX(), player.getY() - SPRITE_SIZE / 2, 5, 0, -600, true));
                                    player.setReloadTimer(3);
                                }
                            }
                        }
                    }

                    for (SpaceEntity i : invaders)
                    {
                        i.update(fr.getFrameLength());

                        if (((Invader) i).reloaded())
                        {
                            beams.add(new Beam(i.getX(), i.getY() + SPRITE_SIZE / 2, 5 + ((Invader) i).getSprite(), 400, 0, false));
                            ((Invader) i).setReloadTimer(30);
                        }
                    }

                    for (SpaceEntity b : beams)
                    {
                        b.update(fr.getFrameLength());

                        b.collidesWith(player);

                        for (SpaceEntity i : invaders)
                        {
                            b.collidesWith(i);
                        }
                    }                                       

                    SpaceEntity.clearUpExired(beams);
                    SpaceEntity.clearUpExired(invaders);

                    player.update(fr.getFrameLength());

                    if (Invader.checkIfEdgeReached())
                    {
                        for (SpaceEntity i : invaders)
                        {                            
                            ((Invader) i).reverseDirection();
                            ((Invader) i).nudgeDown();
                        }
                    }

                    gc.clearRect(0, 0, stage.getWidth(), stage.getHeight());
                    gc.drawImage(background, 0, 0);

                    for (SpaceEntity i : invaders)
                    {
                        gc.drawImage(sprites[i.getSprite()], i.getX() - SPRITE_SIZE / 2, i.getY() - SPRITE_SIZE / 2);
                    }

                    if (player.alive())
                    {
                        gc.drawImage(sprites[player.getSprite()], player.getX() - SPRITE_SIZE / 2, player.getY() - SPRITE_SIZE / 2);
                    }

                    for (SpaceEntity b : beams)
                    {
                        gc.drawImage(sprites[b.getSprite()], b.getX() - BEAM_SIZE / 2, b.getY() - BEAM_SIZE / 2);
                    }

                    fr.updateFPS(now, gc);

                }
            }.start();

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            terminate();
        }
    }

    public static void terminate()
    {
        System.out.println("Terminating Application...");
        System.exit(0);
    }

}