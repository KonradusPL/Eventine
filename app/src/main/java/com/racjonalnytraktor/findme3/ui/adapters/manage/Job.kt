package com.racjonalnytraktor.findme3.ui.adapters.manage

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

data class Job(var name: String, var workersCount: Int,var workers: ArrayList<Worker>): ExpandableGroup<Worker>(name,workers)