package com.dev.divig.githubuserapp.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.dev.divig.githubuserapp.adapter.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}