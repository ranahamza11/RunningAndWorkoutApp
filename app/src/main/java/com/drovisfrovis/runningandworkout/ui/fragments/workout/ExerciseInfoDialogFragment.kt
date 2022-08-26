package com.drovisfrovis.runningandworkout.ui.fragments.workout

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.db.entities.Exercise
import com.drovisfrovis.runningandworkout.misc.WorkoutUtility
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_exercise_info_dialog.*
import kotlin.concurrent.fixedRateTimer


class ExerciseInfoDialogFragment(val exercise: Exercise): BottomSheetDialogFragment() {

    private var dismissListener: (()-> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        dismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val bottomSheetDialog = dialog

            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_exercise_info_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(exercise.totalDuration == 0L) {
            tvTextDuration.text = "Reps"
            tvExDuration.text = "x${exercise.reps}"
        }
        else {
            tvTextDuration.text = "Duration"
            tvExDuration.text = "00:${exercise.totalDuration/1000}"
        }

        tvExDescription.text = exercise.description
        tvExName.text = exercise.name
        Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), WorkoutUtility.getExerciseById(exercise.exerciseId))).into(gifName)

        btnContinue.setOnClickListener {
            dismissListener?.let { yes -> yes() }
            dialog?.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}