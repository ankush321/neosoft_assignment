package com.example.si_assignment.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.si_assignment.data.model.ODIMatch
import com.example.si_assignment.ui.adapter.model.SpinnerModel
import com.example.si_assignment.data.model.match.MatchDetails
import com.example.si_assignment.data.model.team.Team
import com.example.si_assignment.data.model.team.player.PlayerDetail
import com.example.si_assignment.databinding.ActivityPlayerDetailBinding
import com.example.si_assignment.ui.adapter.PlayerAdapter
import com.example.si_assignment.ui.adapter.SpinnerAdapter
import com.example.si_assignment.util.AppConstant
import kotlin.properties.Delegates

class PlayerDetail : AppCompatActivity(), SpinnerAdapter.OnItemSelect {

    private lateinit var selectTeam: Team
    private lateinit var odiMatch: ODIMatch
    private lateinit var homeTeam: Team
    private lateinit var awayTeam: Team
    private lateinit var binding: ActivityPlayerDetailBinding

    private var playerList: ArrayList<PlayerDetail> = ArrayList()
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectTeam = intent.getParcelableExtra(AppConstant.SELECTED_TEAM)!!
        odiMatch = intent.getParcelableExtra(AppConstant.ODI_MATCH_DATA)!!

        setSpinnerAdapter()
        setPlayerAdapter()

        Log.d("Players ", selectTeam.toString())
    }

    private fun setPlayerAdapter() {
        playerList.addAll(ArrayList(selectTeam.players.values.sortedBy { it.position }))
        adapter = PlayerAdapter(this, playerList)
        binding.playerRecView.layoutManager = LinearLayoutManager(this)
        binding.playerRecView.adapter = adapter
    }

    private fun setSpinnerAdapter() {
        val matchDetails: MatchDetails

        var homeTeamId by Delegates.notNull<Int>()
        var awayTeamId by Delegates.notNull<Int>()
        odiMatch.let {
            matchDetails = it.matchDetail
            homeTeamId = matchDetails.teamHome.toInt()
            awayTeamId = matchDetails.teamAway.toInt()
            homeTeam = it.teams[homeTeamId]!!
            awayTeam = it.teams[awayTeamId]!!
        }

        val list: List<SpinnerModel> = arrayListOf(
            SpinnerModel(0, "All"), SpinnerModel(homeTeamId, homeTeam.nameShort),
            SpinnerModel (awayTeamId, awayTeam.nameShort)
        )

        val spinnerAdapter = SpinnerAdapter(this, list, this)
        binding.dropdownSpinner.adapter = spinnerAdapter
    }

    override fun onClick(teamId: Int) {
        playerList.clear()
        adapter.notifyDataSetChanged()
        if(teamId == 0){
            playerList.addAll(ArrayList(homeTeam.players.values.sortedBy { it.position }))
            playerList.addAll(ArrayList(awayTeam.players.values.sortedBy {it.position}))
        }
        else{
            playerList.addAll(ArrayList(odiMatch.teams[teamId]?.players?.values?.sortedBy { it.position }))
        }
        adapter.notifyDataSetChanged()
    }
}