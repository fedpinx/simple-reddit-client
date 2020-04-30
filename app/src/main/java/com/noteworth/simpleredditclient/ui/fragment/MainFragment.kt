package com.noteworth.simpleredditclient.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.noteworth.simpleredditclient.R
import com.noteworth.simpleredditclient.common.IntentKeys
import com.noteworth.simpleredditclient.extension.fadeIn
import com.noteworth.simpleredditclient.extension.fadeOut
import com.noteworth.simpleredditclient.model.RedditPost
import com.noteworth.simpleredditclient.ui.activity.DetailActivity
import com.noteworth.simpleredditclient.ui.adapter.RedditPostsAdapter
import com.noteworth.simpleredditclient.viewmodel.RedditViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var redditViewModel: RedditViewModel
    private var redditPostsAdapter: RedditPostsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AndroidSupportInjection.inject(this)

        redditViewModel =
            ViewModelProviders.of(requireActivity(), factory).get(RedditViewModel::class.java)
        observeRedditPosts()

        // Get Reddit Posts
        redditViewModel.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reddit_posts_list.setHasFixedSize(true)
        reddit_posts_list.isNestedScrollingEnabled = false
        reddit_posts_list.layoutManager = LinearLayoutManager(requireActivity())

        dismiss_all_button.setOnClickListener { dismissAllPosts() }

        swipe_refresh_layout.setOnRefreshListener(this)

        // Check for loading frame
        if (!loading_frame.isVisible) loading_frame.fadeIn()
    }

    private fun observeRedditPosts() {
        redditViewModel.getRedditTopLiveData().observe(this, Observer {

            // Check for loading frame
            if (loading_frame.isVisible) loading_frame.fadeOut()

            if (it.data.children.isNotEmpty()) {
                if (redditPostsAdapter == null) {
                    redditPostsAdapter = RedditPostsAdapter(it.data.children,
                        object : RedditPostsAdapter.RedditPostSelectedListener {
                            override fun onClick(selectedRedditPost: RedditPost) {
                                startRedditPostDetail(selectedRedditPost)
                            }
                        })

                    reddit_posts_list.adapter = redditPostsAdapter
                } else {
                    redditPostsAdapter?.reset(it.data.children)
                }
            }
        })
    }

    private fun dismissAllPosts() {
        redditPostsAdapter?.clear()
    }

    private fun startRedditPostDetail(redditPost: RedditPost) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(IntentKeys.EXTRA_REDDIT_POST, redditPost)
        startActivity(intent)
    }

    override fun onRefresh() {
        // Check for loading frame
        if (!loading_frame.isVisible) loading_frame.fadeIn()

        redditViewModel.refresh()
        swipe_refresh_layout.isRefreshing = false
    }

    companion object {
        fun newInstance() =
            MainFragment()
    }
}
