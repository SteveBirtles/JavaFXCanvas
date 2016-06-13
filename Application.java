import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.canvas.*; 
import javafx.scene.transform.Rotate;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer; 
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.scene.effect.GaussianBlur;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.Deque;
import java.util.ArrayDeque;

public class Application
{

    public static void main(String args[])
    {       
        JFXPanel panel = new JFXPanel();        
        Platform.runLater(() -> start());               
    }

    static Integer frameCounter = 0;
    static Long tick = 0L;
    static Set<KeyCode> keysPressed = new HashSet<>();

    static Deque<Long> frameLengths = new ArrayDeque<>();

    private static void rotateGraphicsContext(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private static void start() 
    {
        try
        {         
            System.out.println("Application Starting...");

            Group root = new Group();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1024, 768, Color.BLACK);

            stage.setTitle("JavaFX Demo");
            stage.setResizable(false);
            stage.setScene(scene);            

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> keysPressed.add(event.getCode()));
            scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> keysPressed.remove(event.getCode()));

            Canvas canvas = new Canvas();
            canvas.widthProperty().bind(stage.widthProperty());
            canvas.heightProperty().bind(stage.heightProperty());

            //canvas.setEffect(new GaussianBlur());

            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.setStroke(Color.WHITE);
            gc.setFont(new Font("Arial", 14));    

            Image[] image = new Image[8];

            final int spriteSize = 128;

            image[0] = new Image("/0.png", spriteSize, spriteSize, true, true);
            image[1] = new Image("/1.png", spriteSize, spriteSize, true, true);
            image[2] = new Image("/2.png", spriteSize, spriteSize, true, true);
            image[3] = new Image("/3.png", spriteSize, spriteSize, true, true);
            image[4] = new Image("/4.png", spriteSize, spriteSize, true, true);

            Random rnd = new Random();
            new AnimationTimer() {
                @Override
                public void handle(long now) {

                    for(KeyCode k : keysPressed)
                    {
                        if (k == KeyCode.ESCAPE) Application.terminate();

                        //if (k == KeyCode.LEFT) x -= 10;

                        //if (k == KeyCode.RIGHT) x += 10;                                               

                    }

                    gc.clearRect(0, 0, stage.getWidth(), stage.getHeight());

                    rnd.setSeed(0);
                    for (int i = 0; i < 1000; i++)
                    {
                        gc.save();
                        double x = (rnd.nextInt(1024 + spriteSize * 2) + tick / 10) % (1024 + spriteSize * 2) - spriteSize;
                        double y = rnd.nextInt(768 + spriteSize) - spriteSize / 2;
                        rotateGraphicsContext(gc, frameCounter + i, x, y);
                        gc.drawImage(image[i % 5], x - spriteSize / 2, y - spriteSize / 2);
                        gc.restore();
                    }

                    frameCounter += 1;
                    Long tock = now / 1000000;           
                    frameLengths.addLast(tock - tick);
                    if (frameLengths.size() > 30) frameLengths.removeFirst();
                    Long total = 0L;
                    for(Long x : frameLengths) total += Math.round(1000.0 / x);
                    Long fps = total / frameLengths.size();
                    gc.strokeText("Frame " + frameCounter.toString() + " | " + fps.toString() + " FPS", 800, 20);
                    tick = tock;

                }

            }.start();

            root.getChildren().add(canvas);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Close button was clicked!");
                        Application.terminate();
                    }
                });

            stage.show(); 

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