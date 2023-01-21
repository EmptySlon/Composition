package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var gameResult: GameResult
    private lateinit var gameSettings: GameSettings
    private val gameViewModel: GameViewModel by lazy {
        ViewModelProvider(this)[GameViewModel::class.java]
    }


    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        gameSettings = GameRepositoryImpl.getGameSettings(level)
        gameViewModel.getGameSettings(level)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserverForButton()

        setTimer()

        setObserverForGameOver()

        setObserverForGameResultParameters()

        setOnClickListenerToNumber()


    }

    private fun setOnClickListenerToNumber() {
        binding.tvOption1.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }

        binding.tvOption2.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }

        binding.tvOption3.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }

        binding.tvOption4.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }

        binding.tvOption5.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }

        binding.tvOption6.setOnClickListener {
            it as TextView
            val number = it.text.toString().toInt()
            gameViewModel.chooseAnswer(number)
        }
    }

    private fun setObserverForGameResultParameters() {
        binding.progressBar.max =
            gameViewModel.gameResultLD.value?.gameSettings?.minCountOfRightAnswers ?: 100

        gameViewModel.gameResultLD.observe(viewLifecycleOwner) {
            val countOfRightAnswers = it.countOfRightAnswers.toString()
            val minCountOfRightAnswers = it.gameSettings.minCountOfRightAnswers.toString()
            binding.tvAnswersProgress.text =
                getString(R.string.progress_answers, countOfRightAnswers, minCountOfRightAnswers)
            binding.progressBar.progress = countOfRightAnswers.toInt()
        }
    }

    private fun setObserverForGameOver() {
        gameViewModel.gameOver.observe(viewLifecycleOwner) {
            if (it) launchGameFinishedFragment()
        }
    }

    private fun setObserverForButton() {
        gameViewModel.questionLD.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            binding.tvOption1.text = it.options[0].toString()
            binding.tvOption2.text = it.options[1].toString()
            binding.tvOption3.text = it.options[2].toString()
            binding.tvOption4.text = it.options[3].toString()
            binding.tvOption5.text = it.options[4].toString()
            binding.tvOption6.text = it.options[5].toString()
        }
    }

    private fun setTimer() {
        gameViewModel.timeUntilFinished.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun launchGameFinishedFragment() {

        gameResult =
            gameViewModel.gameResultLD.value ?: throw RuntimeException("gameResult == null")
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null).commit()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {

        const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }


}
