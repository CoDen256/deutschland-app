package de.app.ui.util

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun View.onClickNavigate(controller: NavController,
                         @IdRes resId: Int,
                         vararg args: Pair<String, Any?>,
                         navOptions: NavOptions? = null){
    setOnClickListener {
        controller.navigate(resId, bundleOf(*args),  navOptions)
    }
}
fun View.onClickNavigate(controller: NavController,
                         dirs: NavDirections,
                         navOptions: NavOptions? = null){
    setOnClickListener {
        controller.navigate(dirs,  navOptions)
    }
}
fun String.editable(): Editable{
    return SpannableStringBuilder(this)
}


fun <R> TabLayout.onTabSelected(mapping: List<R>, handler: (R) -> Unit){
    onTabSelected(mapOf(*mapping.mapIndexed { i, e -> i to e }.toTypedArray()), handler=handler)
}

fun <R> TabLayout.onTabSelected(mapping: Map<Int, R>, handler: (R) -> Unit){
    if (tabCount != mapping.size){
        throw IllegalArgumentException("Mapping of the tabs has to contain mapping for all tabs: $tabCount, but was ${mapping.size}")
    }
    val ref = this
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) { handler(mapping[ref.selectedTabPosition]!!) }
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
}

fun TextView.onActionDone(handler: () -> Unit){
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> handler()
        }
        false
    }
}

fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun TextView.showDatePicker(fragmentManager: FragmentManager) {
    MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                text = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    .withZone(ZoneId.of("CET"))
                    .format(Instant.ofEpochMilli(it))
            }
        }
        .show(fragmentManager, "datePicker")
}