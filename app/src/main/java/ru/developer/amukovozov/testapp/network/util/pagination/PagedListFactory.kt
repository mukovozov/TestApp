package ru.developer.amukovozov.testapp.network.util.pagination

import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.developer.amukovozov.testapp.network.util.ExecutorProvider

class PagedListFactory(private val executorProvider: ExecutorProvider) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 2
        private const val DEFAULT_PREFETCH_DISTANCE = 5
    }

    private val compositeDisposable = CompositeDisposable()

    /**
     * Переменная для хранения последних параметров пагинации.
     * Используется при перезапросе дополнительной страницы.
     */
    private var currentPagingParams: PaginationParams<Item<GroupieViewHolder>>? = null

    fun createPageList(
        pageSize: Int = DEFAULT_PAGE_SIZE,
        prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE,
        onLoadPage: (PaginationParams<Item<GroupieViewHolder>>) -> Unit
    ): PagedList<Item<GroupieViewHolder>> {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize)
            .setPageSize(pageSize)
            .setPrefetchDistance(prefetchDistance)
            .build()

        val dataSource = getDataSource(onLoadPage)

        return PagedList.Builder(dataSource, config)
            .setNotifyExecutor(executorProvider.getMainThreadExecutor())
            .setFetchExecutor(executorProvider.getSingleThreadExecutor())
            .build()
    }

    fun <T : Item<GroupieViewHolder>> createPageList(
        pageSize: Int = DEFAULT_PAGE_SIZE,
        prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE,
        singleDataSource: (PaginationParams<Item<GroupieViewHolder>>) -> Single<List<T>>,
        onPaginationStateUpdated: (PaginationState) -> Unit
    ): PagedList<Item<GroupieViewHolder>> {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize)
            .setPageSize(pageSize)
            .setPrefetchDistance(prefetchDistance)
            .build()

        val dataSource = getDataSource { paginationParams ->
            loadPage(
                singleDataSource = singleDataSource,
                paginationParams = paginationParams,
                onPaginationStateUpdated = onPaginationStateUpdated
            )
        }

        return PagedList.Builder(dataSource, config)
            .setNotifyExecutor(executorProvider.getMainThreadExecutor())
            .setFetchExecutor(executorProvider.getSingleThreadExecutor())
            .build()
    }

    fun clearSubscriptions() {
        compositeDisposable.clear()
    }

    fun <T : Item<GroupieViewHolder>> tryToLoadNextPage(
        singleDataSource: (PaginationParams<Item<GroupieViewHolder>>) -> Single<List<T>>,
        onPaginationStateUpdated: (PaginationState) -> Unit
    ) {
        currentPagingParams?.let {
            loadPage(
                singleDataSource = singleDataSource,
                paginationParams = it,
                onPaginationStateUpdated = onPaginationStateUpdated
            )
        }
    }

    private fun getDataSource(
        onLoadPage: (PaginationParams<Item<GroupieViewHolder>>) -> Unit
    ): PageKeyedDataSource<Int, Item<GroupieViewHolder>> {
        return object : PageKeyedDataSource<Int, Item<GroupieViewHolder>>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, Item<GroupieViewHolder>>
            ) {
                val paginationParams = PaginationParams(
                    pageNumber = 1,
                    pageSize = params.requestedLoadSize,
                    onDataLoaded = { items: List<Item<GroupieViewHolder>>, currentPage: Int? ->
                        callback.onResult(items, null, currentPage?.inc())
                    }
                )
                onLoadPage.invoke(paginationParams)
            }

            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item<GroupieViewHolder>>) {
                val paginationParams = PaginationParams(
                    pageNumber = params.key,
                    pageSize = params.requestedLoadSize,
                    onDataLoaded = { items: List<Item<GroupieViewHolder>>, currentPage: Int? ->
                        callback.onResult(items, currentPage?.inc())
                    }
                )
                onLoadPage.invoke(paginationParams)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, Item<GroupieViewHolder>>
            ) = Unit
        }
    }

    private fun <T : Item<GroupieViewHolder>> loadPage(
        singleDataSource: (PaginationParams<Item<GroupieViewHolder>>) -> Single<List<T>>,
        paginationParams: PaginationParams<Item<GroupieViewHolder>>,
        onPaginationStateUpdated: (PaginationState) -> Unit
    ) {
        currentPagingParams = paginationParams
        val disposable = singleDataSource.invoke(paginationParams)
            .doOnSubscribe {
                if (paginationParams.pageNumber == 1) {
                    onPaginationStateUpdated.invoke(PaginationState.ContentLoading)
                } else {
                    onPaginationStateUpdated.invoke(PaginationState.Loading())
                }
            }
            .subscribe(
                { items ->
                    onPaginationStateUpdated(PaginationState.Content)
                    paginationParams.onDataLoaded.invoke(items, paginationParams.pageNumber)
                    if (items.isEmpty() && paginationParams.pageNumber != 1) {
                        onPaginationStateUpdated.invoke(PaginationState.EndOfList())
                    }
                }, { throwable ->
                    if (paginationParams.pageNumber == 1) {
                        onPaginationStateUpdated.invoke(PaginationState.ContentStub(throwable))
                    } else {
                        onPaginationStateUpdated.invoke(PaginationState.LoadingErrorItem())
                    }
                }
            )
        compositeDisposable.add(disposable)
    }
}
