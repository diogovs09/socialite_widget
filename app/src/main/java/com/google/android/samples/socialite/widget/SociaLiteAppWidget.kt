package com.google.android.samples.socialite.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import com.google.android.samples.socialite.R
import androidx.glance.text.TextStyle
import com.google.android.samples.socialite.widget.model.WidgetModelRepository
import com.google.android.samples.socialite.widget.model.WidgetModel
import com.google.android.samples.socialite.widget.model.WidgetState.Loading
import androidx.compose.runtime.collectAsState
import com.google.android.samples.socialite.widget.ui.FavoriteContact
import androidx.glance.appwidget.action.actionStartActivity
import android.content.Intent
import com.google.android.samples.socialite.MainActivity
import androidx.core.net.toUri
import androidx.glance.LocalContext
import com.google.android.samples.socialite.widget.ui.ZeroState


class SociaLiteAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val repository = WidgetModelRepository.get(context)

        provideContent {
            GlanceTheme {
                Content(repository, widgetId)
            }
        }
    }

    @Composable
    private fun Content(repository: WidgetModelRepository, widgetId: Int) {
        val model = repository.loadModel(widgetId).collectAsState(Loading).value
        when (model) {
            is WidgetModel -> FavoriteContact(model = model, onClick = actionStartActivity(
                Intent(LocalContext.current.applicationContext, MainActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setData("https://socialite.google.com/chat/${model.contactId}".toUri()))
            )
            else -> ZeroState(widgetId)
        }
    }
}
