package ru.developer.amukovozov.testapp.network.repository

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.rxjava3.core.Single
import ru.developer.amukovozov.testapp.network.api.PicturesApi
import ru.developer.amukovozov.testapp.network.model.Picture
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationParams

class PicturesRepository(
    private val api: PicturesApi
) {
    fun getPictures(paginationParams: PaginationParams<Item<GroupieViewHolder>>): Single<List<Picture>> {
        return api.getPictures(paginationParams.pageNumber, paginationParams.pageSize)
    }
}