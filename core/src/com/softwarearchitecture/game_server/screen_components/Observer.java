package com.softwarearchitecture.game_server.screen_components;

public interface Observer {

    /**
     * onAction event that is called when a button is clicked.
     * 
     * @param type: TypeEnum
     */
    public void onAction(TypeEnum type);

}
