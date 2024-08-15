package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.QuestionModel
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.*
import roundToDecimals

/**
 * QuestionItem composable function, used to represent 1 question object; called when iterating through list of Questions
 * retrieved by JsonLoader/JsonTranslatorLoader
 */
@Composable
fun QuestionItem(
    questionModel: QuestionModel,
    onNext: (Float) -> Unit,
    onBack: (Float) -> Unit,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    questionModels: List<QuestionModel>) {

    var sliderValue by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.question_of_template, questionModel.id, questionModels.size), style = MaterialTheme.typography.h4)
        Text(text = questionModel.question, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))


        /**
         * Switch-like statement, we check the question type here, as we assume going forward that numeric may not be the only one.
         * Note, however, that at present (0.0.3), "multiple_choice" is a placeholder.
         */
        when (questionModel.type) {
            "multiple_choice" -> {
                questionModel.options?.forEach { option ->
                    Button(onClick = { /* Handle answer selection */ }) {
                        Text(option)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            /**
             * In case of the question being numeric - as will almost always be the case,
             * but we include the option from the get-go
             * Add a slider with descriptive labels underneath, using Jetpack Compose.
             */
            "numeric" -> {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 0f..3f,
                        steps = 5,
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)) {
                            Text(stringResource(Res.string.not_at_all))
                        }
                    Column(modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)) {
                            Text(stringResource(Res.string.very_much))
                    }
                }
                Text(stringResource(Res.string.selected_score, sliderValue.toDouble().roundToDecimals(1)))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            /**
             * Button Previous and Button Next, either subtracts the questions value (onBack return the value for use in the SurveyPages total)
             * Or stores the value of the slider as the value of the question, and return it to be added to the total on the SurveyPage.
             * We also add onCancel and onSubmit, so the user has visual indication that they are on their way out of the survey.
             */
            if (questionModel == questionModels[0]) {
                Button(onClick = {
                    onCancel()
                }) {
                    Text(stringResource(Res.string.cancel))
                }
            } else {
                Button(onClick = {
                    onBack(questionModel.value)
                    questionModel.value = 0f
                }) {
                    Text(stringResource(Res.string.previous))
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            if (questionModel == questionModels.last()) {
                Button(onClick = {
                    onSubmit()
                }) {
                    Text(stringResource(Res.string.submit))
                }
            } else {
                Button(onClick = {
                    onNext(sliderValue)
                    questionModel.value = sliderValue
                }) {
                    Text(stringResource(Res.string.next))
                }
            }

        }

    }

}