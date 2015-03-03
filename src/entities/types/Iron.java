package entities.types;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author ko
 */
public class Iron extends Entity {    
    
    public Iron(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(Entity.IRON_MODEL, position, rotX, rotY, rotZ, scale);
        super.GRAVITY = -2;
    }
    
}
