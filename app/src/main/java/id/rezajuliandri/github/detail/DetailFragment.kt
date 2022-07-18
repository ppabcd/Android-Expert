package id.rezajuliandri.github.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.github.R
import id.rezajuliandri.github.adapter.PagerAdapter
import id.rezajuliandri.github.databinding.FragmentDetailBinding
import id.rezajuliandri.github.main.MainActivity
import id.rezajuliandri.github.utils.firstUpper
import id.rezajuliandri.github.utils.loadImage
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private var isFavoriteUser = false

    private val viewModel: DetailViewModel by viewModel()
    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val direction =
                    DetailFragmentDirections.actionDetailFragmentToFavoriteFragment()
                findNavController().navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        val adapter = PagerAdapter(activityContext, username)

        activityContext.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHasOptionsMenu(true)
            title = getString(R.string.user_detail, username.firstUpper())
        }

        binding?.apply {
            pager.adapter = adapter
            viewModel.getDetailUser(username)
            viewModel.isFavorite(username)
            viewModel.isFavoriteResponse.observe(viewLifecycleOwner) {
                isFavorite(it)
                isFavoriteUser = it
            }
            fab.setOnClickListener {
                if (isFavoriteUser) {
                    viewModel.deleteFavorite(username)
                } else {
                    viewModel.setFavorite(username)
                }
                viewModel.isFavorite(username)
            }
            viewModel.detailResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    when (responses) {
                        is Resource.Loading -> {
                            userName.text = getString(R.string.loading)
                            userUsername.text = username
                            userWork.text = getString(R.string.loading)
                            userRepository.text = getString(R.string.loading)
                            userFollowers.text = getString(R.string.loading)
                            userFollowing.text = getString(R.string.loading)
                            userLocation.text = getString(R.string.loading)
                        }
                        is Resource.Success -> {
                            val userData = responses.data
                            userData?.let { detailData ->
                                userName.text = detailData.name ?: getString(R.string.github_user)
                                userUsername.text = detailData.username
                                userWork.text = detailData.company ?: getString(R.string.unknown)
                                userRepository.text = detailData.publicRepos.toString()
                                userFollowers.text = detailData.followers.toString()
                                userFollowing.text = detailData.following.toString()
                                userLocation.text =
                                    detailData.location ?: getString(R.string.unknown)
                                userAvatar.loadImage(detailData.avatarUrl)
                            }
                        }
                        is Resource.Error -> {
                            userName.text = getString(R.string.went_wrong)
                            userUsername.text = username
                            userWork.text = getString(R.string.error)
                            userRepository.text = getString(R.string.error_number)
                            userFollowers.text = getString(R.string.error_number)
                            userFollowing.text = getString(R.string.error_number)
                            userLocation.text = getString(R.string.error)
                        }
                        else -> {}
                    }
                }
            }
            TabLayoutMediator(tabs, pager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun isFavorite(isFavorite: Boolean) {
        binding?.fab?.visibility = View.VISIBLE
        if (isFavorite) {
            binding?.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    activityContext.applicationContext,
                    R.drawable.ic_baseline_star_24
                )
            )
        } else {
            binding?.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    activityContext.applicationContext,
                    R.drawable.ic_baseline_star_border_24
                )
            )
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.pager?.adapter = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.detail_tab_1,
            R.string.detail_tab_2,
            R.string.detail_tab_3,
        )
    }
}