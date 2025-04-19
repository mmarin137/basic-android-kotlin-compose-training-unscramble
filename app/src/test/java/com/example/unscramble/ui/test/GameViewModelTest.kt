package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.model.GameViewModel
import com.example.unscramble.ui.state.GameUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val gameViewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset()  {
        var currentGameUiState = gameViewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        gameViewModel.updateUserGuess(correctPlayerWord)
        gameViewModel.checkUserGuess()

        currentGameUiState = gameViewModel.uiState.value

        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet()  {
        val wrongUserGuess = "kakoa"

        gameViewModel.updateUserGuess(wrongUserGuess)
        gameViewModel.checkUserGuess()

        val currentGameUiState = gameViewModel.uiState.value

        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertTrue(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(SCORE_AFTER_FIRST_INCORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_InitialState_InitialValuesSet()  {
        val initialGameUiState: GameUiState = gameViewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(initialGameUiState.currentScrambledWord)

        assertNotEquals(unScrambledWord, initialGameUiState.currentScrambledWord)
        assertFalse(initialGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_INCORRECT_ANSWER, initialGameUiState.score)
        assertEquals(1, initialGameUiState.currentWordCount)
        assertFalse(initialGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = gameViewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            gameViewModel.updateUserGuess(correctPlayerWord)
            gameViewModel.checkUserGuess()
            currentGameUiState = gameViewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = gameViewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        gameViewModel.updateUserGuess(correctPlayerWord)
        gameViewModel.checkUserGuess()

        currentGameUiState = gameViewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount
        gameViewModel.skipWord()
        currentGameUiState = gameViewModel.uiState.value
        // Assert that score remains unchanged after word is skipped.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        // Assert that word count is increased by 1 after word is skipped.
        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
        private const val SCORE_AFTER_FIRST_INCORRECT_ANSWER = 0
    }

}