package entities.types;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author ko
 */
public class Sun extends Entity {   

    public Sun(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(Entity.SUN_MODEL, position, rotX, rotY, rotZ, scale);
        super.GRAVITY = 1000;
    }
    
}