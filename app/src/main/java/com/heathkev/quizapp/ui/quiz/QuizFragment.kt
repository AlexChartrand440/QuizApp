package com.heathkev.quizapp.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.heathkev.quizapp.R
import com.heathkev.quizapp.databinding.FragmentQuizBinding
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false)

        val quizData = QuizFragmentArgs.fromBundle(requireArguments()).quizData

        val firebaseAuth = FirebaseAuth.getInstance()

        val currentUserId = if(firebaseAuth.currentUser != null){
            firebaseAuth.currentUser!!.uid
        }else{
            ""
            // go to home page
        }


        val viewModelFactory = QuizViewModelFactory(quizData, currentUserId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(QuizViewModel::class.java)
        binding.quizViewModel = viewModel
        binding.lifecycleOwner = this

        setButtonVisibility(binding.quizOptionA,View.VISIBLE, true)
        setButtonVisibility(binding.quizOptionB,View.VISIBLE, true)
        setButtonVisibility(binding.quizOptionC,View.VISIBLE, true)
        setButtonVisibility(binding.quizNextBtn,View.INVISIBLE, false)

        binding.quizOptionA.setOnClickListener{
            val btn = it as Button

            validateOptionSelected(viewModel, btn, binding)
        }

        binding.quizOptionB.setOnClickListener{
            val btn = it as Button

            validateOptionSelected(viewModel, btn, binding)
        }

        binding.quizOptionC.setOnClickListener{
            val btn = it as Button

            validateOptionSelected(viewModel, btn, binding)
        }

        binding.quizOptionD.setOnClickListener{
            val btn = it as Button

            validateOptionSelected(viewModel, btn, binding)
        }


        binding.quizNextBtn.setOnClickListener{
            viewModel.loadNextQuestion()
            resetOptions(binding)
        }

        viewModel.isTimeUp.observe(viewLifecycleOwner, Observer {
            if(it){

                // Empty string no answer was selected
                val correctAnswer = viewModel.getCorrectAnswer("")
                highlightCorrectAnswer(correctAnswer)
                setButtonVisibility(binding.quizNextBtn, View.VISIBLE, true)
                viewModel.onTimeUpComplete()
            }
        })

        viewModel.shouldNavigateToResult.observe(viewLifecycleOwner, Observer {
            if(it){
                this.findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToResultFragment(quizData))
                viewModel.navigateToResultPageComplete()
            }
        })

        return binding.root
    }

    private fun resetOptions(binding: FragmentQuizBinding) {
        setButtonBackground(binding.quizOptionA, null)
        setButtonBackground(binding.quizOptionB, null)
        setButtonBackground(binding.quizOptionC, null)
        setButtonBackground(binding.quizOptionD, null)

        binding.quizOptionA.setTextColor(resources.getColor(R.color.colorLightText, null))
        binding.quizOptionB.setTextColor(resources.getColor(R.color.colorLightText, null))
        binding.quizOptionC.setTextColor(resources.getColor(R.color.colorLightText, null))
        binding.quizOptionD.setTextColor(resources.getColor(R.color.colorLightText, null))

        setButtonVisibility(binding.quizNextBtn, View.INVISIBLE, false)
    }

    private fun validateOptionSelected(viewModel: QuizViewModel, option: Button, binding: FragmentQuizBinding){
        if(viewModel.canAnswer()){
            val correctAnswer = viewModel.getCorrectAnswer(option.text.toString())

            option.setTextColor(resources.getColor(R.color.colorDark, null))

            val isCorrect = option.text == correctAnswer
            setButtonBackground(option, isCorrect)
            if(!isCorrect){
                highlightCorrectAnswer(correctAnswer)
            }

            setButtonVisibility(binding.quizNextBtn,View.VISIBLE,true)
        }
    }

    private fun highlightCorrectAnswer(correctAnswer: String) {
        when(correctAnswer){
            quiz_option_a.text -> {
                setButtonBackground(quiz_option_a, true)
                quiz_option_a.setTextColor(resources.getColor(R.color.colorDark, null))
            }
            quiz_option_b.text -> {
                setButtonBackground(quiz_option_b, true)
                quiz_option_b.setTextColor(resources.getColor(R.color.colorDark, null))
            }
            quiz_option_c.text -> {
                setButtonBackground(quiz_option_c, true)
                quiz_option_c.setTextColor(resources.getColor(R.color.colorDark, null))
            }
            else -> {
                setButtonBackground(quiz_option_d, true)
                quiz_option_d.setTextColor(resources.getColor(R.color.colorDark, null))
            }
        }
    }

    private fun setButtonVisibility(button: Button, visibility: Int, isEnabled: Boolean){
        button.visibility = visibility
        button.isEnabled = isEnabled
    }

    private fun setButtonBackground(button: Button, isCorrect: Boolean?){
        if(isCorrect != null && isCorrect){
            button.background = resources.getDrawable(R.drawable.correct_answer_btn_bg, null)
        }else if(isCorrect != null && !isCorrect){
            button.background = resources.getDrawable(R.drawable.wrong_answer_btn_bg, null)
        }else{
            button.background = resources.getDrawable(R.drawable.outline_light_btn_bg, null)
        }
    }

}
