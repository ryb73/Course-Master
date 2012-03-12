Ext.onReady(function() {
    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [{
            region: 'north',
            height: 50,
            border: false,
            layout: 'hbox',
            items: [{
                bodyCls: 'page-header title',
                flex: 1,
                html: 'Course Master'
            }, {
                bodyCls: 'page-header',
                flex: 1,
                style: 'text-align:right;',
                html: '<div>Logged in as: ' + user.id + '</div>'
            }]
        },{
            region: 'center',
            border: false,
            html : '<div>Hello ' + user.id + '! Welcome to Ext JS.</div>'
        }]
    });
});
