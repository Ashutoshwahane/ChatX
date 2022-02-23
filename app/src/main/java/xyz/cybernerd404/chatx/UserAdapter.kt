package xyz.cybernerd404.chatx

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.cybernerd404.chatx.databinding.UserItemLayoutBinding


class UserAdapter(val context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var list: List<User> = arrayListOf()

    fun setUser(response: List<User>) {
        this.list = response
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            UserItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.userNameTv.text = list[position].name
        holder.binding.root.setOnClickListener {
            context.let {
            Intent(it, ChatActivity::class.java).apply {
                putExtra("name", list[position].name)
                putExtra("uid", list[position].uid)
                it.startActivity(this)
            }
            }

        }
    }

    class ViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}
