Ext.onReady(function() {
    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [
            new CM.HeaderPanel({ region: 'north' }),
            new CM.DetailPanel({ region: 'center' })
        ]
    });
});
