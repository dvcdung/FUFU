package com.example.fufu.ui.cart_component

import android.annotation.SuppressLint
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.fufu.asset.Helper
import com.example.fufu.data.model.CartItem
import com.example.fufu.databinding.ActivityCartBinding
import com.example.fufu.ui.adapter.CartItemAdapter
import com.example.fufu.ui.common.action.HeaderLayoutAction
import com.example.fufu.ui.common.action.ICartAction
import com.example.fufu.ui.shop_component.viewmodel.RestaurantDetailViewModel

class CartActivity : AppCompatActivity() {
    lateinit var cartBinding: ActivityCartBinding
    lateinit var cartItemAdapter: CartItemAdapter
    lateinit var cartViewModel: CartViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartBinding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(cartBinding.root)

        //init
        HeaderLayoutAction(this)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartItemAdapter = CartItemAdapter(emptyList())
        cartViewModel.getCart(applicationContext)
        cartBinding.totalPrice.text = "Total amount: "+ "0.0" +" đ"

        cartViewModel.cartLiveData.observe(this) { cart ->
            cartItemAdapter = CartItemAdapter(cart)
            cartBinding.rcvCartList.adapter = cartItemAdapter
            cartItemAdapter.iCartAction = object : ICartAction {
                override fun onIncreaseItemAmount(cartItem: CartItem) {
                    for(i in cart.indices) {
                        if(cart[i].item.itemId == cartItem.item.itemId) {
                            cart[i].item = cartItem.item
                            cart[i].amount = cartItem.amount
                        }
                    }
                    cartViewModel.saveCart(applicationContext, cart)
                    cartBinding.totalPrice.text = "Total amount: "+ calculateTotalAmount(cart).toString() +" đ"
                    cartViewModel.getCart(applicationContext)
                }

                @SuppressLint("SetTextI18n")
                override fun onDecreaseItemAmount(cartItem: CartItem){
                    if (cartItem.amount == 0) {
                        cartViewModel.removeCart(cartItem)
                        cartViewModel.saveCartDefault(applicationContext)
                    } else {
                        for(i in cart.indices) {
                            if(cart[i].item.itemId == cartItem.item.itemId) {
                                cart[i].item = cartItem.item
                                cart[i].amount = cartItem.amount
                            }
                        }
                        cartViewModel.saveCart(applicationContext, cart)
                    }
                    cartBinding.totalPrice.text = "Total amount: "+ calculateTotalAmount(cart).toString() +" đ"
                    cartViewModel.getCart(applicationContext)
                }
            }
        }

    }

    fun calculateTotalAmount(cart: List<CartItem>): Double {
        return cart.fold(0.0) { acc, it -> acc + it.item.itemPrice.toDouble() * it.amount }
    }
}