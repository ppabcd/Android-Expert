package id.rezajuliandri.github.followingfollowers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.github.R
import id.rezajuliandri.github.adapter.UserAdapter
import id.rezajuliandri.github.databinding.FragmentFollowingFollowersBinding
import id.rezajuliandri.github.home.HomeFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class FollowingFollowersFragment : Fragment() {
    private var _binding: FragmentFollowingFollowersBinding? = null
    private val binding get() = _binding

    private val viewModel: FollowingFollowersViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingFollowersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username = ""
        var type = FollType.FOLLOWERS
        if (arguments?.containsKey(ARG_USERNAME) == true) {
            username = arguments?.getString(ARG_USERNAME).toString()
        }
        if (arguments?.containsKey(ARG_SECTION_TYPE) == true) {
            type = arguments?.getSerializable(ARG_SECTION_TYPE) as FollType
        }

        val follAdapter = UserAdapter { selectedUser ->
            val direction =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedUser.username)
            findNavController().popBackStack(R.id.homeFragment, false)
            findNavController().navigate(direction)
        }
        viewModel.getFollowingFollowers(username, type)
        binding?.apply {
            rvDetail.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = follAdapter
            }
            viewModel.follResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    notAvailable.visibility = View.GONE
                    rvDetail.visibility = View.VISIBLE
                    when (responses) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            rvDetail.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            rvDetail.visibility = View.VISIBLE
                            follAdapter.submitList(responses.data)
                        }
                        is Resource.Empty -> {
                            notAvailable.visibility = View.VISIBLE
                            rvDetail.visibility = View.GONE
                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            rvDetail.visibility = View.GONE
                            notAvailable.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val ARG_SECTION_TYPE = "section_type"
        const val ARG_USERNAME = "username"
    }

    enum class FollType {
        FOLLOWING,
        FOLLOWERS
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
        binding?.rvDetail?.adapter = null
    }
}