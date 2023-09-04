package com.example.mywithingsapp.ui.search

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.test.espresso.idling.CountingIdlingResource
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mywithingsapp.R
import com.example.mywithingsapp.commons.communicator.HitsItem
import com.example.mywithingsapp.commons.utils.Constants
import com.example.mywithingsapp.commons.utils.Constants.Companion.EXTRA_QUERY
import com.example.mywithingsapp.commons.utils.Constants.Companion.FRUITS_QUERY_TAG
import com.example.mywithingsapp.commons.utils.UiHelper
import com.example.mywithingsapp.datasource.api.NetworkState
import com.example.mywithingsapp.domain.entities.Image
import com.example.mywithingsapp.ui.animation.AnimationImageActivity
import com.example.mywithingsapp.ui.details.ImageDetailsActivity
import kotlinx.android.synthetic.main.fragment_search.main_rootView
import kotlinx.android.synthetic.main.fragment_search.progress_bar
import kotlinx.android.synthetic.main.fragment_search.recylv_pixabay
import kotlinx.android.synthetic.main.view_search.button_selected
import kotlinx.android.synthetic.main.view_search.close_search_button
import kotlinx.android.synthetic.main.view_search.execute_search_button
import kotlinx.android.synthetic.main.view_search.search_input_text
import org.koin.android.ext.android.inject

class SearchFragment : AppCompatActivity(), View.OnClickListener, HitsItem {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private val viewModel: SearchViewModel by viewModel()
    private val adapter = SearchAdapter()
    private var query: String = FRUITS_QUERY_TAG
    private val uiHelper: UiHelper by inject()

    private var selectedImage: ArrayList<Image> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        savedInstanceState?.getString(EXTRA_QUERY)?.let { query = it }

        initRecyclerView()

        /*
         * Check Internet Connection
         * */

        if (uiHelper.getConnectivityStatus()) subscribeObservables(query)
        else uiHelper.showSnackBar(
            main_rootView,
            resources.getString(R.string.error_message_network)
        )

        execute_search_button.setOnClickListener(this)
        button_selected.setOnClickListener { onAnimateImageListener() }

    }

    private fun subscribeObservables(query: String) {
        viewModel.fetchKeyQuery(query)
        viewModel.pixabayData.observe(this, Observer { adapter.submitList(it) })

        viewModel.networkState?.observe(this, Observer {

            /*
             * Progress Updater
             * */

            it?.let {
                when (it) {
                    is NetworkState.Loading -> {
                        EspressoIdlingResource.increment()
                        showProgressBar(true)
                    }

                    is NetworkState.Success -> {
                        EspressoIdlingResource.decrement()
                        showProgressBar(false)
                    }

                    is NetworkState.Error -> showProgressBar(false)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.execute_search_button -> {
                if (!TextUtils.isEmpty(search_input_text.text.toString())) {
                    uiHelper.hideKeyBoard(v)
                    query = search_input_text.text.toString()
                    searchQuery()
                } else uiHelper.showSnackBar(
                    main_rootView,
                    resources.getString(R.string.enter_query_to_search)
                )
            }
        }
    }

    private fun searchQuery() {
        close_search_button.performClick()
        subscribeObservables(query)
        selectedImage = arrayListOf()
    }

    // Save data to Bundle when screen rotates
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_QUERY, query)
    }

    // Restore data from Bundle when screen rotates
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getString(EXTRA_QUERY)?.let { query = it }
    }

    // Setup the adapter class for the RecyclerView
    private fun initRecyclerView() {
        recylv_pixabay.layoutManager = GridLayoutManager(this, 2)
        recylv_pixabay.adapter = adapter
        adapter.setOnHitsItemClickListener(this)
        adapter.setOnButtonClickListener(this)
    }

    // UPDATE UI ----
    private fun showProgressBar(display: Boolean) {
        if (!display) progress_bar.visibility = View.GONE
        else progress_bar.visibility = View.VISIBLE
    }

    override fun onButtonClickListener(image: Image?) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.show_details)
        alertDialog.setMessage(R.string.show_more_details)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->

            image?.let {
                val intent = Intent(this, ImageDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_IMAGE, image)
                startActivity(intent)
            }
        }
        alertDialog.setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    override fun onHitsItemClickListener(image: Image?, view: View) {
        isSelectedCardAction(image)
        selectedImagesToAnimate(image, view)
        animationButtonAppear()
    }

    private fun onAnimateImageListener() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.show_animation)
        alertDialog.setMessage(R.string.show_animation_text)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->

            selectedImage.let {
                val intent = Intent(this, AnimationImageActivity::class.java)
                intent.putParcelableArrayListExtra(Constants.EXTRA_IMAGE_ANIMATION, it)
                startActivity(intent)
            }
        }
        alertDialog.setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    private fun isSelectedCardAction(image: Image?) {
        image?.isSelected = image?.isSelected != true
    }

    private fun selectedImagesToAnimate(image: Image?, view: View) {
        if (image?.isSelected == true) {
            selectedImage.add(selectedImage.lastIndex + 1, image)
            view.foreground = ColorDrawable(resources.getColor(R.color.colorTransparentDark))
        } else {
            selectedImage.remove(image)
            view.foreground = ColorDrawable(resources.getColor(R.color.colorTransparent))
        }
    }

    private fun animationButtonAppear() {
        button_selected.visibility = if (selectedImage.isNotEmpty()) View.VISIBLE else View.GONE
    }
}

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

