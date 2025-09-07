package com.rajotiyapawan.pokedex.data.mapper

import com.rajotiyapawan.pokedex.data.remote.dto.AbilityDetailDto
import com.rajotiyapawan.pokedex.data.remote.dto.AbilityDto
import com.rajotiyapawan.pokedex.data.remote.dto.EggGroupDetailDto
import com.rajotiyapawan.pokedex.data.remote.dto.EvolutionChainDto
import com.rajotiyapawan.pokedex.data.remote.dto.FlavourEntryDto
import com.rajotiyapawan.pokedex.data.remote.dto.NameUrlDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokeTypesDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonDataDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonListDto
import com.rajotiyapawan.pokedex.data.remote.dto.PokemonSpeciesDataDto
import com.rajotiyapawan.pokedex.data.remote.dto.StatsDto
import com.rajotiyapawan.pokedex.domain.model.Ability
import com.rajotiyapawan.pokedex.domain.model.AbilityDetails
import com.rajotiyapawan.pokedex.domain.model.Chain
import com.rajotiyapawan.pokedex.domain.model.EggGroupDetail
import com.rajotiyapawan.pokedex.domain.model.EvolutionChain
import com.rajotiyapawan.pokedex.domain.model.EvolutionDetail
import com.rajotiyapawan.pokedex.domain.model.FlavourEntry
import com.rajotiyapawan.pokedex.domain.model.NameUrlItem
import com.rajotiyapawan.pokedex.domain.model.PokeType
import com.rajotiyapawan.pokedex.domain.model.PokemonBasicInfo
import com.rajotiyapawan.pokedex.domain.model.PokemonData
import com.rajotiyapawan.pokedex.domain.model.PokemonListData
import com.rajotiyapawan.pokedex.domain.model.PokemonSpeciesData
import com.rajotiyapawan.pokedex.domain.model.Stats

fun PokemonListDto.toDomain(): PokemonListData {
    return PokemonListData(
        count = count,
        next = next, previous = previous, results = results?.map { it.toDomain() }?.toList()
    )
}

fun NameUrlDto.toDomain(): NameUrlItem {
    return NameUrlItem(name, url)
}

fun PokemonDataDto.toDomain(): PokemonData {
    return PokemonData(
        id = id,
        name = name,
        imageUrl = sprites?.other?.officialArtwork?.frontDefault,
        types = types?.map { it.toDomain() }?.toList() ?: listOf(PokeType.NORMAL),
        weight = weight,
        height = height,
        baseExperience = baseExperience,
        abilities = abilities?.map { it.toDomain() }?.toList(),
        species = species?.toDomain(),
        stats = stats?.map { it.toDomain() }?.toList()
    )
}

fun PokeTypesDto.toDomain(): PokeType {
    return fromString(type?.name ?: "") ?: PokeType.NORMAL
}

fun fromString(value: String): PokeType? {
    return enumValues<PokeType>().firstOrNull { it.name.equals(value, ignoreCase = true) }
}

fun AbilityDto.toDomain(): Ability {
    return Ability(
        ability = ability?.toDomain(), isHidden = isHidden ?: false, slot = slot
    )
}

fun StatsDto.toDomain(): Stats {
    return Stats(baseStat = baseStat, effort = effort, stat = stat?.toDomain())
}

fun PokemonSpeciesDataDto.toDomain(): PokemonSpeciesData {
    val femalePercentage = (gender_rate / 8.0) * 100
    return PokemonSpeciesData(
        flavourText = flavor_text_entries
            ?.firstOrNull { it.language.name == "en" && it.version?.name == "ruby" }
            ?.flavor_text
            ?.replace("\n", " ") ?: "",
        genus = genera
            .firstOrNull { it.language.name == "en" }
            ?.genus ?: "",
        growthRate = growth_rate.name ?: "",
        femalePercentage = femalePercentage,
        malePercentage = 100 - femalePercentage,
        baseFriendship = base_happiness,
        hatchCounter = hatch_counter,
        eggGroups = egg_groups?.map { it.toDomain() }?.toList(),
        evolutionChain = evolution_chain?.toDomain(),
        varieties = varieties.map { it.toDomain() }.toList()
    )
}

fun PokemonSpeciesDataDto.VarietyDto.toDomain(): PokemonSpeciesData.Variety {
    return PokemonSpeciesData.Variety(
        is_default, pokemon.toDomain()
    )
}

fun PokemonDataDto.toBasicInfo(): PokemonBasicInfo {
    return PokemonBasicInfo(
        id = id ?: 0,
        name = name ?: "",
        imageUrl = sprites?.other?.officialArtwork?.frontDefault ?: "",
        types = types?.map { it.toDomain() }?.toList() ?: listOf(PokeType.NORMAL)
    )
}

fun AbilityDetailDto.toDomain(): AbilityDetails {
    return AbilityDetails(
        effectEntries = effectEntries.map { it.toDomain() }.toList(),
        flavorTextEntries = flavorTextEntries.map { it.toDomain() }.toList(),
        pokemon = pokemon.map { it.toDomain() }.toList()
    )
}

fun AbilityDetailDto.AbilityEffectDto.toDomain(): AbilityDetails.AbilityEffect {
    return AbilityDetails.AbilityEffect(
        effect, shortEffect, flavorText, language?.toDomain(), null
    )
}

fun FlavourEntryDto.toDomain(): FlavourEntry {
    return FlavourEntry(flavor_text, language.toDomain(), version?.toDomain(), version_group.toDomain())
}

fun AbilityDetailDto.AbilityHoldersDto.toDomain(): AbilityDetails.AbilityHolders {
    return AbilityDetails.AbilityHolders(
        isHidden, pokemon.toDomain(), slot
    )
}

fun EvolutionChainDto.toDomain(
    speciesVarieties: Map<String, List<String>>,
    suffix: String?
): EvolutionChain {
    return EvolutionChain(
        chain = chain?.toDomain(speciesVarieties, suffix)
    )
}

fun EvolutionChainDto.ChainDto.toDomain(
    speciesVarieties: Map<String, List<String>>,
    suffix: String?
): Chain {
    val baseName = species.name ?: ""
    // try to pick baseName + suffix if suffix != null and that exact variant exists in that species' varieties
    val mappedName = if (!suffix.isNullOrEmpty()) {
        val candidate = baseName + suffix
        val varieties = speciesVarieties[baseName] ?: emptyList()
        if (varieties.contains(candidate)) candidate else baseName
    } else baseName
    return Chain(
        evolvesTo = evolvesTo?.map { it.toDomain(speciesVarieties, suffix) } ?: listOf(),
        species = species.copy(name = mappedName).toDomain(),
        evolutionDetails = evolutionDetails.map { it.toDomain() }.toList()
    )
}

fun EvolutionChainDto.EvolutionDetailDto.toDomain(): EvolutionDetail {
    return EvolutionDetail(
        minLevel = minLevel ?: 0,
        minHappiness = minHappiness ?: 0,
        minAffection = minHappiness ?: 0,
        minBeauty = minBeauty ?: 0,
        needsOverworldRain = needsOverworldRain ?: false,
        timeOfDay = timeOfDay ?: "",
        trigger = trigger.toDomain(),
        turnUpsideDown = turnUpsideDown ?: false,
        item = item?.toDomain(),
        location = location?.toDomain(),
        knownMoveType = knownMoveType?.toDomain(),
        knownMove = knownMove?.toDomain(),
        tradeSpecies = tradeSpecies?.toDomain(),
        gender = gender ?: "",
        heldItem = heldItem?.toDomain(),
        partySpecies = partySpecies?.toDomain(),
        partyType = partyType?.toDomain(),
        relativePhysicalStats = relativePhysicalStats
    )
}

fun EggGroupDetailDto.toDomain(): EggGroupDetail {
    return EggGroupDetail(
        id = id,
        name = name,
        pokemonSpecies = pokemonSpecies.map { it.toDomain() }.toList()
    )
}
