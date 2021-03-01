package ru.developer.amukovozov.testapp.ui.home.item

import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import ru.developer.amukovozov.testapp.R
import ru.developer.amukovozov.testapp.databinding.ItemPictureBinding
import ru.developer.amukovozov.testapp.network.model.Picture

data class PictureItem(
    private val picture: Picture
) : BindableItem<ItemPictureBinding>() {

    override fun getId() = picture.url.hashCode().toLong()
    override fun getLayout() = R.layout.item_picture

    override fun bind(viewBinding: ItemPictureBinding, position: Int) {
        Glide.with(viewBinding.picture)
            .load(picture.url)
            .into(viewBinding.picture)

        viewBinding.author.text = picture.author
    }

    override fun initializeViewBinding(view: View): ItemPictureBinding {
        return ItemPictureBinding.bind(view)
    }
}