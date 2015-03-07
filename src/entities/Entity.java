package entities;

import com.nackloose.util.log.Log;
import java.awt.Shape;
import static java.lang.Math.sqrt;
import jinngine.math.Quaternion;
import jinngine.math.Vector3;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

/**
 *
 * @author ko
 */
public class Entity {

    public static TexturedModel IRON_MODEL, STEEL_MODEL, DIRT_MODEL, CARBON_MODEL, SUN_MODEL;

    public static void setIronModel(TexturedModel model) {
        IRON_MODEL = model;
    }

    public static void setSteelModel(TexturedModel model) {
        STEEL_MODEL = model;
    }

    public static void setDirtModel(TexturedModel model) {
        DIRT_MODEL = model;
    }

    public static void setCarbonModel(TexturedModel model) {
        CARBON_MODEL = model;
    }

    public static void setSunModel(TexturedModel model) {
        SUN_MODEL = model;
    }

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    protected float GRAVITY = 50;
    private final float TERRAIN_HEIGHT = 1500;
    private static final float RUN_SPEED = 50;
    private static final float STRAFF_SPEED = 30;
    private static final float TURN_SPEED = 160;
    private static final float JUMP_POWER = 50;

    private float currentSpeed = 0;
    private float straffSpeed = 0;
    private float currentTurnSpeed = 0;
    private float pitch = 0;

    float yForce = 0;
    float xinertia = 0;
    float yinertia = 0;
    float zinertia = 0;
    float xForce = 0;
    float zForce = 0;

    private float calcDist(Vector3f start, Vector3f end) {
        return (float) Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2) + Math.pow(end.z - start.z, 2));
    }

    private float calcDist(float start, float end) {
        return Math.abs(end - start);
    }

    private float calcVectMovement(float start, float end) {
        return (calcDist(end, start)) * DisplayManager.getFramTimeSeconds();
    }

    public Vector3f calcMovementVect(Vector3f end) {
        return calcMovementVect(this.position, end);
    }

    public Vector3f calcMovementVect(Vector3f start, Vector3f end) {
        Vector3f result = start;
        if (start.x < end.x - 0.1) {
            result.x += calcVectMovement(result.x, end.x);
        } else if (start.x > end.x + 0.1) {
            result.x -= calcVectMovement(result.x, end.x);
        }
        if (start.y < end.y - 0.1) {
            result.y += calcVectMovement(result.y, end.y);
        } else if (start.y > end.y + 0.1) {
            result.y -= calcVectMovement(result.y, end.y);
        }
        if (start.z < end.z - 0.1) {
            result.z += calcVectMovement(result.z, end.z);
        } else if (start.z > end.z + 0.1) {
            result.z -= calcVectMovement(result.z, end.z);
        }
        return result;
    }

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public void move() {
        checkInputs();
        increaseRotation(0, currentTurnSpeed * DisplayManager.getFramTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFramTimeSeconds();
        float straffDist = straffSpeed * DisplayManager.getFramTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
        float dy = (float) (distance * Math.sin(Math.toRadians(pitch)));
        float sx = (float) (straffDist * Math.sin(Math.toRadians(getRotY() + 90)));
        float sz = (float) (straffDist * Math.cos(Math.toRadians(getRotY() + 90)));
//        increasePosition(xForce * DisplayManager.getFramTimeSeconds(), yForce * DisplayManager.getFramTimeSeconds(), zForce * DisplayManager.getFramTimeSeconds());
        increasePosition(dx, dy, dz);
        increasePosition(sx, 0, sz);
    }
    
    private float xTurnSpeed = 0;
    private float yTurnSpeed = 0;
    private float zTurnSpeed = 0;

    public void threeDMove() {
        checkInputs();
        increaseRotation(xTurnSpeed, yTurnSpeed, zTurnSpeed);
        float distance = currentSpeed * DisplayManager.getFramTimeSeconds();
        float straffDist = straffSpeed * DisplayManager.getFramTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
        float dy = (float) (distance * Math.sin(Math.toRadians(pitch)));
        float sx = (float) (straffDist * Math.sin(Math.toRadians(getRotY() + 90)));
        float sz = (float) (straffDist * Math.cos(Math.toRadians(getRotY() + 90)));
//        float sy = (float) (straffDist * Math.sin(Math.toRadians(pitch) + 90));
        increasePosition(dx, dy, dz);
        increasePosition(sx, 0, sz);
    }
    
    private void threeDRotate(){
        float rotX = Mouse.getDY() * 5;
        float rotY = Mouse.getDX() * 10;
        float rotZ = Mouse.getDY() * 5;
        Log.log(getRotY());
        this.yTurnSpeed = -rotY * 10 * DisplayManager.getFramTimeSeconds();
        this.xTurnSpeed = rotX * DisplayManager.getFramTimeSeconds();
        this.zTurnSpeed = rotZ * DisplayManager.getFramTimeSeconds();
    }

    public void travel(Vector3f f) {
        this.position = calcMovementVect(f);
    }

    public void gravity() {
        if (getPosition().y < TERRAIN_HEIGHT - 0.0) {
            yForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            yinertia += 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        } else if (getPosition().y > TERRAIN_HEIGHT + 0.0) {
            yForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            yinertia -= 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        } else if (getPosition().y < 3000 && getPosition().y > 1 && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//            yForce *= 0.5;
//            inertia *= 0.5;
//            getPosition().y = TERRAIN_HEIGHT;
        }
        if (getPosition().x < TERRAIN_HEIGHT - 0.0) {
            xForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            xinertia += 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        } else if (getPosition().x > TERRAIN_HEIGHT + 0.0) {
            xForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            xinertia -= 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        }else if(getPosition().x < 3000 && getPosition().x > 1) {
//            xForce *= 0.5;
//            inertia *= 0.5;
        }
        if (getPosition().z < TERRAIN_HEIGHT - 0.0) {
            zForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            zinertia += 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        } else if (getPosition().z > TERRAIN_HEIGHT + 0.0) {
            zForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            zinertia -= 0.5 * ((GRAVITY) * DisplayManager.getFramTimeSeconds());
        }else if(getPosition().z < 3000 && getPosition().z > 1) {
//            zForce *= 0.5;
//            inertia *= 0.5;
        }
        increasePosition(xForce * DisplayManager.getFramTimeSeconds(), yForce * DisplayManager.getFramTimeSeconds(), zForce * DisplayManager.getFramTimeSeconds());
        increasePosition(xinertia * DisplayManager.getFramTimeSeconds(), yinertia * DisplayManager.getFramTimeSeconds(),zinertia * DisplayManager.getFramTimeSeconds());
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W) || Mouse.isButtonDown(0) && Mouse.isButtonDown(1)) {
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.currentSpeed = -RUN_SPEED + 20;
        } else {
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_A) && Mouse.isButtonDown(1)) {
            this.straffSpeed = STRAFF_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_E) || Keyboard.isKeyDown(Keyboard.KEY_D) && Mouse.isButtonDown(1)) {
            this.straffSpeed = -STRAFF_SPEED;
        } else {
            this.straffSpeed = 0;
        }

        if (Mouse.isButtonDown(1)) {
            threeDRotate();
//            tiltAndTurn();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            this.yForce = JUMP_POWER;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            this.yForce = -JUMP_POWER;
//        } else {
//            this.yForce = 0;
        }
    }
    
    private void tiltAndTurn(){
        float angleChange = Mouse.getDX() * 5.0f;
        this.currentTurnSpeed = -angleChange * 10;
        float pitchChange = Mouse.getDY() * 0.5f;
        this.pitch += pitchChange;
    }

//    private void jump() {
//        this.yForce = JUMP_POWER;
//    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
