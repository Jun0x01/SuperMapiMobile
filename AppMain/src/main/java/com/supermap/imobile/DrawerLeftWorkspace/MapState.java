package com.supermap.imobile.DrawerLeftWorkspace;

/**
 * Created by Jun on 2017/5/23.
 */

public class MapState {

    public volatile static boolean isMapOpen = false;

    public volatile static boolean isNewMap = true;

    public volatile static String mapName = null;

    public volatile static boolean isWorkspaceOpen = false;

    public volatile static long mapLoadStartTime = 0;
}
