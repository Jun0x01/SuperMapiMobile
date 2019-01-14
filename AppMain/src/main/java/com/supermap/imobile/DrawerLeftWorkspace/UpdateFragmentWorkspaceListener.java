package com.supermap.imobile.DrawerLeftWorkspace;

import com.supermap.data.Datasource;
import com.supermap.data.Workspace;

/**
 * Created by Jun on 2017/8/3.
 */

public interface UpdateFragmentWorkspaceListener {


    void onCloseWorkspace();

    // Workspace
    void onNewWorkspace(Workspace workspace);

    // New or open a datasource
    void onAddDatasource(Datasource datasource);

    void onCloseDatasource(String name);

    void onUpdateDatasource(Datasource datasource);

    // Map
    void onSaveNewMap(String name);

    void onRemoveMap(String name);
}
