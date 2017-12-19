package com.androidprojectbase.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class PageFragment2 : Fragment() {

    private var mPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = getArguments().getInt(ARG_PAGE)

        listMovies()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_page, container, false)
//        val textView = view as TextView
//        textView.text = "Fragment #" + mPage
        return view
    }

    fun listMovies() {
        val compositeDisposable = CompositeDisposable()
        val service = Service.create()

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
        }
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Int): PageFragment2 {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = PageFragment2()
            fragment.setArguments(args)
            return fragment
        }
    }
}