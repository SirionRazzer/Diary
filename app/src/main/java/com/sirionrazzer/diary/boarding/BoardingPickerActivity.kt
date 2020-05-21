package com.sirionrazzer.diary.boarding

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.R
import com.sirionrazzer.diary.history.HistoryActivity
import com.sirionrazzer.diary.models.TrackItemTemplate
import com.sirionrazzer.diary.models.UserStorage
import com.sirionrazzer.diary.util.GridDividerDecoration
import kotlinx.android.synthetic.main.activity_boarding_picker.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BoardingPickerActivity : AppCompatActivity(), OnCheckExampleTemplateListener {
    @Inject
    lateinit var userStorage: UserStorage

    private val bpViewModel by lazy {
        ViewModelProviders.of(this).get(BoardingPickerViewModel::class.java)
    }
    private val exampleTemplatesAdapter by lazy { ExampleTemplatesAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarding_picker)

        Diary.app.appComponent.inject(this)

        toolbar.setTitle(R.string.title_popular_activities)
        setSupportActionBar(toolbar)

        bpViewModel.data = createExampleTemplates()
        bpViewModel.data.observe(this, Observer {
            exampleTemplatesAdapter.submitList(it)
        })

        btnSave.setOnClickListener {
            proceed()
        }

        btnSkip.setOnClickListener {
            proceed()
        }

        example_list.apply {
            val isPortrait =
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

            if (isPortrait) {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            } else {
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(GridDividerDecoration(context))
            }

            adapter = exampleTemplatesAdapter
        }
    }

    private fun proceed() {
        bpViewModel.createTrackItems()
        userStorage.updateSettings {
            it.boardingPickerShown = true
        }
        bpViewModel.closeRealms()
        startActivity(Intent(this, HistoryActivity::class.java))
        finish()
    }

    override fun onCheckExampleTemplate(position: Int, value: Boolean) =
        bpViewModel.checkExampleTemplate(position, value)

    private fun createExampleTemplates(): MutableLiveData<List<TrackItemTemplate>> {
        val ex0 = TrackItemTemplate(
            "",
            false,
            true,
            getString(R.string.example_outcome),
            "How much did I spend today",
            R.drawable.aaa_circlecolor_credit_card,
            false,
            true,
            false,
            0
        )

        val ex1 = TrackItemTemplate(
            "",
            false,
            true,
            getString(R.string.example_calories),
            "How many calories did I eat today",
            R.drawable.aab_rice,
            false,
            true,
            false,
            1
        )

        val ex2 = TrackItemTemplate(
            "",
            false,
            true,
            getString(R.string.example_workout),
            "What did I do today",
            R.drawable.aab_dumbbell,
            true,
            false,
            false,
            2
        )

        val ex3 = TrackItemTemplate(
            "",
            false,
            true,
            getString(R.string.example_note),
            "My thought for today",
            R.drawable.aaa_circlecolor_writing,
            true,
            false,
            false,
            3
        )

        val ex4 = TrackItemTemplate(
            "",
            false,
            true,
            getString(R.string.example_water),
            "Water milliliters today",
            R.drawable.aab_bottle,
            false,
            true,
            false,
            4
        )

        val ex5 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_steps),
            "Total steps today",
            R.drawable.aab_smartwatch,
            false,
            true,
            false,
            5
        )

        val ex6 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_weight),
            "My weight progress",
            R.drawable.aab_weight,
            false,
            true,
            false,
            6
        )

        val ex7 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_grateful),
            "What am I grateful for today",
            R.drawable.aaa_circlecolor_forest_2,
            true,
            false,
            false,
            7
        )

        val ex8 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_book),
            "My reading journal",
            R.drawable.aaa_circlecolor_library,
            true,
            false,
            false,
            8
        )

        val ex9 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_movie),
            "“The world isn’t split into good people and Death Eaters.” --Harry Potter",
            R.drawable.aaa_circlecolor_planetearth,
            true,
            false,
            false,
            9
        )

        val ex10 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_friends),
            "“I would rather walk with a friend in the dark, than alone in the light.” --Helen Keller",
            R.drawable.aab_wine,
            false,
            false,
            false,
            10
        )

        val ex11 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_practice),
            "“You couldn't not like someone who liked the guitar.” --Stephen King",
            R.drawable.aaa_circlecolor_acoustic_guitar,
            false,
            false,
            false,
            11
        )

        val ex12 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_relax),
            "“Ah! There is nothing like staying at home, for real comfort.” --Jane Austen",
            R.drawable.aaa_circlecolor_river,
            false,
            false,
            false,
            12
        )

        val ex13 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_date),
            "Two people meet socially with the aim of each assessing the other's suitability as a prospective partner in an intimate relationship. --Wikipedia",
            R.drawable.aab_dance,
            false,
            false,
            false,
            13
        )

        val ex14 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_medicine),
            "“Always laugh when you can, it is cheap medicine.” --Lord Byron",
            R.drawable.aab_pills,
            false,
            true,
            false,
            14
        )

        val ex15 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_party),
            "“A party without cake is just a meeting.” --Julia Child",
            R.drawable.aaa_circlecolor_beach,
            false,
            false,
            false,
            15
        )

        val ex16 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_study),
            "“Study the past if you would define the future.” --Confucius",
            R.drawable.aaa_circlecolor_graduate,
            false,
            false,
            false,
            16
        )

        val ex17 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_shopping),
            "My grocery list duty (maybe some nice shirts)",
            R.drawable.aaa_circlecolor_tablet,
            false,
            false,
            false,
            17
        )

        val ex18 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_running),
            "My daily routine",
            R.drawable.aab_running,
            false,
            false,
            false,
            18
        )

        val ex19 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_cycling),
            "I need two wheels",
            R.drawable.aab_cycling,
            false,
            false,
            false,
            19
        )

        val ex20 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_breathing),
            "[Apple Watch] One minute breathing session",
            R.drawable.aab_stopclock,
            false,
            false,
            false,
            20
        )

        val ex21 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_diet),
            "Get rid of the unhealthy foods in your house",
            R.drawable.aab_juice,
            false,
            false,
            false,
            21
        )

        val ex22 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_nojunk),
            "No junk food challenge",
            R.drawable.aab_nofood,
            false,
            false,
            false,
            22
        )

        val ex23 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_meditation),
            "Train attention and awareness, become emotionally calm and stable",
            R.drawable.aaa_circlecolor_sprout,
            false,
            false,
            false,
            23
        )

        val ex24 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_heartpoints),
            "[Google Fit] Activities like brisk walking, jogging, swimming, taking exercise classes, or playing tennis",
            R.drawable.aab_cardiogram,
            false,
            true,
            false,
            24
        )

        val ex25 = TrackItemTemplate(
            "",
            false,
            false,
            getString(R.string.example_moveminutes),
            "[Google Fit] Activities like yoga, dancing, gardening, or household chores",
            R.drawable.aab_stopclock,
            false,
            true,
            false,
            25
        )

        return MutableLiveData(
            listOf(
                ex0,
                ex1,
                ex2,
                ex3,
                ex4,
                ex5,
                ex6,
                ex7,
                ex8,
                ex9,
                ex10,
                ex11,
                ex12,
                ex13,
                ex14,
                ex15,
                ex16,
                ex17,
                ex18,
                ex19,
                ex20,
                ex21,
                ex22,
                ex23,
                ex24,
                ex25
            )
        )
    }
}