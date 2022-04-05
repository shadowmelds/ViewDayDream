package dev.shadowmeld.viewdaydream.util.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * @author Andrew
 * @date 2020/09/06 20:44
 * Recyclerview只需要跟随headView移动即可
 */
class SlideDownShowRVBehavior<V: View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {

        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: V,
            dependency: View
    ): Boolean {

        val dependencyY = dependency.y

        var y = dependency.height + dependencyY
        if (y < 0) {
            y = 0F
        }
        child.y = y
        return true
    }
}