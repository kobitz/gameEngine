package entities;

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

    public static TexturedModel IRON_MODEL, STEEL_MODEL, DIRT_MODEL;
    

    public static void setIronModel(TexturedModel model) {
        IRON_MODEL = model;
    }

    public static void setSteelModel(TexturedModel model) {
        STEEL_MODEL = model;
    }

    public static void setDirtModel(TexturedModel model) {
        DIRT_MODEL = model;
    }

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    protected float GRAVITY = -50;
    private final float TERRAIN_HEIGHT = 0;
    private static final float RUN_SPEED = 50;
    private static final float TURN_SPEED = 160;
    private static final float JUMP_POWER = 40;
    
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;

    float upForce = 0;
    float doubleUpForce = 0;

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

    public void gravity() {
        gravity(0);
    }
    
    public void move(){
        checkInputs();
        increaseRotation(0, currentTurnSpeed * DisplayManager.getFramTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFramTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
        increasePosition(dx, 0, dz);
    }

    public void gravity(int additionalForce) {
        if(additionalForce > 0){
            additionalForce *= -1;
        }
        upForce += (GRAVITY + additionalForce) * DisplayManager.getFramTimeSeconds();
        doubleUpForce += (GRAVITY + additionalForce) * DisplayManager.getFramTimeSeconds();
        increasePosition(0, upForce * DisplayManager.getFramTimeSeconds(), 0);
        increasePosition(0, doubleUpForce * DisplayManager.getFramTimeSeconds(), 0);
        if (getPosition().y < TERRAIN_HEIGHT) {
            upForce = 0;
            doubleUpForce = 0;
            getPosition().y = TERRAIN_HEIGHT;
        }
    }
    
    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W) || Mouse.isButtonDown(0) && Mouse.isButtonDown(1)){
            this.currentSpeed = RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        }else{
            this.currentSpeed = 0;
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
        this.upForce = JUMP_POWER;
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
