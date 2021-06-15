package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.filter { findTrips(it).isEmpty() }
        .toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { findTrips(it).size >= minTrips }
        .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val driverTrips = findTrips(driver)
    return allPassengers.filter { passenger -> driverTrips.filter { passenger in it.passengers }.count() > 1 }
        .toSet()
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { passenger ->
        val (withDiscount, withoutDiscount) = findTrips(passenger).partition { it.hasDiscount() }
        withDiscount.size > withoutDiscount.size
    }
        .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val step = 10
    val maxDuration = trips.maxOfOrNull { it.duration } ?: return null

    val buckets = generateSequence(0) { it + step }
        .takeWhile { it <= maxDuration + step }
        .zipWithNext()
        .map { it.first until it.second }

    return trips.groupBy { trip ->
        buckets.find { bucket -> trip.duration in bucket }
    }
        .maxByOrNull { (_, trips) -> trips.size }
        ?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val totalIncome = trips.sumByDouble { it.cost }
    val driversByIncomeDesc = allDrivers.map { driver -> driver to findTrips(driver).sumByDouble { it.cost } }
        .sortedByDescending { it.second }

    val top20DriversIncome = driversByIncomeDesc.subList(0, (driversByIncomeDesc.size * 0.2).toInt())
        .sumByDouble { it.second }

    return ((top20DriversIncome / totalIncome) * 100) >= 80
}

fun TaxiPark.findTrips(passenger: Passenger): List<Trip> = trips.filter { passenger in it.passengers }
fun TaxiPark.findTrips(driver: Driver): List<Trip> = trips.filter { driver == it.driver }
fun Trip.hasDiscount() = discount != null
