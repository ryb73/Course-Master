Ext.define("CM.Discussion.Topic", {
    extend: 'Ext.panel.Panel',

    initComponent: function() {

        var posts = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'owner', 'ownerName', 'dte', 'content' ],
            proxy: {
                type: 'ajax',
                url: '/service/discussion/get-topic',
                extraParams: {
                    topicId: this.topicId
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            }
        });

        Ext.apply(this, {
            border: false,
            id: this.class + '-topic' + this.topicId,
            title: this.class + ' Discussion Board > ' + this.boardName + ' > ' + this.topicName,
            layout: {
                type: 'vbox',
                align: 'stretch'
            }
        });

        posts.load({
            scope: this,
            callback: function(records, operation, success) {
                if(!success) {
                    Ext.Msg.alert("Error", "Unable to get topic content. Try refreshing the page.");
                    alert(this.class);
                    PageGlobals.contentPanel.getLayout().setActiveItem(this.class + '-thread-list');
                } else {
                    var postCount = posts.getCount();
                    var postItems = new Array(postCount);
                    for(var i = 0; i < postCount; ++i) {
                        var record = posts.getAt(i);
                        postItems[i] = {
                            xtype: 'panel',
                            title: 'Posted by ' + record.get("ownerName") + ' at ' + record.get("dte"),
                            padding: 10,
                            items: {
                                border: false,
                                padding: 10,
                                html: record.get("content")
                            },
                        };

                        if(record.get("owner") == SessionGlobals.id || SessionGlobals.role == 2) {
                            Ext.apply(postItems[i], {
                                bbar: [{ xtype: 'button', text: 'Delete' }]
                            });
                        }
                    }

                    this.add(postItems);
                    this.add({
                        xtype: 'form',
                        title: 'Post Reply',
                        padding: 10,
                        layout: 'fit',
                        items: {
                            xtype: 'textarea',
                            name: 'message',
                            padding: 5,
                            grow: true,
                            maxLength: 1024,
                            enforceMaxLength: true
                        },
                        bbar: [{ xtype: 'button', text: 'Post' }]
                    });
                }
            }
        });

        this.callParent(arguments);
    }
});