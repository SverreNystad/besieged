package com.softwarearchitecture.game_client.states;

import java.util.UUID;

import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;
import com.softwarearchitecture.game_client.Controllers;

public class Tutorial extends State implements Observer  {

    protected Tutorial(Controllers defaultControllers, UUID yourId) {
        super(defaultControllers, yourId);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void onAction(ButtonEnum type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onAction'");
    }

    @Override
    protected void activate() {

        // 
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activate'");
    }
    
}
