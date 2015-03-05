package engineTester;

import entities.Camera;
import entities.Entity;
import entities.types.Iron;
import entities.Light;
import entities.Player;
import entities.types.Carbon;
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
        ModelData octaData = OBJFileLoader.loadOBJ("octa");
        ModelData dodeData = OBJFileLoader.loadOBJ("dode");
        ModelData icosaData = OBJFileLoader.loadOBJ("icosa");
        RawModel rawCube = loader.loadToVAO(cubeData.getVertices(), cubeData.getTextureCoords(), cubeData.getNormals(), cubeData.getIndices());
        RawModel rawTetra = loader.loadToVAO(tetraData.getVertices(), tetraData.getTextureCoords(), tetraData.getNormals(), tetraData.getIndices());
        RawModel rawOcta = loader.loadToVAO(octaData.getVertices(), octaData.getTextureCoords(), octaData.getNormals(), octaData.getIndices());
        RawModel rawDode = loader.loadToVAO(dodeData.getVertices(), dodeData.getTextureCoords(), dodeData.getNormals(), dodeData.getIndices());
        RawModel rawIcosa = loader.loadToVAO(icosaData.getVertices(), icosaData.getTextureCoords(), icosaData.getNormals(), icosaData.getIndices());
        TexturedModel ironModel = new TexturedModel(rawCube, new ModelTexture(loader.loadTexture("iron")));
        //ironModel.getTexture().setHasTransparency(true);
        TexturedModel steelModel = new TexturedModel(rawOcta, new ModelTexture(loader.loadTexture("steel")));
        TexturedModel dirtModel = new TexturedModel(rawDode, new ModelTexture(loader.loadTexture("dirt")));
        TexturedModel carbonModel = new TexturedModel(rawTetra, new ModelTexture(loader.loadTexture("carbon")));
        ModelTexture ironTexture = ironModel.getTexture();
        ModelTexture steelTexture = steelModel.getTexture();
        //ModelTexture dirtTexture = dirtModel.getTexture();
        //ModelTexture carbonTexture = carbonModel.getTexture();
        ironTexture.setShineDamper(12);
        steelTexture.setShineDamper(18);
        ironTexture.setReflectivity(2.0f);
        steelTexture.setReflectivity(3.0f);

        //Entity entity = new Entity(steelModel, new Vector3f(0, 10 ,-25),0,0,0,1);
        
        Light light = new Light(new Vector3f(1500, 1500, 1500), new Vector3f(1, 1, 1));
        
        Entity.setIronModel(ironModel);
        Entity.setSteelModel(steelModel);
        Entity.setDirtModel(dirtModel);
        Entity.setCarbonModel(carbonModel);
        
        Player player = new Player(steelModel, new Vector3f(1500, 1500, 1500), 0, 0, 0, 1);
        Camera camera = new Camera(player);
        
        List<Entity> allEnts = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            float x = r.nextFloat() * 3000;
            float y = r.nextFloat() * 3000;
            float z = r.nextFloat() * 3000;
            allEnts.add(new Iron(new Vector3f(x,y,z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 5000; i++) {
            float x = r.nextFloat() * 3000;
            float y = r.nextFloat() * 3000;
            float z = r.nextFloat() * 3000;
            allEnts.add(new Steel(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 30000; i++) {
            float x = r.nextFloat() * 3000;
            float y = r.nextFloat() * 3000;
            float z = r.nextFloat() * 3000;
            allEnts.add(new Dirt(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 20000; i++) {
            float x = r.nextFloat() * 3000;
            float y = r.nextFloat() * 3000;
            float z = r.nextFloat() * 3000;
            allEnts.add(new Carbon(new Vector3f(x, y, z), r.nextFloat() * 180f, r.nextFloat() * 180f, 0f, 1f));
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
                } else if (obj instanceof Carbon) {
                    Carbon carbon = (Carbon) obj;
                    renderer.processEntity(carbon);
                    carbon.gravity();
//                    carbon.move();
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
