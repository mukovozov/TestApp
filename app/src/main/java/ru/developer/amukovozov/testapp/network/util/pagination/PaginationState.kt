package ru.developer.amukovozov.testapp.network.util.pagination

sealed class PaginationState {

    object Content : PaginationState()

    object ContentLoading : PaginationState()

    data class ContentStub(val throwable: Throwable) : PaginationState()

    class Loading : PaginationState()

    class EndOfList : PaginationState()

    class LoadingErrorItem : PaginationState()
}
