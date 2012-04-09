Ext.onReady(function() {
    PageGlobals = {
        headerPanel:  new CM.Header.Panel({  region: 'north'  }),
        contentPanel: new CM.Content.Panel({ region: 'center' })
    };

    PageGlobals.sidebarPanel = new CM.Sidebar.Panel({ region: 'west' });

    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [
            PageGlobals.headerPanel,
            PageGlobals.sidebarPanel,
            PageGlobals.contentPanel
        ]
    });
});
