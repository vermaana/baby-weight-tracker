package com.anni.babyweighttracker.common.util

interface ModelMapper<SourceModel, DestinationModel> {
    fun sourceToDestination(sourceModel: SourceModel): DestinationModel

    fun sourceToDestination(sourceModel: List<SourceModel>): List<DestinationModel> =
        sourceModel.map {
            sourceToDestination(it)
        }
}
