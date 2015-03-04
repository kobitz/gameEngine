package res;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import res.obj.ResOBJ;
import res.tex.ResTEX;

/**
 *
 * @author ko
 */
public class Resources {

    public static final InputStream getObj(String name) {
        return ResOBJ.class.getResourceAsStream(name + ".obj");
    }

    public static final Texture getTex(String name) throws IOException {
        return TextureLoader.getTexture("PNG", ResTEX.class.getResourceAsStream(name + ".png"));
    }

}
