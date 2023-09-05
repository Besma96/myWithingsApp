package com.example.mywithingsapp.ui.animation

import android.animation.Animator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.mywithingsapp.R
import com.example.mywithingsapp.commons.utils.Constants
import com.example.mywithingsapp.databinding.ActivityAnimationBinding
import com.example.mywithingsapp.domain.entities.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class AnimationImageActivity : AppCompatActivity() {

    private var listToAnimate: MutableList<Image> = mutableListOf()
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    private var imageList: ArrayList<Drawable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAnimationBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_animation
        )

        if (intent != null && intent.hasExtra(Constants.EXTRA_IMAGE_ANIMATION)) {
            listToAnimate = intent.getParcelableArrayListExtra(Constants.EXTRA_IMAGE_ANIMATION)!!
            binding.callback = this
        }

        viewPager = findViewById(R.id.idViewPager)

        convertUrlListToDrawableList()

        while (listToAnimate.size != imageList.size)

            viewPagerAdapter = ViewPagerAdapter(this@AnimationImageActivity, imageList)
            viewPager.adapter = viewPagerAdapter

    }

    //actually not working at all
    private fun animateListImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val circularConceal = ViewAnimationUtils.createCircularReveal(
                viewPager[viewPager.currentItem],
                (viewPager[viewPager.currentItem]?.left?.let {
                    viewPager[viewPager.currentItem]?.right?.plus(
                        it
                    )
                })?.div(2) ?: 0,
                (viewPager[viewPager.currentItem]?.bottom?.let {
                    viewPager[viewPager.currentItem]?.top?.plus(
                        it
                    )
                })?.div(2) ?: 1,
                viewPager[viewPager.currentItem]?.width?.toFloat() ?: 0F, 0f
            )

            circularConceal.duration = 500
            circularConceal.start()
            circularConceal.addListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(p0: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(p0: Animator) {
                    val iteration = viewPager.currentItem
                    if (iteration < listToAnimate.size) {
                        viewPager.currentItem = viewPager.currentItem + 1
                    } else {
                        viewPager.currentItem = 0
                    }
                    viewPagerAdapter.notifyDataSetChanged()

                }

                override fun onAnimationCancel(p0: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator) {

                }
            })
        }
    }

    private fun convertUrlListToDrawableList() {
        listToAnimate.forEach { hitsList ->
            drawableFromUrl(hitsList.largeImageURL)
        }
    }

    @Throws(IOException::class)
    fun drawableFromUrl(url: String?) {
        val scope = CoroutineScope(Dispatchers.Default)
        // Launch a new coroutine in the scope
        scope.launch {
            val x: Bitmap
            val connection = withContext(Dispatchers.IO) {
                URL(url).openConnection()
            } as HttpURLConnection
            withContext(Dispatchers.IO) {
                connection.connect()
            }
            val input = connection.inputStream
            x = BitmapFactory.decodeStream(input)
            imageList.add(BitmapDrawable(Resources.getSystem(), x))
        }
    }

    fun moveToPreviousScreen() = finish()
}



