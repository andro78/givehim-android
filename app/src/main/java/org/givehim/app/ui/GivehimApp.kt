package org.givehim.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.givehim.app.MainViewModel
import org.givehim.app.model.StoryDraft

private enum class Tab { Home, Stories, Submit }

@Composable fun GivehimApp(vm: MainViewModel = viewModel()) {
    var tab by remember { mutableStateOf(Tab.Home) }
    Scaffold(bottomBar = { NavigationBar {
        NavigationBarItem(selected = tab == Tab.Home, onClick = { tab = Tab.Home }, icon = { Icon(Icons.Outlined.Home, null) }, label = { Text("홈") })
        NavigationBarItem(selected = tab == Tab.Stories, onClick = { tab = Tab.Stories }, icon = { Icon(Icons.Outlined.FavoriteBorder, null) }, label = { Text("사연") })
        NavigationBarItem(selected = tab == Tab.Submit, onClick = { tab = Tab.Submit }, icon = { Icon(Icons.Outlined.AddCircle, null) }, label = { Text("사연 접수") })
    } }) { padding -> Box(Modifier.padding(padding)) { when (tab) { Tab.Home -> HomeScreen({ tab = Tab.Stories }, { tab = Tab.Submit }); Tab.Stories -> StoriesScreen(vm); Tab.Submit -> SubmitScreen(vm) } } }
}

@Composable private fun Page(content: @Composable ColumnScope.() -> Unit) = Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 28.dp), verticalArrangement = Arrangement.spacedBy(18.dp), content = content)

@Composable private fun HomeScreen(openStories: () -> Unit, submit: () -> Unit) = Page {
    Text("GIVE WITH CONFIDENCE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
    Text("마음을 보내고,\n변화를 확인하세요.", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
    Text("도움이 필요한 이야기를 안전하게 듣고, 확인 가능한 근거와 결과로 연결합니다.", style = MaterialTheme.typography.bodyLarge)
    Card { Text("기부힘은 윤준호·손효준 공동대표 후보 2인이 준비 중인 프로젝트입니다. 아직 등록된 비영리단체가 아니며 현재 앱에서 후원금이나 기부금을 받지 않습니다.", Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall) }
    Button(submit, Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("사연 전하기") }
    OutlinedButton(openStories, Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("확인된 사연 보기") }
    HorizontalDivider(Modifier.padding(vertical = 14.dp))
    TrustItem("01", "먼저 확인합니다", "사연을 바로 공개하지 않고 당사자 동의와 기본 사실을 확인합니다.")
    TrustItem("02", "필요한 도움을 정리합니다", "금액보다 상황과 해결에 필요한 도움을 구체적으로 정리합니다.")
    TrustItem("03", "결과까지 연결합니다", "처음 계획과 실제 지원 결과를 비교해 공개합니다.")
}

@Composable private fun TrustItem(number: String, title: String, body: String) { Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { Text(number, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold); Column { Text(title, fontWeight = FontWeight.Bold); Text(body, style = MaterialTheme.typography.bodyMedium) } } }

@Composable private fun StoriesScreen(vm: MainViewModel) {
    val state by vm.stories.collectAsStateWithLifecycle()
    Page {
        Text("확인된 사연", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("당사자 동의와 기본 확인을 마친 사연만 보여드립니다.")
        when { state.loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally)); state.error != null -> { Text(state.error ?: "오류가 발생했습니다.", color = MaterialTheme.colorScheme.error); OutlinedButton(vm::refresh) { Text("다시 시도") } }; state.stories.isEmpty() -> Card { Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("첫 사연을 기다리고 있습니다.", fontWeight = FontWeight.Bold); Text("확인과 동의를 마친 사연이 이곳에 소개됩니다.") } }; else -> state.stories.forEach { story -> Card { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(story.category, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium); Text(story.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(story.summary); Text("${story.nickname} · ${story.publishedAt}", style = MaterialTheme.typography.labelSmall) } } }
        }
    }
}

@Composable private fun SubmitScreen(vm: MainViewModel) {
    val state by vm.submit.collectAsStateWithLifecycle(); var category by remember { mutableStateOf("living") }; var title by remember { mutableStateOf("") }; var nickname by remember { mutableStateOf("") }; var contact by remember { mutableStateOf("") }; var story by remember { mutableStateOf("") }; var help by remember { mutableStateOf("") }; var consent by remember { mutableStateOf(false) }
    if (state.receipt != null) AlertDialog(onDismissRequest = vm::clearReceipt, confirmButton = { TextButton(vm::clearReceipt) { Text("확인") } }, title = { Text("사연이 접수됐습니다") }, text = { Text("접수번호 ${state.receipt}\n검토 후 입력하신 연락처로 안내드리겠습니다.") })
    Page {
        Text("사연을 전해주세요", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("접수 내용은 바로 공개되지 않습니다. 연락과 검토 후 동의한 범위만 공개합니다.")
        Text("주민등록번호·계좌번호·진단서 원본·아동의 학교와 정확한 위치는 입력하지 마세요.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        Labeled("분야") { Dropdown(category) { category = it } }
        Labeled("사연 제목") { OutlinedTextField(title, { title = it.take(100) }, Modifier.fillMaxWidth(), singleLine = true) }
        Labeled("이름 또는 별칭") { OutlinedTextField(nickname, { nickname = it.take(40) }, Modifier.fillMaxWidth(), singleLine = true) }
        Labeled("연락 가능한 이메일 또는 전화번호") { OutlinedTextField(contact, { contact = it.take(120) }, Modifier.fillMaxWidth(), singleLine = true) }
        Labeled("사연 내용 (50자 이상)") { OutlinedTextField(story, { story = it.take(3000) }, Modifier.fillMaxWidth().heightIn(min = 180.dp)); Text("${story.length}/3000", style = MaterialTheme.typography.labelSmall) }
        Labeled("필요한 도움") { OutlinedTextField(help, { help = it.take(500) }, Modifier.fillMaxWidth().heightIn(min = 110.dp)) }
        Row(verticalAlignment = Alignment.Top) { Checkbox(consent, { consent = it }); Text("기부힘이 연락과 검토를 위해 제출 내용을 저장하는 데 동의합니다. 공개 전 최종 동의를 다시 확인합니다.", Modifier.padding(top = 10.dp)) }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { vm.submit(StoryDraft(category, title.trim(), nickname.trim(), contact.trim(), story.trim(), help.trim())) }, enabled = !state.sending && consent && title.isNotBlank() && nickname.isNotBlank() && contact.isNotBlank() && story.length >= 50 && help.isNotBlank(), modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp)) { Text(if (state.sending) "접수 중…" else "비공개로 사연 접수하기") }
    }
}

@Composable private fun Labeled(label: String, content: @Composable ColumnScope.() -> Unit) = Column(verticalArrangement = Arrangement.spacedBy(7.dp)) { Text(label, fontWeight = FontWeight.Bold); content() }

@Composable private fun Dropdown(selected: String, select: (String) -> Unit) { var expanded by remember { mutableStateOf(false) }; val labels = mapOf("living" to "생계·주거", "medical" to "의료·건강", "care" to "돌봄", "education" to "교육", "community" to "지역사회", "other" to "기타"); Box { OutlinedButton({ expanded = true }, Modifier.fillMaxWidth()) { Text(labels[selected] ?: selected) }; DropdownMenu(expanded, { expanded = false }) { labels.forEach { (key, value) -> DropdownMenuItem({ Text(value) }, { select(key); expanded = false }) } } } }
