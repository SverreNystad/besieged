package com.softwarearchitecture.ECS.Components;

public class DrawableComponent {
    public String texture_path;
    public float screen_u;
    public float screen_v;
    public float u_size;
    public float v_size;
    public int z_index;

    public DrawableComponent(String texture_path, float screen_u, float screen_v, float width, float height) {
        this.texture_path = texture_path;
        this.screen_u = screen_u;
        this.screen_v = screen_v;
        this.u_size = width;
        this.v_size = height;
    }
}
