Ext.define('CM.Discussion.Topic.Model', {
    extend: 'Ext.data.Model',
    fields: [ 'name', 'postedBy', 'postedOn', 'replies' ]
});

var topics = Ext.create('Ext.data.Store', {
    model: 'CM.Discussion.Topic.Model',
    data: [
        { name: '<a href="#topic=1">Topic 1</a>', postedBy: 'Ryan', postedOn: "2 days ago", replies: 0 },
        { name: '<a href="#topic=2">Topic 2</a>', postedBy: 'Rohit', postedOn: "5 days ago", replies: 2 }
    ]
});

var hashChanged = function(token) {
    if(token.indexOf("board") >= 0) {
        contentPanel.removeAll();
        contentPanel.add(
            Ext.create('Ext.grid.Panel', {
                border: false,
                store: topics,
                title: 'Discussion Board > Board 1',
                columns: [
                    {
                        text: 'Name',
                        dataIndex: 'name'
                    },
                    {
                        text: 'Posted By',
                        dataIndex: 'postedBy'
                    },
                    {
                        text: 'On',
                        dataIndex: 'postedOn'
                    },
                    {
                        text: 'Replies',
                        dataIndex: 'replies'
                    }
                ]
            })
        );
    } else if(token.indexOf("topic") >= 0) {
        contentPanel.removeAll();
        contentPanel.add(
            {
                html: '<div class="discussion-post">\
                            <div class="poster-info">\
                                <h1>Ryan Biwer</h1>\
                                <h2>Posted On: 3:14pm, 3/22/12</h2>\
                            </div>\
                            <div class="post-content">\
                                Testing 1 2 3\
                            </div>\
                        </div>\
                        <div class="discussion-post">\
                            <div class="poster-info">\
                                <h1>Rohit Dhiman</h1>\
                                <h2>Posted On: 5:29pm, 3/22/12</h2>\
                            </div>\
                            <div class="post-content">\
                                cool cool cool\
                            </div>\
                        </div><button>Reply</button>'
            }
        );
    } else {
        contentPanel.removeAll();
        contentPanel.add(
            
        );
    }
};
});
