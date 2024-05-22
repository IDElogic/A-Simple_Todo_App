package se.iwoio.project.datastoredemo.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import se.iwoio.project.datastoredemo.R
import se.iwoio.project.datastoredemo.data.TodoItem
import se.iwoio.project.datastoredemo.data.TodoPriority
import se.iwoio.project.datastoredemo.ui.theme.Green
import se.iwoio.project.datastoredemo.ui.theme.Orange
import se.iwoio.project.datastoredemo.ui.theme.Yellow
import se.iwoio.project.datastoredemo.ui.util.SearchTopAppBar
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    todoListViewModel: TodoListViewModel = hiltViewModel(),
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var expanded by remember { mutableStateOf(false) }

    var todoToEdit: TodoItem? by rememberSaveable {
        mutableStateOf(null)
    }

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    var searchAppBarState by remember {
        mutableStateOf(false)
    }

    val isOrderByTitle = todoListViewModel.getOrderByTitle().collectAsState(initial = false)
    val isOrderByDesc = todoListViewModel.getOrderByDesc().collectAsState(initial = false)

    var selectedBottomTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            if (!searchAppBarState) {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = "TODO-APP-DEMO") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Green),
                    actions = {
                        IconButton(onClick = {
                            todoListViewModel.clearAllTodos()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                        IconButton(onClick = {
                            searchAppBarState = true
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                        IconButton(
                            onClick = { expanded = !expanded }
                        ) { Icon(Icons.Filled.MoreVert, contentDescription = null) }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(onClick = {
                                coroutineScope.launch {
                                    todoListViewModel.storeOrderByTitle(!isOrderByTitle.value)
                                    todoListViewModel.storeOrderByDesc(false)
                                }
                            }, text = {
                                if (isOrderByTitle.value) {
                                    Text(text = "(*) Sort by title")
                                } else {
                                    Text(text = "Sort by title")
                                }
                            })
                            DropdownMenuItem(
                                onClick = {
                                    coroutineScope.launch {
                                        todoListViewModel.storeOrderByTitle(false)
                                        todoListViewModel.storeOrderByDesc(!isOrderByDesc.value)
                                    }
                                },
                                text = {
                                    if (isOrderByDesc.value) {
                                        Text(text = "(*) Sort by description")
                                    } else {
                                        Text(text = "Sort by description")
                                    }
                                })
                        }

                    })
            } else {
                SearchTopAppBar(text = searchText,
                    onTextChange = { searchText = it },
                    onCloseClicked = {
                        searchAppBarState = false
                        searchText = ""
                    },
                    onSearchClicked = { it -> searchAppBarState = false })
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomAppBar(

                content = {
                NavigationBar(modifier = Modifier
                    .fillMaxWidth(),
                    containerColor = Color.Transparent
                ) {
                    NavigationBarItem(selected = selectedBottomTab == 0,
                        onClick = { selectedBottomTab = 0 },
                        label = {
                            Text(
                                text = "Todo",
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Todo",
                            )
                        })
                    NavigationBarItem(selected = selectedBottomTab == 1,
                        onClick = { selectedBottomTab = 1 },
                        label = {
                            Text(
                                text = "Done",
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                            )
                        })
                }
            })
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Green)
                    .padding(innerPadding)
            ) {
                if (showAddDialog) {
                    TodoForm(
                        onDialogClose = {
                            showAddDialog = false
                        },
                        todoListViewModel = todoListViewModel,
                        todoToEdit = todoToEdit
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                if (todoListViewModel.getAllToDoList().isEmpty())
                    Text(modifier = Modifier
                        .padding(horizontal = 20.dp),
                        text = "No items")
                else {
                    LazyColumn(modifier = Modifier
                        .fillMaxHeight()
                        .padding(20.dp)) {
                        items(todoListViewModel.getAllToDoList()) {
                            if (searchText.isEmpty() || it.title.contains(searchText)) {
                                if (selectedBottomTab == 0 && !it.isDone) {
                                    TodoCard(it,
                                        onRemoveItem = { todoListViewModel.removeTodoItem(it) },
                                        onTodoCheckChange = { checked ->
                                            todoListViewModel.changeTodoState(
                                                it,
                                                checked
                                            )
                                        },
                                        onEditItem = {
                                            showAddDialog = true
                                            todoToEdit = it
                                        }
                                    )
                                } else if (selectedBottomTab == 1 && it.isDone) {
                                    TodoCard(it,
                                        onRemoveItem = { todoListViewModel.removeTodoItem(it) },
                                        onTodoCheckChange = { checked ->
                                            todoListViewModel.changeTodoState(
                                                it, checked
                                            )
                                        },
                                        onEditItem = {
                                            showAddDialog = true
                                            todoToEdit = it
                                        })
                                }
                            }
                        }
                    }
                }

            }
        }
    )
}


@Composable
fun TodoCard(
    todoItem: TodoItem,
    onTodoCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (TodoItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Yellow,
        ),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = Modifier
            .padding(5.dp)
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .animateContentSize()
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .defaultMinSize(minHeight = 100.dp)
                    .fillMaxWidth(),
            ) {
                val (imgPriority, titleText, cbDone, iconDelete, iconEdit, iconExpanded) = createRefs()

                Image(
                    painter = painterResource(id = todoItem.priority.getIcon()),
                    contentDescription = "Priority",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                        .constrainAs(imgPriority) {
                            start.linkTo(parent.start, margin = 10.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )
                Checkbox(
                    checked = todoItem.isDone,
                    onCheckedChange = { onTodoCheckChange(it) },
                    modifier = Modifier.constrainAs(cbDone) {
                        start.linkTo(imgPriority.end, margin = 5.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )

                Text(todoItem.title,
                    modifier = Modifier
                        .constrainAs(titleText) {
                    start.linkTo(cbDone.end, margin = 5.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .clickable {
                            onRemoveItem()
                        }
                        .constrainAs(iconDelete) {
                            end.linkTo(iconExpanded.start, margin = 10.dp)
                            top.linkTo(parent.top, margin = 10.dp)
                        },
                    tint = Color.DarkGray
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = stringResource(R.string.icon_edit),
                    modifier = Modifier
                        .clickable {
                            onEditItem(todoItem)
                        }
                        .constrainAs(iconEdit) {
                            end.linkTo(iconExpanded.start, margin = 10.dp)
                            bottom.linkTo(parent.bottom, margin = 10.dp)
                        },
                    tint = Color.DarkGray
                )
                IconButton(onClick = { expanded = !expanded },
                    modifier = Modifier.constrainAs(iconExpanded) {
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    }

                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }

            }
            if (expanded) {
                Text(text = todoItem.description)
                Text(
                    text = todoItem.createDate, style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForm(
    todoListViewModel: TodoListViewModel = viewModel(),
    onDialogClose: () -> Unit = {},
    todoToEdit: TodoItem? = null
) {
    var newTodoTitle by remember { mutableStateOf(todoToEdit?.title ?: "") }
    var newTodoDesc by remember { mutableStateOf(todoToEdit?.description ?: "") }
    var newTodoPriority by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDialogClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Orange)
                .padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(value = newTodoTitle,
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Title",
                                color = Color.White) },
                        onValueChange = {
                            newTodoTitle = it
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(value = newTodoDesc,
                        modifier = Modifier.weight(1f),
                        label = {
                            Text(
                                text = "Description",
                                color = Color.White)},
                        onValueChange = {
                            newTodoDesc = it
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = newTodoPriority,
                        onCheckedChange = {
                        newTodoPriority = it
                        })
                    Text(
                        text = "Important",
                        color = Color.White)
                }

                Button(onClick = {
                    if (todoToEdit == null) {
                        todoListViewModel.addTodoList(
                            TodoItem(
                                id = UUID.randomUUID().toString(),
                                title = newTodoTitle,
                                description = newTodoDesc,
                                createDate = Date(System.currentTimeMillis()).toString(),
                                priority = if (newTodoPriority) TodoPriority.HIGH else TodoPriority.NORMAL,
                                isDone = false
                            )
                        )
                    } else { // EDIT mode
                        var todoEdited = todoToEdit.copy(
                            title = newTodoTitle,
                            description = newTodoDesc,
                            priority = if (newTodoPriority) TodoPriority.HIGH else TodoPriority.NORMAL,
                        )

                        todoListViewModel.editTodoItem(todoToEdit, todoEdited)
                    }

                    onDialogClose()
                },
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)) {
                    Text(text = "Save")
                }
            }
        }
    }
}