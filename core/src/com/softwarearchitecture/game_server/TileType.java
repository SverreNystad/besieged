package com.softwarearchitecture.game_server;

import java.io.Serializable;

public enum TileType implements Serializable {
    START,
    END_TOP,
    END,
    END_BOTTOM,
    PATH,
    PLACEABLE,
    BLOCKED_WATER,
    BLOCKED_OBSTRUCTABLE,
    BLOCKED_TREE
}
