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
        ModelData tetraData = OBJFileLoader.loadOBJ("tetra");
        ModelData dodeData = OBJFileLoader.loadOBJ("dode");
        RawModel rawCube = loader.loadToVAO(cubeData.getVertices(), cubeData.getTextureCoords(), cubeData.getNormals(), cubeData.getIndices());
        RawModel rawTetra = loader.loadToVAO(tetraData.getVertices(), tetraData.getTextureCoords(), tetraData.getNormals(), tetraData.getIndices());
        RawModel rawdode = loader.loadToVAO(dodeData.getVertices(), dodeData.getTextureCoords(), dodeData.getNormals(), dodeData.getIndices());
        TexturedModel ironModel = new TexturedModel(rawCube, new ModelTexture(loader.loadTexture("iron")));
        //ironModel.getTexture().setHasTransparency(true);
        TexturedModel steelModel = new TexturedModel(rawTetra, new ModelTexture(loader.loadTexture("steel")));
        TexturedModel dirtModel = new TexturedModel(rawdode, new ModelTexture(loader.loadTexture("dirt")));
        ModelTexture ironTexture = ironModel.getTexture();
        ModelTexture steelTexture = steelModel.getTexture();
        //ModelTexture dirtTexture = dirtModel.getTexture();
        ironTexture.setShineDamper(12);
        steelTexture.setShineDamper(18);
        ironTexture.setReflectivity(2.0f);
        steelTexture.setReflectivity(3.0f);

        //Entity entity = new Entity(steelModel, new Vector3f(0, 10 ,-25),0,0,0,1);
        
        Light light = new Light(new Vector3f(0, 0, 50), new Vector3f(1, 1, 1));
        
        Entity.setIronModel(ironModel);
        Entity.setSteelModel(steelModel);
        Entity.setDirtModel(dirtModel);
        
        Player player = new Player(ironModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
        Camera camera = new Camera(player);
        
        List<Entity> allEnts = new ArrayList<>();

       for (int i = 0; i < 500; i++) {
            float x = r.nextFloat() * -600;
            float y = r.nextFloat() * 500;
            float z = r.nextFloat() * -600;
            allEnts.add(new Iron(new Vector3f(x,y,z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 250; i++) {
            float x = r.nextFloat() * -600;
            float y = r.nextFloat() * 500;
            float z = r.nextFloat() * -600;
            allEnts.add(new Steel(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 1000; i++) {
            float x = r.nextFloat() * -600;
            float y = r.nextFloat() * 500;
            float z = r.nextFloat() * -600;
            allEnts.add(new Dirt(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {

            player.move();
            camera.move();
            player.gravity();
            renderer.processEntity(player);
            for (Entity obj : allEnts) {
                if (obj instanceof Iron) {
                    Iron iron = (Iron) obj;
                    renderer.processEntity(iron);
                    iron.gravity();
//                    iron.move();
                } else if (obj instanceof Steel) {
                    Steel steel = (Steel) obj;
                    renderer.processEntity(steel);
                    steel.gravity();
//                    steel.move();
                } else if (obj instanceof Dirt) {
                    Dirt dirt = (Dirt) obj;
                    renderer.processEntity(dirt);
                    dirt.gravity();
//                    dirt.move();
                }
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
