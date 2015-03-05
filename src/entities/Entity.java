package entities;

import com.bulletphysics.collision.broadphase.AxisSweep3;
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

    public static TexturedModel IRON_MODEL, STEEL_MODEL, DIRT_MODEL, CARBON_MODEL;
    

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

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    protected float GRAVITY = 100;
    private final float TERRAIN_HEIGHT = 1500;
    private static final float RUN_SPEED = 50;
    private static final float STRAFF_SPEED = 30;
    private static final float TURN_SPEED = 160;
    private static final float JUMP_POWER = 40;
    
    private float currentSpeed = 0;
    private float straffSpeed = 0;
    private float currentTurnSpeed = 0;
    
    private float[][] locations;

    float yForce = 0;
    float doubleYForce = 0;
    float xForce = 0;
    float doubleXForce = 0;
    float zForce = 0;
    float doubleZForce = 0;

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
    
    public void move(){
        checkInputs();
        increaseRotation(0, currentTurnSpeed * DisplayManager.getFramTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFramTimeSeconds();
        float straffDist = straffSpeed * DisplayManager.getFramTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
        float sx = (float) (straffDist * Math.sin(Math.toRadians(getRotY() + 90)));
        float sz = (float) (straffDist * Math.cos(Math.toRadians(getRotY() + 90)));
        increasePosition(dx, 0, dz);
        increasePosition(sx, 0, sz);
    }

    public void gravity() {
        if (getPosition().y < TERRAIN_HEIGHT - 100) {
            yForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleYForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
        }else if(getPosition().y > TERRAIN_HEIGHT + 100) {
            yForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleYForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
        }else if(getPosition().y < 3000 && getPosition().y > 1 && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            yForce *= 0.99;
            doubleYForce *= 0.99;
//            getPosition().y = TERRAIN_HEIGHT;
        }
        if (getPosition().x < 1400) {
            xForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleXForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
        } else if (getPosition().x > 1600) {
            xForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleXForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
        }else if(getPosition().x < 3000 && getPosition().x > 1) {
            xForce *= 0.99;
            doubleXForce *= 0.99;
        }
        if (getPosition().z < 1400) {
            zForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleZForce += (GRAVITY) * DisplayManager.getFramTimeSeconds();
        } else if (getPosition().z > 1600) {
            zForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
            doubleZForce -= (GRAVITY) * DisplayManager.getFramTimeSeconds();
        }else if(getPosition().z < 3000 && getPosition().z > 1) {
            zForce *= 0.99;
            doubleZForce *= 0.99;
        }
        increasePosition(xForce * DisplayManager.getFramTimeSeconds(), yForce * DisplayManager.getFramTimeSeconds(), zForce * DisplayManager.getFramTimeSeconds());
        increasePosition(doubleXForce * DisplayManager.getFramTimeSeconds(), doubleYForce * DisplayManager.getFramTimeSeconds(), doubleZForce * DisplayManager.getFramTimeSeconds());
    }
    
    private void collisionDetection(){
        
    }
    
    private void collisionMove(){
        float dx = 0;
        float dy = 0;
        float dz = 0;
        increasePosition(dx, dy, dz);
    }
    
    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W) || Mouse.isButtonDown(0) && Mouse.isButtonDown(1)){
            this.currentSpeed = RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED + 20;
        }else{
            this.currentSpeed = 0;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_A) && Mouse.isButtonDown(1)){
            this.straffSpeed = STRAFF_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_E) || Keyboard.isKeyDown(Keyboard.KEY_D) && Mouse.isButtonDown(1)){
            this.straffSpeed = -STRAFF_SPEED;
        }else{
            this.straffSpeed = 0;
        }
        
        if(Mouse.isButtonDown(1) || Mouse.isButtonDown(1) && Mouse.isButtonDown(0)){
            float angleChange = Mouse.getDX() * 5.0f;
            this.currentTurnSpeed = -angleChange * 10;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        }else{
            this.currentTurnSpeed = 0;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }
    
    private void jump(){
        this.yForce = JUMP_POWER;
    }

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
