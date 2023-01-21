package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult


class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
        val minCountOfRightAnswers = gameResult.gameSettings.minCountOfRightAnswers.toString()
        binding.tvRequiredAnswers.text = getString(R.string.required_score, minCountOfRightAnswers)
        val countOfRightAnswers = gameResult.countOfRightAnswers.toString()
        binding.tvScoreAnswers.text = getString(R.string.score_answers, countOfRightAnswers)
        val minPercentOfRightAnswers = gameResult.gameSettings.minPercentOfRightAnswers.toString()
        binding.tvRequiredPercentage.text =
            getString(R.string.required_percentage, minPercentOfRightAnswers)
        val percentOfRightAnswers =
            ((countOfRightAnswers.toDouble() / minCountOfRightAnswers.toDouble()) * 100).toInt()
                .toString()
        binding.tvScorePercentage.text = getString(R.string.score_percentage, percentOfRightAnswers)
        val imageDrawable =
            if (minPercentOfRightAnswers.toInt() <= percentOfRightAnswers.toInt()) {
                getDrawable(requireActivity(), R.drawable.ic_smile)
            } else getDrawable(requireActivity(), R.drawable.ic_sad)
        binding.emojiResult.setImageDrawable(imageDrawable)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(GAME_RESULT)?.let {
            gameResult = it
        }

    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val GAME_RESULT = "game Result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }

}
