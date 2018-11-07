package sa.idc.driversapp.repositories.preferences

interface PreferencesRepository {
    var token: String

    var login: String

    var driverName: String

    var driverId: Long

    var acceptedTaskId: Long

}