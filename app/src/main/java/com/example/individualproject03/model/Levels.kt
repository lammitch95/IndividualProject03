package com.example.individualproject03.model

data class Level(
    val mode: String,
    val levelNumber: Int,
    val solution: List<String>,
    val path: List<Pair<Int, Int>>,
    val startTile: Pair<Int, Int>,
    val endTile: Pair<Int, Int>
)

object LevelRepository {
    val levels = listOf(
        Level(
            mode = "Easy",
            levelNumber = 1,
            solution = listOf("right"),
            path = listOf(
                0 to 0,
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                5 to 0,
                6 to 0
            ),
            startTile = 0 to 0,
            endTile = 6 to 0
        ),
        Level(
            mode = "Easy",
            levelNumber = 2,
            solution = listOf("down","right"),
            path = listOf(
                0 to 0,
                0 to 1,
                0 to 2,
                0 to 3,
                0 to 4,
                0 to 5,
                0 to 6,
                0 to 7,
                0 to 8,
                1 to 8,
                2 to 8,
                3 to 8,
                4 to 8,
                5 to 8,
                6 to 8
            ),
            startTile = 0 to 0,
            endTile = 6 to 8
        ),
        Level(
            mode = "Easy",
            levelNumber = 3,
            solution = listOf("right", "down", "right"),
            path = listOf(
                0 to 0,
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                5 to 0,
                5 to 1,
                5 to 2,
                5 to 3,
                5 to 4,
                5 to 5,
                5 to 6,
                6 to 6,
                7 to 6,
                8 to 6,
                9 to 6,

            ),
            startTile = 0 to 0,
            endTile =  9 to 6
        ),

        Level(
            mode = "Hard",
            levelNumber = 1,
            solution = listOf("down","right","down","left"),
            path = listOf(
                0 to 0,
                0 to 1,
                0 to 2,
                0 to 3,
                0 to 4,
                1 to 4,
                2 to 4,
                3 to 4,
                4 to 4,
                4 to 5,
                4 to 6,
                4 to 7,
                3 to 7,
                2 to 7,
                1 to 7
            ),
            startTile = 0 to 0,
            endTile = 1 to 7
        ),
        Level(
            mode = "Hard",
            levelNumber = 2,
            solution = listOf("right", "down", "left", "down", "right"),
            path = listOf(
                0 to 0,
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                4 to 1,
                4 to 2,
                4 to 3,
                4 to 4,
                3 to 4,
                2 to 4,
                1 to 4,
                1 to 5,
                1 to 6,
                1 to 7,
                1 to 8,
                2 to 8,
                3 to 8,
                4 to 8,
                5 to 8,
                6 to 8,
                7 to 8

            ),
            startTile = 0 to 0,
            endTile = 7 to 8
        ),
        Level(
            mode = "Hard",
            levelNumber = 3,
            solution = listOf("down", "right", "up", "left", "down"),
            path = listOf(
                0 to 0,
                0 to 1,
                0 to 2,
                0 to 3,
                0 to 4,
                0 to 5,
                0 to 6,
                1 to 6,
                2 to 6,
                3 to 6,
                4 to 6,
                5 to 6,
                6 to 6,
                7 to 6,
                8 to 6,
                8 to 5,
                8 to 4,
                8 to 3,
                8 to 2,
                8 to 1,
                7 to 1,
                6 to 1,
                5 to 1,
                4 to 1,
                3 to 1,
                3 to 2,
                3 to 3,
                3 to 4

            ),
            startTile = 0 to 0,
            endTile = 3 to 4
        )
    )

    fun getLevelsByMode(mode: String): List<Level> {

        return levels.filter { it.mode == mode }
    }

    fun getLevel(mode: String, levelNumber: Int): Level? {
        return levels.find { it.mode == mode && it.levelNumber == levelNumber }
    }
}