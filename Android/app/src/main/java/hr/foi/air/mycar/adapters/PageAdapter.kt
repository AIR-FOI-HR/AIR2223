package hr.foi.air.mycar.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.reflect.KClass

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter (fragmentManager, lifecycle) {
    data class Item (val Title:Int, val Icon:Int, val FragmentClass:KClass<*>)

    val items = ArrayList<Item>();

    fun Add (fragment:Item){
        items.add(fragment);
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun createFragment(position: Int): Fragment {
        return items[position].FragmentClass.java.newInstance() as Fragment;
    }
}