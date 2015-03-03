package entities.types;

import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author ko
 */
public class Dirt extends Entity {   

    public Dirt(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(Entity.DIRT_MODEL, position, rotX, rotY, rotZ, scale);
        super.GRAVITY = -1;
    }
    
}
