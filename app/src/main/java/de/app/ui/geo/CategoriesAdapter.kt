package de.app.ui.geo

import android.R
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import de.app.databinding.FragmentGeoCategoryGroupBinding
import de.app.databinding.FragmentGeoCategoryGroupItemBinding


class CategoriesAdapter(private val context: Context,
                        private val elements: List<Pair<String, List<String>>>
                        ): BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return elements.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return elements[groupPosition].first
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
        val inflated = inflateGroup(parent)
        val view: View = inflated.root
        val listTitle = getGroup(groupPosition) as String
        val listTitleTextView = inflated.listTitle
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return view
    }

    private fun inflateGroup(parent: ViewGroup?): FragmentGeoCategoryGroupBinding{
        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FragmentGeoCategoryGroupBinding.inflate(layoutInflater, parent, false)
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return elements[groupPosition].second.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return elements[groupPosition].second[childPosition]
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
        val expandedListText = getChild(groupPosition, childPosition) as String
        val child = inflateChild(parent)
        val view = child.root

        val expandedListTextView = child.item
        expandedListTextView.text = expandedListText
        return view
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