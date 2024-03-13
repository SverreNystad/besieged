package com.softwarearchitecture.ECS.Components;

public class InteractableComponent {
    public float screen_u;
    public float screen_v;
    public float u_size;
    public float v_size;
    public float z_index;

    public InteractableComponent(float screen_u, float screen_v, float width, float height) {
        this.screen_u = screen_u;
        this.screen_v = screen_v;
        this.u_size = width;
        this.v_size = height;
    }
}
