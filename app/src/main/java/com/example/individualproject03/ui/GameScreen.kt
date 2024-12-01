package com.example.individualproject03.ui

import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.individualproject03.R
import com.example.individualproject03.data.database.AppDatabase
import kotlinx.coroutines.delay

/**
 * GameScreen.kt
 *
 * This composable function represents the main game screen for the app, which includes
 * the game board, directional controls, and a play button. The screen allows users
 * to play the game by interacting with a path represented on a grid. The player
 * progresses by moving a smiley icon across the grid according to a predefined path.
 *
 * The game involves a series of tiles that the player must follow in the correct
 * sequence of directions (up, down, left, right). The movement of the icon is animated
 * based on the path, and the game checks for correct or incorrect directions,
 * updating the player with the appropriate feedback.
 *
 * The GameScreen composable also contains UI elements like a progress indicator,
 * animated feedback, and direction controls that are draggable and dropable.
 *
 */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(navHostController: NavHostController){

    val context = LocalContext.current
    val appDatabase = remember { AppDatabase.getDatabase(context) }
    val gameScreenViewModel: GameScreenViewModel = viewModel(
        factory = GameScreenViewModel.provideFactory(appDatabase)
    )
    val pathTiles = remember { mutableStateOf(emptyList<Tile>()) }
    val tileSize = with(LocalDensity.current) { 35.dp.toPx() }

    // Target position for the icon
    val targetPosition = remember { mutableStateOf(IntOffset(0, 0)) }

    // Animate the icon's position
    val animatedIconPosition by animateIntOffsetAsState(
        targetValue = targetPosition.value,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "moveIconAnimation"
    )


    var hastStarted by remember{ mutableStateOf(false) }
    var prevPathTile by remember {mutableStateOf<Tile?>(null)}

    // State to control when the movement starts
    val isPlaying = remember { mutableStateOf(false) }

    // Button click handler to start the movement
    fun onPlayButtonClick() {
        isPlaying.value = true
    }

    LaunchedEffect(hastStarted) {
        val firstTile = pathTiles.value.firstOrNull()

        if (firstTile != null && !hastStarted) {
            hastStarted = true
            targetPosition.value = IntOffset(
                firstTile.screenX.toInt(),
                firstTile.screenY.toInt()+35
            )
        }
    }


    // Trigger movement after play is clicked
    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value) {
            // Animate through path tiles (moving the icon along the path)
            val tilesSnapshot = pathTiles.value
            var trackArrowIndex = 0
            val currentLevelMap = gameScreenViewModel.levelMapState.value

            for (pathTile in tilesSnapshot) {
                Log.d("GameScreen", "Moving to tile: (${pathTile.gridX}, ${pathTile.gridY})")
                if(currentLevelMap!= null){
                    val solutionArrow = currentLevelMap.solution[trackArrowIndex]
                    val chosenArrow = gameScreenViewModel.directionSequence.value[trackArrowIndex]

                    Log.d("GameScreen", "Current direction: $chosenArrow, Solution direction: $solutionArrow")

                    if(chosenArrow == solutionArrow){
                        if (prevPathTile != null && trackArrowIndex < currentLevelMap.solution.size - 1) {
                            when (chosenArrow) {
                                "up" -> if (pathTile.gridY != prevPathTile!!.gridY + 1) trackArrowIndex++
                                "down" -> if (pathTile.gridY != prevPathTile!!.gridY - 1) trackArrowIndex++
                                "right" -> if (pathTile.gridX != prevPathTile!!.gridX + 1) trackArrowIndex++
                                "left" -> if (pathTile.gridX != prevPathTile!!.gridX - 1) trackArrowIndex++
                            }
                        }

                        prevPathTile = pathTile

                        Log.d("GameScreen", "Now Changing New Target Position; trackArrowIndex: $trackArrowIndex")

                        targetPosition.value = IntOffset(
                            pathTile.screenX.toInt(),
                            pathTile.screenY.toInt()+35
                        )

                        if(pathTile == tilesSnapshot[tilesSnapshot.size-1]){
                            Log.d("GameScreen", "Level Completed")
                            Toast.makeText(context, "Level Completed.", Toast.LENGTH_SHORT).show()
                            isPlaying.value = false
                            hastStarted = false
                            prevPathTile = null
                            if(gameScreenViewModel.currentModeProgress.value < 3){
                                gameScreenViewModel.updateModeProgress(gameScreenViewModel.currentModeProgress.value + 1)
                            }

                            break
                        }

                        delay(500)  // Delay between each movement step (adjust as needed)
                    }else{
                        Log.e("GameScreen", "Direction mismatch, stopping movement")
                        Toast.makeText(context, "Wrong Movement. Try Again.", Toast.LENGTH_SHORT).show()
                        isPlaying.value = false
                        hastStarted = false
                        prevPathTile = null

                        break
                    }

                }


            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(201, 1, 254),
                        Color(0, 76, 249)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
           "${gameScreenViewModel.currentMode.value}: ${gameScreenViewModel.currentModeProgress.value}/3",
            modifier = Modifier
                .padding(vertical = 10.dp),
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            color = when (gameScreenViewModel.currentMode.value) {
                "Easy" -> Color.Green
                "Hard" -> Color.Red
                else -> Color.Black
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .background(Color(43, 128, 27, 255))
            ){

                Icon(
                    painter = painterResource(id = R.drawable.smile_head),
                    contentDescription = "MoveIcon",
                    tint = Color(255, 0, 255, 255),
                    modifier = Modifier
                        .size(40.dp)
                        .zIndex(1f)
                        .offset { animatedIconPosition }
                )


                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {

                    val levelMap = gameScreenViewModel.levelMapState.value
                    if(levelMap != null){

                        pathTiles.value = emptyList<Tile>()

                        val retrievePath = levelMap.path
                        val (startX, startY) = levelMap.startTile
                        val (endX, endY) = levelMap.endTile

                        repeat(gameScreenViewModel.gridHeight.value) { rowIndex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(gameScreenViewModel.gridWidth.value) { columnIndex ->

                                    val screenX = (columnIndex * tileSize) + (tileSize / 2)
                                    val screenY = (rowIndex * tileSize) + (tileSize / 2)

                                    var chosenColor = Color(17, 100, 1, 255)
                                    val chosenCoordinate = columnIndex to rowIndex

                                    if(retrievePath.contains(chosenCoordinate)){
                                        chosenColor = Color(117, 82, 39, 255)
                                        val pathTile = Tile(
                                            gridX = columnIndex,
                                            gridY = rowIndex,
                                            screenX = screenX,
                                            screenY = screenY
                                        )
                                        pathTiles.value += pathTile

                                        if(startX == columnIndex && startY == rowIndex){
                                            chosenColor = Color(41, 255, 0, 255)
                                        }else if(endX == columnIndex && endY == rowIndex){
                                            chosenColor = Color(255, 221, 0, 255)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .background(chosenColor)
                                    )
                                }
                            }
                        }



                        pathTiles.value = pathTiles.value.sortedBy { tile ->
                            val coordinate = tile.gridX to tile.gridY
                            retrievePath.indexOf(coordinate)
                        }

                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f)
                    .background(Color(148, 104, 57, 255))
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically

                ) {


                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(255, 132, 0, 255))
                                .border(3.dp, Color(255, 255, 255))
                                .padding(4.dp)
                                .dragAndDropTarget(
                                    shouldStartDragAndDrop = { event ->
                                        event
                                            .mimeTypes()
                                            .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                    },
                                    target = remember {
                                        object : DragAndDropTarget {
                                            override fun onDrop(event: DragAndDropEvent): Boolean {

                                                gameScreenViewModel.setDroppedBoxIndex(index)
                                                gameScreenViewModel.updateDirection()
                                                return true
                                            }
                                        }
                                    }
                                )
                        ) {
                            val directionPainter = when (gameScreenViewModel.directionSequence.value[index]) {
                                "up" -> painterResource(id = R.drawable.up_arrow)
                                "down" -> painterResource(id = R.drawable.down_arrow)
                                "left" -> painterResource(id = R.drawable.left_arrow)
                                "right" -> painterResource(id = R.drawable.right_arrow)
                                else -> null
                            }

                            if (directionPainter != null) {
                                Icon(
                                    painter = directionPainter,
                                    contentDescription = "Icon",
                                    tint = Color.Blue,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }

                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0, 100, 167, 255)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(12, 142, 158, 255))
                                .padding(2.dp)
                        ){
                            this@Row.AnimatedVisibility(
                                visible = true,
                                enter = scaleIn() + fadeIn(),
                                exit = scaleOut() + fadeOut()
                            ) {
                                Icon(
                                    painter = painterResource(id = when(gameScreenViewModel.initialDirections[index]){
                                        "up" -> R.drawable.up_arrow
                                        "down" -> R.drawable.down_arrow
                                        "left" -> R.drawable.left_arrow
                                        "right" -> R.drawable.right_arrow
                                        else -> {
                                            R.drawable.baseline_error_outline_24
                                        }
                                    }),
                                    contentDescription = "direction arrow",
                                    tint = Color(0, 217, 255, 255),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .dragAndDropSource {
                                            detectTapGestures(
                                                onLongPress = { offset ->
                                                    gameScreenViewModel.setSelectedArrowIndex(index)
                                                    startTransfer(
                                                        transferData = DragAndDropTransferData(
                                                            clipData = ClipData.newPlainText(
                                                                "drag-icon",
                                                                "DirectionArrow"
                                                            )
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                )
                            }
                           
                        }
                    }
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color(29, 43, 70, 255))
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                IconButton(
                    onClick = { onPlayButtonClick()},
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_circle_24),
                        contentDescription = "play icon",
                        tint = Color(110, 197, 75, 255),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                IconButton(
                    onClick = {
                        Log.e("GameScreen", "RESETING LEVEL...")
                        isPlaying.value = false
                        hastStarted = false
                        prevPathTile = null
                        gameScreenViewModel.resetLevel()
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_circle_24),
                        contentDescription = "reset icon",
                        tint = Color(250, 206, 0, 255),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                IconButton(
                    onClick = {
                        navHostController.navigate("home")
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_exit_to_app_24),
                        contentDescription = "exit icon",
                        tint = Color(255, 0, 0, 255),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }


            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen(){
    GameScreen(navHostController = rememberNavController())
}