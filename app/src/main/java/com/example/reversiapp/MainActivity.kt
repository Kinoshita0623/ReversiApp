package com.example.reversiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jp.panta.reversi.Board
import jp.panta.reversi.Bot
import jp.panta.reversi.Game
import jp.panta.reversi.Player
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bot = Bot()

        val player = object : Player(){
            override fun onSettlement(p0: Int) {
            }

            override fun onTurned(p0: Game?) {
                p0?.let{
                    boardView.setBoard(p0.board)
                }
            }
        }
        val reversiGame = Game(player, bot)
        boardView.setBoard(reversiGame.board)

        passButton.setOnClickListener {
            reversiGame.pass(player.discType)
        }

        surrenderButton.setOnClickListener {
            reversiGame.surrender(player.discType)
        }

        boardView.cellClickedListener = object : ReversiBoardView.OnCellClickedListener{
            override fun onClick(reversiBoardView: ReversiBoardView, x: Int, y: Int) {
                reversiGame.setDisc(Board.BLACK, x, y)
                boardView.setBoard(reversiGame.board)

            }
        }

        reversiGame.finishedCallback = Game.GameFinished {
            when(it.winner){
                Board.WHITE ->{

                    Toast.makeText(this, "あなたは負けました", Toast.LENGTH_LONG).show()
                }
                Board.BLACK ->{
                    Toast.makeText(this, "あなたの勝利", Toast.LENGTH_LONG).show()

                }

            }
        }
    }


}
