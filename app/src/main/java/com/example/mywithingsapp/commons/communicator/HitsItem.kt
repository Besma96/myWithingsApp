package com.example.mywithingsapp.commons.communicator

import android.view.View
import com.example.mywithingsapp.domain.entities.Image

interface HitsItem {
    fun onHitsItemClickListener(image: Image?, view: View)

    fun onButtonClickListener(image: Image?)
}
