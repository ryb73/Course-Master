Ext.onReady(function() {
    PageGlobals = {
        headerPanel:  new CM.HeaderPanel({  region: 'north'  }),
        contentPanel: new CM.ContentPanel({ region: 'center' })
    };

    PageGlobals.sidebarPanel = new CM.SidebarPanel({ region: 'west'   });

    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [
            PageGlobals.headerPanel,
            PageGlobals.sidebarPanel,
            PageGlobals.contentPanel
        ]
    });
});
