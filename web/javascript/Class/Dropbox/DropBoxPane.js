Ext.define("CM.Dropbox.Pane", {
    extend: "Ext.panel.Panel",
	
	
    initComponent: function() {
//    Ext.Loader.setConfig({enabled:true});
    
        var dropboxes = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'name', 'ext', 'start', 'end', 'disp' ],
            proxy: {
                type: 'ajax',
                url: '/service/get-dropbox',
                extraParams: {
                    userId: SessionGlobals.id,
                    courseId: this.courseId
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true
        });
      
		var dpanel = Ext.create('Ext.grid.Panel', {
			border: true,
			store: dropboxes,
				id: this.class + '-dropbox-left',
				title: this.class + ' Open Dropboxes',
			flex: 2,
			listeners: {
				itemclick : function(record) {
			  
					var data = dpanel.getSelectionModel().selected.items[0].data.id;
					console.log(data);
					files.clearFilter();
					files.filter('folder', data);
					files.load();
					Ext.apply(spanel, {
						fid: data
					});
				}
			},
      
		columns: {
			items: [{
				text: 'ID',
				//          width: 25,
				flex:1,
				dataIndex: 'id' 
			}, {
				text: 'Name',
				//          width: 800,
				flex: 1,
				dataIndex: 'name'
			}, {
			    text: 'Description',
			    width: 800,
			    flex: 2,
			    dataIndex: 'ext' 
			}],
			defaults: {
				draggable: false,
				resizable: false,
				hideable: false,
				sortable: true
			}
		}

    });
    
    var files = Ext.create('Ext.data.Store', {
		fields: [ 'id', 'folder', 'path', 'name', 'owner', 'dte' ],
		proxy: {
			type: 'ajax',
			url: '/service/get-files',
			extraParams: {
				userId: SessionGlobals.id,
				courseId: this.courseId
			},
			reader: {
				type: 'json',
				root: 'data'
			}
		},
		autoLoad: true
	});    
    
    var rpanel = Ext.create('Ext.grid.Panel', {
		border: true,
		store: files,
		id: this.class + '-dropbox-right',
		//            title: this.class + ' DropboxRight',
		title: 'Submissions',
		// listeners: {
		// select: this.onSelect,
		// itemdblclick: this.onItemDblClick
		// },
		//      width: 800
		flex : 2,
		columns: {
			items: [{
				text: 'ID',
				width: 25,
				flex:1,
				dataIndex: 'id' 
			}, {
				text: 'Name',
				width: 800,
				flex: 2,
				dataIndex: 'name'
			}, {
				text: 'Path',
				width: 800,
				flex: 1,
				dataIndex: 'path' 
			}, {
				text: 'Owner',
				width: 800,
				flex: 1,
				dataIndex: 'owner' 
			}, {
				text: 'Date',
				width: 800,
				flex: 1,
				dataIndex: 'dte'           
			}],
			defaults: {
				draggable: false,
				resizable: false,
				hideable: false,
				sortable: true
			}
		}
      
    });
    
    var spanel = Ext.create('Ext.panel.Panel', {
		fid : this.fid,
		border: true,
		id: this.class + '-dropbox-rights',
		title: 'Actions',
		layout: 'fit',
		flex : 2,
		items: {  
			xtype: 'form',
			url: '/service/submit-file',  //TODO need name,course,descr
			// layout: {
			// type: 'vbox',
			// align : 'center',
			// pack  : 'center',
			// },
			fieldDefaults: {
			  labelWidth: 55
			},
			bodyPadding: 5,
			items: [{
				xtype: 'textfield',
				name: 'name',
				anchor: '100%',
				fieldLabel: 'Name',
				allowBlank: false
			},{
				xtype: 'filefield',
				name: 'file1',
				anchor: '100%',
				fieldLabel: 'File'
			},{ 
				xtype: 'textarea',
				name: 'info',
				anchor: '100% 10%',
				fieldLabel: 'Details',
				//          height: 160,
				allowBlank: false
			},{

				xtype: 'hidden',
				name: 'owner',
				value: SessionGlobals.id  //USERID
			},{

				xtype: 'hidden',
				name: 'folder',
//				value: this.dpanel.getSelectionModel().selected.items[0].data.id
				value: this.fid
			},{
				xtype: 'hidden',
				name: 'course',
	//            value: this.ownerCT.courseId
				value: this.courseId
			}],
			buttons: [{
				text: 'Upload',
				formBind: true,
				disabled: false,
				handler: function() {
					var form = this.up('form').getForm();
					if(form.isValid()){
						form.submit({
							url: '/service/submit-file',
							waitMsg: 'Uploading your file...',
							success: function(fp, o) {
							  Ext.Msg.alert('Success', 'Your file has been uploaded.');
							},
							failure: function() { Ext.Msg.alert("Error","Unable to upload file"); }
						});
					}
				}
			}]

		}
    });    
    
    //FOR PROF
	if(SessionGlobals.role == 2) {
		Ext.apply(this, {
			items : [
			  dpanel,
			  rpanel
			],
			tbar: {
				xtype: 'toolbar',
				items: [{
					//              xtype: 'button',
					text: 'Add',
					listeners: { click: this.addBoard }
				}]
			}
      });
    };
    //FOR STUDENT
    if(SessionGlobals.role == 1) {
		Ext.apply(this, {
			items : [
				dpanel,
				spanel
			]
		});
    };  

	Ext.apply(this, {
		layout: {
			type: 'hbox',
			pack: 'start',
			align: 'stretch'
		},  
		// items : [
		// dpanel,
		// rpanel
		// ],
		border: false,
		id: this.class + '-dropbox-root',
		title: this.class + ' Dropbox',
   });
   this.callParent(arguments);
},
  
onSelect: function(rowModel, record) {
    console.log("Select fired: " + record.get("id"));
},
  
addBoard: function() {
	Ext.create('widget.window', {
		title: 'Add Dropbox',
		closable: true,
		width: 300,
		height: 270,
		layout: 'fit',
		items: {  
			xtype: 'form',
			url: '/service/create-dropbox',  //TODO need name,course,descr
			// layout: {
			// type: 'vbox',
			// align : 'center',
			// pack  : 'center',
			// },
			fieldDefaults: {
				labelWidth: 55
			},
			bodyPadding: 5,
			items: [{
				xtype: 'textfield',
				name: 'name',
				anchor: '100%',
				fieldLabel: 'Name',
				allowBlank: false
			},{ 				xtype: 'textarea',  				name: 'ext',				anchor: '100% -20',				fieldLabel: 'Info',				//          height: 160,				allowBlank: false			},{				xtype: 'hidden',				name: 'course',				//            value: this.ownerCT.courseId				value: this.courseId 			}],			buttons: [{				text: 'Create Dropbox',				formBind: true,				disabled: true,				handler: function() {					var form = this.up('form').getForm();					if(form.isValid()) {						form.submit({							success: function() { Ext.Msg.alert("Success","Dropbox has been added."); },							failure: function() { Ext.Msg.alert("Error","Unable to add dropbox."); }						});					}				}			}]		}	}).show();  }});