package com.softwarearchitecture.game_client.states;

import com.softwarearchitecture.ecs.components.ButtonComponent.TypeEnum;

public interface Observer {

    /**
     * onAction event that is called when a button is clicked.
     * 
     * @param type: TypeEnum
     */
    public void onAction(TypeEnum type);

}
