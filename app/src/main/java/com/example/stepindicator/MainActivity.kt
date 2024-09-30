package com.example.stepindicator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TimelineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.timelineRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val timelineItems = listOf(
            TimelineItem("Ordered", listOf(
                "Description 1 for Step 1",
                "Description 2 for Step 1",
                "Description 3 for Step 1"
            ), R.drawable.order_placed),
            TimelineItem("Shipped", listOf("Description for Step 2"), R.drawable.shipped),
            TimelineItem("Out to Delivery", listOf("Description for Step 3"), R.drawable.out_of_delivery),
            TimelineItem("Delivered", listOf("Description for Step 4"), R.drawable.delivered)
        )

        adapter = TimelineAdapter(timelineItems)
        recyclerView.adapter = adapter

        simulateOrderProgress()
    }

    private fun simulateOrderProgress() {
        val handler = Handler(Looper.getMainLooper())
        val delayBetweenSteps = 1000L // 3 seconds

        var totalSteps = 0
        for (itemIndex in 0 until adapter.itemCount) {
            val item = (adapter as TimelineAdapter).items[itemIndex]
            repeat(item.descriptions.size + 1) { descIndex ->
                handler.postDelayed({
                    if (descIndex < item.descriptions.size) {
                        adapter.updateProgress(itemIndex)
                    } else if (itemIndex < adapter.itemCount - 1) {
                        adapter.updateProgress(itemIndex + 1)
                    }
                }, totalSteps * delayBetweenSteps)
                totalSteps++
            }
        }
    }
}