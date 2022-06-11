package de.app.ui.geo

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseExpandableListAdapter
import de.app.databinding.FragmentGeoCategoryGroupBinding
import de.app.databinding.FragmentGeoCategoryGroupItemBinding


class CategoriesWrapperAdapter(private val context: Context,
                               private val title: String,
                               private val categories: List<Pair<String, List<String>>>
                        ): BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return title
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val wrapper = inflateGroup(parent)

        wrapper.wrapperTitle.apply {
            setTypeface(null, Typeface.BOLD)
            text = title
        }
        return wrapper.root
    }

    private fun inflateGroup(parent: ViewGroup?): FragmentGeoCategoryGroupBinding{
        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FragmentGeoCategoryGroupBinding.inflate(layoutInflater, parent, false)
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return categories.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return categories[groupPosition].first
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val category = categories[childPosition]
        val (title, objects) = category
        val categoryView = inflateChild(parent)

        val objectsSpinner = categoryView.objects

        objectsSpinner.adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, objects)
        objectsSpinner.setSelection()
        return categoryView.root
    }

    private fun inflateChild(parent: ViewGroup?): FragmentGeoCategoryGroupItemBinding{
        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FragmentGeoCategoryGroupItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}