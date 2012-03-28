Ext.onReady(function() {
    PageGlobals = {
        headerPanel:  new CM.HeaderPanel({  region: 'north'  }),
        sidebarPanel: new CM.SidebarPanel({ region: 'west'   }),
        contentPanel: new CM.ContentPanel({ region: 'center' })
    }

    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [
            PageGlobals.headerPanel,
            PageGlobals.sidebarPanel,
            PageGlobals.contentPanel
        ]
    });
});
