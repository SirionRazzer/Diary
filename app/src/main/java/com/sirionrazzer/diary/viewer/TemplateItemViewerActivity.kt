package com.sirionrazzer.diary.viewer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnListScrollListener
import com.google.android.material.snackbar.Snackbar
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.creator.TemplateItemCreatorActivity
import com.sirionrazzer.diary.models.TrackItemTemplate
import kotlinx.android.synthetic.main.activity_templateitem_viewer.*
import kotlinx.android.synthetic.main.toolbar.*

class TemplateItemViewerActivity : AppCompatActivity() {

    companion object {
        val CHANGE = 1
        val NOCHANGE = 0
    }

    lateinit var tiViewModel: TemplateItemViewerViewModel
    lateinit var adapter: TemplateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templateitem_viewer)

        Diary.app.appComponent.inject(this)

        tiViewModel = createViewModel()

        toolbar.setTitle(R.string.title_manage_activities)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (tiViewModel.currentTemplateItems.value != null) {
            adapter = TemplateAdapter(tiViewModel.currentTemplateItems.value!!, tiViewModel, this)
            rvTemplates.layoutManager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
            rvTemplates.adapter = adapter
        }
        rvTemplates.orientation =
            DragDropSwipeRecyclerView.ListOrientation.GRID_LIST_WITH_HORIZONTAL_SWIPING
        rvTemplates.orientation?.removeSwipeDirectionFlag(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
        rvTemplates.orientation?.removeSwipeDirectionFlag(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.LEFT)
        rvTemplates.swipeListener = onItemSwipeListener
        rvTemplates.dragListener = onItemDragListener
        //rvTemplates.scrollListener = onListScrollListener

        tiViewModel.currentTemplateItems.observe(this, Observer { updated ->
//            if (tiViewModel.currentTemplateItems.value != null) {
//                if (updated != null) adapter.refresh(updated)
//            }
        })
    }

    private val onItemSwipeListener = object : OnItemSwipeListener<TrackItemTemplate> {
        override fun onItemSwiped(
            position: Int,
            direction: OnItemSwipeListener.SwipeDirection,
            item: TrackItemTemplate
        ): Boolean {
            // Handle action of item swiped
            // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
            // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
            return false
        }
    }

    private val onItemDragListener = object : OnItemDragListener<TrackItemTemplate> {
        override fun onItemDragged(
            previousPosition: Int,
            newPosition: Int,
            item: TrackItemTemplate
        ) {
            // Handle action of item being dragged from one position to another
        }

        override fun onItemDropped(
            initialPosition: Int,
            finalPosition: Int,
            item: TrackItemTemplate
        ) {
            // Handle action of item dropped

            // Move items between initial and final position by -1
            if (initialPosition < finalPosition) {
                tiViewModel.currentTemplateItems.value?.forEach {
                    val template = TrackItemTemplate(
                        it.id,
                        it.deleted,
                        it.name,
                        it.image,
                        it.hasTextField,
                        it.hasNumberField,
                        it.position
                    )

                    if (it.position == initialPosition) {
                        template.position = finalPosition
                    }

                    if (it.position in (initialPosition + 1)..finalPosition) {
                        template.position = it.position - 1
                    }

                    tiViewModel.updateTemplate(template)
                }
                tiViewModel.hasChanged = true
            }

            // Move items between initial and final position by +1
            if (initialPosition > finalPosition) {
                tiViewModel.currentTemplateItems.value?.forEach {
                    val template = TrackItemTemplate(
                        it.id,
                        it.deleted,
                        it.name,
                        it.image,
                        it.hasTextField,
                        it.hasNumberField,
                        it.position
                    )

                    if (it.position == initialPosition) {
                        template.position = finalPosition
                    }

                    if (it.position in finalPosition until initialPosition) {
                        template.position = it.position + 1
                    }

                    tiViewModel.updateTemplate(template)
                }
                tiViewModel.hasChanged = true
            }

            if (tiViewModel.hasChanged && initialPosition != finalPosition) {
                tiViewModel.refreshTemplateList()
                rvTemplates.adapter?.notifyDataSetChanged()
            }
        }
    }

//    private val onListScrollListener = object : OnListScrollListener {
//        override fun onListScrollStateChanged(scrollState: OnListScrollListener.ScrollState) {
//            //not needed
//        }
//
//        override fun onListScrolled(
//            scrollDirection: OnListScrollListener.ScrollDirection,
//            distance: Int
//        ) {
//            // Handle scrolling
//        }
//    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    fun createViewModel(): TemplateItemViewerViewModel {
        return ViewModelProviders.of(this).get(TemplateItemViewerViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_create_activity_template, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mAddTemplate) {
            val intent = Intent(this, TemplateItemCreatorActivity::class.java)
            startActivityForResult(intent, CHANGE)
        } else {
            onBackPressed()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == resultCode) { // template was added
            tiViewModel.refreshTemplateList()
            tiViewModel.hasChanged = true
            adapter.refresh()
            val snackbar = Snackbar.make(rlMain, "Activity added", Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
    }

    override fun onBackPressed() {
        // TODO change result also if user created new activity!

        val intent = Intent()

        if (tiViewModel.hasChanged) setResult(CHANGE, intent) else {
            setResult(NOCHANGE, intent)
        }
        finish()
    }
}