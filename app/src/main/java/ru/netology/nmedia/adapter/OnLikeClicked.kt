package ru.netology.nmedia.adapter

import ru.netology.nmedia.post.Post

typealias OnLikeClicked = (post: Post) -> Unit
typealias OnShareClicked = (post: Post) -> Unit
typealias OnRemoveClicked = (post: Post) -> Unit