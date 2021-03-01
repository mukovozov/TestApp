package ru.developer.amukovozov.testapp.ui.home

import androidx.paging.PagedList
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationState

data class HomeViewState(
    val items: PagedList<Item<GroupieViewHolder>>? = null,
    val paginationState: PaginationState = PaginationState.ContentLoading
)