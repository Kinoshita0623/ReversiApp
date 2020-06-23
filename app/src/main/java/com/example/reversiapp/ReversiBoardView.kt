package com.example.reversiapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import jp.panta.reversi.Board

class ReversiBoardView : View{

    interface OnCellClickedListener{

        fun onClick(reversiBoardView: ReversiBoardView, x: Int, y: Int)
    }

    var cellClickedListener: OnCellClickedListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var cellCount = 8
    private var mBoard: Board? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = if(MeasureSpec.getSize(widthMeasureSpec) > MeasureSpec.getSize(heightMeasureSpec)){
            heightMeasureSpec
        }else{
            widthMeasureSpec
        }
        super.onMeasure(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?: return

        val paint = Paint()


        canvas.drawColor(Color.argb(255, 0, 125, 0))

        paint.color = Color.argb(255, 0, 0, 0)
        paint.strokeWidth = 2F
        paint.isAntiAlias = true



        val cellSize = getCellSize()
        for( i in 1 until cellCount ){
            canvas.drawLine( 0F, cellSize.toFloat() * i, width.toFloat(), cellSize.toFloat() * i, paint)
            canvas.drawLine( cellSize.toFloat() * i, 0F, cellSize.toFloat() * i, height.toFloat(), paint)

        }

        val board = this.mBoard?.board?: return
        for( i in board.indices){
            for( j in board[i].indices){
                val half = cellSize.toFloat() / 2;
                val cx = (j + 1) * cellSize - half
                val cy = (i + 1) * cellSize - half
                if(board[i][j] == Board.BLACK){
                    paint.color = Color.argb(255, 0, 0, 0)
                    canvas.drawCircle( cx, cy, half - 10,  paint)
                }else if(board[i][j] == Board.WHITE){
                    paint.color = Color.argb(255, 255, 255, 255)

                    canvas.drawCircle( cx, cy, half - 10,  paint)
                }


            }
        }




    }

    fun setBoard(board: Board){
        val array= board.board
        cellCount = array.size
        mBoard = board;
        invalidate()
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // タッチの位置からタッチされたセルの位置を求めコールバックする

        val x = event?.x
            ?: return false
        val y = event.y

        var cellX: Int? = null
        var cellY: Int? = null

        val cellSize = getCellSize()
        for( i in 0 until cellCount ){
            val start = (if( i == 0 ) 0 else i * cellSize).toFloat()
            val end = ((i + 1) * cellSize).toFloat()
            if(x in start..end){
                cellX = i
            }
            if(y in start..end){
                cellY = i
            }

        }

        Log.d("ReversiBoardView", "クリックされた:$cellX, $cellY")
        if(cellX != null && cellY != null){
            cellClickedListener?.onClick(this, cellX, cellY)
        }else{
            return false
        }

        return super.onTouchEvent(event)
    }

    private fun getCellSize(): Int{
        return width / cellCount
    }
}