package id.rezajuliandri.github.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.github.R
import id.rezajuliandri.github.adapter.UserAdapter
import id.rezajuliandri.github.databinding.FragmentHomeBinding
import id.rezajuliandri.github.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModel()
    private var isNotValidData: Boolean = false

    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val direction =
                    HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                findNavController().navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        activityContext.title = getString(R.string.app_name)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityContext.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            setHasOptionsMenu(true)
            title = getString(R.string.app_name)
        }

        binding?.apply {
            val userAdapter = UserAdapter { selectedUser ->
                val direction =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedUser.username)
                findNavController().navigate(direction)
            }
            rvContent.apply {
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
                adapter = userAdapter
            }
            val searchStream = RxTextView.textChanges(searchField)
                .skipInitialValue()
                .map { search ->
                    search.length < 3
                }
            searchStream.subscribe {
                isNotValidData = it
                showSearchAlert(it)
            }
            searchField.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    if (!isNotValidData) {
                        viewModel.searchUser(searchField.text.toString())
                    }
                    true
                } else {
                    false
                }
            }
            viewModel.searchResponse.observe(viewLifecycleOwner) { responses ->
                if (responses != null) {
                    notAvailable.visibility = View.GONE
                    when (responses) {
                        is Resource.Loading -> progressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            userAdapter.submitList(responses.data)
                        }
                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            notAvailable.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showSearchAlert(isNotValid: Boolean) {
        binding?.searchField?.error =
            if (isNotValid) getString(R.string.min_lenght_search) else null
    }
}