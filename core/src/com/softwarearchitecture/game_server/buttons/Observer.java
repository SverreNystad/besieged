package com.softwarearchitecture.game_server.buttons;

public interface Observer {

    /**
     * onAction event that is called when a button is clicked.
     * 
     * @param type: TypeEnum
     */
    public void onAction(TypeEnum type);

}
