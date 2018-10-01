package com.racjonalnytraktor.findme3.ui.map.fragments.addtask

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.fragment_add_task.*

class AddTaskFragment <V: MapMvp.View>: BaseFragment<V>(), DescriptionListener,
        UsersListener {

    lateinit var firstFragment: AddTaskDescrFragment<V>
    lateinit var secondFragment: AddTaskUsersFragment<V>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        firstFragment = AddTaskDescrFragment()
        firstFragment.parentListener = this
        secondFragment = AddTaskUsersFragment()
        secondFragment.parentListener = this
        return inflater.inflate(R.layout.fragment_add_task,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
                .replace(R.id.detailsContainer,firstFragment)
                .commit()

        iconArrow.setImageDrawable(IconicsDrawable(parentContext)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                .sizeDp(24)
                .color(Color.BLACK))

        iconArrow.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("addTask")
        }

        buttonAddTask.setOnClickListener {
            val action = firstFragment.getActionData()
            val usersList = secondFragment.getList()
            action.users = usersList
            parentMvp.getPresenter().onCreateActionClick(action)

        }
    }

    override fun onTitleChanged(title: String) {
        titleSummary.text = "Zadanie: $title"
    }

    override fun onAddUsersClick() {
        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left)
                .replace(R.id.detailsContainer,secondFragment)
                .commit()
    }

    override fun onBackArrowPressed() {
        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.detailsContainer,firstFragment)
                .commit()
    }

    override fun onDateChanged(date: String) {
        textFinalDate?.text = date
    }


}



interface DescriptionListener{
    fun onTitleChanged(title: String)
    fun onAddUsersClick()
    fun onDateChanged(date: String)
}
interface UsersListener{
    fun onBackArrowPressed()
}