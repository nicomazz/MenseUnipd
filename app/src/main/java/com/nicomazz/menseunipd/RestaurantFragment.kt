package com.nicomazz.menseunipd


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.nicomazz.menseunipd.services.EsuRestApi
import com.nicomazz.menseunipd.services.Restaurant
import kotlinx.android.synthetic.main.fragment_restaurant.view.*
import org.jetbrains.anko.support.v4.act


/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantFragment : Fragment() {

    private var restaurant: Restaurant? = null
    private var restaurantName: String? = "murialdo"

    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_restaurant, container, false)

        if (restaurant != null) {
            setRestaurantMenu()
        } else fetchRestaurant()
        return rootView

    }

    private fun fetchRestaurant() {
        EsuRestApi().getRestaurant(restaurantName!!,
                onSuccess = { fetchedRestaurant ->
                    restaurant = fetchedRestaurant
                    setRestaurantMenu()
                },
                onError = { message ->
                    context?.let {
                        Toast.makeText(context, "Error in retrieve restaurant info: $message", Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun setRestaurantMenu() {
        restaurant?.name.let{
            activity?.title = it
        }
        rootView.menuText.text = restaurant?.menu.toString().toHtml()
    }

    fun getArgs() {
        if (arguments == null) return
        try {
            val restaurantString = arguments.getString(RESTAURANT_PARAM, null)
            restaurant = Gson().fromJson(restaurantString, Restaurant::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        restaurantName = arguments.getString(RESTAURANT_NAME_PARAM, null)

        if (restaurant == null && restaurantName == null)
            Toast.makeText(context, "No restaurant info", Toast.LENGTH_LONG).show()

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val RESTAURANT_PARAM = "RESTAURANT_PARAM"
        private val RESTAURANT_NAME_PARAM = "RESTAURANT_NAME_PARAM" // when we don't have data jet

        fun newInstance(aRestaurant: Restaurant): RestaurantFragment {
            return RestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(RESTAURANT_PARAM, Gson().toJson(aRestaurant))
                }
            }
        }

        fun newInstance(aRestaurantName: String?): RestaurantFragment {
            return RestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(RESTAURANT_NAME_PARAM, aRestaurantName)
                }
            }
        }
        fun newInstance(aRestaurantName: String?, aRestaurant: Restaurant?): RestaurantFragment {
            if (aRestaurant != null) return newInstance(aRestaurant)
            return newInstance(aRestaurantName)
        }
    }

}// Required empty public constructor
