package entities.types;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author ko
 */
public class Carbon extends Entity {   

    public Carbon(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(Entity.CARBON_MODEL, position, rotX, rotY, rotZ, scale);
        super.GRAVITY = 90;
    }
    
}
