package ru.developer.amukovozov.testapp.network.util.pagination

data class PaginationParams<T>(
        val pageNumber: Int,
        val pageSize: Int,
        val onDataLoaded: (items: List<T>, currentPage: Int?) -> Unit
)

