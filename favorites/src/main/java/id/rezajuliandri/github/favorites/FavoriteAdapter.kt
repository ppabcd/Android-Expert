package id.rezajuliandri.github.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.github.databinding.ItemRowBinding
import id.rezajuliandri.github.favorites.FavoriteAdapter.MyViewHolder
import id.rezajuliandri.github.utils.firstUpper
import id.rezajuliandri.github.utils.loadImage

class FavoriteAdapter(private val onItemClick: (User) -> Unit) :
    ListAdapter<User, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    class MyViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(users: User) {
            binding.userPicture.loadImage(users.avatarUrl, 56, 56)
            binding.username.text = users.username.firstUpper()
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }
}