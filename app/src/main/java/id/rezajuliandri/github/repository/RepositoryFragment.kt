package id.rezajuliandri.github.repository

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.github.adapter.RepositoryAdapter
import id.rezajuliandri.github.databinding.FragmentRepositoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryFragment : Fragment() {
    private var _binding: FragmentRepositoryBinding? = null
    private val binding get() = _binding

    private val viewModel: RepositoryViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            val username = getString("username")
            username?.let { userName ->
                viewModel.getRepository(userName)
            }
        }

        val repoAdapter = RepositoryAdapter { repository ->
            val intent = Intent(Intent.ACTION_VIEW, repository.htmlUrl.toUri())
            startActivity(intent)
        }
        binding?.apply {
            rvDetail.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = repoAdapter
            }
            viewModel.repositoryResponse.observe(viewLifecycleOwner) { responses ->
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
                            repoAdapter.submitList(responses.data)
                            if (responses.data?.isEmpty() == true) {
                                rvDetail.visibility = View.GONE
                                notAvailable.visibility = View.VISIBLE
                                repoAdapter.submitList(null)
                            }
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

    companion object {
        const val ARG_USERNAME = "username"
    }
}