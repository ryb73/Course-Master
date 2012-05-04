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
                    encode: true
                })
            },
            autoLoad: true
        });

        var calStore = new Extensible.calendar.data.MemoryCalendarStore({
            proxy: {
                type: 'ajax',
                url : '/service/courses',
                extraParams: {
                    userId: SessionGlobals.id
                },
                autoLoad: true,
                reader: {
                    type: 'json',
                    root: 'data'
                }
            }
        });

        Ext.apply(this, {
            title: 'Dashboard',
            border: false,
            id: "Dashboard-panel",
            eventStore: eventStore,
            calendarStore: calStore
        });

        this.callParent(arguments);
    }
});