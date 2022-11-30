package com.example.guessnumber

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import org.koin.androidx.compose.koinViewModel


@Composable
fun Header() {
    Text(
        text = "Guess Number",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp)
}

@Composable
fun Answer(viewModel: GuessNumberViewModel = koinViewModel()) {
    val answer by viewModel.answer.observeAsState(0)
    Text(text = "Correct answer is $answer", fontWeight = FontWeight.W600)
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun InputField(viewModel: GuessNumberViewModel = koinViewModel()) {
    val value by viewModel.guessesNumber.observeAsState()
    fun onGameStatusChange(newStatus: GameStatus) =
        viewModel.onGameStatusChange(newStatus)

    val keyboardController = LocalSoftwareKeyboardController.current

    val guessesNumber by viewModel.guessesNumber.observeAsState("")

    OutlinedTextField(
        value = value.toString(),
        onValueChange = { viewModel.onGuessesNumberChanged(it) },
        label = { Text("Your answer here") },
        shape = RoundedCornerShape(20),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Decimal
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (guessesNumber.isNotEmpty() && guessesNumber.isDigitsOnly()) {
                    if (viewModel.checkAnswer()) {
                        onGameStatusChange(GameStatus.Won)
                    } else {
                        onGameStatusChange(GameStatus.Lose)
                    }
                } else {
                    onGameStatusChange(GameStatus.InputError)
                }
                keyboardController?.hide() // hide keyboard
                viewModel.onShowDialogChange()
            },
        ),
    )
}

@Composable
fun RegenerateBtn(viewModel: GuessNumberViewModel = koinViewModel()) {
    Button(onClick = { viewModel.changeAnswer() }) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Regenerate a new number")
        }
    }
}

@Composable
fun ResultDialog(viewModel: GuessNumberViewModel = koinViewModel()) {
    val context = LocalContext.current
    val gameStatus by viewModel.gameStatus.observeAsState(GameStatus.InputError)
    val showDialog by viewModel.showDialog.observeAsState(false)

    if (showDialog) {
        when (gameStatus) {
            GameStatus.Won -> {
                Dialog(onDismissRequest = { viewModel.onShowDialogChange() }) {
                    SuccessCard()
                }
            }
            GameStatus.Lose -> {
                Toast.makeText(context, "Guess again!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Dialog(onDismissRequest = { viewModel.onShowDialogChange() }) {
                    ErrorCard()
                }
            }
        }
    }
}

@Composable
fun ErrorCard(viewModel: GuessNumberViewModel = koinViewModel()) {
    val onDismiss = { viewModel.onShowDialogChange() }
    Card(modifier = Modifier.padding(20.dp), border = BorderStroke(2.dp, Color.Red)) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Input Error!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp)
            Text(
                text = "Please re-enter again",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)

            Button(onClick = onDismiss) {
                Text("Ok")
            }
        }
    }
}

@Composable
fun SuccessCard(viewModel: GuessNumberViewModel = koinViewModel()) {
    val onDismiss = { viewModel.onShowDialogChange() }
    Card(modifier = Modifier.padding(20.dp), border = BorderStroke(2.dp, Color.Red)) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Success!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp)
            Text(
                text = "You win",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)

            Button(onClick = {
                onDismiss.invoke()
                viewModel.changeAnswer()
            }) {
                Text("Regenerate a number")
            }
        }
    }
}