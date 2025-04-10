package ru.netology.nmedia.view
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityPageBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.empty
import util.AndroidUtils
import util.focusAndShowKeyboard

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        }
        )
        binding.main.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.main.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) { editedPost ->
            if (editedPost.id == 0L) {
                return@observe
            }
                binding.content.requestFocus()
                binding.content.setText(editedPost.content)
                binding.content.focusAndShowKeyboard()
                binding.editGroup.visibility = View.VISIBLE
            }
            binding.save.setOnClickListener {
                with(binding.content) {
                    if (text.isNullOrBlank()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Content can't be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    viewModel.changeContent(text.toString())
                    viewModel.save()
                    binding.content.setText("")
                    binding.content.clearFocus()
                    binding.content.focusAndShowKeyboard()
                    AndroidUtils.hideKeyboard(this)
                }
            }
            binding.cancelButton.setOnClickListener {
                viewModel.edited.value = empty
                binding.content.setText("")
                binding.content.clearFocus()
                binding.content.focusAndShowKeyboard()
                binding.editGroup.visibility = View.GONE
            }
        }
    }