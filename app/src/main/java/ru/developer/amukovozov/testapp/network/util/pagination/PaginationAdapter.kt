package ru.developer.amukovozov.testapp.network.util.pagination

import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

open class PaginationAdapter : GroupAdapter<GroupieViewHolder>() {

    override fun add(group: Group) {
        //Такой вариант сделан для того, что бы item загрузки, появлялся только после добавления элементов
        //основного списка
        if (itemCount == 0 && group is PagedListGroup<*>) {
            group.onItemsInserted = {
                setState(PaginationState.Loading())
            }
        }
        super.add(group)
    }

    fun setState(paginationState: PaginationState) {
        if (paginationState is PaginationState.Content
            || paginationState is PaginationState.ContentLoading
            || paginationState is PaginationState.ContentStub
        ) {
            return
        }
        removePaginationStateItem()
        when (paginationState) {
            is PaginationState.Loading -> {
                add(PaginationLoadingItem())
            }
            else -> {
                // empty
            }
        }
    }

    private fun removePaginationStateItem() {
        if (itemCount > 0) {
            val lastItem = getItem(itemCount - 1)
            if (lastItem is PaginationStateItem) {
                remove(lastItem)
            }
        }
    }
}
