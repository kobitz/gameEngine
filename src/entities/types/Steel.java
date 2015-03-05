package entities.types;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author ko
 */
public class Steel extends Entity {
    
    public Steel(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(Entity.STEEL_MODEL, position, rotX, rotY, rotZ, scale);
        super.GRAVITY = 80;
    }
    
}
