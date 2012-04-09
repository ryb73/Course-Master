Ext.Loader.setConfig({
    enabled: true,
    disableCaching: false,
    paths: {
        "Extensible.example": "/3rdparty/extensible/examples"
    }
});
Ext.require([
    'Extensible.calendar.data.MemoryEventStore'
]);

Ext.define("CM.Class.Dashboard", {
    extend: "Ext.panel.Panel",

    initComponent: function() {
        var eventStore = Ext.create('Extensible.calendar.data.MemoryEventStore', {
            data: Ext.create('Extensible.example.calendar.data.Events')
        });

        Ext.apply(this, {
            border: false,
            id: "Dashboard-class",
            title: "Dashboard",
            items: [
                Ext.create('Extensible.calendar.CalendarPanel', {
                    eventStore: eventStore,
                    title: 'My Calendar',
                    width: '100%',
                    height: 600
                })
            ]
        });

        this.callParent(arguments);
    }
});