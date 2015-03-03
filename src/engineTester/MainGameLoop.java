package engineTester;

import entities.Camera;
import entities.Entity;
import entities.types.Iron;
import entities.Light;
import entities.Player;
import entities.types.Dirt;
import entities.types.Steel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.RawModel;
import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.TexturedModel;
import objConverter.ModelData;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterRenderer;
import objConverter.OBJFileLoader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

/**
 *
 * @author ko
 */
public class MainGameLoop {

    static Random r = new Random();

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        ModelData cubeData = OBJFileLoader.loadOBJ("cube");
        RawModel rawCube = loader.loadToVAO(cubeData.getVertices(), cubeData.getTextureCoords(), cubeData.getNormals(), cubeData.getIndices());
        TexturedModel ironModel = new TexturedModel(OBJLoader.loadObjModel("cube", loader), new ModelTexture(loader.loadTexture("iron")));

        //Entity entity = new Entity(staticModel, new Vector3f(0,-5,-25),0,0,0,1);
        Light light = new Light(new Vector3f(0, 0, 50), new Vector3f(1, 1, 1));

        Player player = new Player(ironModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
        Camera camera = new Camera(player);
        
//        List<Entity> allEnts = new ArrayList<>();
//
//        for (int i = 0; i < 800; i++) {
//            float x = r.nextFloat() * 100 - 50;
//            float y = r.nextFloat() * 100 - 50;
//            float z = r.nextFloat() * -300;
//            allEnts.add(new Iron(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
//        }
//
//        for (int i = 0; i < 500; i++) {
//            float x = r.nextFloat() * 100 - 50;
//            float y = r.nextFloat() * 100 - 50;
//            float z = r.nextFloat() * -300;
//            allEnts.add(new Steel(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
//        }
//
//        for (int i = 0; i < 1000; i++) {
//            float x = r.nextFloat() * 100 - 50;
//            float y = r.nextFloat() * 100 - 50;
//            float z = r.nextFloat() * -300;
//            allEnts.add(new Dirt(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
//        }

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {

            camera.move();
            player.move();
            player.gravity();
            renderer.processEntity(player);
//            for (Entity obj : allEnts) {
//                if (obj instanceof Iron) {
//                    Iron iron = (Iron) obj;
//                    renderer.processEntity(iron);
//                    iron.gravity();
//                } else if (obj instanceof Steel) {
//                    Steel steel = (Steel) obj;
//                    renderer.processEntity(steel);
//                    steel.gravity();
//                } else if (obj instanceof Dirt) {
//                    Dirt dirt = (Dirt) obj;
//                    renderer.processEntity(dirt);
//                    dirt.gravity();
//                }
//            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
