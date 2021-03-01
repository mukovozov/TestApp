package ru.developer.amukovozov.testapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.developer.amukovozov.testapp.network.repository.PicturesRepository
import ru.developer.amukovozov.testapp.network.util.pagination.PagedListFactory
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationParams
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationState
import ru.developer.amukovozov.testapp.ui.home.item.PictureItem

class HomeViewModel(
    private val repository: PicturesRepository,
    private val pagedListFactory: PagedListFactory
) : ViewModel() {

    val viewState = MutableLiveData<HomeViewState>().apply {
        value = HomeViewState()
    }

    init {
        loadPictures()
    }

    override fun onCleared() {
        pagedListFactory.clearSubscriptions()
        super.onCleared()
    }

    private fun loadPictures() {
        val updatedValue = viewState.value?.copy(
            items = pagedListFactory.createPageList(
                singleDataSource = ::loadPicturesPage,
                onPaginationStateUpdated = ::updatePaginationState
            )
        )
        viewState.value = updatedValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadPicturesPage(paginationParams: PaginationParams<Item<GroupieViewHolder>>): Single<List<Item<GroupieViewHolder>>> {
        return repository.getPictures(paginationParams)
            .map { it.map(::PictureItem) as List<Item<GroupieViewHolder>> }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun updatePaginationState(paginationState: PaginationState) {
        viewState.postValue(viewState.value?.copy(paginationState = paginationState))
    }
}