Ext.onReady(function() {
    PageGlobals = {
        headerPanel:  new CM.Header.Panel({  region: 'north'  }),
        contentPanel: new CM.Content.Panel({ region: 'center' }),
        statusOptions: Ext.create('Ext.data.Store', {
            fields: ['id', 'name'],
            data: [
                { 'id': '1', 'name': 'Open' },
                { 'id': '2', 'name': 'Response Only' },
                { 'id': '3', 'name': 'Closed' }
            ]
        })
    };

    PageGlobals.sidebarPanel = new CM.Sidebar.Panel({ region: 'west' });
    PageGlobals.contentPanel.add(new CM.Admin.User.List());
    PageGlobals.contentPanel.add(new CM.Admin.Course.List());

    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [
            PageGlobals.headerPanel,
            PageGlobals.sidebarPanel,
            PageGlobals.contentPanel
        ]
    });

    if (!Ext.firefoxVersion && !Ext.isChrome && !Ext.isIE) {
        Ext.MessageBox.alert('Unsupported Browser Detected', 'The browser detected is not officially supported by Course Master.');
    }
});
