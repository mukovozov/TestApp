package ru.developer.amukovozov.testapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.developer.amukovozov.testapp.R
import ru.developer.amukovozov.testapp.databinding.FragmentHomeBinding
import ru.developer.amukovozov.testapp.network.util.pagination.PagedListGroup
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationAdapter
import ru.developer.amukovozov.testapp.network.util.pagination.PaginationState
import ru.developer.amukovozov.testapp.util.BaseFragment
import ru.developer.amukovozov.testapp.util.observe

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()
    private val adapter by lazy { PaginationAdapter() }
    private val pagedGroupList: PagedListGroup<Item<GroupieViewHolder>> = PagedListGroup()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        initUi(binding)

        observe(viewModel.viewState, { viewState -> render(viewState, binding) })
    }

    private fun initUi(binding: FragmentHomeBinding) {
        adapter.add(pagedGroupList)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
    }

    private fun render(
        viewState: HomeViewState,
        binding: FragmentHomeBinding
    ) {
        viewState.items?.let(pagedGroupList::submitList)
        renderPaginationState(binding, viewState.paginationState)
    }

    private fun renderPaginationState(binding: FragmentHomeBinding, paginationState: PaginationState?) {
        with(binding) {
            list.isVisible =
                paginationState !is PaginationState.ContentLoading || paginationState !is PaginationState.ContentStub
            progressBar.isVisible = paginationState is PaginationState.ContentLoading
            stub.isVisible = paginationState is PaginationState.ContentStub
        }
        paginationState?.let(adapter::setState)
    }

}