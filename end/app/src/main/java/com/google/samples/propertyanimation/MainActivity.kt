/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun rotater() {

        // Rotate the view for a second around its center once

        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    private fun translater() {

        // Translate the view 200 pixels to the right and back

        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {

        // Scale the view up to 4x its default size and back

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {

        // Fade the view out to completely transparent and then back to completely opaque

        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {

        // Animate the color of the star's container from black to red over a half
        // second, then reverse back to black. Note that using a propertyName of
        // "backgroundColor" will cause the animator to call the backgroundColor property
        // (in Kotlin) or setBackgroundColor(int) (in Java).

        var animator = ObjectAnimator.ofArgb(star.parent,
            "backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {

        // Create a new star view in a random X position above the container.
        // Make it rotateButton about its center as it falls to the bottom.

        // Local variables we'll need in the code below
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        // Create the new star (an ImageView holding our drawable) and add it to the container
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        // Scale the view randomly between 10-160% of its default size
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        // Position the view at a random place between the left and right edges of the container
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2

        // Create an animator that moves the view from a starting position right about the container
        // to an ending position right below the container. Set an accelerate interpolator to give
        // it a gravity/falling feel
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)

        // Create an animator to rotateButton the view around its center up to three times
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        // Use an AnimatorSet to play the falling and rotating animators in parallel for a duration
        // of a half-second to two seconds
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        // When the animation is done, remove the created view from the container
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })

        // Start the animation
        set.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {

        // This extension method listens for start/end events on an animation and disables
        // the given view for the entirety of that animation.

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

}
