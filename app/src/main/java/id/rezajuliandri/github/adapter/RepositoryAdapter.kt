package id.rezajuliandri.github.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.github.adapter.RepositoryAdapter.MyViewHolder
import id.rezajuliandri.github.databinding.RepoRowBinding
import id.rezajuliandri.github.utils.formatDate

class RepositoryAdapter(private val onItemClick: (Repository) -> Unit) :
    ListAdapter<Repository, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RepoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository)
        holder.itemView.setOnClickListener {
            onItemClick(repository)
        }
    }

    class MyViewHolder(private val binding: RepoRowBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(repo: Repository) {
            val repoName = "${repo.username}/${repo.name}"
            binding.apply {
                username.text = repoName
                if (repo.description == null) {
                    description.visibility = View.GONE
                }
                description.text = repo.description
                repoDate.text = formatDate(repo.updatedAt)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Repository> =
            object : DiffUtil.ItemCallback<Repository>() {
                override fun areItemsTheSame(oldUser: Repository, newUser: Repository): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Repository, newUser: Repository): Boolean {
                    return oldUser == newUser
                }
            }
    }
}