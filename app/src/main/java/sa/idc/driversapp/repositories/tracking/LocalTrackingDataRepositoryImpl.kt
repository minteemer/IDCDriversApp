package sa.idc.driversapp.repositories.tracking

import android.location.Location
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.GetQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.data.db.DBHelper
import sa.idc.driversapp.data.db.tracking.TrackingDataTable
import sa.idc.driversapp.data.db.tracking.entities.TrackingData
import sa.idc.driversapp.domain.interactors.tracking.LocalTrackingDataRepository
import java.util.*

class LocalTrackingDataRepositoryImpl : LocalTrackingDataRepository {

    private val db: StorIOSQLite = DBHelper.defaultStorIOBuilder.build()

    override fun saveTrackingData(location: Location, time: Date): Completable =
            db.put()
                    .`object`(TrackingData(location.latitude, location.longitude, time.time))
                    .prepare()
                    .asRxCompletable()

    override fun getTrackingData(): Single<List<TrackingData>> = db.get()
            .listOfObjects(TrackingData::class.java)
            .withQuery(
                    Query.builder()
                            .table(TrackingDataTable.NAME)
                            .build()
            )
            .prepare()
            .asRxSingle()

    override fun removeTrackingData(data: TrackingData): Completable = db.delete()
            .`object`(data)
            .prepare()
            .asRxCompletable()
}