package com.softwarearchitecture.ecs.components;

import java.io.Serializable;

import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;
import com.softwarearchitecture.math.Vector2;

public class ButtonComponent implements Serializable {
    public Vector2 uv_offset;
    public Vector2 uv_size;
    public TypeEnum type;
    public int z_index;
    public Runnable callback;

    public enum TypeEnum {
        OPTIONS,
        GAME_MENU,
        QUIT,
        JOIN,
        HOST,
        PAUSE,
        MULTI_PLAYER,
        SINGLE_PLAYER,
        PLAY,
        BACK
    }

    public ButtonComponent(Vector2 uv_offset, Vector2 uv_size, TypeEnum type, int z_index, Runnable callback) {

        this.uv_offset = uv_offset;
        this.uv_size = uv_size;
        this.type = type;
        this.z_index = z_index;
        this.callback = callback;

        this.uv_size.x = uv_size.x * 100;
        this.uv_size.y = uv_size.y * 100;
        System.out.println(this.toString());
    }

    public String toString() {
        return "ButtonComponent{" + "uv_offset=" + uv_offset + ", uv_size=" + uv_size + ", type=" + type + ", z_index="
                + z_index + ", callback=" + callback + '}';
    }

    public void triggerAction() {
        if (callback != null) {
            callback.run();
        }
    }

}