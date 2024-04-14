package com.softwarearchitecture.game_client.states;

import com.softwarearchitecture.ecs.components.ButtonComponent.ButtonEnum;

public interface Observer {

    /**
     * onAction event that is called when a button is clicked.
     * 
     * @param type: ButtonEnum
     */
    public void onAction(ButtonEnum type);

}
