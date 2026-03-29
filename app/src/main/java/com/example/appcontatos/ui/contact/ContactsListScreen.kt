package com.example.appcontatos.ui.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcontatos.R
import com.example.appcontatos.data.Contact
import com.example.appcontatos.ui.theme.AppContatosTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
){
    val isInitialCompositionState: MutableState<Boolean> = rememberSaveable() {
        mutableStateOf(true)
    }
    val isLoadingState: MutableState<Boolean> = rememberSaveable() {
        mutableStateOf(false)
    }
    val isErrorState: MutableState<Boolean> = rememberSaveable() {
        mutableStateOf(false)
    }
    val contactsState: MutableState<List<Contact>> = rememberSaveable() {
        mutableStateOf(listOf())
    }

    val loadContacts: () -> Unit = {
        isLoadingState.value = true
        isErrorState.value = false

        coroutineScope.launch {
            delay(2000)
            isErrorState.value = Random.nextBoolean()
            if (!isErrorState.value) {
                val isEmpty = Random.nextBoolean()
                if (isEmpty) {
                    contactsState.value = listOf()
                } else {
                    contactsState.value = generateContacts()
                }
            }
            isLoadingState.value = false
        }
    }

    if (isInitialCompositionState.value) {
        loadContacts()
        isInitialCompositionState.value = false
    }

    val contentModifier = modifier.fillMaxSize()
    if (isLoadingState.value) {
        LoadingState(contentModifier)
    } else if (isErrorState.value) {
        ErrorState(contentModifier, loadContacts)
    } else {
        Scaffold(
            modifier = contentModifier,
            topBar = {
                AppBar(
                    onRefreshPressed = loadContacts
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    contactsState.value = contactsState.value.plus(
                        Contact(
                            id = contactsState.value.size + 1,
                            fistName = "Novo",
                            lastName = "Contato"
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Novo contato"
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Novo contato")
                }
            }
        ) { paddingValues ->
            val defaultModifier: Modifier = Modifier.padding(paddingValues)
            if (contactsState.value.isEmpty()) {
                EmptyList(defaultModifier)
            } else {
                List(
                    defaultModifier,
                    contactsState.value
                )
            }
        }
    }
}

private fun generateContacts(): List<Contact> {
    return listOf(
        Contact(
            id = 1,
            fistName = "Breno",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 2,
            fistName = "Naira",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 3,
            fistName = "Gabriel",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 4,
            fistName = "Lucas",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 5,
            fistName = "Matheus",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 6,
            fistName = "Pedro",
            lastName = "Zaffalon",
        ),
        Contact(
            id = 7,
            fistName = "João",
            lastName = "Zaffalon",
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onRefreshPressed: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(R.string.contatos))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(
                onClick = {
                    onRefreshPressed()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Atualizar"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        AppBar()
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun LoadingStatePreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        LoadingState()
    }
}

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    onRetryPressed: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = "Erro ao carregar",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        val textPadding = PaddingValues(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp
        )
        Text(
            text = "Ocorreu um erro ao carregar",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(textPadding)
        )
        Text(
            text = "Aguarde um momento e tente novamente",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(textPadding)
        )
        ElevatedButton(
            modifier = Modifier.padding(top = 8.dp),
            onClick = onRetryPressed
        ) {
            Text("Tentar novamente")
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun ErrorStatePreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        ErrorState()
    }
}

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.no_data),
            contentDescription = "Nenhum contato...",
        )
        Text(
            text = "Nenhum contato...",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Você ainda não adicionou nenhum cantato." +
                    "\nAdicione o primero utilizando o botão \"Novo contato\"",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun EmptyListPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        EmptyList()
    }
}

@Composable
fun List(
    modifier: Modifier = Modifier,
    contacts: List<Contact> = emptyList()
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(contacts) { contact ->
            ContactListItem(contact = contact)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        List(
            contacts = listOf(
                Contact(
                    id = 1,
                    fistName = "Breno",
                    lastName = "Zaffalon",
                    phoneNumber = "(11) 99999-9999",
                    isFavorite = true
                ),
                Contact(
                    id = 2,
                    fistName = "Naira",
                    lastName = "Zaffalon",
                    phoneNumber = "(11) 99999-9999",
                    isFavorite = false
                )
            )
        )
    }
}

@Composable
private fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: Contact
) {
    val isFavoriteState: MutableState<Boolean> = rememberSaveable() {
        mutableStateOf(contact.isFavorite)
    }
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(contact.fullName)
        },
        trailingContent = {
            IconButton(
                onClick = {
                    isFavoriteState.value = !isFavoriteState.value
                }
            ) {
                Icon(
                    imageVector = if (isFavoriteState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favoritar",
                    tint = if (isFavoriteState.value) Color.Red else Color.Gray
                )
            }
        }
    )
}