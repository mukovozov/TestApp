package ru.developer.amukovozov.testapp.network.util.pagination

import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

class PagedListGroup<T : Item<*>> : Group, GroupDataObserver {

    var parentObserver: GroupDataObserver? = null
    var onItemsInserted: (() -> Unit)? = null

    val listUpdateCallback = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            parentObserver?.onItemRangeChanged(this@PagedListGroup, position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            parentObserver?.onItemRangeInserted(this@PagedListGroup, position, count)
            if (count > 0) {
                onItemsInserted?.invoke()
            }
        }

        override fun onRemoved(position: Int, count: Int) {
            parentObserver?.onItemRangeRemoved(this@PagedListGroup, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            parentObserver?.onItemMoved(this@PagedListGroup, fromPosition, toPosition)
        }
    }

    private val differ = AsyncPagedListDiffer(
            listUpdateCallback,
            AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return newItem.isSameAs(oldItem)
                }

                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return newItem == oldItem
                }
            }).build()
    )

    private lateinit var placeHolder: Item<*>

    fun setPlaceHolder(placeHolder: Item<*>) {
        this.placeHolder = placeHolder
    }

    fun submitList(newPagedList: PagedList<T>) {
        differ.submitList(newPagedList)
    }

    override fun getItemCount(): Int {
        return differ.itemCount
    }

    override fun getItem(position: Int): Item<*> {
        return differ.getItem(position) ?: placeHolder
    }

    override fun getPosition(item: Item<*>): Int {
        val currentList = differ.currentList ?: return -1

        return currentList.indexOf(item)
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = null
    }

    override fun onChanged(group: Group) {
        parentObserver?.onChanged(this)
    }

    override fun onItemInserted(group: Group, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemChanged(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemChanged(this, index)
        }
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemChanged(this, index, payload)
        }
    }

    override fun onItemRemoved(group: Group, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int,
            payload: Any) {
        throw UnsupportedOperationException()
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        throw UnsupportedOperationException()
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        throw UnsupportedOperationException()
    }

    override fun onDataSetInvalidated() {
        parentObserver?.onDataSetInvalidated()
    }

    private fun getItemPosition(group: Group): Int {
        val currentList = differ.currentList ?: return -1

        return currentList.indexOf(group)
    }
}
