package com.shalchian.sarrafi.utils.animatedTabLayout

import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.shalchian.sarrafi.R

class AnimatedTabLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    interface OnChangeListener{
        fun onChanged(position: Int)
    }

    var containerLinearLayout: LinearLayout

    var tabs: List<AnimatedTabItemContainer>

    lateinit var selectedTab: AnimatedTabItemContainer

    lateinit var viewPager: ViewPager

    private var onChangeListener: OnChangeListener? = null

    init {
        val typedArray = context.theme?.obtainStyledAttributes(attrs, R.styleable.AnimatedTabLayout, 0, 0)
        val tabXmlResource = typedArray?.getResourceId(R.styleable.AnimatedTabLayout_atl_tabs, 0)

        tabs = AnimatedTabResourceParser(context, tabXmlResource!!).parse()

        val layoutInflater = LayoutInflater.from(getContext())
        val parentView = layoutInflater.inflate(R.layout.view_tablayout_container, this, true)
        containerLinearLayout = parentView.findViewById(R.id.linear_layout_container)

        tabs.forEach { tab ->
            containerLinearLayout.addView(tab)
            tab.setOnClickListener {
                val selectedIndex = tabs.indexOf(tab)
                onPageChangeListener.onPageSelected(selectedIndex)
                viewPager.currentItem = selectedIndex
            }
        }
    }

    fun setTabChangeListener(onChangeListener: OnChangeListener?){
        this.onChangeListener = onChangeListener
    }

    fun setupViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        this.viewPager.addOnPageChangeListener(onPageChangeListener)
        selectedTab = tabs[viewPager.currentItem]
        selectedTab.expand()
    }

    private var onPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (tabs[position] == selectedTab) {
                return
            }
            selectedTab.collapse()
            selectedTab = tabs[position]
            selectedTab.expand()

            this@AnimatedTabLayout.onChangeListener?.onChanged(position)
        }
    }

}

