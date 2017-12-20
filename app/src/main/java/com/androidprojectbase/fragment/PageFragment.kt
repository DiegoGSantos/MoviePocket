package com.androidprojectbase.fragment

import android.widget.TextView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.View.GONE
import com.androidprojectbase.adapter.MoviesAdapter
import com.androidprojectbase.restclient.response.MovieListResponse
import com.moviepocket.R
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_page.*
/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_page, container, false)
    }

    override fun onResume() {
        super.onResume()

        listMovies()
    }

    fun listMovies() {
        val compositeDisposable = CompositeDisposable()
        val service = Service.Factory.create()

        compositeDisposable.add(
                service.listMovies()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->
                            //                            listener.onProductsListGotten(result)
                            updateUi(result)
                        }, { error ->
                            error.printStackTrace()
//                            listener.onProductsListError()
//                                updateUi(null)
                        })
        )
    }

    fun updateUi(movieListResponse: MovieListResponse) {
        moviesList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = MoviesAdapter(this.context, movieListResponse.results)

            progress.visibility = GONE 
        }
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = PageFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}