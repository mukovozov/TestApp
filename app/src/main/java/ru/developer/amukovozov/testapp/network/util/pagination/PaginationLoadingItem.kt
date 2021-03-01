package ru.developer.amukovozov.testapp.network.util.pagination

import com.xwray.groupie.GroupieViewHolder
import ru.developer.amukovozov.testapp.R


class PaginationLoadingItem : PaginationStateItem() {

    override fun getLayout() = R.layout.item_pagination_loading

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = Unit
}
