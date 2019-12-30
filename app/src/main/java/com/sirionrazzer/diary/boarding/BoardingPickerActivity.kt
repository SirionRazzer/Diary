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
import com.sirionrazzer.diary.models.ExampleTemplate
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

    private fun createExampleTemplates(): MutableLiveData<List<ExampleTemplate>> {
        val ex0 = ExampleTemplate(
            getString(R.string.example_outcome),
            R.drawable.aaa_circlecolor_credit_card,
            false,
            true,
            "How much did I spend today",
            true
        )

        val ex1 = ExampleTemplate(
            getString(R.string.example_calories),
            R.drawable.aab_rice,
            false,
            true,
            "How many calories did I eat today",
            true
        )

        val ex2 = ExampleTemplate(
            getString(R.string.example_workout),
            R.drawable.aab_dumbbell,
            true,
            false,
            "What did I do today",
            true
        )

        val ex3 = ExampleTemplate(
            getString(R.string.example_note),
            R.drawable.aaa_circlecolor_writing,
            true,
            false,
            "My thought for today",
            true
        )

        val ex4 = ExampleTemplate(
            getString(R.string.example_water),
            R.drawable.aab_bottle,
            false,
            true,
            "Water milliliters today",
            true
        )

        val ex5 = ExampleTemplate(
            getString(R.string.example_steps),
            R.drawable.aab_smartwatch,
            false,
            true,
            "Total steps today",
            true
        )

        val ex6 = ExampleTemplate(
            getString(R.string.example_weight),
            R.drawable.aab_weight,
            false,
            true,
            "My weight progress",
            true
        )

        val ex7 = ExampleTemplate(
            getString(R.string.example_grateful),
            R.drawable.aaa_circlecolor_forest_2,
            true,
            false,
            "What am I grateful for today",
            true
        )

        val ex8 = ExampleTemplate(
            getString(R.string.example_book),
            R.drawable.aaa_circlecolor_library,
            true,
            false,
            "My reading journal",
            true
        )

        val ex9 = ExampleTemplate(
            getString(R.string.example_movie),
            R.drawable.aaa_circlecolor_planetearth,
            true,
            false,
            "“The world isn’t split into good people and Death Eaters.” --Harry Potter",
            true
        )

        val ex10 = ExampleTemplate(
            getString(R.string.example_friends),
            R.drawable.aab_wine,
            false,
            false,
            "“I would rather walk with a friend in the dark, than alone in the light.” --Helen Keller",
            true
        )

        val ex11 = ExampleTemplate(
            getString(R.string.example_practice),
            R.drawable.aaa_circlecolor_acoustic_guitar,
            false,
            false,
            "“You couldn't not like someone who liked the guitar.” --Stephen King",
            true
        )

        val ex12 = ExampleTemplate(
            getString(R.string.example_relax),
            R.drawable.aaa_circlecolor_river,
            false,
            false,
            "“Ah! There is nothing like staying at home, for real comfort.” --Jane Austen",
            true
        )

        val ex13 = ExampleTemplate(
            getString(R.string.example_date),
            R.drawable.aab_dance,
            false,
            false,
            "Two people meet socially with the aim of each assessing the other's suitability as a prospective partner in an intimate relationship. --Wikipedia",
            true
        )

        val ex14 = ExampleTemplate(
            getString(R.string.example_medicine),
            R.drawable.aab_pills,
            true,
            true,
            "“Always laugh when you can, it is cheap medicine.” --Lord Byron",
            true
        )

        val ex15 = ExampleTemplate(
            getString(R.string.example_party),
            R.drawable.aaa_circlecolor_beach,
            false,
            false,
            "“A party without cake is just a meeting.” --Julia Child",
            true
        )

        val ex16 = ExampleTemplate(
            getString(R.string.example_study),
            R.drawable.aaa_circlecolor_graduate,
            true,
            false,
            "“Study the past if you would define the future.” --Confucius",
            true
        )

        val ex17 = ExampleTemplate(
            getString(R.string.example_shopping),
            R.drawable.aaa_circlecolor_tablet,
            false,
            false,
            "My grocery list duty (maybe some nice shirts)",
            true
        )

        val ex18 = ExampleTemplate(
            getString(R.string.example_running),
            R.drawable.aab_running,
            false,
            false,
            "My daily routine",
            true
        )

        val ex19 = ExampleTemplate(
            getString(R.string.example_cycling),
            R.drawable.aab_cycling,
            false,
            false,
            "I need two wheels",
            true
        )

        val ex20 = ExampleTemplate(
            getString(R.string.example_breathing),
            R.drawable.aab_stopclock,
            false,
            false,
            "[Apple Watch] One minute breathing session",
            true
        )

        val ex21 = ExampleTemplate(
            getString(R.string.example_diet),
            R.drawable.aab_juice,
            false,
            false,
            "Get rid of the unhealthy foods in your house",
            true
        )

        val ex22 = ExampleTemplate(
            getString(R.string.example_nojunk),
            R.drawable.aab_nofood,
            false,
            false,
            "No junk food challenge",
            true
        )

        val ex23 = ExampleTemplate(
            getString(R.string.example_meditation),
            R.drawable.aaa_circlecolor_sprout,
            false,
            false,
            "Train attention and awareness, become emotionally calm and stable",
            true
        )

        val ex24 = ExampleTemplate(
            getString(R.string.example_heartpoints),
            R.drawable.aab_cardiogram,
            false,
            true,
            "[Google Fit] Activities like brisk walking, jogging, swimming, taking exercise classes, or playing tennis",
            true
        )

        val ex25 = ExampleTemplate(
            getString(R.string.example_moveminutes),
            R.drawable.aab_stopclock,
            false,
            true,
            "[Google Fit] Activities like yoga, dancing, gardening, or household chores",
            true
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