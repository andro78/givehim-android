package org.givehim.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.givehim.app.data.StoryRepository
import org.givehim.app.model.Story
import org.givehim.app.model.StoryDraft

data class StoriesState(val loading: Boolean = true, val stories: List<Story> = emptyList(), val error: String? = null)
data class SubmitState(val sending: Boolean = false, val receipt: String? = null, val error: String? = null)

class MainViewModel(private val repository: StoryRepository = StoryRepository()) : ViewModel() {
    private val _stories = MutableStateFlow(StoriesState())
    val stories: StateFlow<StoriesState> = _stories.asStateFlow()
    private val _submit = MutableStateFlow(SubmitState())
    val submit: StateFlow<SubmitState> = _submit.asStateFlow()
    init { refresh() }
    fun refresh() = viewModelScope.launch { _stories.value = StoriesState(loading = true); _stories.value = runCatching { StoriesState(false, repository.stories()) }.getOrElse { StoriesState(false, error = it.message) } }
    fun submit(draft: StoryDraft) = viewModelScope.launch { _submit.value = SubmitState(sending = true); _submit.value = runCatching { SubmitState(receipt = repository.submit(draft)) }.getOrElse { SubmitState(error = it.message) } }
    fun clearReceipt() { _submit.value = SubmitState() }
}
