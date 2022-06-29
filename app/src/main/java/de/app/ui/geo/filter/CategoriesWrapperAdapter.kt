package de.app.ui.geo.filter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import de.app.api.geo.GeoCategory
import de.app.api.geo.GeoSetHeader
import de.app.databinding.FragmentGeoCategoryGroupBinding
import de.app.databinding.FragmentGeoCategoryGroupItemBinding


class CategoriesWrapperAdapter(private val context: Context,
                               private val categories: List<GeoCategory>
                        ): BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return categories.size
    }

    override fun getGroup(groupPosition: Int): GeoCategory {
        return categories[groupPosition]
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
            val group = getGroup(groupPosition)
            setTypeface(null, Typeface.BOLD)
            text = group.categoryName
        }
        return wrapper.root
    }

    private fun inflateGroup(parent: ViewGroup?): FragmentGeoCategoryGroupBinding{
        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FragmentGeoCategoryGroupBinding.inflate(layoutInflater, parent, false)
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return categories[groupPosition].sets.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): GeoSetHeader {
        return categories[groupPosition].sets[childPosition]
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
        val mapObject = getChild(groupPosition, childPosition)
        val categoryView = inflateChild(parent)
        categoryView.`object`.text = mapObject.name
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