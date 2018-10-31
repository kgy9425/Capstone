package teamwoogie.woogie;

/**
 * Created by flatfisher on 1/10/2018 AD.
 */

import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Users implements IChatUser {
    private Integer id;
    private String name;
    private Bitmap icon;

    public Users(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return this.id.toString();
    }


    public String getName() {
        return this.name;
    }


    public Bitmap getIcon() {
        return this.icon;
    }


    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
