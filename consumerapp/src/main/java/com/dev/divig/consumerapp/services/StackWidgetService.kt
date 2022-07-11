package com.dev.divig.consumerapp.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.dev.divig.consumerapp.adapter.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}