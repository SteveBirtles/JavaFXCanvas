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
import javafx.scene.effect.Effect;
import javafx.scene.effect.ColorAdjust;

public class Application
{

    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;

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
            Random rnd = new Random();

            Group root = new Group();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            Canvas canvas = new Canvas();

            stage.setTitle("JavaFX Canvas Demo");
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

            Image image = new Image("/sprite.png");

            Set<Sprite> sprites = new HashSet<>();

            for (int i = 0; i < 100; i++)
            {
                Sprite s = new Sprite(rnd.nextInt(WINDOW_WIDTH), rnd.nextInt(WINDOW_HEIGHT), 32, image);
                s.setVelocity(rnd.nextDouble()*100-50, rnd.nextDouble()*100-50);
                sprites.add(s);

            }

            new AnimationTimer() {
                @Override
                public void handle(long now) {

                    for(KeyCode k : keysPressed)
                    {
                        if (k == KeyCode.ESCAPE) Application.terminate();                        
                    }

                    for (Sprite s: sprites)
                    {
                        s.update(fr.getFrameLength());
                    }
                    Sprite.clearUpExired(sprites);

                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setSaturation(1);

                    for (Sprite s : sprites)
                    {
                        colorAdjust.setHue(3.14159 * (s.getX() / WINDOW_WIDTH - 0.5));
                        gc.setEffect(colorAdjust);
                        gc.drawImage(s.getImage(), s.getX() - s.getImage().getWidth() / 2, s.getY() - s.getImage().getHeight() / 2);
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