package com.dicoding.storyapp.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.di.Injection
import com.dicoding.storyapp.data.response.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory,
    CoroutineScope {

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    private var mWidgetItems = listOf<Story>()
    private val storyRepository = Injection.provideRepository(mContext)

    override fun onCreate() {
        launch {
            storyRepository.getToken().collect{
                token ->
                if (token != null) {
                    val stories = storyRepository.getStoryWidget().listStory
                    updateWidgetItems(stories)
                }
            }

        }
    }

    override fun onDataSetChanged() {
    }

    private fun updateWidgetItems(newItems: List<Story>) {
        mWidgetItems = newItems
        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(
            AppWidgetManager.getInstance(mContext).getAppWidgetIds(
                ComponentName(mContext, ImageBannerWidget::class.java)
            ), R.id.stack_view
        )
    }

    override fun onDestroy() {
        parentJob.cancel()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)


        mWidgetItems.forEach{
            data ->
            val imageUrl = data.photoUrl

            Glide.with(mContext)
                .asBitmap()
                .load(imageUrl)
                .override(400, 400)
                .centerCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        rv.setImageViewBitmap(R.id.imageView, resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

        }

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}