Ext.define("CM.Discussion.CreateTopic", {
    extend: 'Ext.form.Panel',

    initComponent: function() {
        if(SessionGlobals.role == 2 /* Professor */) {
            this.hasDateRange = true;
            this.openRange = Ext.create('Extensible.form.field.DateRange', {
                fieldLabel: 'Date range',
                name: 'open-range',
                singleLine: true,
                anchor: '100%'
            });
        } else {
            this.hasDateRange = false;
            this.openRange = {
                xtype: 'hidden',
                name: 'dummy',
                value: 'i don\'t care'
            };
        }

        if(SessionGlobals.role == 2 /* Professor */) {
            this.hasDateRange = true;
            this.openRange = Ext.create('Extensible.form.field.DateRange', {
                fieldLabel: 'Date range',
                name: 'open-range',
                singleLine: true,
                anchor: '100%'
            });
        } else {
            this.hasDateRange = false;
            this.openRange = {
                xtype: 'hidden',
                name: 'dummy',
                value: 'i don\'t care'
            };
        }

        Ext.apply(this, {
            border: false,
            id: 'create-topic',
            title: 'Create Topic',
            layout: 'fit',
            items: {
                xtype: 'form',
                url: '/service/discussion/post-topic',
                border: false,
                bodyPadding: 5,
                instance: this,
                // layout: {
                    // type: 'table',
                    // columns: 4
                // },
                fieldDefaults: {
                    //labelAlign: 'left',
                    //labelWidth: 90
                },
                items: [{
                    xtype: 'textfield',
                    name: 'topic-name',
                    fieldLabel: 'Topic Name',
                    anchor: '100%',
                    allowBlank: false
                },
                this.openRange,
                {
                    xtype: 'hidden',
                    name: 'start-date'
                }, {
                    xtype: 'hidden',
                    name: 'end-date'
                }, {
                    xtype: 'textarea',
                    name: 'message',
                    fieldLabel: 'Message',
                    anchor: '100% 90%',
                    maxLength: 1024,
                    enforceMaxLength: true,
                    allowBlank: false
                },{
                    xtype: 'hidden',
                    name: 'board',
                    value: this.boardId
                }],
                tbar: [{
                    text: 'Post',
                    listeners: { click: this.postTopic }
                }, {
                    text: 'Cancel',
                    listeners: { click: this.cancel }
                }]
            }
        });

        this.callParent(arguments);
    },

    dateToString: function(date) {
        if(!date) return null;
        var str = date.getFullYear().toString() + '-' +
                  (date.getMonth() + 1).toString() + '-' +
                  date.getDate().toString() + ' ' +
                  date.getHours().toString() + ':' +
                  date.getMinutes().toString() + ':' +
                  date.getSeconds().toString();
        return str;
    },

    postTopic: function() {
        var form = this.up('form');
        if(form.getForm().isValid()) {
            if(form.instance.hasDateRange) {
                var dates = form.instance.openRange.getValue();
                form.getForm().findField("start-date").setValue(form.instance.dateToString(dates[0]));
                form.getForm().findField("end-date").setValue(form.instance.dateToString(dates[1]));
            }

            form.submit({
                success: function(f, action) {
                    PageGlobals.contentPanel.add(new CM.Discussion.Topic({ class: form.instance.class, courseId: form.instance.courseId,
                        boardId: form.instance.boardId, boardName: form.instance.boardName, topicId: action.result.topicId,
                        topicName: form.getForm().findField("topic-name").getValue(), newPost: false }));
                    PageGlobals.contentPanel.getLayout().setActiveItem(form.instance.class + "-topic" + action.result.topicId);
                },
                failure: function() { Ext.Msg.alert("Error","Unable to create topic. Try refreshing the page."); }
            });
        }
    },

    cancel: function() {
        var instance = this.up('form').instance;
        PageGlobals.contentPanel.getLayout().setActiveItem(instance.class + "-thread-list");
        PageGlobals.contentPanel.remove(instance);
    }
});