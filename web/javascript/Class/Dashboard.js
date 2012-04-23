Ext.define("CM.Class.Dashboard", {
    extend: "Extensible.calendar.CalendarPanel",

    initComponent: function() {
        var eventStore = new Extensible.calendar.data.MemoryEventStore({
            proxy: {
                type: 'ajax',
                url: '/service/events',
                api: {
                    create  : 'events/create',
                    read    : 'events/all',
                    update  : 'events/update',
                    destroy : 'events/destroy'
                },
                reader: {
                    type: 'json',
                    root: 'data'
                },
                writer: new Ext.data.JsonWriter({
                    root: 'data',
                    encode: true,
                    writeAllFields: true
                })
            },
            autoLoad: true
        });

        Ext.apply(this, {
            title: 'Dashboard',
            border: false,
            id: "dashboard-panel",
            eventStore: eventStore
        });

        this.callParent(arguments);
    }
});